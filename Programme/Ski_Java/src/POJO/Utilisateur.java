package POJO;

import java.sql.Date;
import java.util.ArrayList;

import DAO.AbstractDAOFactory;
import DAO.DAO;

public class Utilisateur extends Personne{
	// VARIABLES
	private String pseudo;
	private String mdp;
	private int typeUtilisateur;
	private int numUtilisateur;
	AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<Utilisateur> UtilisateurDao = adf.getUtilisateurDAO();
	
	// CONSTRUCTEURs
	public Utilisateur(){}
	public Utilisateur(int numUtilisateur, String pseudo, String mdp, int typeUtilisateur){
		this.numUtilisateur 	= numUtilisateur;
		this.pseudo 			= pseudo;
		this.mdp 				= mdp;
		this.typeUtilisateur 	= typeUtilisateur;
	}
	
	public Utilisateur(int numPersonne, String nom, String pre, String adresse, String sexe, Date dateNaissance,
			String pseudo, String mdp, int typeUtilisateur){
		super(numPersonne, nom, pre, adresse, sexe, dateNaissance);
		this.pseudo 			= pseudo;
		this.mdp 				= mdp;
		this.typeUtilisateur 	= typeUtilisateur;
		//this.numUtilisateur 	= numUtilisateur;
	}

	// METHODES
	public int createUtilisateur(){
		return UtilisateurDao.create(this);
	}
	
	public Utilisateur returnUser(){
		ArrayList<Utilisateur> liste = new ArrayList<Utilisateur>();
		liste = UtilisateurDao.getList();
		for(Utilisateur U : liste){
			//System.out.println("U.pseudo : " + U.getPseudo() + " this.getPseudo : " + this.getPseudo());
			//System.out.println("U.mdp : " + U.getMdp() + " this.getMdp : " + this.getMdp());
			if (U.getMdp().equals(this.getMdp()) && U.getPseudo().equals(this.getPseudo()))
				return U;
		}
		return null;
	}
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
