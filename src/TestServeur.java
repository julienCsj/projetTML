import java.net.InetAddress;
import java.text.DateFormat;
import java.util.Date;

import serveur.Arene;

/**
 * Lancement de l'Arene. A lancer en premier. 
 */
public class TestServeur {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		int port=5099;	//par defaut, port=5099
		if (args.length!=0) port=Integer.parseInt(args[0]);
		
		System.out.println("Creation du registre RMI sur le port "+port+"...");
		java.rmi.registry.LocateRegistry.createRegistry(port);
	    
	    System.out.println("Creation du serveur sur le port "+port+"...");
	    Arene server=new Arene(port);
	    
	    while(true) {
	    	System.out.println("[Arene sur "+InetAddress.getLocalHost().getCanonicalHostName()+":"+port+"] "
	    			+DateFormat.getTimeInstance().format(new Date())
	    			+" ("+server.countClients()+" clients)"
	    			+"\n");
	    	try {
	    		Thread.sleep(1000*60); //delai = 1 minute
	    	} catch (InterruptedException e) {}
	    }

	}

}
