package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import POJO.Cours;
import POJO.DisponibiliteMoniteur;
import POJO.Eleve;
import POJO.Moniteur;
import POJO.Semaine;

public class DisponibiliteMoniteurDAO extends DAO<DisponibiliteMoniteur> {
	AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<Semaine> SemaineDAO = adf.getSemaineDAO();
	DAO<Moniteur> MoniteurDAO = adf.getMoniteurDAO();

	public DisponibiliteMoniteurDAO(Connection conn) { super(conn); }

	@Override
	public int create (DisponibiliteMoniteur obj) { 
		PreparedStatement rechNumDispo = null;
		try {
			String ins_DMO = "INSERT INTO DisponibiliteMoniteur (numMoniteur, numSemaine, disponible) VALUES (?,?,?)";
			PreparedStatement pst_DMO = connect.prepareStatement(ins_DMO);

			pst_DMO.setInt(1, obj.getNumMoniteur());
			pst_DMO.setInt(2, obj.getNumSemaine());
			pst_DMO.setBoolean(3, obj.getDisponible());

			pst_DMO.executeUpdate();
			pst_DMO.close();

			String sql = "SELECT MAX(numDispo) FROM DisponibiliteMoniteur";
			rechNumDispo = this.connect.prepareStatement(sql);
			ResultSet rs = rechNumDispo.executeQuery();
			while (rs.next()) {
				obj.setNumDispo(rs.getInt(1));
			}
			System.out.println("rechNumDispoDao -> " + obj.getNumDispo());
			return obj.getNumDispo();
		} 
		catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return -1;
		}
		finally {
			if (rechNumDispo != null) {
				try { rechNumDispo.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
	}
	
	public boolean delete	(DisponibiliteMoniteur obj) { return false; }
	public boolean update	(DisponibiliteMoniteur obj) { return false; }

	// On cherche une DisponibiliteMoniteur gr�ce � son id
	public DisponibiliteMoniteur find(int id) { return null;
	/*DisponibiliteMoniteur coursCollectif = new DisponibiliteMoniteur();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM DisponibiliteMoniteur INNER JOIN Cours ON DisponibiliteMoniteur.numDisponibiliteMoniteur = Cours.numCours WHERE numDisponibiliteMoniteur = ?;";
			pst = this.connect.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet result = pst.executeQuery();
			while (result.next()) {
				// int numCours, String nomSport, double prix, int minEleve, int maxEleve, String periodeCours, String categorieAge, String niveauCours
				coursCollectif = new DisponibiliteMoniteur(result.getInt("numCours"), result.getString("nomSport"), result.getInt("prix"),
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
		}*/
	}

	public ArrayList<DisponibiliteMoniteur> getList() { 
	ArrayList<DisponibiliteMoniteur> liste = new ArrayList<DisponibiliteMoniteur>();

		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM DisponibiliteMoniteur";
			pst = this.connect.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				liste.add(new DisponibiliteMoniteur(rs.getInt("numMoniteur"), rs.getInt("numSemaine"), rs.getBoolean("disponible")));
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
	
	@Override
	public ArrayList<DisponibiliteMoniteur> getMyList(int idMoniteur) { 
		ArrayList<DisponibiliteMoniteur> liste = new ArrayList<DisponibiliteMoniteur>();

			PreparedStatement pst = null;
			try {
				String sql = "SELECT * FROM DisponibiliteMoniteur WHERE numMoniteur = ? ";
				pst = this.connect.prepareStatement(sql);
				pst.setInt(1, idMoniteur);
				ResultSet rs = pst.executeQuery();
				while (rs.next()) {
					liste.add(new DisponibiliteMoniteur(rs.getInt("numMoniteur"), rs.getInt("numSemaine"), rs.getBoolean("disponible")));
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
	
	public void creerTouteDisponibilites(){
		for (Moniteur M : MoniteurDAO.getList()){ creerTouteDisponibilitesSelonMoniteur(M.getNumPersonne()); }
	}
	
	public void creerTouteDisponibilitesSelonMoniteur(int numMoniteur){
		ArrayList<Semaine> S = SemaineDAO.getList();
		for (int i = S.get(0).getNumSemaine(); i < (S.get(0).getNumSemaine() + S.size()); i++){
			create(new DisponibiliteMoniteur(numMoniteur, i, true));
		}
	}

	@Override public String calculerPlaceCours(int numCours, int numSemaine) { return -1 + ""; }
	@Override public ArrayList<DisponibiliteMoniteur> getListCoursSelonId(int idMoniteur) { return null; }
	@Override public ArrayList<DisponibiliteMoniteur> getListCoursParticulierSelonId(int numMoniteur, String periode) { return null; }
	@Override public ArrayList<DisponibiliteMoniteur> getListEleveSelonAccredProfEtCours(int numSemaine, int numMoniteur, String periode, int cours) { return null; }
	@Override public ArrayList<DisponibiliteMoniteur> getListSemainePerdiodeMoniteur(int numMoniteur, int numSemaine, String periode) { return null; }
	@Override public boolean updateAssurance(int numEleve, int numSemaine, String periode) { return false; }
	@Override public ArrayList<DisponibiliteMoniteur> getListCoursCollectifSelonId(int numMoniteur, int numEleve, String periode) { return null; }
}


