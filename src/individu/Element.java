/**
 * 
 */
package individu;

import interfaceGraphique.VueElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;


public class Element implements IElement, Serializable {
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Nom de l'element
	 */
	private String nom;                    
	
	/**
	 * Nombre de points de vie restants de l'element
	 */
	private int PV;                             
	
	/**
	 * Liste des references des elements avec lesquels on a deja joue
	 */	
	private ArrayList<Integer> elementsConnus = new ArrayList<Integer>();   
	
	/**
	 * Constructeur
	 * @param nom le nom de l'element a creer
	 * le nombre de vie est d�fini � la cr�ation de l'objet
	 */
	public Element(String nom){
		this.nom = nom;
	}
	
	/**
	 * Retourne le nom de l'element
	 * @return le nom de l'element
	 */
	public String getNom() {
		return this.nom;
	}

	/**
	 * Retourne le nombre de vies de l'element
	 * @return le nombre de point de vie restant
	 */
	public int getPV() {
		return this.PV;
	}
	
	/**
	 * Reinitialise le nombre de vies de l'element
	 * @param vie le nouveau nombre de vie
	 */	
	public void setPV(int vie){
		this.PV = vie;
	}
	
	/**
	 * Retourne une liste des references des elements avec lesquels l'element
	 * courant a deja joue
	 * @return une liste de reference
	 */
	public ArrayList<Integer> getElementsConnus() {
		return this.elementsConnus;
	}

	/**
	 * Ajoute a la liste des elements connus (elements avec lesquels l'objet 
	 * courant a joue) un nouvel element
	 * @param ref le reference de l'objet avec qui il joue
	 */
	public void ajouterConnu(int ref) {
		elementsConnus.add(ref);		
	}

	/**
	 * Renvoie les informations concernant l'element courant
	 * @return chaine de caractere contenant au moins le nom de l'element 
	 * 	et le nombre de vies tel qu'il sera affiche sur l'interface graphique
	 */
	public String toString(){
		return this.getNom();
	}
	
	/**
	 * Ici la strategie est implement� , la fonction renvois vers quel
	 *  �l�ment se deplacer
	 * @param ve la vue de l'element
	 * @param voisins les elements situes a proximites
	 * @return la liste des elements situes a proximites pour une refrence
	 */
	public HashMap<Integer, HashMap<Integer,VueElement>> chercherElementProche 
		(VueElement ve, Hashtable<Integer,VueElement> voisins)
	{
		return null;
	}
}
