package individu.classe;

import individu.Combattant;
import interfaceGraphique.VueElement;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

public class Hodor extends Combattant {

	/**
	 * Cree un combattant de classe Hodor et de nom donne
	 * @param nom nom du combattant
	 */
	public Hodor (String nom) 
	{
		super(nom, 0, 40, 85, 2);
	}
	
	// Hodor est idiot Hodor vas aleatoirement aller vers quelqun
	/**
	 * 
	 */
	public HashMap<Integer, HashMap<Integer,VueElement>> chercherElementProche 
		(VueElement ve, Hashtable<Integer,VueElement> voisins){
		
		// Liste des elements situes a proximite pour un element associe
		HashMap<Integer, HashMap<Integer,VueElement>> resultat =
				new HashMap<Integer, HashMap<Integer,VueElement>>();

		// distance la plus proche
		int distPlusProche = 100;
		// reference de l'element le plus proche		
		int refPlusProche = 0;

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
		}
		// Sinon, deplacement aleatoire
		else 
			refPlusProche = 0;

		// ajout de l'element situe a proximite a liste des elements deja joue
		HashMap<Integer,VueElement> cible = new HashMap<Integer,VueElement>();
		cible.put((Integer)refPlusProche, voisins.get(refPlusProche));
		resultat.put((Integer)distPlusProche, cible);

		return resultat;
	}

}
