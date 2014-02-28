import individu.Combattant;
import individu.classe.Marchand;
import java.rmi.RemoteException;
import java.util.Random;

import controle.Console;


public class JeuMarchand {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws RemoteException {
		// TODO Auto-generated method stub

		int r1 = new Random().nextInt(100);
		int r2 = new Random().nextInt(100);

		Combattant marchand = new Marchand("Marchand");

		new Console(marchand, r1, r2);
	}

}
