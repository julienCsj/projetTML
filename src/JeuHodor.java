import individu.Combattant;
import individu.classe.Hodor;
import java.rmi.RemoteException;
import java.util.Random;

import controle.Console;

/**
 * Test de la Console avec un Element qui s'ajoute a l'Arene (apres lancement Arene et IHM). A lancer en plusieurs exemplaires.
 */
public class JeuHodor {

	/**
	 * @param args
	 * @throws RemoteException 
	 */
	public static void main(String[] args) throws RemoteException {

		int r1 = new Random().nextInt(100);
		int r2 = new Random().nextInt(100);

		Combattant hodor = new Hodor("Hodor");

		new Console(hodor, r1, r2);	
	}

}
