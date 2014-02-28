package utilitaires;

import interfaceGraphique.PointComp;
import interfaceGraphique.VueElement;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;


public class UtilitaireConsole {

	/**
	 * Renvoie la distance Chebyshev entre deux points
	 * @param p1 le premier point
	 * @param p2 le deuxieme point
	 * @return un entier representant la distance
	 */
	public static int distanceChebyshev(Point p1, Point p2) {
		return Math.max(Math.abs(p1.x-p2.x), Math.abs(p1.y-p2.y));
	}

	/**
	 * Verifie si les cases voisines de l'element ne sont pas deja occupe avec 
	 * un autre element
	 * @param p la position de l'element courant
	 * @param voisins les elements voisins sur l'interface graphique
	 * @return true si la case n'est pas occupe et false si la case est occupe
	 */
	public static boolean caseVide(Point p, Hashtable<Integer,VueElement> voisins){
		boolean trouve=false;
		Point paux;
		Enumeration<VueElement> enump = voisins.elements();
		while (!trouve && enump.hasMoreElements()) {
			paux = enump.nextElement().getPoint();
			trouve = p.equals(paux); 
		}
		
		return !trouve;
	}
	
	/** 
	 * Renvoie le meilleur point a occuper par l'element courant dans la 
	 * direction de la cible
	 * @param depart le point sur lequel se trouve l'element courant
	 * @param objectif le point sur lequel se trouve la cible
	 * @param voisins le positionement sur l'interface graphique de tous 
	 * les elements en vie
	 * @return le meilleur point libre dans la direction de la cible
	 */
	public static Point meilleurPoint(Point depart, Point objectif, Hashtable<Integer,VueElement> voisins){
		//liste contenant tous les positions vers lesquelles l'element peut avancer
		ArrayList<Point> listePossibles = new ArrayList<Point>();
		
		//pour chaque de 8 cases autour de lui
		for (int i = -1; i <= 1; i++){
			for (int j = -1; j <= 1; j++){
				if ((i != 0) || (j != 0))  {
 					//on ajoute la position (en valeur absolur pour eviter de 
					//sortir du cadre)
					listePossibles.add(new Point(Math.abs(depart.x+i),
												 Math.abs(depart.y+j)));
				}
			}
		}
		
		//organise les points de la liste du plus pres vers le plus eloigne de la cible
		Collections.sort(listePossibles,new PointComp(objectif));
		
		//cherche la case vide la plus proche de la cible
		boolean ok = false;
		int i = 0;
		Point res = null;
		while (!ok & i < listePossibles.size()) {
			res = listePossibles.get(i);
			ok = caseVide(res,voisins);
			i++;
		}
		
		//renvoie cette case
		return res;
	}

	/* 
	private Hashtable<Integer,VueElement> extraireInconnus(Element elem, Hashtable<Integer,VueElement> voisins){
		Hashtable<Integer,VueElement> res = new Hashtable<Integer,VueElement>();
	
		for (Integer i:voisins.keySet()){
			// TODO pour le moment on le fait avec tous les voisins
			//if (!elem.getElementsConnus().contains(i)) {
				res.put(i,voisins.get(i));
			//}
		}
		return res;
	}*/


}
