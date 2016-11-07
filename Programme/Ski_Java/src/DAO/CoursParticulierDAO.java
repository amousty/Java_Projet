package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import POJO.Cours;
import POJO.CoursParticulier;

public class CoursParticulierDAO extends DAO<CoursParticulier> {
	AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<Cours> CoursDAO = adf.getCoursDAO();

	public CoursParticulierDAO(Connection conn) { super(conn); }

	@Override
	public int create		(CoursParticulier obj) { return -1; }
	public boolean delete	(CoursParticulier obj) { return false; }
	public boolean update	(CoursParticulier obj) { return false; }

	// On cherche une CoursParticulier gr�ce � son id
	public CoursParticulier find(int id) {
		CoursParticulier coursParticulier = new CoursParticulier();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM CoursParticulier INNER JOIN Cours ON CoursParticulier.numCoursParticulier = Cours.numCours WHERE numCoursParticulier = ?;";
			pst = this.connect.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet result = pst.executeQuery();
			while (result.next()) {
				// int numCours, String nomSport, double prix, int minEleve, int maxEleve, String periodeCours, int numCoursParticulier, int nombreHeures
				coursParticulier = new CoursParticulier(result.getInt("numCours"), result.getString("nomSport"), result.getInt("prix"),
						result.getInt("minEleve"), result.getInt("maxEleve"), result.getString("periodeCours"), result.getInt("nombreHeures"));
			}
			return coursParticulier;
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst != null) {
				try { pst.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return null;
	}

	public ArrayList<CoursParticulier> getList() {
		ArrayList<CoursParticulier> liste = new ArrayList<CoursParticulier>();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM CoursParticulier INNER JOIN Cours ON Cours.numCours = CoursParticulier.numCoursParticulier";
			pst = this.connect.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				CoursParticulier coursParticulier = new CoursParticulier(rs.getInt("numCours"), rs.getString("nomSport"), rs.getInt("prix"),
						rs.getInt("minEleve"), rs.getInt("maxEleve"), rs.getString("periodeCours"), rs.getInt("nombreHeures"));
				liste.add(coursParticulier);
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

	/*public ArrayList<CoursParticulier> getListCoursParticulierSelonId(int idMoniteur, String periode){
		//System.out.println("Entree fonc");
		ArrayList<Cours> listCours = CoursDAO.getListCoursSelonId(idMoniteur);
		ArrayList<CoursParticulier> listFull = getList();
		ArrayList<CoursParticulier> listSelonId = new ArrayList<CoursParticulier>();
		for (CoursParticulier CP : listFull){
			for (Cours C : listCours){
				if (CP.getNumCours() == C.getNumCours()){
					if(CP.getPeriodeCours().equals(periode)){
						listSelonId.add(CP);
					}
				}
			}
		}
		return listSelonId;
	}*/
	
	public ArrayList<CoursParticulier> getListCoursParticulierSelonId(int idMoniteur, String periode, int numSemaine){
		PreparedStatement pst_lst_cou1 = null;
		PreparedStatement pst_lst_cou2 = null;
		ArrayList<CoursParticulier> listSelonId = new ArrayList<CoursParticulier>();
		try {
			String verifPeriode;
			switch(periode){
				case "12-13": verifPeriode = " IN('12-13') ";
					break;
				case "13-14": verifPeriode = " IN('13-14') ";
					break;
				case "12-14": verifPeriode = " IN('12-14') ";
					break;
				default : verifPeriode = " = ? ";
					break;
			}
			String sql1 = "SELECT * from Cours "
			+ "INNER JOIN CoursParticulier ON CoursParticulier.numCoursParticulier = Cours.numCours "
			+ "WHERE nomSport in "
			+ "(SELECT nomAccreditation from accreditation where numAccreditation in "
			+ "(SELECT numAccreditation from ligneAccreditation where numMoniteur = ?)) "
			
			+ "AND   Cours.numCours IN "
            + "(SELECT CoursMoniteur.numCours FROM CoursMoniteur "
           	+ "INNER JOIN CoursSemaine ON CoursSemaine.numCours = CoursMoniteur.numCours "         
           	+ "WHERE  CoursSemaine.numCours IN "
            + "(SELECT CoursSemaine.numCours FROM Cours WHERE CoursSemaine.numSemaine = ? AND periodeCours " + verifPeriode + " AND numMoniteur = ?));";
			pst_lst_cou1 = this.connect.prepareStatement(sql1);
			pst_lst_cou1.setInt(1, idMoniteur);
			pst_lst_cou1.setInt(2, numSemaine);
			//pst_lst_cou1.setString(4, periode);
			pst_lst_cou1.setInt(3, idMoniteur);
			ResultSet res_lst_cou1 = pst_lst_cou1.executeQuery();
			while (res_lst_cou1.next()) {
				CoursParticulier coursParticulier = new CoursParticulier(res_lst_cou1.getInt("numCours"), res_lst_cou1.getString("nomSport"), res_lst_cou1.getInt("prix"),
						res_lst_cou1.getInt("minEleve"), res_lst_cou1.getInt("maxEleve"), res_lst_cou1.getString("periodeCours"), res_lst_cou1.getInt("nombreHeures"));
				listSelonId.add(coursParticulier);
			}
			if (listSelonId.isEmpty()){
				String sql2 = "SELECT * from Cours "
						+ "INNER JOIN CoursParticulier ON CoursParticulier.numCoursParticulier = Cours.numCours "
						+ "WHERE PeriodeCours  " + verifPeriode + "  AND nomSport in "
						+ "(SELECT nomAccreditation from accreditation where numAccreditation in "
						+ "(SELECT numAccreditation from ligneAccreditation where numMoniteur = ?));";
				pst_lst_cou2 = this.connect.prepareStatement(sql2);
				//pst_lst_cou2.setString(1, periode);
				pst_lst_cou2.setInt(1, idMoniteur);
						ResultSet res_lst_cou2 = pst_lst_cou2.executeQuery();
						while (res_lst_cou2.next()) {
							CoursParticulier coursParticulier = new CoursParticulier(res_lst_cou2.getInt("numCours"), res_lst_cou2.getString("nomSport"), res_lst_cou2.getInt("prix"),
									res_lst_cou2.getInt("minEleve"), res_lst_cou2.getInt("maxEleve"), res_lst_cou2.getString("periodeCours"), res_lst_cou2.getInt("nombreHeures"));
							listSelonId.add(coursParticulier);
						}
						pst_lst_cou2.close();
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst_lst_cou1 != null) {
				try { pst_lst_cou1.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return listSelonId;
		
		
		
	}

	@Override public String calculerPlaceCours(int numCours, int numSemaine, int numMoniteur) { return -1 + ""; }
	@Override public ArrayList<CoursParticulier> getListCoursSelonId(int idMoniteur) { return null; }
	@Override public ArrayList<CoursParticulier> getListCoursCollectifSelonId(int numMoniteur, int numEleve, String periode, int numSemaine) { return null; }
	@Override public ArrayList<CoursParticulier> getListEleveSelonAccredProfEtCours(int numSemaine, int numMoniteur, String periode) { return null; }
	@Override public ArrayList<CoursParticulier> getMyList(int idPersonne) { return null; }
	@Override public ArrayList<CoursParticulier> getListSemainePerdiodeMoniteur(int numMoniteur, int numSemaine, String periode) { return null; }
	@Override public boolean updateAssurance(int numEleve, int numSemaine, String periode) { return false; }
	@Override public void creerTouteDisponibilites() { }
	@Override public void creerTouteDisponibilitesSelonMoniteur(int i) { }
	@Override public boolean changeDispoSelonIdSemaine(int numSemaine, int numMoniteur) { return false; }
	@Override public ArrayList<CoursParticulier> getListDispo(int numSemaine, String periode) { return null; }
	@Override public CoursParticulier returnUser(String mdp, String pseudo) { return null; }
	@Override public int valeurReduction(int numSem) { return 0; }
}
