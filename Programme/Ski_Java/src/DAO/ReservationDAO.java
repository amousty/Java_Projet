package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import POJO.Accreditation;
import POJO.Client;
import POJO.Cours;
import POJO.Eleve;
import POJO.Moniteur;
import POJO.Reservation;
import POJO.Semaine;


public class ReservationDAO extends DAO<Reservation> {

	public ReservationDAO(Connection conn) { super(conn); }

	public int create(Reservation obj) { 
		PreparedStatement pst_Res = null;
		PreparedStatement pst_Res_Cli = null;
		PreparedStatement pst_Res_Ele = null;
		PreparedStatement pst_Cou_Mon = null;
		PreparedStatement pst_Cou_Sem = null;
		PreparedStatement pst_Res_Cou = null; 
		PreparedStatement pst_numReserv = null;
		try
		{
			Eleve E = obj.getEleve();
			E.calculerAge();
			// Un enfant ne peut pas faire du snow s'il est moins ag� que 6 ans
			if(obj.getCours().getNomSport().equals("Snowboard") && E.calculerAge() < 6){ return -1; }
			else{
				String insertReservation = "INSERT INTO Reservation (heureDebut, heurefin, aPrisAssurance) VALUES (?,?,?)";
				pst_Res = connect.prepareStatement(insertReservation);
				pst_Res.setInt(1, obj.getHeureDebut());
				pst_Res.setInt(2, obj.getHeureFin());
				pst_Res.setBoolean(3, obj.getAUneAssurance());
				pst_Res.executeUpdate();
				//pst_Res.close();

				
				String selectNumReserv = "SELECT MAX(numReservation) FROM Reservation";
				pst_numReserv = this.connect.prepareStatement(selectNumReserv);
				ResultSet rs = pst_numReserv.executeQuery();
				while (rs.next()) { obj.setNumReservation(rs.getInt(1)); }
				System.out.println("ReservationDao -> " + obj.getNumReservation());

				String insertReservCli 		= "INSERT INTO ReservationClient 	(numReservation, numClient) VALUES (?,?)";
				pst_Res_Cli = connect.prepareStatement(insertReservCli);
				pst_Res_Cli.setInt(1, obj.getNumReservation());
				pst_Res_Cli.setInt(2, obj.getClient().getNumPersonne());
				pst_Res_Cli.executeUpdate();
				System.out.println("ReservationClient insert");
				String insertReservEleve 	= "INSERT INTO ReservationEleve 	(numReservation, numEleve) 	VALUES (?,?)";
				pst_Res_Ele = connect.prepareStatement(insertReservEleve);
				pst_Res_Ele.setInt(1, obj.getNumReservation());
				pst_Res_Ele.setInt(2, obj.getEleve().getNumPersonne());
				pst_Res_Ele.executeUpdate();
				System.out.println("ReservationEleve insert");
				String insertCoursMoniteur 	= "INSERT INTO CoursMoniteur 		(numCours, numMoniteur) 	VALUES (?,?)";
				pst_Cou_Mon = connect.prepareStatement(insertCoursMoniteur);
				pst_Cou_Mon.setInt(1, obj.getCours().getNumCours());
				pst_Cou_Mon.setInt(2, obj.getMoniteur().getNumPersonne());
				pst_Cou_Mon.executeUpdate();
				System.out.println("CoursMoniteur insert");
				String insertCoursSemaine 	= "INSERT INTO CoursSemaine 		(numCours, numSemaine)	 	VALUES (?,?)";
				pst_Cou_Sem = connect.prepareStatement(insertCoursSemaine);
				pst_Cou_Sem.setInt(1, obj.getCours().getNumCours());
				pst_Cou_Sem.setInt(2, obj.getSemaine().getNumSemaine());
				pst_Cou_Sem.executeUpdate();
				System.out.println("CoursSemaine insert");
				String insertReservCours 	= "INSERT INTO ReservationCours 	(numReservation, numCours) 	VALUES (?,?)";
				pst_Res_Cou = connect.prepareStatement(insertReservCours);
				pst_Res_Cou.setInt(1, obj.getNumReservation());
				pst_Res_Cou.setInt(2, obj.getCours().getNumCours());
				pst_Res_Cou.executeUpdate();
				System.out.println("ReservationCours insert");

				/*pst_Res_Cli.close();
				pst_Res_Ele.close();
				pst_Cou_Mon.close();
				pst_Cou_Sem.close();
				pst_Res_Cou.close();*/

				return obj.getNumReservation();
			}
		}

		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst_Res != null || pst_Res_Cli != null || pst_Res_Ele != null || pst_Res_Cou != null || pst_Cou_Mon != null || pst_Cou_Sem != null || pst_numReserv != null) {
				try {
					pst_Res_Cli.close();
					pst_Res_Ele.close();
					pst_Cou_Mon.close();
					pst_Cou_Sem.close();
					pst_Res_Cou.close();
					pst_Res.close(); 
					pst_numReserv.close();
				}
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return -1;
	}
	public boolean delete(Reservation obj) {
		PreparedStatement pst_Res 		= null;
		PreparedStatement pst_Res_Cli 	= null;
		PreparedStatement pst_Res_Ele 	= null;
		PreparedStatement pst_Res_Cou 	= null;
		PreparedStatement pst_Cou_Mon 	= null;
		PreparedStatement pst_Cou_Sem 	= null;
		try
		{
			String deleteReservCli 		= "DELETE FROM ReservationClient 	WHERE numReservation = ?";
			pst_Res_Cli 	= connect.prepareStatement(deleteReservCli);
			pst_Res_Cli.setInt(1, obj.getNumReservation());
			pst_Res_Cli.executeUpdate();
			
			
			
			String deleteReservEleve 	= "DELETE FROM ReservationEleve 	WHERE numReservation = ?";
			pst_Res_Ele 	= connect.prepareStatement(deleteReservEleve);
			pst_Res_Ele.setInt(1, obj.getNumReservation());
			pst_Res_Ele.executeUpdate();
			
			
			
			String deleteReservCours 	= "DELETE FROM ReservationCours 	WHERE numReservation = ?";
			pst_Res_Cou 	= connect.prepareStatement(deleteReservCours);
			pst_Res_Cou.setInt(1, obj.getNumReservation());
			pst_Res_Cou.executeUpdate();
			
			
			
			String deleteCoursMoniteur 	= "DELETE FROM CoursMoniteur WHERE numCours = (SELECT numCours FROM ReservationCours WHERE numReservation = ? ); ";
			pst_Cou_Mon 	= connect.prepareStatement(deleteCoursMoniteur);
			pst_Cou_Mon.setInt(1, obj.getNumReservation());
			pst_Cou_Mon.executeUpdate();
			
			
			String deleteCoursSemaine 	= "DELETE FROM CoursSemaine WHERE numCours = (SELECT numCours FROM ReservationCours WHERE numReservation = ? );";
			pst_Cou_Sem 	= connect.prepareStatement(deleteCoursSemaine);
			pst_Cou_Sem.setInt(1, obj.getNumReservation());
			pst_Cou_Sem.executeUpdate();
			
			String deleteReservation 	= "DELETE FROM Reservation 			WHERE numReservation = ?";
			pst_Res 		= connect.prepareStatement(deleteReservation);
			pst_Res.setInt(1, obj.getNumReservation());
			pst_Res.executeUpdate();
			

			/*PreparedStatement pst_numReserv;
			String selectNumReserv = "SELECT MAX(numReservation) FROM Reservation";
			pst_numReserv = this.connect.prepareStatement(selectNumReserv);
			ResultSet rs = pst_numReserv.executeQuery();
			while (rs.next()) { obj.setNumReservation(rs.getInt(1)); }
			System.out.println("ReservationDao -> " + obj.getNumReservation());*/

			return true;
		}

		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst_Res != null || pst_Res_Cli != null || pst_Res_Ele != null || pst_Res_Cou != null || pst_Cou_Mon != null || pst_Cou_Sem != null) {
				try {
					pst_Res_Cli.close();
					pst_Res_Ele.close();
					pst_Cou_Mon.close();
					pst_Cou_Sem.close();
					pst_Res_Cou.close();
					pst_Res.close(); 
				}
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return false;
	}

	public boolean update(Reservation obj) {
		return false;
	}

	// On cherche un �l�ve gr�ce � son id
	public Reservation find(int id) {
		PreparedStatement pst = null;
		try {
			Semaine S 	= new Semaine();
			Cours C 	= new Cours();
			Eleve E 	= new Eleve();
			Client Cli 	= new Client();
			Moniteur M 	= new Moniteur();

			String sql = "SELECT * FROM Cours "
					+ "INNER JOIN CoursMoniteur ON CoursMoniteur.numCours = Cours.numCours "
					+ "INNER JOIN CoursSemaine ON CoursSemaine.numCours = Cours.numCours "
					//+ "INNER JOIN Personne On Personne.numPersonne = CoursMoniteur.numMoniteur "
					+ "INNER JOIN ReservationCours ON ReservationCours.numCours = Cours.numCours "
					+ "INNER JOIN Reservation ON Reservation.numReservation = ReservationCours.numReservation "
					+ "INNER JOIN ReservationClient ON ReservationClient.numReservation = Reservation.numReservation "
					+ "INNER JOIN ReservationEleve ON ReservationEleve.numReservation = Reservation.numReservation "
					+ "WHERE ReservationClient.numReservation = ? ;";
			// Modifier ReservationClient.numClient si �a ne va pas

			pst = this.connect.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				ArrayList<Accreditation> listeAccred = new ArrayList<Accreditation>();

				String selectSemaine 	= "SELECT * FROM Semaine	WHERE numSemaine 	= ? ";
				String selectCours 		= "SELECT * FROM Cours 		WHERE numCours 		= ? ";
				String selectEleve 		= "SELECT * FROM Eleve "
						+ "INNER JOIN Personne ON Personne.numPersonne = Eleve.numEleve "
						+ "WHERE numPersonne =  ? ";
				String selectClient 	= "SELECT * FROM Client 	INNER JOIN Utilisateur ON Utilisateur.numUtilisateur = Client.numClient "
						+ "INNER JOIN Personne ON Personne.numPersonne = Utilisateur.numUtilisateur "
						+ "WHERE numPersonne =  ?  ";
				String selectMoniteur 	= "SELECT * FROM Moniteur	"
						+ "INNER JOIN Utilisateur ON Utilisateur.numUtilisateur = Moniteur.numMoniteur "
						+ "INNER JOIN Personne ON Personne.numPersonne = Utilisateur.numUtilisateur "
						+ "WHERE numPersonne =  ? ";
				String selectAccredMoni = "Select * from Accreditation INNER JOIN LigneAccreditation "
						+ "ON Accreditation.numAccreditation = LigneAccreditation.numAccreditation WHERE numMoniteur = ?;";

				PreparedStatement pst_Sem = connect.prepareStatement(selectSemaine);
				PreparedStatement pst_Cou = connect.prepareStatement(selectCours);
				PreparedStatement pst_Ele = connect.prepareStatement(selectEleve);
				PreparedStatement pst_Cli = connect.prepareStatement(selectClient);
				PreparedStatement pst_Mon = connect.prepareStatement(selectMoniteur);
				PreparedStatement pst_Acc = connect.prepareStatement(selectAccredMoni);

				pst_Sem.setInt(1, rs.getInt("numSemaine"));
				pst_Cou.setInt(1, rs.getInt("numCours"));
				pst_Ele.setInt(1, rs.getInt("numEleve"));
				pst_Cli.setInt(1, rs.getInt("numClient"));
				pst_Mon.setInt(1, rs.getInt("numMoniteur"));
				pst_Acc.setInt(1, rs.getInt("numMoniteur"));

				ResultSet rs_Sem = pst_Sem.executeQuery();
				ResultSet rs_Cou = pst_Cou.executeQuery();
				ResultSet rs_Ele = pst_Ele.executeQuery();
				ResultSet rs_Cli = pst_Cli.executeQuery();
				ResultSet rs_Acc = pst_Acc.executeQuery();
				ResultSet rs_Mon = pst_Mon.executeQuery();

				while (rs_Sem.next()) { S = new Semaine(rs_Sem.getInt("numSemaine"), rs_Sem.getBoolean("CongeScolaireOuNon"), rs_Sem.getDate("dateDebut"), rs_Sem.getDate("dateFin"), rs_Sem.getInt("numSemaineDansAnnee")); }
				while (rs_Cou.next()) { C = new Cours(rs_Cou.getInt("numCours"), rs_Cou.getString("nomSport"), rs_Cou.getInt("prix"), rs_Cou.getInt("minEleve"), 
						rs_Cou.getInt("maxEleve"), rs_Cou.getString("periodeCours")); }
				while (rs_Ele.next()) { E = new Eleve(rs_Ele.getInt("numEleve"), rs_Ele.getString("nom"), rs_Ele.getString("prenom"),
						rs_Ele.getString("adresse"), rs_Ele.getString("sexe"), rs_Ele.getDate("dateNaissance")); }
				while (rs_Cli.next()) { Cli = new Client(rs_Cli.getInt("numPersonne"), rs_Cli.getString("nom"), rs_Cli.getString("prenom"),
						rs_Cli.getString("adresse"), rs_Cli.getString("sexe"), rs_Cli.getDate("dateNaissance"), rs_Cli.getString("pseudo"),
						rs_Cli.getString("mdp"), rs_Cli.getInt("typeUtilisateur"), rs_Cli.getString("adresseFacturation")); }
				while(rs_Acc.next()){ listeAccred.add(new Accreditation(rs_Acc.getString("nomAccreditation"))); }
				while (rs_Mon.next()) { M = new Moniteur(rs_Mon.getInt("numMoniteur"), rs_Mon.getString("nom"), rs_Mon.getString("prenom"),
						rs_Mon.getString("adresse"), rs_Mon.getString("sexe"), rs_Mon.getDate("dateNaissance"), rs_Mon.getString("pseudo"), rs_Mon.getString("mdp"),
						rs_Mon.getInt("typeUtilisateur"), listeAccred); }

				Reservation reservation = new Reservation(rs.getInt("heureDebut"), rs.getInt("heureFin"), rs.getInt("numReservation"), rs.getBoolean("aPrisAssurance"), S, C, E, Cli, M);
				return reservation;
			}
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

	public  ArrayList<Reservation> getList() {
		ArrayList<Reservation> liste = new ArrayList<Reservation>();

		PreparedStatement pst = null;
		try {
			Semaine S 	= new Semaine();
			Cours C 	= new Cours();
			Eleve E 	= new Eleve();
			Client Cli 	= new Client();
			Moniteur M 	= new Moniteur();

			String sql = "SELECT * FROM Cours "
					+ "INNER JOIN CoursMoniteur ON CoursMoniteur.numCours = Cours.numCours "
					+ "INNER JOIN CoursSemaine ON CoursSemaine.numCours = Cours.numCours "
					//+ "INNER JOIN Personne On Personne.numPersonne = CoursMoniteur.numMoniteur "
					+ "INNER JOIN ReservationCours ON ReservationCours.numCours = Cours.numCours "
					+ "INNER JOIN Reservation ON Reservation.numReservation = ReservationCours.numReservation "
					+ "INNER JOIN ReservationClient ON ReservationClient.numReservation = Reservation.numReservation "
					+ "INNER JOIN ReservationEleve ON ReservationEleve.numReservation = Reservation.numReservation"
					+ "GROUP BY Reservation.numReservation;";

			pst = this.connect.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				//System.out.println("Reservation DAO -> " + rs.getInt("numReservation"));
				ArrayList<Accreditation> listeAccred = new ArrayList<Accreditation>();

				String selectSemaine 	= "SELECT * FROM Semaine	WHERE numSemaine 	= ? ";
				String selectCours 		= "SELECT * FROM Cours 		WHERE numCours 		= ? ";
				String selectEleve 		= "SELECT * FROM Eleve "
						+ "INNER JOIN Personne ON Personne.numPersonne = Eleve.numEleve "
						+ "WHERE numPersonne =  ? ";
				String selectClient 	= "SELECT * FROM Client 	INNER JOIN Utilisateur ON Utilisateur.numUtilisateur = Client.numClient "
						+ "INNER JOIN Personne ON Personne.numPersonne = Utilisateur.numUtilisateur "
						+ "WHERE numPersonne =  ?  ";
				String selectMoniteur 	= "SELECT * FROM Moniteur	"
						+ "INNER JOIN Utilisateur ON Utilisateur.numUtilisateur = Moniteur.numMoniteur "
						+ "INNER JOIN Personne ON Personne.numPersonne = Utilisateur.numUtilisateur "
						+ "WHERE numPersonne =  ? ";
				String selectAccredMoni = "Select * from Accreditation INNER JOIN LigneAccreditation "
						+ "ON Accreditation.numAccreditation = LigneAccreditation.numAccreditation WHERE numMoniteur = ?;";

				PreparedStatement pst_Sem = connect.prepareStatement(selectSemaine);
				PreparedStatement pst_Cou = connect.prepareStatement(selectCours);
				PreparedStatement pst_Ele = connect.prepareStatement(selectEleve);
				PreparedStatement pst_Cli = connect.prepareStatement(selectClient);
				PreparedStatement pst_Mon = connect.prepareStatement(selectMoniteur);
				PreparedStatement pst_Acc = connect.prepareStatement(selectAccredMoni);

				pst_Sem.setInt(1, rs.getInt("numSemaine"));
				pst_Cou.setInt(1, rs.getInt("numCours"));
				pst_Ele.setInt(1, rs.getInt("numEleve"));
				pst_Cli.setInt(1, rs.getInt("numClient"));
				pst_Mon.setInt(1, rs.getInt("numMoniteur"));
				pst_Acc.setInt(1, rs.getInt("numMoniteur"));

				ResultSet rs_Sem = pst_Sem.executeQuery();
				ResultSet rs_Cou = pst_Cou.executeQuery();
				ResultSet rs_Ele = pst_Ele.executeQuery();
				ResultSet rs_Cli = pst_Cli.executeQuery();
				ResultSet rs_Acc = pst_Acc.executeQuery();
				ResultSet rs_Mon = pst_Mon.executeQuery();

				while (rs_Sem.next()) { S = new Semaine(rs_Sem.getInt("numSemaine"), rs_Sem.getBoolean("CongeScolaireOuNon"), rs_Sem.getDate("dateDebut"), rs_Sem.getDate("dateFin"), rs_Sem.getInt("numSemaineDansAnnee")); }
				while (rs_Cou.next()) { C = new Cours(rs_Cou.getInt("numCours"), rs_Cou.getString("nomSport"), rs_Cou.getInt("prix"), rs_Cou.getInt("minEleve"), 
						rs_Cou.getInt("maxEleve"), rs_Cou.getString("periodeCours")); }
				while (rs_Ele.next()) { E = new Eleve(rs_Ele.getInt("numEleve"), rs_Ele.getString("nom"), rs_Ele.getString("prenom"),
						rs_Ele.getString("adresse"), rs_Ele.getString("sexe"), rs_Ele.getDate("dateNaissance")); }
				while (rs_Cli.next()) { Cli = new Client(rs_Cli.getInt("numPersonne"), rs_Cli.getString("nom"), rs_Cli.getString("prenom"),
						rs_Cli.getString("adresse"), rs_Cli.getString("sexe"), rs_Cli.getDate("dateNaissance"), rs_Cli.getString("pseudo"),
						rs_Cli.getString("mdp"), rs_Cli.getInt("typeUtilisateur"), rs_Cli.getString("adresseFacturation")); }
				while(rs_Acc.next()){ listeAccred.add(new Accreditation(rs_Acc.getString("nomAccreditation"))); }
				while (rs_Mon.next()) { M = new Moniteur(rs_Mon.getInt("numMoniteur"), rs_Mon.getString("nom"), rs_Mon.getString("prenom"),
						rs_Mon.getString("adresse"), rs_Mon.getString("sexe"), rs_Mon.getDate("dateNaissance"), rs_Mon.getString("pseudo"), rs_Mon.getString("mdp"),
						rs_Mon.getInt("typeUtilisateur"), listeAccred); }

				Reservation reservation = new Reservation(rs.getInt("heureDebut"), rs.getInt("heureFin"), rs.getInt("numReservation"), rs.getBoolean("aPrisAssurance"), S, C, E, Cli, M);
				liste.add(reservation);
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

	public  ArrayList<Reservation> getMyList(int idPersonne) {
		ArrayList<Reservation> liste = new ArrayList<Reservation>();

		PreparedStatement pst = null;
		try {
			Semaine S 	= new Semaine();
			Cours C 	= new Cours();
			Eleve E 	= new Eleve();
			Client Cli 	= new Client();
			Moniteur M 	= new Moniteur();

			String innerTypePersonne = "INNER JOIN Personne On Personne.numPersonne = ReservationClient.numClient ";

			// IL FAUT SAVOIR SI LA PERSONNE ENTREE EN PARAM EST UN CLIENT OU UN MONITEUR
			String rechercheMon = "SELECT * FROM Moniteur WHERE numMoniteur = ?";
			//String rechercheCli = "SELECT * FROM Client WHERE numClient = ?";

			PreparedStatement pst_Rec_Mon = this.connect.prepareStatement(rechercheMon);
			//PreparedStatement pst_Rec_Cli = this.connect.prepareStatement(rechercheCli);

			pst_Rec_Mon.setInt(1, idPersonne);
			//pst_Rec_Cli.setInt(1, idPersonne);

			ResultSet res_Rec_Mon = pst_Rec_Mon.executeQuery();
			//ResultSet res_Rec_Cli = pst_Rec_Cli.executeQuery();

			if (res_Rec_Mon.isBeforeFirst() ) { innerTypePersonne = "INNER JOIN Personne On Personne.numPersonne = CoursMoniteur.numMoniteur "; } 

			String sql = "SELECT distinct * FROM Cours "
					+ "INNER JOIN CoursMoniteur ON CoursMoniteur.numCours = Cours.numCours "
					+ "INNER JOIN CoursSemaine ON CoursSemaine.numCours = Cours.numCours "
					+ "INNER JOIN ReservationCours ON ReservationCours.numCours = Cours.numCours "
					+ "INNER JOIN Reservation ON Reservation.numReservation = ReservationCours.numReservation "
					+ "INNER JOIN ReservationClient ON ReservationClient.numReservation = Reservation.numReservation "
					+ "INNER JOIN ReservationEleve ON ReservationEleve.numReservation = Reservation.numReservation "
					+ innerTypePersonne
					+ "WHERE Personne.numPersonne = ?"
					+ "GROUP BY Reservation.numReservation;";

			pst = this.connect.prepareStatement(sql);
			pst.setInt(1, idPersonne);
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				ArrayList<Accreditation> listeAccred = new ArrayList<Accreditation>();

				String selectSemaine 	= "SELECT * FROM Semaine	WHERE numSemaine 	= ? ";
				String selectCours 		= "SELECT * FROM Cours 		WHERE numCours 		= ? ";
				String selectEleve 		= "SELECT * FROM Eleve "
						+ "INNER JOIN Personne ON Personne.numPersonne = Eleve.numEleve "
						+ "WHERE numPersonne =  ? ";
				String selectClient 	= "SELECT * FROM Client 	INNER JOIN Utilisateur ON Utilisateur.numUtilisateur = Client.numClient "
						+ "INNER JOIN Personne ON Personne.numPersonne = Utilisateur.numUtilisateur "
						+ "WHERE numPersonne =  ?  ";
				String selectMoniteur 	= "SELECT * FROM Moniteur	"
						+ "INNER JOIN Utilisateur ON Utilisateur.numUtilisateur = Moniteur.numMoniteur "
						+ "INNER JOIN Personne ON Personne.numPersonne = Utilisateur.numUtilisateur "
						+ "WHERE numPersonne =  ? ";
				String selectAccredMoni = "Select * from Accreditation INNER JOIN LigneAccreditation "
						+ "ON Accreditation.numAccreditation = LigneAccreditation.numAccreditation WHERE numMoniteur = ?;";

				PreparedStatement pst_Sem = connect.prepareStatement(selectSemaine);
				PreparedStatement pst_Cou = connect.prepareStatement(selectCours);
				PreparedStatement pst_Ele = connect.prepareStatement(selectEleve);
				PreparedStatement pst_Cli = connect.prepareStatement(selectClient);
				PreparedStatement pst_Mon = connect.prepareStatement(selectMoniteur);
				PreparedStatement pst_Acc = connect.prepareStatement(selectAccredMoni);

				pst_Sem.setInt(1, rs.getInt("numSemaine"));
				pst_Cou.setInt(1, rs.getInt("numCours"));
				pst_Ele.setInt(1, rs.getInt("numEleve"));
				pst_Cli.setInt(1, rs.getInt("numClient"));
				pst_Mon.setInt(1, rs.getInt("numMoniteur"));
				pst_Acc.setInt(1, rs.getInt("numMoniteur"));

				ResultSet rs_Sem = pst_Sem.executeQuery();
				ResultSet rs_Cou = pst_Cou.executeQuery();
				ResultSet rs_Ele = pst_Ele.executeQuery();
				ResultSet rs_Cli = pst_Cli.executeQuery();
				ResultSet rs_Acc = pst_Acc.executeQuery();
				ResultSet rs_Mon = pst_Mon.executeQuery();

				while (rs_Sem.next()) { S = new Semaine(rs_Sem.getInt("numSemaine"), rs_Sem.getBoolean("CongeScolaireOuNon"), rs_Sem.getDate("dateDebut"), rs_Sem.getDate("dateFin"), rs_Sem.getInt("numSemaineDansAnnee")); }
				while (rs_Cou.next()) { C = new Cours(rs_Cou.getInt("numCours"), rs_Cou.getString("nomSport"), rs_Cou.getInt("prix"), rs_Cou.getInt("minEleve"), 
						rs_Cou.getInt("maxEleve"), rs_Cou.getString("periodeCours")); }
				while (rs_Ele.next()) { E = new Eleve(rs_Ele.getInt("numEleve"), rs_Ele.getString("nom"), rs_Ele.getString("prenom"),
						rs_Ele.getString("adresse"), rs_Ele.getString("sexe"), rs_Ele.getDate("dateNaissance")); }
				while (rs_Cli.next()) { Cli = new Client(rs_Cli.getInt("numPersonne"), rs_Cli.getString("nom"), rs_Cli.getString("prenom"),
						rs_Cli.getString("adresse"), rs_Cli.getString("sexe"), rs_Cli.getDate("dateNaissance"), rs_Cli.getString("pseudo"),
						rs_Cli.getString("mdp"), rs_Cli.getInt("typeUtilisateur"), rs_Cli.getString("adresseFacturation")); }
				while(rs_Acc.next()){ listeAccred.add(new Accreditation(rs_Acc.getString("nomAccreditation"))); }
				while (rs_Mon.next()) { M = new Moniteur(rs_Mon.getInt("numMoniteur"), rs_Mon.getString("nom"), rs_Mon.getString("prenom"),
						rs_Mon.getString("adresse"), rs_Mon.getString("sexe"), rs_Mon.getDate("dateNaissance"), rs_Mon.getString("pseudo"), rs_Mon.getString("mdp"),
						rs_Mon.getInt("typeUtilisateur"), listeAccred); }

				Reservation reservation = new Reservation(rs.getInt("heureDebut"), rs.getInt("heureFin"), rs.getInt("numReservation"), rs.getBoolean("aPrisAssurance"), S, C, E, Cli, M);
				liste.add(reservation);
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

	public  ArrayList<Reservation> getListSemainePerdiodeMoniteur(int numMoniteur, int numSemaine, String periode) {
		ArrayList<Reservation> liste = new ArrayList<Reservation>();

		PreparedStatement pst = null;
		try {
			Semaine S 	= new Semaine();
			Cours C 	= new Cours();
			Eleve E 	= new Eleve();
			Client Cli 	= new Client();
			Moniteur M 	= new Moniteur();

			String sql = "SELECT * FROM Cours "
					+ "INNER JOIN CoursMoniteur ON CoursMoniteur.numCours = Cours.numCours "
					+ "INNER JOIN CoursSemaine ON CoursSemaine.numCours = Cours.numCours "
					+ "INNER JOIN ReservationCours ON ReservationCours.numCours = Cours.numCours "
					+ "INNER JOIN Reservation ON Reservation.numReservation = ReservationCours.numReservation "
					+ "INNER JOIN ReservationClient ON ReservationClient.numReservation = Reservation.numReservation "
					+ "INNER JOIN ReservationEleve ON ReservationEleve.numReservation = Reservation.numReservation "
					+ "WHERE CoursSemaine.numSemaine = ? AND Cours.periodeCours = ? ;";

			pst = this.connect.prepareStatement(sql);
			//pst.setInt(1, numMoniteur);
			pst.setInt(1, numSemaine);
			pst.setString(2, periode);
			System.out.println("ReservationDao -> numSemaine : " + numSemaine);
			System.out.println("ReservationDao -> periode : " + periode);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				ArrayList<Accreditation> listeAccred = new ArrayList<Accreditation>();

				String selectSemaine 	= "SELECT * FROM Semaine	WHERE numSemaine 	= ? ";
				String selectCours 		= "SELECT * FROM Cours 		WHERE numCours 		= ? ";
				String selectEleve 		= "SELECT * FROM Eleve "
						+ "INNER JOIN Personne ON Personne.numPersonne = Eleve.numEleve "
						+ "WHERE numPersonne =  ? ";
				String selectClient 	= "SELECT * FROM Client 	INNER JOIN Utilisateur ON Utilisateur.numUtilisateur = Client.numClient "
						+ "INNER JOIN Personne ON Personne.numPersonne = Utilisateur.numUtilisateur "
						+ "WHERE numPersonne =  ?  ";
				String selectMoniteur 	= "SELECT * FROM Moniteur	"
						+ "INNER JOIN Utilisateur ON Utilisateur.numUtilisateur = Moniteur.numMoniteur "
						+ "INNER JOIN Personne ON Personne.numPersonne = Utilisateur.numUtilisateur "
						+ "WHERE numPersonne =  ? ";
				String selectAccredMoni = "Select * from Accreditation INNER JOIN LigneAccreditation "
						+ "ON Accreditation.numAccreditation = LigneAccreditation.numAccreditation WHERE numMoniteur = ?;";

				PreparedStatement pst_Sem = connect.prepareStatement(selectSemaine);
				PreparedStatement pst_Cou = connect.prepareStatement(selectCours);
				PreparedStatement pst_Ele = connect.prepareStatement(selectEleve);
				PreparedStatement pst_Cli = connect.prepareStatement(selectClient);
				PreparedStatement pst_Mon = connect.prepareStatement(selectMoniteur);
				PreparedStatement pst_Acc = connect.prepareStatement(selectAccredMoni);

				pst_Sem.setInt(1, rs.getInt("numSemaine"));
				pst_Cou.setInt(1, rs.getInt("numCours"));
				pst_Ele.setInt(1, rs.getInt("numEleve"));
				pst_Cli.setInt(1, rs.getInt("numClient"));
				pst_Mon.setInt(1, rs.getInt("numMoniteur"));
				pst_Acc.setInt(1, rs.getInt("numMoniteur"));

				ResultSet rs_Sem = pst_Sem.executeQuery();
				ResultSet rs_Cou = pst_Cou.executeQuery();
				ResultSet rs_Ele = pst_Ele.executeQuery();
				ResultSet rs_Cli = pst_Cli.executeQuery();
				ResultSet rs_Acc = pst_Acc.executeQuery();
				ResultSet rs_Mon = pst_Mon.executeQuery();

				while (rs_Sem.next()) { S = new Semaine(rs_Sem.getInt("numSemaine"), rs_Sem.getBoolean("CongeScolaireOuNon"), rs_Sem.getDate("dateDebut"), rs_Sem.getDate("dateFin"), rs_Sem.getInt("numSemaineDansAnnee")); }
				while (rs_Cou.next()) { C = new Cours(rs_Cou.getInt("numCours"), rs_Cou.getString("nomSport"), rs_Cou.getInt("prix"), rs_Cou.getInt("minEleve"), 
						rs_Cou.getInt("maxEleve"), rs_Cou.getString("periodeCours")); }
				while (rs_Ele.next()) { E = new Eleve(rs_Ele.getInt("numEleve"), rs_Ele.getString("nom"), rs_Ele.getString("prenom"),
						rs_Ele.getString("adresse"), rs_Ele.getString("sexe"), rs_Ele.getDate("dateNaissance")); }
				while (rs_Cli.next()) { Cli = new Client(rs_Cli.getInt("numPersonne"), rs_Cli.getString("nom"), rs_Cli.getString("prenom"),
						rs_Cli.getString("adresse"), rs_Cli.getString("sexe"), rs_Cli.getDate("dateNaissance"), rs_Cli.getString("pseudo"),
						rs_Cli.getString("mdp"), rs_Cli.getInt("typeUtilisateur"), rs_Cli.getString("adresseFacturation")); }
				while(rs_Acc.next()){ listeAccred.add(new Accreditation(rs_Acc.getString("nomAccreditation"))); }
				while (rs_Mon.next()) { M = new Moniteur(rs_Mon.getInt("numMoniteur"), rs_Mon.getString("nom"), rs_Mon.getString("prenom"),
						rs_Mon.getString("adresse"), rs_Mon.getString("sexe"), rs_Mon.getDate("dateNaissance"), rs_Mon.getString("pseudo"), rs_Mon.getString("mdp"),
						rs_Mon.getInt("typeUtilisateur"), listeAccred); }

				Reservation reservation = new Reservation(rs.getInt("heureDebut"), rs.getInt("heureFin"), rs.getInt("numReservation"), rs.getBoolean("aPrisAssurance"), S, C, E, Cli, M);
				liste.add(reservation);
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

	public boolean updateAssurance(int numEleve, int numSemaine, String periode){
		PreparedStatement pst_upd = null;
		try {
			String verifPeriode;
			switch(periode){
			case "09-12": verifPeriode = " = '14-17' ";
			break;
			case "14-17": verifPeriode = " = '09-12' ";
			break;
			case "12-13": verifPeriode = " IN('12-14', '13-14') ";
			break;
			case "13-14": verifPeriode = " IN('12-13', '12-14') ";
			break;
			case "12-14": verifPeriode = " IN('12-13', '13-14') ";
			break;
			default : verifPeriode = " = ? ";
			break;
			}


			String upd_ass = "UPDATE Reservation SET aPrisAssurance = 0 WHERE Reservation.numReservation in ( "
					+ "SELECT ReservationClient.numReservation FROM ReservationClient "
					+ "INNER JOIN ReservationEleve ON ReservationEleve.numReservation = Reservation.numReservation "
					+ "INNER JOIN ReservationCours ON ReservationCours.numReservation = Reservation.numReservation "
					+ "INNER JOIN Cours ON ReservationCours.numCours = Cours.numCours "
					+ "INNER JOIN CoursSemaine ON CoursSemaine.numCours = Cours.numCours "
					+ "WHERE aPrisAssurance = 1 AND numEleve = ? AND numSemaine = ? AND Cours.periodeCours "+ verifPeriode +");";

			pst_upd = this.connect.prepareStatement(upd_ass);
			pst_upd.setInt(1, numEleve);
			pst_upd.setInt(2, numSemaine);
			//pst_upd.setString(3, periode);
			pst_upd.executeUpdate();
			return true;
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst_upd != null) {
				try { pst_upd.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return false;
	}

	public int valeurReduction(int numSemaine){
		PreparedStatement pst_rech_elev = null;
		PreparedStatement pst_rech_reduc = null;
		int sommeReduc = 0;
		try {
			String sql_rech_elev = "select distinct numEleve from ReservationEleve WHERE numReservation IN "
					+ "(SELECT numReservation FROM ReservationCours WHERE numCours IN "
					+ "(SELECT numCours FROM CoursSemaine WHERE numSemaine = ?));";
			pst_rech_elev = this.connect.prepareStatement(sql_rech_elev);
			pst_rech_elev.setInt(1, numSemaine);

			ResultSet res_rech_Elev = pst_rech_elev.executeQuery();
			while (res_rech_Elev.next()) {
				int numEleve = res_rech_Elev.getInt("numEleve");
				
					String sql_rech_reduc = "select count(*) AS cptCours, (sum(prix)*15 /100) AS somme FROM Cours WHERE numCours IN "
							+ "( SELECT distinct Cours.numCours FROM Cours "
							+ "INNER JOIN ReservationCours ON ReservationCours.numCours = Cours.numCours "
							+ "INNER JOIN ReservationEleve ON ReservationCours.numReservation = ReservationEleve.numReservation "
							+ "INNER JOIN CoursSemaine ON CoursSemaine.numCours = Cours.numCours "
							+ "WHERE numSemaine = ? AND periodeCours IN('09-12', '14-17') AND numEleve = " + numEleve + ");";

					pst_rech_reduc = this.connect.prepareStatement(sql_rech_reduc);
					pst_rech_reduc.setInt(1, numSemaine);
					ResultSet res_rech_reduc = pst_rech_reduc.executeQuery();
					while (res_rech_reduc.next()) {
						if (res_rech_reduc.getInt("cptCours") > 1)
							sommeReduc+= res_rech_reduc.getInt("somme");
					}
				}
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst_rech_elev != null || pst_rech_reduc != null) {
				try { 
					pst_rech_elev.close();
					pst_rech_reduc.close();
				}
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return sommeReduc;
	}

	@Override public String calculerPlaceCours(int numCours, int numSemaine, int numMoniteur) { return -1 + ""; }
	@Override public ArrayList<Reservation> getListCoursSelonId(int idMoniteur) { return null; }
	@Override public ArrayList<Reservation> getListCoursCollectifSelonId(int numMoniteur, int numEleve, String periode, int numSemaine) { return null; }
	@Override public ArrayList<Reservation> getListCoursParticulierSelonId(int numMoniteur, String periode, int numSemaine) { return null; }
	@Override public ArrayList<Reservation> getListEleveSelonAccredProfEtCours(int numSemaine, int numMoniteur, String periode) { return null; }
	@Override public void creerTouteDisponibilites() { }
	@Override public void creerTouteDisponibilitesSelonMoniteur(int i) { }
	@Override public boolean changeDispoSelonIdSemaine(int numSemaine, int numMoniteur) { return false; }
	@Override public ArrayList<Reservation> getListDispo(int numSemaine, String periode) { return null; }
	@Override public Reservation returnUser(String mdp, String pseudo) { return null; }
}

