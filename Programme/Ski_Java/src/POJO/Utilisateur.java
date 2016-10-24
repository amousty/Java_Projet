package POJO;

import java.sql.Date;

public abstract class Utilisateur extends Personne{
	// VARIABLES
	private String pseudo;
	private String mdp;
	private int typeUtilisateur;
	private int numUtilisateur;

	// CONSTRUCTEURs
	public Utilisateur(){}
	public Utilisateur(String nom, String pre, String adresse, String sexe, Date dateNaissance,
			String pseudo, String mdp, int typeUtilisateur){
		super(nom, pre, adresse, sexe, dateNaissance);
		this.pseudo 			= pseudo;
		this.mdp 				= mdp;
		this.typeUtilisateur 	= typeUtilisateur;
		//this.numUtilisateur 	= numUtilisateur;
	}

	// METHODES

	// METHODES SURCHARGEES
	@Override
	public String toString() { 
		return 
				"Utilisateur." + System.getProperty("line.separator")
				+ "User name    : " + pseudo +  System.getProperty("line.separator")
				+ "Mot de passe : " + mdp + System.getProperty("line.separator");
	}

	// PROPRIETES
	public String getPseudo			() { return pseudo; }
	public String getMdp			() { return mdp; }
	public int getTypeUtilisateur	() { return typeUtilisateur; }
	public int getNumUtilisateur	() { return numUtilisateur; }
	public void setPseudo			(String pseudo) 		{ this.pseudo = pseudo; }
	public void setMdp				(String mdp)			{ this.mdp = mdp; }
	public void setTypeUtilisateur	(int typeUtilisateur) 	{ this.typeUtilisateur = typeUtilisateur; }
	public void setNumUtilisateur	(int numUtilisateur) 	{ this.numUtilisateur = numUtilisateur; }
}
