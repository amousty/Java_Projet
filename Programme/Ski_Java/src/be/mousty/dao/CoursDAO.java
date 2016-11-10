package be.mousty.dao;

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
	public Cours find(int id) {
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

	public ArrayList<Cours> getList() {
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
	
	public ArrayList<Cours> getMyListSelonID(int idMoniteur, int nonUsed, int nonUsed2, String nonUsed3){
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
	
	public String calculerPlaceCours(int idCours, int idSemaine, int idMoniteur){
		PreparedStatement pst_rec_cou = null;
		PreparedStatement pst_cpt_cou = null;
		try {
			int min = 0;
			int max = 0;
			int placeActuelle = 0;
			String sql_rec_cou = "SELECT * FROM Cours WHERE numCours = ?";
			pst_rec_cou = this.connect.prepareStatement(sql_rec_cou);
			pst_rec_cou.setInt(1, idCours);
			ResultSet res_rec_cou = pst_rec_cou.executeQuery();
			
			String sql_cpt_res = "SELECT Count(*) AS total FROM ReservationCours "
					+ "INNER JOIN Cours ON Cours.numCours = ReservationCours.numCours "
					+ "INNER JOIN CoursSemaine ON CoursSemaine.numCours = Cours.numCours "
					+ "INNER JOIN CoursMoniteur ON CoursMoniteur.numCours = Cours.numCours "
					+ "WHERE ReservationCours.numCours = ? AND CoursSemaine.numSemaine = ? AND CoursMoniteur.numMoniteur = ? ;";
			
			pst_cpt_cou = this.connect.prepareStatement(sql_cpt_res);
			
			pst_cpt_cou.setInt(1, idCours);
			pst_cpt_cou.setInt(2, idSemaine);
			pst_cpt_cou.setInt(3, idMoniteur);
			
			
			ResultSet res_cpt_cou = pst_cpt_cou.executeQuery();
			
			while (res_rec_cou.next()) {
				min = res_rec_cou.getInt("minEleve");
				max = res_rec_cou.getInt("maxEleve");
			}
			
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
	public int valeurReduction(int numSem) {
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


	
}