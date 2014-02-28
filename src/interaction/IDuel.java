package interaction;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface  IDuel {
	
	/**
	 * Realise le combat entre deux personnages
	 * @throws RemoteException
	 */
	public int realiserCombat() throws RemoteException; 
	
	/**
	 * Renvoie la reference de l'attaquant connue par le serveur
	 * @throws RemoteException
	 */
	public Remote getRefAttaquant() throws RemoteException;
	
	/**
	 * Renvoie la reference du defenseur connue patr le serveur
	 * @throws RemoteException
	 */
	public Remote getRefDefenseur() throws RemoteException;
	
	
	
}
