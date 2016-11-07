package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import POJO.Cours;
import POJO.CoursCollectif;
import POJO.Eleve;

public class CoursCollectifDAO extends DAO<CoursCollectif> {
	AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<Eleve> EleveDAO = adf.getEleveDAO();
	DAO<Cours> CoursDAO = adf.getCoursDAO();
	
	public CoursCollectifDAO(Connection conn) { super(conn); }

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
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst != null) {
				try { pst.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return null;
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
	
	public ArrayList<CoursCollectif> getListCoursCollectifSelonId(int idMoniteur, int idEleve, String periode, int numSemaine){
		//System.out.println("Entree fonc");
		/*ArrayList<Cours> listCours = CoursDAO.getListCoursSelonId(idMoniteur);
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
		return listSelonId;*/
		/* S�lection des cours par rapport � la cat�gorie de l'�l�ve et de la p�riode */
		/*S�lection des �tudiants par rapport aux accreds du moniteur */
		PreparedStatement pst_lst_cou1 = null;
		PreparedStatement pst_lst_cou2 = null;
		ArrayList<CoursCollectif> listSelonId = new ArrayList<CoursCollectif>();
		try {
			String sql1 = "SELECT * from Cours "
			+ "INNER JOIN CoursCollectif ON CoursCollectif.numCoursCollectif = Cours.numCours "
			+ "WHERE PeriodeCours = ? AND CoursCollectif.categorieAge in "
			+ "(SELECT Categorie from ELEVE Where numEleve = ?) "
			+ "AND nomSport in "
			+ "(SELECT nomAccreditation from accreditation where numAccreditation in "
			+ "(SELECT numAccreditation from ligneAccreditation where numMoniteur = ?)) "
			
			+ "AND   Cours.numCours IN "
            + "(SELECT CoursMoniteur.numCours FROM CoursMoniteur "
           	+ "INNER JOIN CoursSemaine ON CoursSemaine.numCours = CoursMoniteur.numCours "         
           	+ "WHERE  CoursSemaine.numCours IN "
            + "(SELECT CoursSemaine.numCours FROM Cours WHERE CoursSemaine.numSemaine = ? AND periodeCours = ? AND numMoniteur = ?));";
			pst_lst_cou1 = this.connect.prepareStatement(sql1);
			pst_lst_cou1.setString(1, periode);
			pst_lst_cou1.setInt(2, idEleve);
			pst_lst_cou1.setInt(3, idMoniteur);
			pst_lst_cou1.setInt(4, numSemaine);
			pst_lst_cou1.setString(5, periode);
			pst_lst_cou1.setInt(6, idMoniteur);
			ResultSet res_lst_cou1 = pst_lst_cou1.executeQuery();
			while (res_lst_cou1.next()) {
				CoursCollectif coursCollectif = new CoursCollectif(res_lst_cou1.getInt("numCours"), res_lst_cou1.getString("nomSport"), res_lst_cou1.getInt("prix"),
						res_lst_cou1.getInt("minEleve"), res_lst_cou1.getInt("maxEleve"), res_lst_cou1.getString("periodeCours"), res_lst_cou1.getString("categorieAge"), res_lst_cou1.getString("niveauCours"));
				listSelonId.add(coursCollectif);
			}
			if (listSelonId.isEmpty()){
				String sql2 = "SELECT * from Cours "
						+ "INNER JOIN CoursCollectif ON CoursCollectif.numCoursCollectif = Cours.numCours "
						+ "WHERE PeriodeCours = ? AND CoursCollectif.categorieAge in "
						+ "(SELECT Categorie from ELEVE Where numEleve = ?) "
						+ "AND nomSport in "
						+ "(SELECT nomAccreditation from accreditation where numAccreditation in "
						+ "(SELECT numAccreditation from ligneAccreditation where numMoniteur = ?));";
				pst_lst_cou2 = this.connect.prepareStatement(sql2);
				pst_lst_cou2.setString(1, periode);
				pst_lst_cou2.setInt(2, idEleve);
				pst_lst_cou2.setInt(3, idMoniteur);
						ResultSet res_lst_cou2 = pst_lst_cou2.executeQuery();
						while (res_lst_cou2.next()) {
							CoursCollectif coursCollectif = new CoursCollectif(res_lst_cou2.getInt("numCours"), res_lst_cou2.getString("nomSport"), res_lst_cou2.getInt("prix"),
									res_lst_cou2.getInt("minEleve"), res_lst_cou2.getInt("maxEleve"), res_lst_cou2.getString("periodeCours"), res_lst_cou2.getString("categorieAge"), res_lst_cou2.getString("niveauCours"));
							listSelonId.add(coursCollectif);
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
	@Override public ArrayList<CoursCollectif> getListCoursSelonId(int idMoniteur) { return null; }
	@Override public ArrayList<CoursCollectif> getListCoursParticulierSelonId(int numMoniteur, String periode, int numSemaine) { return null; }
	@Override public ArrayList<CoursCollectif> getListEleveSelonAccredProfEtCours(int numSemaine, int numMoniteur, String periode) { return null; }
	@Override public ArrayList<CoursCollectif> getMyList(int idPersonne) { return null; }
	@Override public ArrayList<CoursCollectif> getListSemainePerdiodeMoniteur(int numMoniteur, int numSemaine, String periode) { return null; }
	@Override public boolean updateAssurance(int numEleve, int numSemaine, String periode) { return false; }
	@Override public void creerTouteDisponibilites() { }
	@Override public void creerTouteDisponibilitesSelonMoniteur(int i) { }
	@Override public boolean changeDispoSelonIdSemaine(int numSemaine, int numMoniteur) { return false; }
	@Override public ArrayList<CoursCollectif> getListDispo(int numSemaine, String periode) { return null; }
	@Override public CoursCollectif returnUser(String mdp, String pseudo) { return null; }
	@Override public int valeurReduction(int numSem) { return 0; }
}

