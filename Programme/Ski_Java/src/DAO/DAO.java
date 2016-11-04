package DAO;
import java.sql.Connection;
import java.util.ArrayList;

public abstract class DAO<T> {
	protected Connection connect = null;
	public DAO(Connection conn) { this.connect = conn; }

	public abstract int create(T obj);
	public abstract boolean delete(T obj);
	public abstract boolean update(T obj);
	public abstract T find(int id);
	public abstract ArrayList<T> getList();

	// Réservation
	public abstract ArrayList<T> getMyList(int idPersonne);
	public abstract ArrayList<T> getListSemainePerdiodeMoniteur(int numMoniteur, int numSemaine, String periode);
	public abstract boolean updateAssurance(int numEleve, int numSemaine, String periode);
	
	// Cours
	public abstract String calculerPlaceCours(int numCours, int numSemaine);
	public abstract ArrayList<T> getListCoursSelonId(int idMoniteur);
	
	// Cours Collectif
	public abstract ArrayList<T> getListCoursCollectifSelonId(int numMoniteur, int numEleve, String periode);
	
	// Cours Particulier
	public abstract ArrayList<T> getListCoursParticulierSelonId(int numMoniteur, String periode);

	// Eleve
	public abstract ArrayList<T> getListEleveSelonAccredProfEtCours(int numSemaine, int numMoniteur, String periode, int cours);

	// DisponibiliteMoniteur
	public void creerTouteDisponibilites() { }
	public void creerTouteDisponibilitesSelonMoniteur(int i) { }
}