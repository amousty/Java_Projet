package be.mousty.pojo;

import java.util.ArrayList;

public class Moniteur extends Utilisateur{
	// VARIABLES 
	private int numMoniteur;
	private int anneeDexp;
	private ArrayList<Accreditation> listAccreditation = new ArrayList<Accreditation>();
	
	// CONSTRUCTEUR SANS ARGUMENTS
	public Moniteur(){}
	
	// PROPRIETES
	public int getAnneeExp		() 			{ return anneeDexp; }
	public int getNumMoniteur	() 			{ return numMoniteur; }
	public void setNumMoniteur	(int el) 	{ this.numMoniteur = el;}
	public void setAnneeExp 	(int el) 	{ this.anneeDexp = el; }
	public ArrayList<Accreditation> getAccrediList() { return listAccreditation; }
	public void setAccrediList (ArrayList<Accreditation> accrediList) { this.listAccreditation = accrediList; 	}
}