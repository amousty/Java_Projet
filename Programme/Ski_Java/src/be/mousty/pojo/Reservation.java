package be.mousty.pojo;

public class Reservation {
	// VARIABLES
	private int numReservation;
	private int heureDebut;
	private int heureFin;
	private boolean aPrisAssurance;
	Semaine S;
	Cours 	C;
	Eleve	E;
	Client	Cli;
	Moniteur M;
	
	
	
	// CONSTRUCTEURS
	public Reservation(){}
	/*public Reservation(int heureDebut, int heureFin, int numReservation, boolean aPrisAssurance, Semaine S, Cours C, Eleve E, Client Cli, Moniteur M){
		this.heureDebut 		= heureDebut;
		this.heureFin 			= heureFin;
		this.numReservation 	= numReservation;
		this.aPrisAssurance 	= aPrisAssurance;
		this.C 					= C;
		this.S 					= S;
		this.E 					= E;
		this.Cli 				= Cli;
		this.M 					= M;
	}*/
	
	// PROPRIETE
	public int 		getHeureDebut		() { return heureDebut; }
	public int 		getHeureFin			() { return heureFin; }
	public int 		getNumReservation	() { return numReservation; }
	public Semaine 	getSemaine			() { return S;}
	public Cours 	getCours			() { return C;}
	public Eleve 	getEleve			() { return E;}
	public Client 	getClient			() { return Cli;}
	public Moniteur getMoniteur			() { return M;}
	public boolean 	getAUneAssurance	() { return aPrisAssurance; }
	public void setHeureDebut			(int heureDebut) 		{ this.heureDebut = heureDebut; }
	public void setHeureFin				(int heureFin) 			{ this.heureFin = heureFin; }
	public void setNumReservation		(int numReservation) 	{ this.numReservation = numReservation; }
	public void setAUneAssurance		(boolean aUneAssurance) { this.aPrisAssurance = aUneAssurance; }
	public void setSemaine 				(Semaine S)  			{ this.S = S;}
	public void setCours 				(Cours C)  				{ this.C = C;}
	public void setEleve 				(Eleve E)  				{ this.E = E;}
	public void setClient 				(Client Cli)  			{ this.Cli = Cli;}
	public void setMoniteur 			(Moniteur M)  			{ this.M = M;}
}