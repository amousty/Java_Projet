package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import POJO.Eleve;
import POJO.Personne;

public class EleveDAO extends DAO<Eleve> {
	AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<Personne> PersonneDao = adf.getPersonneDAO();
	public EleveDAO(Connection conn) { super(conn); }

	public int create(Eleve obj) {
		PreparedStatement pst = null;
		try {
			System.out.println("EleveDao -> " + obj.getNumPersonne());
			int numPersonne = obj.getNumPersonne();
			//V�rification que la personne n'est pas encore inscrite en tant qu'�l�ve.
			Eleve e = find(numPersonne);
			// La personne n'existe pas
			if (e == null || numPersonne == -1){
				Personne P = new Personne(-1,obj.getNom(), obj.getPre(), obj.getAdresse(), obj.getSexe(), obj.getDateNaissance());
				numPersonne = PersonneDao.create(P);
				if (numPersonne == -1){
					PersonneDao.delete(P);
					return -1;
				}
			}

			String requete5 = "INSERT INTO Eleve (categorie, numEleve) VALUES (?,?)";
			pst = connect.prepareStatement(requete5);

			pst.setString(1, obj.getCategorie());
			pst.setInt(2, numPersonne);
			pst.executeUpdate();
			pst.close();
			System.out.println("Ajout d'un eleve effectue");
			return numPersonne;
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

	public boolean delete(Eleve obj) { return false; }
	public boolean update(Eleve obj) { return false; }

	// On cherche une Eleve gr�ce � son id
	public Eleve find(int id) {
		Eleve eleve = new Eleve();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM Eleve INNER JOIN Personne ON Eleve.numEleve = Personne.numPersonne WHERE numEleve = ?;";
			pst = this.connect.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet result = pst.executeQuery();
			// int numPersonne, String nom, String pre, String adresse, String sexe, Date dateNaissance, boolean aUneAssurance
			while (result.next()) {
				eleve = new Eleve(result.getInt("numEleve"), result.getString("nom"), result.getString("prenom"), result.getString("adresse"), 
						result.getString("sexe"), result.getDate("dateNaissance"));
			}
			return eleve;
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

	public ArrayList<Eleve> getList() {

		ArrayList<Eleve> liste = new ArrayList<Eleve>();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM Eleve INNER JOIN Personne On Personne.numPersonne = Eleve.numEleve";
			pst = this.connect.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				//int numPersonne, String nom, String pre, String adresse, String sexe, Date dateNaissance, boolean aUneAssurance
				Eleve eleve = new Eleve(rs.getInt("numEleve"), rs.getString("nom"), rs.getString("prenom"), rs.getString("adresse"), rs.getString("sexe"), rs.getDate("dateNaissance"));
				liste.add(eleve);
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

	/*public HashSet<Eleve> getListEleveSelonAccredProfEtCours(int numMoniteur, int numSemaine, String periode){
		// La personne ne peut pas �tre visible si elle a d�j� �t� s�lectionn�e pour un cours (attention aux horaires)
		boolean aDejaItere = false;
		HashSet<Eleve> listeFiltree = new HashSet<Eleve>();
		ArrayList<Eleve> listeFull =  getList(); 
		Moniteur M = MoniteurDAO.find(numMoniteur);
		ArrayList<Accreditation> listeAccredMoniteur = M.getAccrediList();
		ArrayList<Reservation> listReservation = ReservationDAO.getListSemainePerdiodeMoniteur(numMoniteur, numSemaine, periode);

		for(Accreditation A : listeAccredMoniteur){
			for(Reservation R : listReservation){
				if(!aDejaItere){
					for(Eleve eFull : listeFull){
						if(A.getNom().equals(eFull.getCategorie())){
							listeFiltree.add(eFull);
						}
					}
				} // if bool
				
			}
			
		}
		aDejaItere = true;
		Iterator<Reservation> res = listReservation.iterator();
		while (res.hasNext()) {
			Reservation resNext = res.next();
			Iterator<Eleve> el = listeFiltree.iterator();
			while (el.hasNext()) {
				Eleve elNext = el.next();
				if (elNext.getNumPersonne() == resNext.getEleve().getNumPersonne()) { el.remove(); } 
			}
		}
		return listeFiltree;
	}*/
	
	public ArrayList<Eleve> getListEleveSelonAccredProfEtCours(int numSemaine, int numMoniteur, String periode){
		ArrayList<Eleve> liste = new ArrayList<Eleve>();
		PreparedStatement pst = null;
		try {
			/*String sql = "SELECT * from Eleve "
				+ "INNER JOIN Personne ON Eleve.numEleve = Personne.numPersonne "
				+ "WHERE numEleve NOT IN "
				+ "(Select numEleve from ReservationEleve "
				+ "INNER JOIN ReservationCours ON ReservationCours.numReservation = ReservationEleve.numReservation "
				+ "INNER JOIN Cours ON Cours.numCours = ReservationCours.numCours "
				+ "INNER JOIN CoursSemaine ON CoursSemaine.numCours = Cours.numCours "
				+ "INNER JOIN CoursMoniteur ON CoursMoniteur.numCours = Cours.numCours "
				+ "WHERE CoursSemaine.numSemaine = ? AND CoursMoniteur.numMoniteur = ? AND Cours.PeriodeCours = ? AND Cours.numCours = ?)"
				+ "AND categorie IN " 
				+ "(SELECT NomAccreditation FROM Accreditation WHERE numAccreditation IN "
				+ "(SELECT numAccreditation FROM LigneAccreditation WHERE numMoniteur =  ?));";
			pst = this.connect.prepareStatement(sql);
			pst.setInt(1, numSemaine);
			pst.setInt(2, numMoniteur);
			pst.setString(3, periode);
			pst.setInt(4, cours);
			pst.setInt(5, numMoniteur);
				*/
			String verifPeriode;
			switch(periode){
				case "12-13": verifPeriode = " IN('12-14',?) ";
					break;
				case "13-14": verifPeriode = " IN('12-14',?) ";
					break;
				case "12-14": verifPeriode = " IN('12-13', '13-14', ?) ";
					break;
				default : verifPeriode = " = ? ";
					break;
			}
				
			
			String sql = "SELECT * FROM Eleve " 
					+ "INNER JOIN PERSONNE ON Personne.numPersonne = Eleve.numEleve "
					+ "WHERE categorie IN "
					    + "(SELECT NomAccreditation FROM Accreditation WHERE numAccreditation IN "
					        + "(SELECT numAccreditation FROM LigneAccreditation WHERE numMoniteur =  ?)) "
					+ "AND numEleve NOT IN "
					    + "( SELECT numEleve FROM ReservationEleve WHERE numReservation IN ( "
					        + "SELECT numReservation FROM ReservationCours  "
					        + "WHERE ReservationCours.numCours IN ( SELECT Cours.numCours FROM COURS "
					        + "INNER JOIN CoursSemaine ON CoursSemaine.numCours = Cours.numCours "
					        + "WHERE periodeCours" + verifPeriode + "AND CoursSemaine.numSemaine = ?)));";
	        pst = this.connect.prepareStatement(sql);
			
			pst.setInt(1, numMoniteur);
			pst.setString(2, periode);
			//pst.setInt(4, cours);
			pst.setInt(3, numSemaine);
			ResultSet result = pst.executeQuery();
			// int numPersonne, String nom, String pre, String adresse, String sexe, Date dateNaissance, boolean aUneAssurance
			while (result.next()) {
				liste.add(new Eleve(result.getInt("numEleve"), result.getString("nom"), result.getString("prenom"), result.getString("adresse"), 
						result.getString("sexe"), result.getDate("dateNaissance")));
			}
			return liste;
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

	@Override public String calculerPlaceCours(int numCours, int numSemaine, int numMoniteur) { return -1 + ""; }
	@Override public ArrayList<Eleve> getListCoursSelonId(int idMoniteur) { return null; }
	@Override public ArrayList<Eleve> getListCoursCollectifSelonId(int numMoniteur, int numEleve, String periode, int numSemaine) { return null; }
	@Override public ArrayList<Eleve> getListCoursParticulierSelonId(int numMoniteur, String periode, int numSemaine) { return null; }
	@Override public ArrayList<Eleve> getMyList(int idPersonne) { return null; }
	@Override public ArrayList<Eleve> getListSemainePerdiodeMoniteur(int numMoniteur, int numSemaine, String periode) { return null; }
	@Override public boolean updateAssurance(int numEleve, int numSemaine, String periode) { return false; }
	@Override public void creerTouteDisponibilites() { }
	@Override public void creerTouteDisponibilitesSelonMoniteur(int i) { }
	@Override public boolean changeDispoSelonIdSemaine(int numSemaine, int numMoniteur) { return false; }
	@Override public ArrayList<Eleve> getListDispo(int numSemaine, String periode) { return null; }
	@Override public Eleve returnUser(String mdp, String pseudo) { return null; }
	@Override public int valeurReduction(int numSem) { return 0; }
}