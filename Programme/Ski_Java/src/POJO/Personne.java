package POJO;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Personne {
	// VARIABLES 
	private String 	nom;
	private String 	pre;
	private String 	adresse;
	private String 	sexe;
	private Date 	dateNaissance;
	private int 	numPersonne;
		
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	// CONSTRUCTEURS
	public Personne(){}
	public Personne(String nom, String pre, String adresse, String sexe, Date dateNaissance, int numPersonne){
		this.nom 			= nom;
		this.pre 			= pre;
		this.adresse 		= adresse;
		this.sexe 			= sexe;
		this.dateNaissance 	= dateNaissance;
		this.numPersonne 	= numPersonne;
	}
	
	
	// METHODES
	public double calculerAge(){
		Date now = new Date(Calendar.getInstance().getTime().getTime());
		long diffInMillies = now.getTime() - this.getDateNaissance().getTime();
		long tu =  TimeUnit.DAYS.convert(diffInMillies,TimeUnit.MILLISECONDS);
				//- this.getDateNaissance();
		return (double)tu/365;
	}
	
	public void ajouterPersonne() throws Exception{
		Scanner sc = new Scanner(System.in);
//		Scanner nomPersonne = new Scanner(System.in);
//		Scanner prePersonne = new Scanner(System.in);
//		Scanner villePersonne = new Scanner(System.in);
//		Scanner ruePersonne = new Scanner(System.in);
//		Scanner sexePersonne = new Scanner(System.in);
//		Scanner dateNaissancePersonne = new Scanner(System.in);
		do{
			System.out.println();
			System.out.println("Nom de la personne : "); setNom(sc.next());
			System.out.println("Pr�nom de la personne : "); setPre(sc.next());
			System.out.println("Sexe de la personne  [m/f]: "); while (!sc.hasNext("[mf]")) { System.out.print("Reponse incorrecte, entrez m ou f.");setSexe(sc.next()); } // Autorise juste h ou f
			System.out.println("Adresse de la personne: "); setAdresse(sc.next());
		} while(!sc.hasNext("[a-z]")); // autorise que des caracteres
		//System.out.println("Date de naissance de la personne [jj/mm/yyyy] : "); while (!sc.hasNext("[/123456789]")) setDateNaissance(Utilitaire.stringToDate(sc.next()));
	}
	
	// METHODE SURCHARGEE
	@Override
	public String toString() { 
		return 
			nom.toUpperCase() + " " + pre + ", " + sexe + System.getProperty("line.separator") 
			+ "Date de naissance : " + dateFormat.format(dateNaissance) + " (" + (int)calculerAge() + ")" + System.getProperty("line.separator") 
			+ "Residence : " + adresse.toUpperCase() + System.getProperty("line.separator");
	}
	
	// PROPRIETE
	public String getNom		() { return nom; }
	public String getPre		() { return pre; }
	public String getAdresse	() { return adresse; }
	public String getSexe		() { return sexe; }
	public Date getDateNaissance() { return dateNaissance; }
	public int getNumPersonne 	() { return numPersonne; }
	public void setNom			(String nom) 			{ this.nom = nom; }
	public void setPre			(String pre) 			{ this.pre = pre; }
	public void setAdresse		(String adresse) 		{ this.adresse = adresse;	}
	public void setSexe			(String sexe) 			{ this.sexe = sexe; }
	public void setDateNaissance(Date dateNaissance) 	{ this.dateNaissance = dateNaissance; }
	public void setNumPersonne	(int el)				{ this.numPersonne = el; }
}
