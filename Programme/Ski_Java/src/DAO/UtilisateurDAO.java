package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import POJO.Utilisateur;
public class UtilisateurDAO extends DAO<Utilisateur> {
	public UtilisateurDAO(Connection conn) {
		super(conn);
	}

	public boolean create(Utilisateur obj) { return false;} 
//		try {
//			String requete = 
//					"SELECT numUtilisateur, typeUtilisateur FROM  Utilisateur " + 
//					"INNER JOIN  Personne ON Personne.numPersonne = Utilisateur.numPersonne " + 
//					"WHERE pseudo = '" + obj.getPseudo() + "' AND mdp = '" + obj.getMdp()  +"';";
//			 
//			 Statement stmt = connect.createStatement();
//
//			// 5.2 Execution de l'insert into 
//			 ResultSet find = stmt.executeQuery(requete);
//			if (!find.next()){
//				PersonneDAO
//				String sql = "INSERT INTO utilisateur " + "(pseudo, mdp, typeUtilisateur) " + " VALUES(?,?,?)";
//				PreparedStatement pst = this.connect.prepareStatement(sql);
//				pst.setString(1, obj.getPseudo());
//				pst.setString(2, obj.getMdp());
//				pst.setInt(3, obj.getTypeUtilisateur());
//				pst.executeUpdate();
//				
//			pst.close();
//			}
		/*try{
			//On ajoute les donn�es n�cessaires dans la table personne
			String requete = "INSERT INTO Personne (nom, prenom, adresse, dateNaissance, sexe) VALUES (?,?,?,?, ?)";
			PreparedStatement pst = connect.prepareStatement(requete);
			
			pst.setString(1, obj.getNom());
			pst.setString(2, obj.getPre());
			pst.setString(3, obj.getAdresse());
			pst.setDate(4, obj.getDateNaissance());
			pst.setString(5, obj.getSexe());
			
			pst.executeUpdate();
			pst.close();
			
			//on r�cup�re l'id cr�e en dernier dans la table
			String requete2 = "SELECT MAX(numPersonne) FROM personne";
			PreparedStatement pst2 = connect.prepareStatement(requete2);
			ResultSet resultat = pst2.executeQuery();
			pst2.close();
			int numUtilisateur = -1;
			if(!resultat.next())
				return false;
			else{
				numUtilisateur = resultat.getInt(1);
			
			
				//on l'utilise pour ajouter les donn�es dans la table client
				String requete3 = "INSERT INTO Utilisateur (numUtilisateur, pseudo, mdp, typeUtilisateur) VALUES (?,?,?, ?)";
				PreparedStatement pst3 = connect.prepareStatement(requete3);
				
				pst3.setInt(1, numUtilisateur);     //L'id qui lie la table client a la table personne
				pst3.setString(2, obj.getPseudo());
				pst3.setString(3, obj.getMdp());
				pst3.setInt(4, obj.getTypeUtilisateur());
				
				pst3.executeUpdate();
				pst3.close();
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}*/

	public boolean delete(Utilisateur obj) {
		return false;
	}

	public boolean update(Utilisateur obj) {
		return false;
	}

	// On cherche un �l�ve gr�ce � son id
	public Utilisateur find(int id) {return null; } /*{
		Utilisateur utilisateur = new Utilisateur();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM utilisateur WHERE numUtilisateur = ?";
			pst = this.connect.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				utilisateur.setPseudo(rs.getString("pseudo"));
				utilisateur.setMdp(rs.getString("mdp"));
				utilisateur.setTypeUtilisateur(rs.getInt("typeUtilisateur"));
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
		return utilisateur;
	}*/
	
	public  Utilisateur verifPseudoMdp(String pseu, String pwd){ return null; } /*{
		Utilisateur utilisateur = new Utilisateur();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM utilisateur WHERE pseudo = ? AND mdp = ? ";
			pst = this.connect.prepareStatement(sql);
			pst.setString(1, pseu);
			pst.setString(2, pwd);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				utilisateur.setPseudo(rs.getString("pseudo"));
				utilisateur.setMdp(rs.getString("mdp"));
				utilisateur.setTypeUtilisateur(rs.getInt("typeUtilisateur"));
				utilisateur.setNumUtilisateur(rs.getInt("numUtilisateur"));
				
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
		return utilisateur;
	}*/

	public  ArrayList<Utilisateur> getList() { return null;}  //{
		/*Utilisateur utilisateur;
		ArrayList<Utilisateur> liste = new ArrayList<Utilisateur>();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM utilisateur";
			pst = this.connect.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				utilisateur = new Utilisateur();
				utilisateur.setPseudo(rs.getString("pseudo"));
				utilisateur.setMdp(rs.getString("mdp"));
				utilisateur.setTypeUtilisateur(rs.getInt("typeUtilisateur"));
				liste.add(utilisateur);
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
}
