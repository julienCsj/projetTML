package individu.classe;

import individu.Combattant;
import individu.Element;
import individu.Item;
import interfaceGraphique.VueElement;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

import utilitaires.UtilitaireConsole;

public class Normal extends Combattant {
	
	static int nbobjet = 0;

	/**
	 * Cree un personnage de la classe "Normal"
	 * @param nom nom du combattant
	 */
	public Normal(String nom) 
	{
		super(nom, 33, 33, 33, 3);
	}

	/**
	 * Cherche l'element le plus proche vers lequel se didiger
	 * @param ve l'element courant
	 * @param voisins les elements voisins
	 * @return un hashmap contenant la distance a parcourir vers 
	 * l'element le plus proche, son identifiant et sa vue
	 */
	public HashMap<Integer, HashMap<Integer,VueElement>> 
	chercherElementProche(VueElement ve, Hashtable<Integer,VueElement> voisins){

		/* On change la strategie de maniere a ce que le plus proche soit en
		 * fait le plus loin sauf si c'est un objet */
		
		// Liste des elements situes a proximite pour un element associe		
		HashMap<Integer, HashMap<Integer,VueElement>> resultat = 
				new HashMap<Integer, HashMap<Integer,VueElement>>();
		
		// distance la plus proche
		int distPlusProche = 100;
		// reference de l'element le plus proche
		int refPlusProche = 0;
		// reference
		int ref = 0;
		// distance entre deux elements
		int distEntre2points = 0;
		
		HashMap<Integer,VueElement> cible = new HashMap<Integer,VueElement>();

		// Strategie du random
		
		// Ensemble des references des elements a proximites
		Set<Integer> tabref = voisins.keySet();
		
		int nbElements = tabref.size();
		Integer[] tabEntiers = new Integer[nbElements];
		Random r = new Random();
		
		tabref.toArray(tabEntiers);
		
		
		// S'il existe au moins un element a proximite, on attribue une 
		// une reference aleatoire existante
		if( (nbElements >= 1) && (tabEntiers[0] != null) )
		{
			// On trouve un element aleatoire dans la liste de voisins
			int eltAleatoire = r.nextInt(nbElements);
			refPlusProche = tabEntiers[eltAleatoire];
			// Si l'element est un objet equipement
			Element eltVoisin;
			try {
				eltVoisin = voisins.get(ref).getControleur().getElement();
				// si l'element a proximite est un objet 
				if (eltVoisin instanceof Item){
					if (nbobjet == 0) {
						refPlusProche=ref;
						// calcule la distance entre l'element courant ve
						// et l'element situe a proximmite de reference ref
						distEntre2points = UtilitaireConsole.distanceChebyshev(ve.getPoint(),
																	voisins.get(ref).getPoint());
						// si la distance entre les 2 elements est de 1
						if(distEntre2points == 1 ) 
							nbobjet = 1;
					}
					else {
						ref = 0;
					}
				}
				// Sinon, l'element est un combattant
				else
				{
					Element elt = ve.getControleur().getElement();
					// si le joueur voisin a une vie au moins 50% inferieure a 
					// la notre on l'attaque
					if(eltVoisin.getPV() <= elt.getPV()*0.5 )
					{
						refPlusProche = ref;
					}
					else {
						distEntre2points = UtilitaireConsole.distanceChebyshev(ve.getPoint(),
								voisins.get(ref).getPoint());
						// si si le joueur est a une case de nous on l'attaque
						if (distEntre2points == 1) {
							refPlusProche = ref;
						}
					}					
				} 
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		// si la reference est celle d'un element a proximite
		if(ref != 0) {
			distEntre2points = UtilitaireConsole.distanceChebyshev(ve.getPoint(),
					voisins.get(ref).getPoint());
			distPlusProche = distEntre2points;
		}
		// ajout de l'element situe a proximite a liste des elements deja joue
		cible.put((Integer)refPlusProche,voisins.get(refPlusProche));
		resultat.put((Integer)distPlusProche, cible);

		return resultat;
	}
}
