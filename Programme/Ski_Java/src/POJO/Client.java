package POJO;

import java.sql.Date;
import java.util.Scanner;

public class Client extends Utilisateur{
	// VARIABLES
	private int numClient;
	
	// CONSTRUCTEURs
	public Client(){}
	public Client(String nom, String pre, String adresse, String sexe, Date dateNaissance, int numPersonne,
			String pseudo, String mdp, int typeUtilisateur, int numClient){
		super(nom, pre, adresse, sexe, dateNaissance, numPersonne, pseudo, mdp, typeUtilisateur);
		this.numClient = numClient;
	}
	
	// METHODES
	public void ajouterClient() throws Exception{
		Scanner numClient = new Scanner(System.in);
		System.out.println("Ajout d'un client");
		super.ajouterPersonne();
		
		System.out.print("Num�ro de client : "); setNom(numClient.next());
	}
	
	
	// METHODES SURCHARGEES
	@Override
	public String toString() { 
		return 
			super.toString()+ System.getProperty("line.separator")
			+ "CLIENT." + System.getProperty("line.separator")
			+ "N� de client : " + numClient + System.getProperty("line.separator");
	}
	
	// PROPRIETES
	public int getNumClient		() 				{ return numClient; }
	public void setNumClient 	(int numClient) { this.numClient = numClient; }
}
