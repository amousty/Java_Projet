package POJO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;

import DAO.AbstractDAOFactory;
import DAO.DAO;

public class Moniteur extends Utilisateur{
	// VARIABLES 
	private int anneeDexp;
	private int numMoniteur;
	private ArrayList<Accreditation> listAccreditation = new ArrayList<Accreditation>();
	AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<Moniteur> MoniteurDao = adf.getMoniteurDAO();
	
	// CONSTRUCTEURS
	public Moniteur(){}
	public Moniteur(int numPersonne, String nom, String pre, String adresse, String sexe, Date dateNaissance, 
			String pseudo, String mdp, int typeUtilisateur, ArrayList<Accreditation> listAccreditation){
		super(numPersonne, nom, pre, adresse, sexe, dateNaissance, pseudo, mdp, typeUtilisateur);
		this.anneeDexp 	= 0 ;// A CHANGER via une methode calculerAnneeExp
		//this.numMoniteur = numMoniteur;
		this.listAccreditation = listAccreditation;
	}
	
	// METHODES 
		
	// Pour ne pas additionner 2 fois le m�me moniteur
	public void addAccreditation(Accreditation ac){
		if(!listAccreditation.contains(ac))
			listAccreditation.add(ac);
	}
	public void removeAccreditation(Accreditation ac){ this.listAccreditation.remove(ac); }
	public boolean equals(Moniteur mo){ return this.getNumMoniteur() == mo.getNumMoniteur(); }
	
	public ArrayList<Moniteur> getListMoniteur(){ return MoniteurDao.getList(); }
	
	// METHODE SURCHARGEE
	@Override
	public String toString() { 
		return 
			super.toString()+ System.getProperty("line.separator")
			+ "MONITEUR, a accumule" + anneeDexp + " ann�e(s) d'ex�rience." + System.getProperty("line.separator");
	}
	
	// PROPRIETES
	public int getAnneeExp		() 			{ return anneeDexp; }
	public int getNumMoniteur	() 			{ return numMoniteur; }
	public void setNumMoniteur	(int el) 	{ this.numMoniteur = el;}
	public void setAnneeExp 	(int el) 	{ this.anneeDexp = el; }
	public ArrayList<Accreditation> getAccrediList() { return listAccreditation; }
	public void setAccrediList						(ArrayList<Accreditation> accrediList) { this.listAccreditation = accrediList; 	}
}
