package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import POJO.Accreditation;
import POJO.Cours;
import POJO.CoursCollectif;
import POJO.Eleve;

public class CoursCollectifDAO extends DAO<CoursCollectif> {
	AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<Eleve> EleveDAO = adf.getEleveDAO();
	DAO<Cours> CoursDAO = adf.getCoursDAO();
	
	public CoursCollectifDAO(Connection conn) {
		super(conn);
	}

	@Override
	public int create		(CoursCollectif obj) { return -1; }
	public boolean delete	(CoursCollectif obj) { return false; }
	public boolean update	(CoursCollectif obj) { return false; }

	// On cherche une CoursCollectif gr�ce � son id
	public CoursCollectif find(int id) {
		CoursCollectif coursCollectif = new CoursCollectif();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM CoursCollectif INNER JOIN Cours ON CoursCollectif.numCoursCollectif = Cours.numCours WHERE numCoursCollectif = ?;";
			pst = this.connect.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet result = pst.executeQuery();
			while (result.next()) {
				// int numCours, String nomSport, double prix, int minEleve, int maxEleve, String periodeCours, String categorieAge, String niveauCours
				coursCollectif = new CoursCollectif(result.getInt("numCours"), result.getString("nomSport"), result.getInt("prix"),
						result.getInt("minEleve"), result.getInt("maxEleve"), result.getString("periodeCours"), 
						result.getString("categorieAge"), result.getString("niveauCours"));
			}
			return coursCollectif;
		}
		catch (SQLException e) { e.printStackTrace(); return null; }
		finally { 
			if (pst != null) {
				try {
					pst.close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ArrayList<CoursCollectif> getList() {
		ArrayList<CoursCollectif> liste = new ArrayList<CoursCollectif>();
		
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM CoursCollectif INNER JOIN Cours ON Cours.numCours = CoursCollectif.numCoursCollectif";
			pst = this.connect.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				CoursCollectif coursCollectif = new CoursCollectif(rs.getInt("numCours"), rs.getString("nomSport"), rs.getInt("prix"),
						rs.getInt("minEleve"), rs.getInt("maxEleve"), rs.getString("periodeCours"), rs.getString("categorieAge"), rs.getString("niveauCours"));
				liste.add(coursCollectif);
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
	
	public ArrayList<CoursCollectif> getListCoursCollectifSelonId(int idMoniteur, int idEleve, String periode){
		//System.out.println("Entree fonc");
		ArrayList<Cours> listCours = CoursDAO.getListCoursSelonId(idMoniteur);
		ArrayList<CoursCollectif> listFull = getList();
		ArrayList<CoursCollectif> listSelonId = new ArrayList<CoursCollectif>();
		Eleve E = EleveDAO.find(idEleve);
		for (CoursCollectif CC : listFull){
			for (Cours C : listCours){
				if (CC.getNumCours() == C.getNumCours() && E.getCategorie().equals(CC.getCategorieAge())){
					//System.out.println("For String de taille " + periode.size());
					//for(String S : periode){
						//System.out.println(S + " / " + CC.getPeriodeCours());
						if(CC.getPeriodeCours().equals(periode)){
							//System.out.println("Ajout Cours Collectif");
							listSelonId.add(CC);
						}
					//}
				}
			}
		}
		return listSelonId;
	}
	
	@Override public String calculerPlaceCours(int numCours, int numSemaine) { return -1 + ""; }
	@Override public ArrayList<CoursCollectif> getListCoursSelonId(int idMoniteur) { return null; }
	@Override public ArrayList<CoursCollectif> getListCoursParticulierSelonId(int numMoniteur, String periode) { return null; }
	@Override public ArrayList<CoursCollectif> getListEleveSelonAccredProfEtCours(int numSemaine, int numMoniteur, String periode, int cours) { return null; }
	@Override public ArrayList<CoursCollectif> getMyList(int idPersonne) { return null; }
	@Override public ArrayList<CoursCollectif> getListSemainePerdiodeMoniteur(int numMoniteur, int numSemaine, String periode) { return null; }
	@Override public boolean updateAssurance(int numEleve, int numSemaine, String periode) { return false; }
	@Override public void creerTouteDisponibilites() { }
	@Override public void creerTouteDisponibilitesSelonMoniteur(int i) { }
	@Override public boolean changeDispoSelonIdSemaine(int numSemaine, int numMoniteur) { return false; }
	@Override public ArrayList<CoursCollectif> getListDispo(int numSemaine) { return null; }
}

