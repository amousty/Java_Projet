package POJO;

import java.sql.Date;
import java.util.Scanner;

public class Moniteur extends Utilisateur{
	// VARIABLES 
	private int anneeDexp;
	
	// CONSTRUCTEURS
	public Moniteur(){}
	public Moniteur(String nom, String pre, String adresse, String sexe, Date dateNaissance, int numPersonne, 
			String pseudo, String mdp, int typeUtilisateur){
		super(nom, pre, adresse, sexe, dateNaissance, numPersonne, pseudo, mdp, typeUtilisateur);
		this.anneeDexp 	= 0 ;// A CHANGER via une methode calculerAnneeExp
	}
	
	// METHODES 
	public void ajouterMoniteur() throws Exception{
		Scanner dateDiplomeMoniteur = new Scanner(System.in);
		Scanner sportPratiqueMoniteur = new Scanner(System.in);
		Scanner evaluationMoniteur = new Scanner(System.in);
		System.out.println("Ajout d'un moniteur");
		super.ajouterPersonne();
		
		System.out.print("Date de dipl�me du moniteur [jj-mm-yyyy] : "); setNom(dateDiplomeMoniteur.next());
		System.out.print("Sport enseign� par le moniteur : "); setNom(sportPratiqueMoniteur.next());
		System.out.print("Evaluation du moniteur (/10) : "); setNom(evaluationMoniteur.next());
	}
	
	// METHODE SURCHARGEE
	@Override
	public String toString() { 
		return 
			super.toString()+ System.getProperty("line.separator")
			+ "MONITEUR, a accumule" + anneeDexp + " ann�e(s) d'ex�rience." + System.getProperty("line.separator");
	}
	
	// PROPRIETES
	public int getAnneeExp	() 			{ return anneeDexp; }
	public void setAnneeExp (int el) 	{ this.anneeDexp = el; }
}
