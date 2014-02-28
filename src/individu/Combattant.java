/**
 * 
 */
package individu;


import java.util.Random;


/**
 * @author Vincent Deniau
 *
 */
public class Combattant extends Element {

	private static final long serialVersionUID = 1L;
	/**
	 * Valeur de l'attaque/force du combattant [0, 150]
	 */
	private int attaque; 	
	
	/**
	 * Defense ou bouclier [0, 150] du combattant
	 */
	private int bouclier;      	
	
	/**
	 * Capacite du combattant a esquiver un coup [0, 150]
	 */
	private int esquive;        
	
	/**
	 * Capacite de port du personnage exprime en nombre de 'cases' du sac [0, 5]
	 */
	private int sac;			

	
	
	/**
	 * Cree un combattant 
	 * @param nom nom du combattant
	 * @param attaque force du combattant
	 * @param bouclier defense du combattant
	 * @param esquive capacite a esquiver un coup du combattant
	 * @param sac capacite de port du sac
	 */
	public Combattant(String nom, int attaque, int bouclier, int esquive, int sac) {

		super(nom);		
		// au depart, chaque personnage possede une vie comprise entre 25 et 100
		this.setPV(new Random().nextInt(75)+25);
		this.attaque = attaque;
		this.bouclier = bouclier;
		this.esquive = esquive;
		this.sac = 2;
	}

	/**
	 * Retourne le nombre de points de vie restant du combattant
	 * @return point de vie du combattant
	 */
	/*
	 CA SERT A RIEN 
	public int getVie() {
		return super.getPV();
	}
	 */
	
	/**
	 * Retourne la force d'attaque du combattant
	 * @return attaque du combattant
	 */
	public int getAttaque() {
		return this.attaque;
	}
	
	/**
	 * Retourne la valeur de defense du combattant
	 * @return defense du bouclier
	 */
	public int getBouclier() {
		return this.bouclier ;
	}
	
	/**
	 * Retourne la valeur de capacite d'esquive du combattant pour un coup
	 * @return l'esquive du combattant
	 */
	public int getEsquive() {
		return esquive;
	}
	
	/**
	 * Retourne le nombre de 'cases' du sac permettant d'avoir des objets
	 * @return le nombre de 'cases' du sac
	 */
	public int getSac() {
		return sac;
	}
	
	/**
	 * Modifie la valeur d'attaque du combattant
	 * @param nouvelle valeur d'attaque du combattant
	 */
	public void setAttaque(int attaque) {
		this.attaque = attaque;
	}

	/**
	 * Modifie la valeur de capacite d'esquive du combattant
	 * @param la nouvelle valeur d'esquive du combattant
	 */
	public void setEsquive(int esquive) {
		this.esquive = esquive;
	}

	/**
	 * Modifie le nombre de 'cases' du sac
	 * @param nombre de 'cases' du sac
	 */
	public void setSac(int sac) {
		this.sac = sac;
	}

	/**
	 * Modifie la valeur de defense/bouclier du combattant
	 * @param la nouvelle valeur de bouclier du combattant
	 */
	public void setBouclier(int bouclier) {
		this.bouclier = bouclier;
	}

	
	/**
	 * Recalcule le nombre de vies du combattant en enlevant celles qu'il a perdues
	 * @param viePerdue le nombre de vies perdues par le combattant
	 */
	/* CA SERT A RIEN CA
	public void perdrePV(int viePerdue) {
		super.setPV(super.getPV() - viePerdue);
		this.setPV(this.getPV() - viePerdue);
	}	
	*/
}
