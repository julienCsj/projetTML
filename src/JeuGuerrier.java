import individu.Combattant;
import individu.classe.Guerrier;
import java.rmi.RemoteException;
import java.util.Random;

import controle.Console;


public class JeuGuerrier {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws RemoteException {
		// TODO Auto-generated method stub

		int r1 = new Random().nextInt(100);
		int r2 = new Random().nextInt(100);

		Combattant guerrier = new Guerrier("Guerrier");

		new Console(guerrier, r1, r2);
	}

}
