package be.mousty.pojo;

import java.util.Date;

public class LigneAccreditation {
	// VARIABLES
	private int 	numAccreditation;
	private int 	numMoniteur;
	private Date 	dateAccreditation;
	
	// CONSTRUCTEUR SANS ARGUMENTSS
	public LigneAccreditation(){}
	
	// PROPRIETE
	public int getNumAccreditation		() { return numAccreditation; }
	public int getNumMoniteur			() { return numMoniteur; }
	public Date getDateAccreditation 	() { return dateAccreditation;}
	public void setNumAccreditation		(int el) 	{ this.numAccreditation = el; }
	public void setNumMoniteur			(int el) 	{ this.numMoniteur = el; }
	public void setDateAccreditation 	(Date el)	{ this.dateAccreditation = el;}
}
