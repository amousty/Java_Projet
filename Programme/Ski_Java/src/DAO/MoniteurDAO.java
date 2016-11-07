package DAO;

import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import POJO.Accreditation;
import POJO.Moniteur;
import POJO.Personne;
import POJO.Utilisateur;

public class MoniteurDAO extends DAO<Moniteur>{
	AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<Utilisateur> UtilisateurDAO = adf.getUtilisateurDAO();
	DAO<Personne> PersonneDao = adf.getPersonneDAO();
	public MoniteurDAO(Connection conn) { super(conn); }

	public int create(Moniteur obj) {
		PreparedStatement pst = null;
		PreparedStatement pst_accred = null;
		try {
			Personne P = new Personne(-1,obj.getNom(), obj.getPre(), obj.getAdresse(), obj.getSexe(), obj.getDateNaissance());
			int numPersonne = PersonneDao.create(P);
			if (numPersonne != -1){
				if(UtilisateurDAO.create(new Utilisateur(numPersonne, obj.getPseudo(), obj.getMdp(), obj.getTypeUtilisateur())) != -1){
					//on l'utilise pour ajouter les donn�es dans la table Moniteur
					String requete3 = "INSERT INTO Moniteur (anneeDexp, numMoniteur) VALUES (?, ?)";
					pst = connect.prepareStatement(requete3);

					pst.setInt(1, 0); // obj.getAnneeExp()
					pst.setInt(2, numPersonne);
					pst.executeUpdate();

					// On lui ajoute les accr�ditations
					java.util.Date ud = new Date();
					java.sql.Date now = new java.sql.Date(ud.getTime());
					for(int i = 0; i < obj.getAccrediList().size(); i++){
						String sqlAccred = "SELECT numAccreditation from Accreditation WHERE nomAccreditation = ? ";
						pst = this.connect.prepareStatement(sqlAccred);
						pst.setString(1, obj.getAccrediList().get(i).toString()); // Nom de l'accr�ditation
						ResultSet rsAccred = pst.executeQuery();
						int numAccred = -1 ;
						while (rsAccred.next()) numAccred = rsAccred.getInt(1); // On a l'id du moniteur

						String requete4 = "INSERT INTO LigneAccreditation (numMoniteur, numAccreditation, dateAccreditation) VALUES (?,?,?)";
						pst_accred = connect.prepareStatement(requete4);

						pst_accred.setInt(1, numPersonne);
						pst_accred.setInt(2, numAccred);
						pst_accred.setDate(3, now);

						pst_accred.executeUpdate();
					}
					System.out.println("Ajout d'un moniteur effectue");
					return numPersonne;
				}
				else {
					PersonneDao.delete(P);
					return -1;
				} // utilisateur
			} else { return -1; } // personne
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst != null || pst_accred != null) {
				try { pst.close(); pst_accred.close();}
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return -1;
	}

	public boolean delete(Moniteur obj) { return false; }

	public boolean update(Moniteur obj) { return false; }

	// On cherche une Moniteur gr�ce � son id
	public Moniteur find(int id) {
		Moniteur moniteur = null;
		PreparedStatement pst = null;
		PreparedStatement pstAccred = null;
		try {
			String sql = "SELECT * FROM Moniteur "
					+ "INNER JOIN Utilisateur ON Utilisateur.numUtilisateur = Moniteur.numMoniteur "
					+ "INNER JOIN Personne ON Utilisateur.numUtilisateur = Personne.numPersonne "
					+ "WHERE numPersonne = ?";
			pst = this.connect.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				ArrayList<Accreditation> listeAccred = new ArrayList<Accreditation>();
				String sqlAccred = "Select * from Accreditation "
						+ "INNER JOIN LigneAccreditation ON Accreditation.numAccreditation = LigneAccreditation.numAccreditation "
						+ "WHERE numMoniteur = ?;";
				pstAccred = this.connect.prepareStatement(sqlAccred);
				pstAccred.setInt(1, id);
				ResultSet rsAccred = pstAccred.executeQuery();
				while(rsAccred.next()){
					listeAccred.add(new Accreditation(rsAccred.getString("nomAccreditation")));
				}
				moniteur = new Moniteur(rs.getInt("numMoniteur"), rs.getString("nom"), rs.getString("prenom"),
						rs.getString("adresse"), rs.getString("sexe"), rs.getDate("dateNaissance"), rs.getString("pseudo"), rs.getString("mdp"),
						rs.getInt("typeUtilisateur"), listeAccred);
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst != null || pstAccred != null) {
				try { pst.close(); pstAccred.close();}
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return moniteur;
	}



	public ArrayList<Moniteur> getList() {
		ArrayList<Moniteur> liste = new ArrayList<Moniteur>();
		PreparedStatement pst_mon = null;
		PreparedStatement pstAccred = null;
		try {
			String sql_mon = "SELECT * FROM Moniteur "
					+ "INNER JOIN Utilisateur ON Utilisateur.numUtilisateur = Moniteur.numMoniteur "
					+ "INNER JOIN Personne ON Utilisateur.numUtilisateur = Personne.numPersonne";
			pst_mon = this.connect.prepareStatement(sql_mon);
			ResultSet res_mon = pst_mon.executeQuery();
			while (res_mon.next()) {
				ArrayList<Accreditation> listeAccred = new ArrayList<Accreditation>();
				String sqlAccred = "Select * from Accreditation "
						+ "INNER JOIN LigneAccreditation ON Accreditation.numAccreditation = LigneAccreditation.numAccreditation "
						+ "WHERE numMoniteur = ?;";
				pstAccred = this.connect.prepareStatement(sqlAccred);
				pstAccred.setInt(1, res_mon.getInt("numMoniteur"));
				ResultSet rsAccred = pstAccred.executeQuery();
				while(rsAccred.next()){
					Accreditation a = new Accreditation(rsAccred.getString("nomAccreditation"));
					listeAccred.add(a);
				}
				Moniteur moniteur = new Moniteur(res_mon.getInt("numMoniteur"), res_mon.getString("nom"), res_mon.getString("prenom"),
						res_mon.getString("adresse"), res_mon.getString("sexe"), res_mon.getDate("dateNaissance"), res_mon.getString("pseudo"), res_mon.getString("mdp"),
						res_mon.getInt("typeUtilisateur"), listeAccred);
				liste.add(moniteur);
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst_mon != null || pstAccred != null) {
				try {
					pst_mon.close();
					pstAccred.close();
				}
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return liste;
	}
	
	public ArrayList<Moniteur> getListDispo(int numSemaine, String periode) {
		ArrayList<Moniteur> liste = new ArrayList<Moniteur>();
		PreparedStatement pst_mon = null;
		PreparedStatement pstAccred = null;
		try {
			String verifPeriode;
			switch(periode){
				case "09-12" : verifPeriode = " = '09-12'";
					break;
				case "14-17" : verifPeriode = " = '14-17'";
					break;
				case "12-13": verifPeriode = " IN('12-14','12-13') ";
					break;
				case "13-14": verifPeriode = " IN('12-14','13-14') ";
					break;
				case "12-14": verifPeriode = " IN('12-13', '13-14', '12-14') ";
					break;
				default : verifPeriode = " = ? ";
					break;
			}
			String sql_mon =
					"SELECT distinct * FROM Moniteur "
					+ "INNER JOIN Utilisateur ON Utilisateur.numUtilisateur = Moniteur.numMoniteur "
					+ "INNER JOIN DisponibiliteMoniteur ON DisponibiliteMoniteur.numMoniteur = Utilisateur.numUtilisateur "
					+ "INNER JOIN Personne ON Personne.numPersonne = Moniteur.numMoniteur "
							+ "WHERE disponible = 1 "
							+ "AND numSemaine = ? "
							+ "AND Moniteur.numMoniteur NOT IN "
							+ "( SELECT numMoniteur FROM CoursMoniteur WHERE numCours IN "
							+ "( SELECT Cours.numCours FROM COURS "
							+ "INNER JOIN CoursSemaine ON CoursSemaine.numCours = Cours.numCours "
							+ "WHERE periodeCours " + verifPeriode + "))"
									+ "OR  DisponibiliteMoniteur.numMoniteur IN "
           + "(SELECT CoursMoniteur.numMoniteur FROM CoursMoniteur WHERE  numCours IN "
             + "(SELECT numCours FROM Cours WHERE numSemaine = ? AND disponible = 1 AND periodeCours "+ verifPeriode +" ));";
			pst_mon = this.connect.prepareStatement(sql_mon);
			pst_mon.setInt(1, numSemaine);
			//pst_mon.setString(2, periode);
			pst_mon.setInt(2, numSemaine);
			ResultSet res_mon = pst_mon.executeQuery();
			while (res_mon.next()) {
				ArrayList<Accreditation> listeAccred = new ArrayList<Accreditation>();
				String sqlAccred = "Select * from Accreditation "
						+ "INNER JOIN LigneAccreditation ON Accreditation.numAccreditation = LigneAccreditation.numAccreditation "
						+ "WHERE numMoniteur = ?;";
				pstAccred = this.connect.prepareStatement(sqlAccred);
				pstAccred.setInt(1, res_mon.getInt("numMoniteur"));
				ResultSet rsAccred = pstAccred.executeQuery();
				while(rsAccred.next()){
					Accreditation a = new Accreditation(rsAccred.getString("nomAccreditation"));
					listeAccred.add(a);
				}
				Moniteur moniteur = new Moniteur(res_mon.getInt("numMoniteur"), res_mon.getString("nom"), res_mon.getString("prenom"),
						res_mon.getString("adresse"), res_mon.getString("sexe"), res_mon.getDate("dateNaissance"), res_mon.getString("pseudo"), res_mon.getString("mdp"),
						res_mon.getInt("typeUtilisateur"), listeAccred);
				liste.add(moniteur);
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst_mon != null || pstAccred != null) {
				try {
					pst_mon.close();
					pstAccred.close();
				}
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return liste;
	}

	@Override public String calculerPlaceCours(int numCours, int numSemaine, int numMoniteur) { return -1 + ""; }
	@Override public ArrayList<Moniteur> getListCoursSelonId(int idMoniteur) { return null; }
	@Override public ArrayList<Moniteur> getListCoursCollectifSelonId(int numMoniteur, int numEleve, String periode, int numSemaine) { return null; }
	@Override public ArrayList<Moniteur> getListCoursParticulierSelonId(int numMoniteur, String periode, int numSemaine) { return null; }
	@Override public ArrayList<Moniteur> getListEleveSelonAccredProfEtCours(int numSemaine, int numMoniteur, String periode) { return null; }
	@Override public ArrayList<Moniteur> getMyList(int idPersonne) { return null; }
	@Override public ArrayList<Moniteur> getListSemainePerdiodeMoniteur(int numMoniteur, int numSemaine, String periode) { return null; }
	@Override public boolean updateAssurance(int numEleve, int numSemaine, String periode) { return false; }
	@Override public void creerTouteDisponibilites() { }
	@Override public void creerTouteDisponibilitesSelonMoniteur(int i) { }
	@Override public boolean changeDispoSelonIdSemaine(int numSemaine, int numMoniteur) { return false; }
	@Override public Moniteur returnUser(String mdp, String pseudo) { return null; }
	@Override public int valeurReduction(int numSem) { return 0; }
}
