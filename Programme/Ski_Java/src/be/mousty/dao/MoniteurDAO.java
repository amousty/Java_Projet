package be.mousty.dao;

import java.sql.Connection;
import java.util.Date;
import java.util.Random;

import be.mousty.pojo.Accreditation;
import be.mousty.pojo.Moniteur;
import be.mousty.pojo.Personne;
import be.mousty.pojo.Utilisateur;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MoniteurDAO extends DAO<Moniteur>{
	AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<Utilisateur> UtilisateurDAO = adf.getUtilisateurDAO();
	DAO<Personne> PersonneDao = adf.getPersonneDAO();
	public MoniteurDAO(Connection conn) { super(conn); }

	public int create(Moniteur obj) {
		PreparedStatement pst = null;
		PreparedStatement pst_accred = null;
		try {
			Personne P = new Personne();
			//P.setNumPersonne(res_Rec_Util.getInt("numPersonne"));
			P.setNom(obj.getNom());
			P.setPre( obj.getPre());
			P.setDateNaissance(obj.getDateNaissance());
			P.setAdresse(obj.getAdresse());
			P.setSexe(obj.getSexe());
			int numPersonne = PersonneDao.create(P);
			System.out.println("Moniteur DAO -> " + numPersonne);
			if (numPersonne != -1){
				Utilisateur U = new Utilisateur();
				U.setNumUtilisateur(numPersonne);
				U.setPseudo(obj.getPseudo());
				U.setMdp(obj.getMdp());
				U.setTypeUtilisateur( obj.getTypeUtilisateur());
				/*U.setNumPersonne(numPersonne);
				U.setNom(res_Rec_Util.getString("nom"));
				U.setPre(res_Rec_Util.getString("prenom"));
				U.setDateNaissance(res_Rec_Util.getDate("dateNaissance"));
				U.setAdresse(res_Rec_Util.getString("adresse"));
				U.setSexe(res_Rec_Util.getString("sexe"));*/
				if(UtilisateurDAO.create(U) != -1){
					//on l'utilise pour ajouter les donn�es dans la table Moniteur
					String requete3 = "INSERT INTO Moniteur (anneeDexp, numMoniteur) VALUES (?, ?)";
					pst = connect.prepareStatement(requete3);
					Random r = new Random();
					pst.setInt(1, r.nextInt(10 - 0 + 1) + 0); // obj.getAnneeExp()
					pst.setInt(2, numPersonne);
					pst.executeUpdate();

					// On lui ajoute les accr�ditations
					java.util.Date ud = new Date();
					java.sql.Date now = new java.sql.Date(ud.getTime());

					for(int i = 0; i < obj.getAccrediList().size(); i++){
						/*System.out.println("Moniteur DAO : taille liste accred -> " + obj.getAccrediList().size());
						String sqlAccred = "SELECT numAccreditation from Accreditation WHERE nomAccreditation = ? ";
						pst = this.connect.prepareStatement(sqlAccred);
						pst.setString(1, obj.getAccrediList().get(i).toString()); // Nom de l'accr�ditation
						ResultSet rsAccred = pst.executeQuery();*/
						int numAccred = obj.getAccrediList().get(i).getNumAccreditation();
						//while (rsAccred.next()) numAccred = rsAccred.getInt(1); // On a l'id de l'accreditation
						if (numAccred != -1){
							String requete4 = "INSERT INTO LigneAccreditation (numMoniteur, numAccreditation, dateAccreditation) VALUES (?,?,?)";
							pst_accred = connect.prepareStatement(requete4);

							pst_accred.setInt(1, numPersonne);
							pst_accred.setInt(2, numAccred);
							pst_accred.setDate(3, now);

							pst_accred.executeUpdate();
							pst_accred.close();
						}
						else {
							PersonneDao.delete(P);
							return -1;
						} // utilisateur
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
			if (pst != null) {
				try { pst.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return -1;
	}

	public boolean delete(Moniteur obj) { return false; }

	public boolean update(Moniteur obj) { return false; }

	// On cherche une Moniteur gr�ce � son id
	public Moniteur find(int id) {
		Moniteur M = new Moniteur();
		PreparedStatement pst = null;
		PreparedStatement pstAccred = null;
		try {
			String sql = "SELECT * FROM Moniteur "
					+ "INNER JOIN Utilisateur ON Utilisateur.numUtilisateur = Moniteur.numMoniteur "
					+ "INNER JOIN Personne ON Utilisateur.numUtilisateur = Personne.numPersonne "
					+ "WHERE numPersonne = ?";
			pst = this.connect.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet res_mon = pst.executeQuery();
			while (res_mon.next()) {
				ArrayList<Accreditation> listeAccred = new ArrayList<Accreditation>();
				String sqlAccred = "Select * from Accreditation "
						+ "INNER JOIN LigneAccreditation ON Accreditation.numAccreditation = LigneAccreditation.numAccreditation "
						+ "WHERE numMoniteur = ?;";
				pstAccred = this.connect.prepareStatement(sqlAccred);
				pstAccred.setInt(1, id);
				ResultSet rsAccred = pstAccred.executeQuery();
				while(rsAccred.next()){
					Accreditation A = new Accreditation();
					A.setNomAccreditation(rsAccred.getString("nomAccreditation"));
					A.setNumAccreditation(rsAccred.getInt("numAccreditation"));
					listeAccred.add(A);
				}
				M = new Moniteur();
				M.setNumMoniteur(res_mon.getInt("numMoniteur"));
				M.setAnneeExp(0);
				M.setAccrediList(listeAccred);
				M.setNumUtilisateur(res_mon.getInt("numUtilisateur"));
				M.setPseudo(res_mon.getString("pseudo"));
				M.setMdp(res_mon.getString("mdp"));
				M.setTypeUtilisateur(res_mon.getInt("typeUtilisateur"));
				M.setNumPersonne(res_mon.getInt("numPersonne"));
				M.setNom(res_mon.getString("nom"));
				M.setPre(res_mon.getString("prenom"));
				M.setDateNaissance(res_mon.getDate("dateNaissance"));
				M.setAdresse(res_mon.getString("adresse"));
				M.setSexe(res_mon.getString("sexe"));
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst != null || pstAccred != null) {
				try { pst.close(); pstAccred.close();}
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return M;
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
					Accreditation A = new Accreditation();
					A.setNomAccreditation(rsAccred.getString("nomAccreditation"));
					A.setNumAccreditation(rsAccred.getInt("numAccreditation"));
					listeAccred.add(A);
				}
				Moniteur M = new Moniteur();
				M.setNumMoniteur(res_mon.getInt("numMoniteur"));
				M.setAnneeExp(0);
				M.setAccrediList(listeAccred);
				M.setNumUtilisateur(res_mon.getInt("numUtilisateur"));
				M.setPseudo(res_mon.getString("pseudo"));
				M.setMdp(res_mon.getString("mdp"));
				M.setTypeUtilisateur(res_mon.getInt("typeUtilisateur"));
				M.setNumPersonne(res_mon.getInt("numPersonne"));
				M.setNom(res_mon.getString("nom"));
				M.setPre(res_mon.getString("prenom"));
				M.setDateNaissance(res_mon.getDate("dateNaissance"));
				M.setAdresse(res_mon.getString("adresse"));
				M.setSexe(res_mon.getString("sexe"));
				liste.add(M);
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

	public ArrayList<Moniteur> getMyListSelonID(int nonUsed,  int numSemaine, int nonUsed2, String periode) {
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
					Accreditation A = new Accreditation();
					A.setNomAccreditation(rsAccred.getString("nomAccreditation"));
					A.setNumAccreditation(rsAccred.getInt("numAccreditation"));
					listeAccred.add(A);
				}
				Moniteur M = new Moniteur();
				M.setNumMoniteur(res_mon.getInt("numMoniteur"));
				M.setAnneeExp(0);
				M.setAccrediList(listeAccred);
				M.setNumUtilisateur(res_mon.getInt("numUtilisateur"));
				M.setPseudo(res_mon.getString("pseudo"));
				M.setMdp(res_mon.getString("mdp"));
				M.setTypeUtilisateur(res_mon.getInt("typeUtilisateur"));
				M.setNumPersonne(res_mon.getInt("numPersonne"));
				M.setNom(res_mon.getString("nom"));
				M.setPre(res_mon.getString("prenom"));
				M.setDateNaissance(res_mon.getDate("dateNaissance"));
				M.setAdresse(res_mon.getString("adresse"));
				M.setSexe(res_mon.getString("sexe"));
				liste.add(M);
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

	@Override
	public Moniteur getId(Moniteur obj) {
		PreparedStatement pst = null;
		Moniteur M = new Moniteur();
		try {
			String sql = "SELECT * FROM Personne INNER JOIN Utilisateur ON Utilisateur.numUtilisateur = Personne.numPersonne "
					+ "INNER JOIN Moniteur ON Moniteur.numMoniteur = Utilisateur.numUtilisateur "
					+ "WHERE nom = ? AND prenom = ? AND adresse = ? ;";
			pst = this.connect.prepareStatement(sql);
			pst.setString(1, obj.getNom());
			pst.setString(2, obj.getPre());
			pst.setString(3, obj.getAdresse());
			ResultSet res_Rec_Mon = pst.executeQuery();
			//System.out.println("trouve");
			while (res_Rec_Mon.next()) {
				M.setNumMoniteur(res_Rec_Mon.getInt("numMoniteur"));
				M.setNumPersonne(res_Rec_Mon.getInt("numPersonne"));
				M.setAnneeExp(res_Rec_Mon.getInt("anneeDexp"));
				M.setAccrediList(null); // no need
				M.setNumUtilisateur(res_Rec_Mon.getInt("numUtilisateur"));
				M.setPseudo(res_Rec_Mon.getString("pseudo"));
				M.setMdp(res_Rec_Mon.getString("mdp"));
				M.setTypeUtilisateur(res_Rec_Mon.getInt("typeUtilisateur"));
				M.setNumPersonne(res_Rec_Mon.getInt("numPersonne"));
				M.setNom(res_Rec_Mon.getString("nom"));
				M.setPre(res_Rec_Mon.getString("prenom"));
				M.setDateNaissance(res_Rec_Mon.getDate("dateNaissance"));
				M.setAdresse(res_Rec_Mon.getString("adresse"));
				M.setSexe(res_Rec_Mon.getString("sexe"));
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst != null) {
				try { pst.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return M;
	}

	@Override
	public ArrayList<Moniteur> getListSelonCriteres(Moniteur obj) {
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
	public String calculerPlaceCours(int numCours, int numSemaine, int idMoniteur) {
		// TODO Auto-generated method stub
		return null;
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