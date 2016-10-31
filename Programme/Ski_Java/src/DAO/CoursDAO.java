package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import POJO.Cours;

public class CoursDAO extends DAO<Cours> {
	public CoursDAO(Connection conn) {
		super(conn);
	}

	@Override
	public int create		(Cours obj) { return -1; }
	public boolean delete	(Cours obj) { return false; }
	public boolean update	(Cours obj) { return false; }

	// On cherche une Cours gr�ce � son id
	public Cours find(int id) {
		Cours cours = new Cours();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM Cours numCours = ?;";
			pst = this.connect.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet result = pst.executeQuery();
			while (result.next()) {
				cours = new Cours(result.getInt("numCours"), result.getString("nomSport"), result.getInt("prix"),
						result.getInt("minEleve"), result.getInt("maxEleve"),  result.getString("periodeCours"));
			}
			return cours;
		}
		catch (SQLException e) { e.printStackTrace(); return null; }
		finally { 
			if (pst != null) {
				try { pst.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
	}

	public ArrayList<Cours> getList() {
		Cours cours = null;
		ArrayList<Cours> liste = new ArrayList<Cours>();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM Cours";
			pst = this.connect.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				cours.setNumCours(rs.getInt("numCours"));
				cours.setNomSport(rs.getString("nomSport"));
				cours.setPrix(rs.getInt("prix"));
				cours.setMinEl(rs.getInt("minEleve"));
				cours.setMaxEl(rs.getInt("maxEleve"));
				cours.setPeriodeCours(rs.getString("periodeCours"));
				liste.add(cours);
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
}
