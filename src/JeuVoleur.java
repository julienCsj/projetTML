import individu.Combattant;
import individu.classe.Voleur;

import java.rmi.RemoteException;
import java.util.Random;

import controle.Console;


public class JeuVoleur {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws RemoteException {
		// TODO Auto-generated method stub

		int r1 = new Random().nextInt(100);
		int r2 = new Random().nextInt(100);

		Combattant voleur = new Voleur("Voleur");

		new Console(voleur, r1, r2);
	}

}
