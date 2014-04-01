package interfaceGraphique;

import individu.Item;
import individu.classe.Hodor;
import individu.classe.Marchand;
import individu.classe.Normal;
import individu.classe.Voleur;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import serveur.IArene;

public class IHM extends JFrame {

	private static final long serialVersionUID = 1L;
	/**
	 * port par defaut pour communiquer avec le serveur RMI
	 */
	private static final int port=5099;	  
	
	enum State{INIT, PLAYING};
	
	/**
	 * etat de l'interface
	 */
	private State state=State.INIT;   
	
	/**
	 * serveur
	 */
	private Remote serveur; 
	
	/**
	 *  thread de connexion au serveur
	 */
	private Thread connection = null;    
	
	/**
	 * erreur de connexion
	 */
	private boolean cnxError = false;                                 
	
	/**
	 * liste de tous les elements connectes a l'interface
	 */
	private ArrayList<VueElement> world=new ArrayList<VueElement>();  
	
	
	// Classe interne
	private class AreneJTextArea extends JTextArea {

		private static final long serialVersionUID = 1L;

		AreneJTextArea() {
			super("Connexion...", 10, 10);
			setEditable(false);
		}
	}
	
	// Classe interne
	private class AreneJPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		private JTextArea jta;
		
		//Conteneur qui affiche l'arene de jeu
		AreneJPanel(JTextArea jta) {
			this.jta = jta;
		}
		
		public void paintComponent(Graphics g) {
			//affiche l'arene comme un rectangle
			Rectangle rect = this.getBounds();
			
			//si la connexion est en cours ou il y a une erreur
			if ((state == State.INIT) || (cnxError)) {
				Font of=g.getFont();
				g.setFont(new Font("Arial",Font.BOLD,20));
				//affiche le message correspondant
				if (!cnxError) 
					g.drawString("Connexion en cours sur le serveur Arene...",
							20, rect.height-20);
				else 
					g.drawString("Erreur de connexion !", 20, rect.height-20);
				g.setFont(of);
				
				//verifie si la connexion a ete realisee - isAlive (Thread)==true si on est en cours de connexion
				if ((connection!=null) && (! connection.isAlive())) {
					//affiche le message correspondant
					if (!cnxError) 
						jta.append("ok !"); 
					else 
						jta.append("erreur !");
					//mets a jour l'etat de l'arene
					state=State.PLAYING;
					//remets la connexion a null pour une autre execution
					connection=null;
				}
			} 
			else {
				try {
					//mets a jour la liste des elements en vie sur l'arene
					world=((IArene) serveur).getWorld();
					
					int ref,cx,cy;
					String dial;
					
					//reinitialise l'affichage de l'arene
					jta.setText("");
					
					//pour chaque element en vie sur l'arene
					for(VueElement s:world) {
						//recupere sa reference
						ref=s.getRef();
						
						Random r=new Random(ref);
						//calcule une couleur pour la representation
						g.setColor(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255), 200));
						
						//recupere les coordonnes de l'element
						cx=s.getPoint().x*rect.width/100;
						cy=s.getPoint().y*rect.height/100;
						
						
						//on change l'aspect de l'Element dans la console
						if (s.getControleur().getElement() instanceof Item)
						{
							//construis un oval aux coordonnes cx,cy de taille 8 x 8
							char[] o={'o'};
							g.drawChars(o,0,o.length, cx, cy);
						}
						else
						{
							char[] h = {'h'};
							char[] m = {'m'};
							char[] v = {'v'};
							char[] n = {'n'};
							char[] gu = {'g'};
							if(s.getControleur().getElement() instanceof Hodor)
								g.drawChars(h, 0, h.length, cx, cy);
							else if(s.getControleur().getElement() instanceof Marchand)
								g.drawChars(m, 0, m.length, cx, cy);
							else if(s.getControleur().getElement() instanceof Voleur)
								g.drawChars(v, 0, v.length, cx, cy);
							else if(s.getControleur().getElement() instanceof Normal)
								g.drawChars(n, 0, n.length, cx, cy);
							else g.drawChars(gu, 0,gu.length, cx, cy);
						}
						
						//recupere les phrases dites par l'element
						dial = (s.getPhrase()==null)?"":" : "+s.getPhrase();
						
						//affiche au dessus du point ses informations
						g.drawString(s.afficher(), cx+10, cy);
						int life = s.getControleur().getElement().getPV();
						String bVie = "";
						
						for (int i = 0; i < life/10 ;i++)
							bVie=bVie.concat("â–ˆ");
						//affiche un barre de vie
						g.drawString(bVie,cx+10,cy-12);
						
						
						//affiche dans la fenetre a cote ses informations
						jta.append(s.afficher()+dial+"\n");
					}
					
				} 
				catch (RemoteException e) {
					//en cas de deconnexion ou erreur du serveur
					//remets l'etat de l'arene a jour
					state=State.INIT;
					//affiche un dialog avec le message d'erreur
					JOptionPane.showMessageDialog(this,"Erreur de connection !\nRaison : "+e.getMessage(),"Message",JOptionPane.ERROR_MESSAGE);
					cnxError=true;
					e.printStackTrace();
				}
			}
			
			//affiche l'heure courante
			g.setColor(Color.BLACK);
			g.drawString(DateFormat.getTimeInstance().format(new Date()),
															rect.width-60,20);
		}
	}
	
	/**
	 * Cree une interface graphique
	 */
	public IHM() {
		Toolkit kit = Toolkit.getDefaultToolkit();
		
		//personnalise et positionne la fenetre par rapport a l'ecran
		Dimension size = kit.getScreenSize();
		setSize(size.width/2, size.height/2);
		setLocation(size.width, size.height/4);
		//setResizable(false);
		
		//cree un titre de la fenetre
		setTitle("IHM - Arene / UPS - Projet Prog");
		
		//ajout une operation si le bouton de fermeture de la fenetre est clique
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//ajout d'une action pour arreter l'execution de l'interface graphique
		Action exitAction=new AbstractAction("Quitter") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		};
		
		Action aboutAction=new AbstractAction("A propos") {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent ae) {
				JOptionPane.showMessageDialog(null,"Arene\nInspiree des TP de L3");
			}
		};
		
		//creation d'un menu Fichier avec deux options - quitter et a propos 	
		JMenuBar m = new JMenuBar();
		JMenu file = new JMenu("Fichier");
		file.add(aboutAction);
		file.add(exitAction);
		m.add(file);
		setJMenuBar(m);
				
		//ajout de l'arene dans la fenetre
		AreneJTextArea ajta = new AreneJTextArea();
		getContentPane().add(new AreneJPanel(ajta));
		setVisible(true);
		
		//Fenetre qui affiche les messages des console
		JFrame jf=new JFrame();
		jf.setSize(size.width/4, size.height/4);
		jf.setLocation(size.width*3/5, size.height/10);
		jf.getContentPane().add(new JScrollPane(ajta));
		jf.setTitle("The triangle square");
		jf.setVisible(true);
	}
	
	/**
	 * Lance une connexion au serveur dans un thread separe
	 */
	public void connect() {
		connection=new Thread() {
			public void run() {
				try {
					serveur=Naming.lookup("rmi://localhost:"+port+"/Arene");
				} 
				catch (Exception e) {
					cnxError=true;
					JOptionPane.showMessageDialog(null,"Impossible de se connecter au serveur Arene:"+port+" !\n(le serveur ne doit pas etre actif...)\nRaison : "+e.getMessage(),"Message",JOptionPane.ERROR_MESSAGE);
				}
			}
		};
		connection.start();
	}
}
