package individu;


public class Item extends Element {
	
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Nom de l'objet 
	 */
	private String 	nom;
	
	/**
	 * Bonus d'attaque confere par l'objet [0, 100] 
	 */
	private int	bonusAttaque;
	
	/**
	 * Bonus de defense/bouclier confere par l'objet [0, 100]
	 */
	private int	bonusBouclier;
	
	/**
	 * Bonus de capacite a esquiver un coup [0, 100]
	 */
	private int	bonusEsquive;
	
	/**
	 * Bonus de points de vie [0, 100]
	 */
	private int	bonusPV;
	
	/**
	 * Bonus de capacite de port en nombre de 'cases' (emplacements) [-5, 5]
	 */
	private int	bonusSac;			

	
	/**
	 * Cree un item de nom <i>nom</i>, de capacite d'attaque <i>bonusAttaque</i>,
	 * de capacite de bouclier <i>bonusBouclier</i>, de capacite d'esquive
	 * <i>bonusEsquive</i>, conferant un bonus de vie <i>bonusPV</i> et un bonus
	 * de sac <i>bonusSac</i>
	 * @param nom nom de l'objet
	 * @param bonusAttaque bonus de degat d'attaque que confere l'objet
	 * @param bonusBouclier bonus de defense du bouclier que confere l'objet
	 * @param bonusEsquive bonus de capacite d'esquive que confere l'objet
	 * @param bonusPV bonus de vie que confere l'objet
	 * @param bonusSac bonus du nombre de 'cases' que confere l'objet
	 */
	public Item(String nom, int bonusAttaque, int bonusBouclier, 
			int bonusEsquive, int bonusPV, int bonusSac) {
		
		super(nom);
		this.setPV(1);
		this.bonusAttaque = bonusAttaque;
		this.bonusBouclier = bonusBouclier;
		this.bonusPV = bonusPV;
		this.bonusEsquive = bonusEsquive;
		this.bonusSac = bonusSac;
	}
	
	/**
	 * Retourne le bonus d'attaque que confere l'objet
	 * @return bonus d'attaque
	 */
	public int getBonusAttaque(){
		return bonusAttaque;
	}
	
	/**
	 * Retourne le bonus de bouclier que confere l'objet
	 * @return bonus de bouclier
	 */
	public int getBonusBouclier(){
		return bonusBouclier;
	}
	
	/**
	 * Retourne le bonus d'esquive que confere l'objet
	 * @return bonus d'esquive
	 */
	public int getBonusEsquive(){
		return bonusEsquive;
	}
	
	/**
	 * Retourne le bonus de vie que confere l'objet
	 * @return bonus de points de vie
	 */
	public int getBonusPV(){
		return bonusPV;
	}
	
	/**
	 * Retourne le nombre de 'cases' supplementaires qu'apport le bonus au sac
	 * @return bonus de contenance du sac
	 */
	public int getBonusSac(){
		return bonusSac;
	}	

}
