/**
 * 
 */
package individu.classe;

import java.awt.Point;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

import utilitaires.UtilitaireConsole;
import individu.Combattant;
import interfaceGraphique.VueElement;

/**
 * @author daolf
 *	
 */
public class Guerrier extends Combattant {

	/**
	 * Cree un personnage de classe "Guerrier"
	 * @param nom nom du combattant
	 */
	public Guerrier (String nom) 
	{
		super(nom, 0, 25, 25, 2);
		// Degat d'attaque comprise entre 25 et 100
		int attaque = (new Random().nextInt(75)+25);
		this.setAttaque(attaque);
	}
	
	// Ici la strategie consiste simplement en aller vers le plus pr�s 
	/**
	 * Cherche l'element le
	 */
	public HashMap<Integer, HashMap<Integer,VueElement>> chercherElementProche 
		(VueElement ve, Hashtable<Integer,VueElement> voisins){
		
		// Liste des elements situes a proximite pour un element associe
		HashMap<Integer, HashMap<Integer,VueElement>> resultat = 
				new HashMap<Integer, HashMap<Integer, VueElement>>();

		// distance la plus proche
		int distPlusProche = 100;
		// reference de l'element le plus proche
		int refPlusProche = 0;
		// distance entre deux elements
		int distEntre2points = 0;
		
		// parcours l'ensemble des elements situes a proximite
		for(Integer ref : voisins.keySet()) {
			
			// Point ou est localisé la reference de l'element de reference ref
			Point target = voisins.get(ref).getPoint();
			
			// calcule la distance entre l'element courant ve
			// et l'element situe a proximmite de reference ref
			distEntre2points = UtilitaireConsole.distanceChebyshev(ve.getPoint(), target);
			
			// Si l'element est plus proche
			if (distEntre2points < distPlusProche) {
				distPlusProche = distEntre2points;
				refPlusProche = ref;
			}
		}

		// ajout de l'element situe a proximite a liste des elements deja joue
		HashMap<Integer,VueElement> cible = new HashMap<Integer,VueElement>();
		cible.put((Integer)refPlusProche, voisins.get(refPlusProche));
		resultat.put((Integer)distPlusProche, cible);

		return resultat;
	}
}
