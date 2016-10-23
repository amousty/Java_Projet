package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import POJO.Personne;

public class PersonneDAO  extends DAO<Personne> {
	public PersonneDAO(Connection conn) {
		super(conn);
	}

	public boolean create(Personne obj) {
		try {
			String requete = "SELECT numPersonne FROM  Personne WHERE nom = '" + obj.getNom() + "' AND prenom = '" + obj.getPre()  
			+"' AND sexe = '" + obj.getSexe() + "' AND adresse = '" + obj.getAdresse() + "';";
			 
			 Statement stmt = connect.createStatement();

			// 5.2 Execution de l'insert into 
			 ResultSet find = stmt.executeQuery(requete);
			if (!find.next()){
				String sql = "INSERT INTO Personne " + "(nom, prenom, adresse, dateNaissance, sexe) " + " VALUES(?,?,?, ?)";
				PreparedStatement pst = this.connect.prepareStatement(sql);
				pst.setString(1, obj.getNom());
				pst.setString(2, obj.getPre());
				pst.setString(3, obj.getAdresse());
				pst.setDate(4, obj.getDateNaissance());
				pst.setString(5, obj.getSexe());
				pst.executeUpdate();
			pst.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean delete(Personne obj) {
		return false;
	}

	public boolean update(Personne obj) {
		return false;
	}

	// On cherche une personne gr�ce � son id
	public Personne find(int id) {
		Personne pers = new Personne();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM Personne WHERE numPersonne = ?";
			pst = this.connect.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				pers.setNom(rs.getString("nom"));
				pers.setPre(rs.getString("prenom"));
				pers.setAdresse(rs.getString("adresse"));
				pers.setDateNaissance(rs.getDate("dateNaissance"));
				pers.setSexe(rs.getString("sexe"));
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
		return pers;
	}
	
	

	public ArrayList<Personne> getList() {
		Personne pers = null;
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
	}

	@Override
	public Personne verifPseudoMdp(String text, String text2) {
		// TODO Auto-generated method stub
		return null;
	}
}
