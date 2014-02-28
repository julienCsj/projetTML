import individu.Combattant;
import individu.classe.Normal;
import java.rmi.RemoteException;
import java.util.Random;

import controle.Console;


public class JeuNormal {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws RemoteException {
		// TODO Auto-generated method stub

		int r1 = new Random().nextInt(100);
		int r2 = new Random().nextInt(100);

		Combattant normal = new Normal("Normal");

		new Console(normal, r1, r2);
	}

}
