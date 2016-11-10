package be.mousty.accessToDao;

import java.util.ArrayList;

import be.mousty.dao.AbstractDAOFactory;
import be.mousty.dao.DAO;
import be.mousty.pojo.Client;
import be.mousty.pojo.Cours;
import be.mousty.pojo.Eleve;
import be.mousty.pojo.Moniteur;
import be.mousty.pojo.Reservation;
import be.mousty.pojo.Semaine;

public class ReservationATD {
	// VARIABLES
	Semaine S;
	Cours C;
	Eleve E;
	Client Cli;
	Moniteur M;
	private int heureDebut;
	private int heureFin;
	private int numReservation;
	private boolean aUneAssurance;

	// CONSTRUCTEURS
	public ReservationATD() {
	}

	public ReservationATD(int heureDebut, int heureFin, int numReservation, boolean aUneAssurance, Semaine S, Cours C,
			Eleve E, Client Cli, Moniteur M) {
		this.heureDebut = heureDebut;
		this.heureFin = heureFin;
		this.numReservation = numReservation;
		this.aUneAssurance = aUneAssurance;
		this.C = C;
		this.S = S;
		this.E = E;
		this.Cli = Cli;
		this.M = M;
	}

	public ReservationATD(Reservation R) {
		this.heureDebut = R.getHeureDebut();
		this.heureFin = R.getHeureFin();
		this.numReservation = R.getNumReservation();
		this.aUneAssurance = R.getAUneAssurance();
		this.C = R.getCours();
		this.S = R.getSemaine();
		this.E = R.getEleve();
		this.Cli = R.getClient();
		this.M = R.getMoniteur();
	}

	// APPEL AUX METHODES DAO DANS LES CLASSES METIER
	AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<Reservation> ReservationDAO = adf.getReservationDAO();

	public int create(Reservation r) {
		return ReservationDAO.create(r);
	}

	public boolean delete(Reservation r) {
		return ReservationDAO.delete(r);
	}

	public Reservation getId(Reservation r) {
		return ReservationDAO.getId(r);
	}

	public boolean update(Reservation r) {
		return ReservationDAO.update(r);
	}

	public Reservation find(int id) {
		return ReservationDAO.find(id);
	}

	public ArrayList<Reservation> getListRes() {
		return ReservationDAO.getList();
	}

	public ArrayList<Reservation> getMyList(int id) {
		return ReservationDAO.getMyListSelonID(id, -1, -1, "");
	}

	public ArrayList<Reservation> getListSelonCriteres(Reservation r) {
		return ReservationDAO.getListSelonCriteres(r);
	}

	public int valeurReduction(int numSemaine) {
		return ReservationDAO.valeurReduction(numSemaine);
	}

	public boolean updateAssurance(int numEleve, int numSemaine, String periode) {
		return ReservationDAO.updateAssurance(numEleve, numSemaine, periode);
	}

	// FONCTION SURCHARGEE
	@Override
	public String toString() {
		return "Heure de d�but de s�ance : " + heureDebut + System.getProperty("line.separator")
				+ "Heure de fin de s�ance : " + heureFin + System.getProperty("line.separator") + S.toString()
				+ System.getProperty("line.separator") + C.toString() + System.getProperty("line.separator")
				+ E.toString() + System.getProperty("line.separator") + Cli.toString()
				+ System.getProperty("line.separator") + M.toString() + System.getProperty("line.separator");
	}

	// METHODES
	public ArrayList<ReservationATD> getMyListATD(int id) {
		ArrayList<Reservation> listDispo = getMyList(id);
		ArrayList<ReservationATD> listDispoATD = new ArrayList<ReservationATD>();
		for (int i = 0; i < listDispo.size(); i++) {
			ReservationATD RATD = new ReservationATD();
			RATD.setHeureDebut(listDispo.get(i).getHeureDebut());
			RATD.setHeureFin(listDispo.get(i).getHeureDebut());
			RATD.setAUneAssurance(listDispo.get(i).getAUneAssurance());
			RATD.setSemaine(listDispo.get(i).getSemaine());
			RATD.setCours(listDispo.get(i).getCours());
			RATD.setEleve(listDispo.get(i).getEleve());
			RATD.setClient(listDispo.get(i).getClient());
			RATD.setMoniteur(listDispo.get(i).getMoniteur());
			listDispoATD.add(RATD);
		}
		return listDispoATD;
	}
	
	public int getIdReserv(){
		Reservation R = new Reservation();
		R.setSemaine(S);
		R.setCours(C);
		R.setClient(Cli);
		R.setEleve(this.getEleve());
		return (getId(R).getNumReservation());
	}

	public int createReservation() {
		Reservation R = new Reservation();
		R.setHeureDebut(this.getHeureDebut());
		R.setHeureFin(this.getHeureFin());
		// R.setNumReservation(this.getNumReservation());
		R.setAUneAssurance(this.getAUneAssurance());
		R.setSemaine(S);
		R.setCours(C);
		R.setEleve(E);
		R.setClient(Cli);
		R.setMoniteur(M);
		System.out.println(R.getHeureDebut());
		return ReservationDAO.create(R);
	}

	// public void deleteReservation () { ReservationDAO.delete(null); }

	// PROPRIETE
	public int getHeureDebut() {
		return heureDebut;
	}

	public int getHeureFin() {
		return heureFin;
	}

	public int getNumReservation() {
		return numReservation;
	}

	public Semaine getSemaine() {
		return S;
	}

	public Cours getCours() {
		return C;
	}

	public Eleve getEleve() {
		return E;
	}

	public Client getClient() {
		return Cli;
	}

	public Moniteur getMoniteur() {
		return M;
	}

	public boolean getAUneAssurance() {
		return aUneAssurance;
	}

	public void setHeureDebut(int heureDebut) {
		this.heureDebut = heureDebut;
	}

	public void setHeureFin(int heureFin) {
		this.heureFin = heureFin;
	}

	public void setNumReservation(int numReservation) {
		this.numReservation = numReservation;
	}
	// public void serMoniteur(Moniteur M) { this.M = M;}

	public void setAUneAssurance(boolean aUneAssurance) {
		this.aUneAssurance = aUneAssurance;
	}
	
	public void setSemaine 				(Semaine S)  			{ this.S = S;}
	public void setCours 				(Cours C)  				{ this.C = C;}
	public void setEleve 				(Eleve E)  				{ this.E = E;}
	public void setClient 				(Client Cli)  			{ this.Cli = Cli;}
	public void setMoniteur 			(Moniteur M)  			{ this.M = M;}
}