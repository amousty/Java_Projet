package be.mousty.dao;
/**
	Classe DAO permettant � effectuer des requ�tes et les transformer en objet POJO.
	@author Adrien MOUSTY
	@version Finale 1.3.3
	@category DAO
*/
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import be.mousty.pojo.Cours;

public class CoursDAO extends DAO<Cours> {
	public CoursDAO(Connection conn) { super(conn); }

	@Override
	public int create		(Cours obj) { return -1; }
	public boolean delete	(Cours obj) { return false; }
	public boolean update	(Cours obj) { return false; }

	// On cherche une Cours gr�ce � son id
	/**
		Objectif : Retourner un enregistrement de la DB par rapport � sa cl� primaire.
		@version Finale 1.3.3
		@param la valeur de la cl� primaire.
		@return Une instance de l'objet initialis�e avec les valeurs issue de la DB.
	 */
	@Override public Cours find(int id) {
		Cours C = new Cours();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM Cours WHERE numCours = ?;";
			pst = this.connect.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet res_cou = pst.executeQuery();
			while (res_cou.next()) {
				C.setNumCours(res_cou.getInt("numCours"));
				C.setNomSport(res_cou.getString("nomSport"));
				C.setPrix(res_cou.getInt("prix"));
				C.setMinEl(res_cou.getInt("minEleve"));
				C.setMaxEl(res_cou.getInt("maxEleve"));
				C.setPeriodeCours(res_cou.getString("periodeCours"));
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally { 
			if (pst != null) {
				try { pst.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return C;
	}

	/**
		Objectif : Retourner la liste compl�te des enregistrements contenu dans une table
		@version Finale 1.3.3
		@return La liste compl�te des utilisateurs.
	 */
	@Override public ArrayList<Cours> getList() {
		ArrayList<Cours> liste = new ArrayList<Cours>();
		PreparedStatement pst = null;
		
		try {
			String sql = "SELECT * FROM Cours";
			pst = this.connect.prepareStatement(sql);
			ResultSet res_cou = pst.executeQuery();
			while (res_cou.next()) {
				Cours C = new Cours();
				C.setNumCours(res_cou.getInt("numCours"));
				C.setNomSport(res_cou.getString("nomSport"));
				C.setPrix(res_cou.getInt("prix"));
				C.setMinEl(res_cou.getInt("minEleve"));
				C.setMaxEl(res_cou.getInt("maxEleve"));
				C.setPeriodeCours(res_cou.getString("periodeCours"));
				liste.add(C);
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst != null) {
				try { pst.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return liste;
	}
	
	/**
		Objectif : Retourner une liste filtr�e de cours.
		@version Finale 1.3.3
		@param Le num�ro du moniteur pour afficher les cours correspondant � ses accr�ditations.
		@return La liste filtr�e de cours.
	 */
	@Override public ArrayList<Cours> getMyListSelonID(int idMoniteur, long nonUsed, int nonUsed2, String nonUsed3){
		PreparedStatement pst_rec_cou = null;
		ArrayList<Cours> listSelonId = new ArrayList<Cours>();
		try {

			String sql_rec_cou = "SELECT * FROM Cours WHERE nomSport IN "
					+ "( SELECT nomAccreditation FROM Accreditation WHERE numAccreditation IN "
					+ "( SELECT numAccreditation FROM LigneAccreditation WHERE numMoniteur =  ?));";
			
			pst_rec_cou = this.connect.prepareStatement(sql_rec_cou);
			
			pst_rec_cou.setInt(1, idMoniteur);
			
			ResultSet res_cou = pst_rec_cou.executeQuery();
			
			while (res_cou.next()) {
				Cours C = new Cours();
				C.setNumCours(res_cou.getInt("numCours"));
				C.setNomSport(res_cou.getString("nomSport"));
				C.setPrix(res_cou.getInt("prix"));
				C.setMinEl(res_cou.getInt("minEleve"));
				C.setMaxEl(res_cou.getInt("maxEleve"));
				C.setPeriodeCours(res_cou.getString("periodeCours"));
				listSelonId.add(C);
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst_rec_cou != null) {
				try { pst_rec_cou.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return listSelonId;
	}
	
	/**
		@version Finale 1.3.3
		Objectif : Calculer la place maximale et minimale accepte pour qu ele cours soit valid� (uniquement ceux qui ont pay�s).
		@param Le num�ro de cours.
		@param Le num�ro de semaine, car un cours donn� le lundi et un autre le mardi n'ont pas le m�me nombre de r�servations.
		@param La p�riode pour savoir si le moniteur est dispo � ce moment.
		@return Une liste filtr�e de moniteurs.
	 */
	public String calculerPlaceCours(int idCours, long idSemaine, int idMoniteur){
		PreparedStatement pst_rec_cou = null;
		PreparedStatement pst_cpt_cou = null;
		try {
			int min = 0;
			int max = 0;
			// Recherch des cours
			String sql_rec_cou = "SELECT * FROM Cours WHERE numCours = ?";
			
			pst_rec_cou = this.connect.prepareStatement(sql_rec_cou);
			pst_rec_cou.setInt(1, idCours);
			ResultSet res_rec_cou = pst_rec_cou.executeQuery();
			while (res_rec_cou.next()) {
				min = res_rec_cou.getInt("minEleve");
				max = res_rec_cou.getInt("maxEleve");
			}
			
			String semaineOuJournee = " WHERE numSemaine = ? "; // semaine
			if (idSemaine > 1000 || idSemaine < 0) // journ�e
				semaineOuJournee = " WHERE dateDebutReserv = ? ";
			int placeActuelle = 0;
			
			String sql_cpt_res = "SELECT count(*) as total FROM ReservationCours "
					+ "INNER JOIN Reservation ON Reservation.numReservation = ReservationCours.numReservation "
					+ "WHERE aPaye = 1 AND numCours IN "
					+ "(SELECT numCours FROM Cours WHERE numCours IN "
						+ "(SELECT numCours FROM ReservationCours WHERE numCours = ? AND numCours IN "
							+ "(SELECT numCours FROM CoursSemaine  " + semaineOuJournee + " AND numCours IN "
								+ "(SELECT numCours FROM CoursMoniteur WHERE numMoniteur = ?)))) "
					+ "group by numCours;";
			
			pst_cpt_cou = this.connect.prepareStatement(sql_cpt_res);
			
			pst_cpt_cou.setInt(1, idCours);
			pst_cpt_cou.setLong(2, idSemaine);
			pst_cpt_cou.setInt(3, idMoniteur);
			
			ResultSet res_cpt_cou = pst_cpt_cou.executeQuery();
			while (res_cpt_cou.next()) { placeActuelle = res_cpt_cou.getInt("total"); }
			int seuilMini = min - placeActuelle;
			if (seuilMini < 0){ seuilMini = 0; }
			return (max - placeActuelle) + "-" + seuilMini;
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst_rec_cou != null || pst_cpt_cou != null) {
				try { 
					pst_rec_cou.close();
					pst_cpt_cou.close();
					}
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return -1 + "";
	}

	@Override
	public Cours getId(Cours obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Cours> getListSelonCriteres(Cours obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateAssurance(int numEleve, int numSemaine, String periode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int valeurReduction(int numSem, int numEleve, double prixCours) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void creerTouteDisponibilites() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void creerTouteDisponibilitesSelonMoniteur(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void AjouterSemainesDansDB(String start, String end) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getDateDebutReserv(int numReserv) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<Cours> getReservationAnnulee(int numUtilisateur, int typeUtilisateur) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getCategorieReservation(int numMoniteur, int numSemaine, String periode) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
