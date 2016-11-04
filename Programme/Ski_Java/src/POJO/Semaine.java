package POJO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import DAO.AbstractDAOFactory;
import DAO.DAO;

import java.sql.Date;

public class Semaine {
	// VARIABLES
	private Date dateDebut;
	private Date dateFin;
	//private String descriptif;
	private boolean congeScolaire;
	private int numSemaineDansAnnee;
	private int numSemaine;
	AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<Semaine> SemaineDao = adf.getSemaineDAO();

	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	// CONSTRUCTEURS
	public Semaine(){}
	public Semaine(int numSemaine,  boolean congeScolaire, Date dateDebut, Date dateFin, int numSemaineDansAnnee){
		this.numSemaine 			= numSemaine;
		this.dateDebut 				= dateDebut;
		this.dateFin 				= dateFin;
		//this.descriptif 			= descriptif;
		this.congeScolaire 			= congeScolaire;
		this.numSemaineDansAnnee 	= numSemaineDansAnnee;
	}

	// METHODES
	// Juste faire tourner une fois pour ajouter les semaines dans la DB 
	public void AjouterSemainesDansDB(){
		// Date de d�but d'ajout
		//int jour = 03;
		//int mois = 12;
		//int annee = 2016;
		try{
			// date du jour
			//java.sql.Date now = new java.sql.Date(Calendar.getInstance().getTime().getTime());

			// test internet 
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date startDate = formatter.parse("2016-10-30");
			java.util.Date endDate = formatter.parse("2016-11-25");
			java.util.Date startDateWeek = null;
			java.util.Date endDateWeek = null;
			Calendar start = Calendar.getInstance();
			start.setTime(startDate);
			Calendar end = Calendar.getInstance();
			end.setTime(endDate);

			for (java.util.Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 7), date = start.getTime()) {
				/* 	�	Du 24/12/2016 => 07/01/2017 (No�l / Nouvel an)
					�	Du 25/02/2017 au 04/03/2017 (Carnaval)
					�	Du 01/04/2017 au 15/04/2017 (P�ques) */
				boolean estFerme = false;
				if((date.after(formatter.parse("2016-12-24")) && date.before(formatter.parse("2017-01-07"))) 
						|| (date.after(formatter.parse("2017-02-25")) && date.before(formatter.parse("2017-03-04")))
						|| date.after(formatter.parse("2017-04-01")) && date.before(formatter.parse("2017-04-15"))){
					// In between
					estFerme = true;
				}


				// Do your job here with `date`.
				startDateWeek = date;
				Calendar c = Calendar.getInstance(); 
				c.setTime(date); 
				c.add(Calendar.DATE, 6);
				endDateWeek = c.getTime();
				//System.out.println("Debut : " + startDateWeek + " - Fin : " + endDateWeek + " -> " + estFerme + " numSem : " + c.get(Calendar.WEEK_OF_YEAR));

				// Initialisation des propri�t�s
				setNumSemaine(-1);
				setCongeScolaire(estFerme);
				setDateDebut(new java.sql.Date(startDateWeek.getTime()));
				setDateFin(new java.sql.Date(endDateWeek.getTime()));
				setNumSemaineDansAnnee(c.get(Calendar.WEEK_OF_YEAR));

				// Ajout dans la DB
				SemaineDao.create(this);
			}
		}
		catch(Exception e){
			e.getStackTrace();
		}
	}
	
	private boolean EstEnPeriodeDeConge(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return !((date.after(formatter.parse("2016-12-24")) && date.before(formatter.parse("2017-01-07"))) 
					|| (date.after(formatter.parse("2017-02-25")) && date.before(formatter.parse("2017-03-04")))
					|| date.after(formatter.parse("2017-04-01")) && date.before(formatter.parse("2017-04-15")));
		} 
		catch (ParseException e) { e.printStackTrace(); return false; }
	}
	public ArrayList<Semaine> getListSemaine(){
		return SemaineDao.getList();
	}

	public ArrayList<Semaine> getListSemaineSelonDateDuJour(){
		//AjouterSemainesDansDB();
		// ATTENTION SEMAINE NUMANNEE 44 A 47 SONT A SUPPRIMER
		Semaine S = new Semaine();
		ArrayList<Semaine> listeRetour = new ArrayList<Semaine>();
		ArrayList<Semaine> listSemaine = S.getListSemaine();
		//  Aujourd'hui
		java.sql.Date today = new java.sql.Date(Calendar.getInstance().getTime().getTime());
		int jourDelaisMax = EstEnPeriodeDeConge(today) ? 7 : 31; 
		Calendar c = Calendar.getInstance(); 
		c.setTime(today); 
		// 1 mois si p�riode scolaire, sinon 7 jours.
		c.add(Calendar.DATE, jourDelaisMax); // Ajout d'un mois ou d'un jour si c'est un p�riode de cong� ou pas.
		java.util.Date maxDateToDisplay = c.getTime();
		
		if (listSemaine != null)
			for(Semaine s : listSemaine){
				// N'affiche que les semaines ou il n'y a pas de cong�s et qui ne sont pas pass�es.
				if (!s.getCongeScolaire() && s.dateDebut.after(today) && s.dateDebut.before(maxDateToDisplay)) 
					listeRetour.add(s);
			}
		return listeRetour;
	}
	
	// FONCTION SURCHARGEE
	@Override
	public String toString() { 
		return  //descriptif + System.getProperty("line.separator") 
				"P�riode de cong� scolaire : " + congeScolaire + System.getProperty("line.separator")
				+ "P�riode comprise entre le " + dateDebut.toString() + " et le " + dateFin.toString() + System.getProperty("line.separator"); 
	}


	// PROPRIETE
	public Date getDateDebut			() { return dateDebut; }
	public Date getDateFin				() { return dateFin; }
	//public String getDescriptif			() { return descriptif; }
	public boolean getCongeScolaire		() { return congeScolaire; }
	public int getNumSemaineDansAnnee	() { return numSemaineDansAnnee; }
	public int getNumSemaine			() { return numSemaine; }
	public void setDateDebut			(Date dateDebut) 			{ this.dateDebut = dateDebut; }
	public void setDateFin				(Date dateFin) 				{ this.dateFin = dateFin; }
	//public void setDescriptif			(String descriptif) 		{ this.descriptif = descriptif; }
	public void setCongeScolaire		(boolean congeScolaire) 	{ this.congeScolaire = congeScolaire; }
	public void setNumSemaineDansAnnee	(int numSemaineDansAnnee) 	{ this.numSemaineDansAnnee = numSemaineDansAnnee; }
	public void setNumSemaine			(int numSemaine) 			{ this.numSemaine = numSemaine; }
}
