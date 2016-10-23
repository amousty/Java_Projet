package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import POJO.Client;

public class ClientDAO extends DAO<Client> {
	public ClientDAO(Connection conn) {
		super(conn);
	}

	public boolean create(Client obj) {
		try {
			String requete = "SELECT numClient FROM  Client WHERE nom = '" + obj.getNom() + "' AND prenom = '" + obj.getPre()  
			+"' AND sexe = '" + obj.getSexe() + "' AND adresse = '" + obj.getAdresse() + "';";
			 
			 Statement stmt = connect.createStatement();

			// 5.2 Execution de l'insert into 
			 ResultSet find = stmt.executeQuery(requete);
			if (!find.next()){
				String sql = "INSERT INTO Client " + "(nom, prenom, adresse, dateNaissance, sexe) " + " VALUES(?,?,?, ?)";
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

	public boolean delete(Client obj) {
		return false;
	}

	public boolean update(Client obj) {
		return false;
	}

	// On cherche une Client gr�ce � son id
	public Client find(int id) {
		Client pers = new Client();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM Client WHERE numClient = ?";
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
	
	

	public ArrayList<Client> getList() {
		Client pers = null;
		ArrayList<Client> liste = new ArrayList<Client>();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM Client";
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
	public Client verifPseudoMdp(String text, String text2) {
		// TODO Auto-generated method stub
		return null;
	}
}