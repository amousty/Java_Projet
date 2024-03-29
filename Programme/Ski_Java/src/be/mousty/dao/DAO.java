package be.mousty.dao;
/**
Classe DAO permettant � effectuer des requ�tes et les transformer en objet POJO.
@author Adrien MOUSTY
@version Finale 1.3.3
@category DAO
*/
import java.sql.Connection;
import java.util.ArrayList;

public abstract class DAO<T> {
	protected Connection connect = null;
	public DAO(Connection conn) { this.connect = conn; }

	public abstract int 			create				(T obj);
	public abstract boolean 		delete				(T obj);
	public abstract boolean 		update				(T obj);
	public abstract T 				find				(int id);
	public abstract ArrayList<T> 	getList				();
	public abstract T 				getId 				(T obj);
	public abstract ArrayList<T> 	getListSelonCriteres(T obj);
	public abstract ArrayList<T> 	getMyListSelonID 	(int id1 , long id2, int id3, String str1);

	// R�servation
	public abstract boolean updateAssurance(int numEleve, int numSemaine, String periode);
	public abstract int valeurReduction(int numSem, int numEleve, double prixCours);
	public abstract long getDateDebutReserv(int numReserv);
	public abstract ArrayList<T> getReservationAnnulee(int numUtilisateur, int typeUtilisateur);
	public abstract String getCategorieReservation(int numMoniteur, int numSemaine, String periode);	
	
	// Cours
	public abstract String calculerPlaceCours(int numCours, long numSemaine, int idMoniteur);

	// DisponibiliteMoniteur
	public abstract void creerTouteDisponibilites();
	public abstract void creerTouteDisponibilitesSelonMoniteur(int i);
	
	// Semaine
	public abstract void AjouterSemainesDansDB(String start, String end);
}