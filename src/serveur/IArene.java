package serveur;

import interfaceGraphique.VueElement;

import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;


/**
 * 
 * Interface de l'Arene
 *
 */
public interface IArene extends Remote {

	/**
	 * Renvoie une reference pour la console en train de se connecter
	 * @throws RemoteException
	 */
	int allocateRef() throws RemoteException;
	
	/**
	 * Connexion d'un element au serveur
	 * @param ve la vue de l'element a se connecter
	 * @throws RemoteException
	 */
	void connect(VueElement ve) throws RemoteException;
	
	/**
	 * Calcule la liste de toutes les representations d'elements presentes dans l'arene. 
	 * @return la liste des representations
	 * @throws RemoteException
	 */
	ArrayList<VueElement> getWorld() throws RemoteException;
	
	/**
	 * Liste les voisins (representations d'element) d'une coordonnee dans l'arene sous la forme de couples (reference,coordonnees) dans une Hashtable
	 * @throws RemoteException
	 */
	Hashtable<Integer, VueElement> voisins(Point pos, int ref) throws RemoteException;
	
	/**
	 * Methode d'interaction (combat) entre deux elements dont on a la reference
	 * @param ref le premier element
	 * @param ref2 le deuxieme element
	 * @throws RemoteException
	 */
	void interaction(int ref, int ref2) throws RemoteException;
	
	/**
	 * Ramassage de l'equipement par le comabattant
	 * @param ref le combattant
	 * @param ref2 l'objet a ramasser
	 * @throws RemoteException
	 */
	void ramasser(int ref, int ref2) throws RemoteException;
	
}

