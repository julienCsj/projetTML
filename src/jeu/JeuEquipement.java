package jeu;
import individu.Item;

import java.rmi.RemoteException;
import java.util.Random;

import controle.Console;

/**
 * Test de la Console avec un Element qui s'ajoute a l'Arene (apres lancement Arene et IHM). A lancer en plusieurs exemplaires.
 */
public class JeuEquipement {

	/**
	 * @param args
	 * @throws RemoteException 
	 */
	public static void main(String[] args) throws RemoteException {

		int r1 = new Random().nextInt(100);
		int r2 = new Random().nextInt(100);
		int r3 = new Random().nextInt(100);
		int r4 = new Random().nextInt(100);
		int r5 = new Random().nextInt(100);
		int r6 = new Random().nextInt(100);
		int r7 = new Random().nextInt(10)-5;
		
		Item pomme = new Item("� A:"+r3+" B:"+r4+" E:"+r5+" V:"+r6+" S:"
				+ ""+r7, r3, r4, r5, r6, r7);
		
		new Console(pomme, r1, r2);
		
	}

}
