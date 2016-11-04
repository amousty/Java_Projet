package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import POJO.Accreditation;
import POJO.Personne;

public class PersonneDAO  extends DAO<Personne> {
	public PersonneDAO(Connection conn) {
		super(conn);
	}

	public int create(Personne obj) {
		try {
			String requete = "INSERT INTO Personne (nom, prenom, adresse, dateNaissance, sexe) VALUES (?,?,?,?,?)";
			PreparedStatement pst = connect.prepareStatement(requete);

			pst.setString(1, obj.getNom());
			pst.setString(2, obj.getPre());
			pst.setString(3, obj.getAdresse());
			pst.setDate(4, obj.getDateNaissance());
			pst.setString(5, obj.getSexe());

			pst.executeUpdate();
			pst.close();

			PreparedStatement rechNumPersPst;
			String sql = "SELECT MAX(numPersonne) FROM Personne";
			rechNumPersPst = this.connect.prepareStatement(sql);
			ResultSet rs = rechNumPersPst.executeQuery();
			while (rs.next()) {
				obj.setNumPersonne(rs.getInt(1));
			}
			System.out.println("personneDao -> " + obj.getNumPersonne());
			return obj.getNumPersonne();
		} 
		catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return -1;
		}
	}

	public boolean delete(Personne obj) {
		try {
			String requete = "DELETE FROM Personne WHERE numPersonne = (SELECT MAX(numPersonne) FROM Personne);";
			PreparedStatement pst = connect.prepareStatement(requete);
			pst.executeUpdate();
			pst.close();
			System.out.println("Personne supprim�e");
			return true;
		} 
		catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean update(Personne obj) { return false; }

	// On cherche une personne gr�ce � son id
	public Personne find(int id) { return null; } 
		//		Personne pers = new Personne();
		//		PreparedStatement pst = null;
		//		try {
		//			String sql = "SELECT * FROM Personne WHERE numPersonne = ?";
		//			pst = this.connect.prepareStatement(sql);
		//			pst.setInt(1, id);
		//			ResultSet rs = pst.executeQuery();
		//			while (rs.next()) {
		//				pers.setNom(rs.getString("nom"));
		//				pers.setPre(rs.getString("prenom"));
		//				pers.setAdresse(rs.getString("adresse"));
		//				pers.setDateNaissance(rs.getDate("dateNaissance"));
		//				pers.setSexe(rs.getString("sexe"));
		//			}
		//		} catch (SQLException e) {
		//			e.printStackTrace();
		//		} finally {
		//			if (pst != null) {
		//				try {
		//					pst.close();
		//				} catch (SQLException e) {
		//					e.printStackTrace();
		//				}
		//			}
		//		}
		//		return pers;

		/*Personne pers = new Personne();
		try{
			// On recherche si l�id rentr� correspond � un �l�ve, s�il y a un res on retourne les infos
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT * FROM Personne WHERE numPersonne = " + id);
			if(result.first())
				// Appel des classes pojo
				pers = new Personne(result.getString("nom"), result.getString("prenom"), result.getString("adresse"), result.getString("sexe"), result.getDate("dateNaissance"), id);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		// On renvoie l��l�ve
		return pers;
	}*/



	public ArrayList<Personne> getList() { return null; } 
		/*Personne pers = null;
		ArrayList<Personne> liste = new ArrayList<Personne>();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM Personne";
			pst = this.connect.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				pers.setNom(rs.getString("nom"));
				pers.setPre(rs.getString("prenom"));
				pers.setAdresse(rs.getString("adresse"));
				pers.setDateNaissance(rs.getDate("dateNaissance"));
				pers.setSexe(rs.getString("sexe"));
				liste.add(pers);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return liste;
	}*/

	@Override public String calculerPlaceCours(int numCours, int numSemaine) { return -1 + ""; }
	@Override public ArrayList<Personne> getListCoursSelonId(int idMoniteur) { return null; }
	@Override public ArrayList<Personne> getListCoursCollectifSelonId(int numMoniteur, int numEleve, String periode) { return null; }
	@Override public ArrayList<Personne> getListCoursParticulierSelonId(int numMoniteur, String periode) { return null; }
	@Override public ArrayList<Personne> getListEleveSelonAccredProfEtCours(int numSemaine, int numMoniteur, String periode, int cours) { return null; }
	@Override public ArrayList<Personne> getMyList(int idPersonne) { return null; }
	@Override public boolean updateAssurance(int numEleve, int numSemaine, String periode) { return false; }
	@Override public ArrayList<Personne> getListSemainePerdiodeMoniteur(int numMoniteur, int numSemaine, String periode) { return null; }
	@Override public void creerTouteDisponibilites() { }
	@Override public void creerTouteDisponibilitesSelonMoniteur(int i) { }
	@Override public boolean changeDispoSelonIdSemaine(int numSemaine, int numMoniteur) { return false; }
	@Override public ArrayList<Personne> getListDispo(int numSemaine) { return null; }
}
