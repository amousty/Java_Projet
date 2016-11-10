package be.mousty.pojo;

public class Client extends Utilisateur{
	// VARIABLES
	private int 	numClient;
	private String 	adresseFacturation;
	
	// CONSTRUCTEUR SANS ARGUMENTS
	public Client(){}
	
	// PROPRIETES
	public String getAdresseFacturation	() 				{ return adresseFacturation; }
	public int getNumClient				() 				{ return numClient; }
	public void setAdresseFacturation 	(String el) 	{ this.adresseFacturation  = el; }
	public void setNumClient 			(int numClient) { this.numClient = numClient; }
}