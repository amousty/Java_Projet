package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import POJO.Eleve;
import POJO.Utilisateur;
public class UtilisateurDAO extends DAO<Utilisateur> {
	public UtilisateurDAO(Connection conn) {
		super(conn);
	}

	public int create(Utilisateur obj) { 
		try
		{
			// V�rifier si la personne existe d�j� (username/mdp)
			Utilisateur uTmp= find(obj.getTypeUtilisateur());
			//int numType = uTmp.getTypeUtilisateur();
			if(uTmp == null){
				return -1;
			}
			else {
				/*String sql = "SELECT MAX(numPersonne) from Personne";
				PreparedStatement pst = this.connect.prepareStatement(sql);
				ResultSet rs = pst.executeQuery();
				int numPersonne = -1 ;
				while (rs.next()) numPersonne = rs.getInt(1); // On a l'id de la personne*/

				//on l'utilise pour ajouter les donn�es dans la table Utilisateur
				System.out.println("UtilisateurDao -> " + obj.getNumUtilisateur());
				String requete2 = "INSERT INTO Utilisateur (pseudo, mdp, typeUtilisateur, numUtilisateur) VALUES (?,?,?,?)";
				PreparedStatement pst2 = connect.prepareStatement(requete2);

				//pst2.setInt(1, numUtilisateur);     //L'id qui lie la table moniteur a la table personne
				pst2.setString(1, obj.getPseudo());
				pst2.setString(2, obj.getMdp());
				pst2.setInt(3, obj.getTypeUtilisateur());
				pst2.setInt(4, obj.getNumUtilisateur());

				pst2.executeUpdate();
				pst2.close();
				//System.out.println("Ajout d'un moniteur effectue");
				System.out.println("UtilisateurDao -> " + obj.getNumUtilisateur());
				return obj.getNumUtilisateur();
			}
		}

		catch (SQLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return -1;
		}
	}
	public boolean delete(Utilisateur obj) {
		return false;
	}

	public boolean update(Utilisateur obj) {
		return false;
	}

	// On cherche un �l�ve gr�ce � son id
	public Utilisateur find(int id) {
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
	}

	/*public  int verifPseudoMdp(Utilisateur obj){
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM utilisateur WHERE pseudo = ? AND mdp = ? ";
			pst = this.connect.prepareStatement(sql);
			pst.setString(1, obj.getPseudo());
			pst.setString(2, obj.getMdp());
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				obj.setPseudo(rs.getString("pseudo"));
				obj.setMdp(rs.getString("mdp"));
				obj.setTypeUtilisateur(rs.getInt("typeUtilisateur"));
				obj.setNumUtilisateur(rs.getInt("numUtilisateur"));
			}
			else {
				// pas de r�sultat
				return -1;
			}
		} 
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst != null) {
				try { pst.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return obj.getNumUtilisateur(); // retourne le type d'objet
	}*/

	public  ArrayList<Utilisateur> getList() {
		ArrayList<Utilisateur> liste = new ArrayList<Utilisateur>();
		
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM Utilisateur INNER JOIN Personne ON Personne.numPersonne = Utilisateur.numUtilisateur";
			pst = this.connect.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				Utilisateur utilisateur = new Utilisateur(rs.getInt("numUtilisateur"), rs.getString("nom"), rs.getString("prenom"), rs.getString("adresse"),
						rs.getString("sexe"), rs.getDate("dateNaissance"), rs.getString("pseudo"), rs.getString("mdp"), rs.getInt("typeUtilisateur"));
				liste.add(utilisateur);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
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
		return liste;
	}
}
