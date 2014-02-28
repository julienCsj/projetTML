package serveur;

import individu.Element;
import individu.Item;
import individu.Combattant;
import interaction.DuelBasic;
import interfaceGraphique.VueElement;

import java.awt.Point;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import controle.IConsole;
import utilitaires.UtilitaireConsole;


/**
 * Le serveur ou se connectent les Consoles en RMI.
 * En localhost pour l'instant
 *
 */
public class  Arene extends UnicastRemoteObject implements IArene, Runnable {

	private static final long serialVersionUID = 1L;
	/**
	 * port a utiliser pour la connexion
	 */
	private int port;

	/**
	 * nombre d'elements connectes au serveur
	 */
	private int compteur = 0 ;   

	/**
	 * elements connectes au serveur
	 */
	private  Hashtable<Remote,VueElement> elements = null; 

	/**
	 * Constructeur 
	 * @param port le port de connexion
	 * @throws Exception
	 */
	public Arene(int port) throws Exception {
		super();
		this.port=port;
		Naming.rebind("rmi://localhost:"+port+"/Arene",this);
		elements = new Hashtable<Remote,VueElement>();
		new Thread(this).start();
	}

	/**
	 * Fournit une reference (entiere) pour chaque nouvel element present dans 
	 * l'arene. La reference est donnee par un compteur qui represente le 
	 * nombre d'elements 
	 * La synchronisation permet de garantir l'acces a un seul thread a la fois
	 * au compteur++  
	 * @return reference (entiere) utilisee pour rmi, compter les elements 
	 * @throws RemoteException 
	 */
	public synchronized int allocateRef() throws RemoteException {
		compteur++;
		return compteur;
	}

	/**
	 * boucle principale du thread serveur
	 */
	public void run() {
		TimeoutOp to;
		while (true) {
			try {
				//on verouille le serveur durant un tour de jeu 
				// -> pas de connexion/deconnexion
				synchronized(this) {	
					// a cet instant, pour chaque client connecte, 
					// on verifie s'il est en vie
					for(Enumeration<Remote> enu = elements.keys(); enu.hasMoreElements();) {
						//boucle de jeu
						Remote r = enu.nextElement();
						to = new TimeoutOp(r);
						to.join(1000);
						if (to.isAlive()) {
							to = null;
							System.out.println("Depassement du temps (client ne"
									+ "+"+elements.get(r).getRef()+") !");
							elements.remove(r);
							((IConsole) r).shutDown("Presence sur l'arene trop "
									+ "long. Degage !");
						}
						else{
							Element elem = ((IConsole) r).getElement();

							if(elem.getPV() <= 0){
								System.out.println(elem.getNom() + " est mort...");
								elements.remove(r);
								((IConsole) r).shutDown("Vous etes mort...");
							}
						}
					}
				}
				// dormir 'au plus' 1 seconde (difference temps execution est 1s)
				// pour permettre connexion/deconnexion des qconsoles
				Thread.sleep(1000);	
			} catch (Exception e) {e.getMessage();}
		}
	}


	/** Associe ("connecte") la representation d'un element en y associant un
	 * Remote, ajoute la representation d'un element dans l'arene 
	 * 
	 * La synchronisation permet de garantir qu'on ne fait pas de nouvelle 
	 * connection pendant un tour de jeu
	 * @param s vue (representation) de l'element 
	 * @throws RemoteException
	 */
	public synchronized void connect(VueElement s) throws RemoteException {
		try {
			Remote r=Naming.lookup("rmi://localhost:"+port+"/Console"+s.getRef());
			elements.put(r, s);
		} catch (Exception e) {
			System.out.println("Erreur lors de la connexion d'un client (ref = "
					+ ""+s.getRef()+") :");
			e.printStackTrace();
		}

	}

	/**
	 * appele par l'IHM pour afficher une representation de l'arene
	 * via RMI, on envoie une copie (serialisee) du monde 
	 */
	public ArrayList<VueElement> getWorld() throws RemoteException {
		ArrayList<VueElement> aux = new ArrayList<VueElement>();
		for(VueElement s : elements.values()) {
			aux.add(s);
		}
		return aux; 
	}


	/**
	 * Liste des reference des voisins et leurs coordonnees a partir d'une position
	 */
	public Hashtable<Integer, VueElement> voisins(Point pos,int ref)
			throws RemoteException {
		//Hashtable<Integer, Point> aux=new Hashtable<Integer, Point>();

		Hashtable<Integer,VueElement> aux = new Hashtable<Integer, VueElement>();

		for(VueElement s:elements.values()) {
			if (((UtilitaireConsole.distanceChebyshev(s.getPoint(), pos))<10) & (s.getRef()!=ref)) {
				//aux.put(s.getRef(),new Point(s.getPoint().x,s.getPoint().y)); //on cree un nouveau point
				aux.put(s.getRef(), s);
			}
		}
		return aux;
	}




	/**
	 * Classe permettant de lancer une execution du client (run)
	 * dans un thread separe, pour pouvoir limiter son temps d'execution
	 * via un join(timeout)
	 */
	public class TimeoutOp extends Thread {
		private Remote r;
		TimeoutOp(Remote r) {this.r=r; start();}
		public void run() {
			try {
				//on lance une execution
				((IConsole) r).run(); 
				//maj du serveur ac les infos du client, pourquoi clonage ??
				elements.put(r,((IConsole) r).update()); 
				if (elements.get(r).getTTL()==0) {
					elements.remove(r);
					((IConsole) r).shutDown("Vous etes reste trop longtemps dans"
							+ " l'arene, vous etes elimine !");
				}

			} catch (Exception e) {
				//les exceptions sont inhibees ici, que ce soit une deconnection
				//du client ou autre
				//en cas d'erreur, le client ne sera plus jamais execute
				//e.printStackTrace();
				elements.remove(r);
			} 
		}
	}


	/**
	 * Renvoie le nombre de clients connectes
	 * @return nombre de clients connectes
	 */
	public int countClients() {
		return elements.size();
	}

	/**
	 * Recupere les informations des deux combattants de reference <i>ref</i> et
	 * <i>ref2</i>, les font combattre l'un contre l'autre puis ajoute l'element
	 * combattu a sa liste des elements connus pour chacun des deux combattants
	 * @param ref reference du combattant attaquant
	 * @param ref2 reference du combattant defenseur
	 */
	public void interaction (int ref, int ref2) throws RemoteException {
		try {
			//recupere l'attaquant et le defenseur
			Remote attaquant = Naming.lookup("rmi://localhost:"+port+"/Console"+ref);
			Remote defenseur = Naming.lookup("rmi://localhost:"+port+"/Console"+ref2);
			//maj du serveur ac les infos du client, pourquoi clonage ??
			//cree le duel
			DuelBasic duel = new DuelBasic(this,	(IConsole) attaquant, 
					(IConsole) defenseur);

			//realise combat
			duel.realiserCombat();

			// ajout les elements avec lesquels on a joue dans la liste des 
			// elements connus
			((IConsole) attaquant).ajouterConnu(ref2);
			((IConsole) defenseur).ajouterConnu(ref);		
		} 
		catch (MalformedURLException e) {
			e.printStackTrace();
		} 
		catch (NotBoundException e) {
			e.printStackTrace();
		}
	}	

	/**
	 * Permet au combattant de reference <i>ref</i> de ramasser l'objet ayant
	 * pour reference <i>ref2</i>
	 * Necessite que le sac du combattant ne soit pas plein (limite a 5 cases)
	 * @param ref reference du combattant
	 * @param ref2 reference de l'objet a ramasser
	 */
	public void ramasser(int ref, int ref2) throws RemoteException {
		try {
			//recupere le perso et l'item
			Remote rperso = Naming.lookup("rmi://localhost:"+port+"/Console"+ref);
			Remote ritem = Naming.lookup("rmi://localhost:"+port+"/Console"+ref2);

			IConsole cperso = (IConsole) rperso;
			Combattant perso = (Combattant) (cperso).getElement();
			IConsole citem = (IConsole) ritem;
			Item item = (Item) (citem).getElement();

			// On v�rifie que le perso peut prendre l'item dans son sac 
			// (limit� � 5 cases)
			if(perso.getSac() + item.getBonusSac() <= 5){
				((IConsole)rperso).ramasserObjet((IConsole)ritem);
				((IConsole)ritem).perdrePV(1);					
			}

		} 
		catch (MalformedURLException e) {
			e.printStackTrace();
		} 
		catch (NotBoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Supprime un element de la liste des elements connectes au serveur
	 * @param elem l'element a enlever
	 */
	public void supprimerElement(Remote elem) {
		this.elements.remove(elem);
	}


}
