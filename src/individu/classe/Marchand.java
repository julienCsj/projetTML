package individu.classe;

import individu.Combattant;
import individu.Element;
import individu.Item;
import interfaceGraphique.VueElement;

import java.awt.Point;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Hashtable;

import utilitaires.UtilitaireConsole;

public class Marchand extends Combattant {

	/**
	 * Cree un combattant de la classe "Marchand" de nom donne
	 * @param nom nom du combattant
	 */
	public Marchand (String nom) 
	{
		super(nom, 25, 25, 25, 20);
	}

	
	
/*Ici la strategie consiste a aller vers tous les objets le plus 
 * rapidement possible et n'attaquer que si le combattant a moins de 50% de la 
 * vie du marchand*/
	
	public HashMap<Integer, HashMap<Integer,VueElement>> chercherElementProche 
		(VueElement ve, Hashtable<Integer,VueElement> voisins){
		
		// Liste des elements situes a proximite pour un element associe
		HashMap<Integer, HashMap<Integer,VueElement>> resultat = 
				new HashMap<Integer, HashMap<Integer,VueElement>>();
		
		// distance la plus proche
		int distPlusProche = 100;
		// reference de l'element le plus proche		
		int refPlusProche = 0;
		// distance entre deux elements
		int distEntre2points = 0;
		// aller en direction de l'element a proximite		
		boolean yAller = false;
		
		// parcours l'ensemble des elements situes a proximite
		for(Integer ref:voisins.keySet()) {
			// Point ou est localisé la reference de l'element de reference ref
			Point target = voisins.get(ref).getPoint();
			
			// element a proximite
			Element elt;
			try {
				elt = voisins.get(ref).getControleur().getElement();
				// si l'element a proximite est un objet 
				// ou si l'element a proximite a au mieux 50% de notre vie
				// alors on se dirige vers lui
				if (elt instanceof Item  || elt.getPV() < this.getPV()*0.5)
					yAller = true;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			// calcule la distance entre l'element courant ve
			// et l'element situe a proximmite de reference ref
			distEntre2points = UtilitaireConsole.distanceChebyshev(ve.getPoint(), target);
			
			// Si l'element est plus proche
			if (distEntre2points < distPlusProche && yAller) {
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
