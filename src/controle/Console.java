package controle;

import individu.Element;
import individu.Item;
import individu.Combattant;
import individu.classe.Hodor;
import interfaceGraphique.VueElement;

import java.awt.Point;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

import serveur.IArene;
import utilitaires.UtilitaireConsole;

/**
 * 
 * Se connecte au serveur avec un Element et sa VueElement.
 * "run" permet a l'Element/VueElement de se deplacer
 *
 */
public class Console extends UnicastRemoteObject implements IConsole {
	
	private static final long serialVersionUID = 1L;
	/**
	 * port par defaut pour communiquer avec le serveur RMI
	 */
	private static final int port=5099;	  
	
	/**
	 * le serveur avec lequel le controleur communique
	 */
	private Remote serveur = null;    
	
	/**
	 * la vue de l'element (pour l'interface graphique)
	 */
	private VueElement ve = null;   
	
	/**
	 * l'element pour lequel le controleur est cree
	 */
	private Element elem = null;       
	
	/**
	 * les vues des voisins sur l'interface graphique
	 */
	private Hashtable<Integer,VueElement> voisins;
	
	/**
	 * le point ou aller errer
	 */
	private Point pointErrance;  
	
	/**
	 * la reference (entiere) attribuee par le serveur a la connexion
	 */
	private int refRMI;                               
	
	
	/**
	 * Constructeur
	 * @param elem l'element pour lequel le controleur est cree
	 * @param dx la position initiale de l'element sur l'ordonnee 
	 * 		(interface graphique)
	 * @param dy la position initiale de l'element sur l'abscisse
	 * 		(interface graphique)
	 * @throws RemoteException
	 */
	public Console(Element elem, int dx, int dy) throws RemoteException {
		 //appel au constructeur de la super-classe -> il peut etre implicite
		super();
		try{
			//initialisation de l'element pour lequel le controleur est cree
			this.elem=elem;
			
			//position initiale aleatoire
			//Random r=new Random();
			//Point pos = new Point(r.nextInt(100),r.nextInt(100));
			
			//Creation de la position initiale de la vue de l'element sur l'interface graphique
			Point pos = new Point(dx,dy);
			
			//initialisation des voisins (vide avant la connexion)
			voisins = new Hashtable<Integer,VueElement>();
			
			//preparation connexion au serveur
			//handshake/enregistrement RMI
			serveur=Naming.lookup("rmi://localhost:"+port+"/Arene");
			serveur.toString();
			
			//initialisation de la reference du controleur sur le serveur
			this.refRMI=((IArene) serveur).allocateRef();
			Naming.rebind("rmi://localhost:"+port+"/Console"+refRMI,this);
			
			//initialisation de la vue sur l'element
			ve=new VueElement(refRMI, pos, this, "Atterrissage...");
						
			//connexion au serveur
			((IArene) serveur).connect(ve);
			
			//affiche message si succes
			System.out.println("Console connectee ["+refRMI+"]");
 		} catch (Exception e) {
  			System.out.println("Erreur : la console n'a pa pu etre creee !\nCause : "+e.getCause());
  			e.printStackTrace();
 		}
	}


	/**
	 * Permet au serveur de faire "jouer" un tour a l'element.
	 * Calcule ses voisins (donnes par le serveur), cherche le plus proche, 
	 * s'il est a proximite, lance l'interaction sinon se dirige vers lui 
	 * (s'il existe un plus proche)
	 * Cette methode est execute chaque seconde  
	 */
	public void run() throws RemoteException {
		if(ve.getControleur().getElement() instanceof Combattant) {

			//decremente sa duree de vie
			ve.decrTTL(); 
			
			//mets a jour ses voisins 
			voisins = ((IArene) serveur).voisins(ve.getPoint(),refRMI);	
					
			// Recherche du plus proche, sinon erreur
			Element element = ve.getControleur().getElement();
			HashMap<Integer, HashMap<Integer,VueElement>> resultat = 
					element.chercherElementProche(ve, voisins);
			this.parler("run");
			
			int distPlusProche = resultat.keySet().iterator().next();
			int refPlusProche =  resultat.get(distPlusProche).keySet().iterator().next();
			VueElement cible = resultat.get(distPlusProche).get(refPlusProche);
			
			// Element proche
			if (distPlusProche <= 1) {
				// Elemen = Item
				if(cible.getControleur().getElement() instanceof Item) {
					parler("Je tente de ramasser un objet");
					((IArene) serveur).ramasser(refRMI, refPlusProche);
				}
				// Element = combattant
				else {
					// On verifie que l'element n'est pas de la classe Hodor 
					// pour pouvoir attaquer
					if(cible.getControleur().getElement() instanceof Combattant
						&&	!(this.getElement() instanceof Hodor)) {
						parler("Je combat "+refPlusProche);
						((IArene) serveur).interaction(refRMI, refPlusProche);
					}
				}
			}
			//sinon, element eloigné
			else { 
				//l'element courant se deplace vers le plus proche (s'il existe)
				if (refPlusProche != 0) {
					parler("Je me dirige vers "+refPlusProche);
				}
				// sinon il erre
				else {
					parler("J'erre..."); 
				}
							
				seDirigerVers(refPlusProche);	
			}
		}
	}
	
	/**
	 * Deplace ce sujet d'une case en direction du sujet dont la 
	 * reference <i>ref</i> est donnee en parametre
	 * @param ref la reference de l'element cible
	 */
	public void seDirigerVers(int ref) {
		Point pvers;
		
		//si la cible est l'element meme, il reste sur place
		if (ref==ve.getRef()) return;

		//s'il n'y a pas de reference la plus proche
		if (ref==0) {
			// si le point ou l'element se trouve est le point d'errance 
			// precedemment defini
			if ((pointErrance!=null) && (pointErrance.equals(ve.getPoint()))) { 
				//le point d'errance est reinitialise
				pointErrance=null;
			}
			if (pointErrance==null) {
				//initialisation aleatoire
				Random r=new Random();
				pointErrance=new Point(r.nextInt(100), r.nextInt(100));
			}
			//la cible devient le nouveau point d'errance
			pvers=pointErrance;
		} 
		// sinon la cible devient le point sur lequel se trouve 
		// l'element le plus proche
		else {
			pvers=voisins.get(ref).getPoint();
		}
		
		//si l'element n'existe plus (cas posible: deconnexion du serveur), 
		//le point reste sur place
		if (pvers == null) return;
		
		//calcule la direction pour atteindre la ref 
		//(+1/-1 par rapport a la position courante)
		int dx = (int) (pvers.getX()-ve.getPoint().x);
		if (dx != 0)	
			dx = dx/Math.abs(dx);
		int dy = (int) (pvers.getY()-ve.getPoint().y);
		if (dy != 0)  
			dy = dy/Math.abs(dy);
		
		//instancie le point destination
		Point dest = new Point(ve.getPoint().x+dx,ve.getPoint().y+dy);
		
		//si le point destination est libre
		if (UtilitaireConsole.caseVide(dest,voisins)) {		
			//l'element courant se deplace
			ve.setPoint(dest);
		} 
		else {
			//cherche la case libre la plus proche dans la direction de la cible
			Point top = UtilitaireConsole.meilleurPoint(ve.getPoint(),dest,voisins);
			//deplace l'element courant sur celle-la
			ve.setPoint(top);
		}
	}

	/**
	 * Appelle par le serveur pour faire la MAJ du sujet 
	 */
	public VueElement update() throws RemoteException {
		VueElement aux=(VueElement) ve.clone();
		aux.setPhrase(ve.getPhrase()); 
		return aux;
	}

	/**
	 * Deconnexion du controleur du serveur
	 * @param cause le message a afficher comme cause de la deconnexion
	 */
	public void shutDown(String cause) throws RemoteException {
		System.out.println("Console "+refRMI+" deconnectee : "+cause);
		System.exit(1);
	}

	/**
	 * Retourne l'element creer
	 * @return l'element
	 */
	public Element getElement() throws RemoteException {
		return elem;
	}
	
	/**
	 * retourne la vue element
	 * @return la vue element
	 */
	public VueElement getVueElement() throws RemoteException {
		return ve;
	}
	
	/**
	 * Affiche le texte <i>s</i> passe en parametre dans la vue element
	 * @param texte a affiche
	 */
	public void parler(String s) throws RemoteException {
		ve.setPhrase(s);	
	}
	
	/**
	 * Enleve des points de vie <i>viePerdue</i> a l'element et affiche un 
	 * message indiquant la vie perdue et la vie restante
	 * Necessite que la vie restante soit toujours positive ou nulle
	 * Si obtention d'une valeur negative, remettre a 0
	 */	
	public void perdrePV(int viePerdue) throws RemoteException {
		if ( (this.elem.getPV() - viePerdue) < 0 )
			this.elem.setPV(0);
		else 
			this.elem.setPV(this.elem.getPV()-viePerdue);
		
		System.out.println("Ouch, j'ai perdu " + viePerdue + ""
				+ " points de vie. "
				+ "Il me reste " + this.elem.getPV() + " points de vie.");		
	}
	
	/**
	 * L'element associe au controleur ramasse un objet qui confere un
	 * bonus de capacite a l'element
	 * L'augmentation des capacités :
	 *  - ne doit pas exceder 150
	 *  - peut etre negative
	 *  Le bonus sac ne doit pas exceder 5 
	 * @param objet l'objet ramasse par l'element
	 */
	public void ramasserObjet(IConsole objet) throws RemoteException {
		
		// Item a ramasser
		Item item = (Item)(objet.getElement());
		// Combattant rammasant l'item
		Combattant combattant = (Combattant)this.elem;
		
		// L'item confere un bonus d'attaque
		if ( (combattant.getAttaque() + item.getBonusAttaque()) > 150)
			combattant.setAttaque(150);
		else 
			combattant.setAttaque(combattant.getAttaque() 
									+ item.getBonusAttaque());
		
		// L'item confere un bonus de bouclier
		if ( (combattant.getBouclier() + item.getBonusBouclier()) > 150)
			combattant.setBouclier(150);
		else 
			combattant.setBouclier(combattant.getBouclier() 
									+ item.getBonusBouclier());
		
		// L'item confere un bonus de point de vie (PV)
		if ( (combattant.getPV() + item.getBonusPV()) > 150)
			combattant.setPV(150);
		else 
			combattant.setPV(combattant.getPV() + item.getBonusPV());
		
		// L'item confere un bonus d'esquive		
		if ( (combattant.getEsquive() + item.getBonusEsquive()) > 150)
			combattant.setEsquive(150);
		else 
			combattant.setEsquive(combattant.getEsquive() 
									+ item.getBonusEsquive());
		
		// L'item confere un bonus sur le nombre d'objet pouvant etre 
		// contenant par le sac
		if ( (combattant.getSac() + item.getBonusSac()) > 5 )
			combattant.setSac(5);
		else 
			combattant.setSac(combattant.getSac() + item.getBonusSac());
		
	}
		
	/**
	 * Renvoie l'etat de l'element a afficher sur l'interface graphique
	 * @throws RemoteException
	 */
	public String afficher() throws RemoteException{
		return this.elem.toString();
	}

	/**
	 * Ajout l'element dans la liste des elements connus (combattants et Items)
	 * @param ref l'element a ajouter
	 */
	public void ajouterConnu(int ref) throws RemoteException {
		this.elem.ajouterConnu(ref);
	}
		
}
