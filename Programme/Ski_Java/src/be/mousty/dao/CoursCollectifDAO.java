package be.mousty.dao;
/**
	Classe DAO permettant � effectuer des requ�tes et les transformer en objet POJO.
	@author Adrien MOUSTY
	@version Finale 1.3.3
	@category DAO
*/
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import be.mousty.pojo.Cours;
import be.mousty.pojo.CoursCollectif;
import be.mousty.pojo.Eleve;

public class CoursCollectifDAO extends DAO<CoursCollectif> {
	AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<Eleve> EleveDAO = adf.getEleveDAO();
	DAO<Cours> CoursDAO = adf.getCoursDAO();
	
	public CoursCollectifDAO(Connection conn) { super(conn); }

	@Override
	public int create		(CoursCollectif obj) { return -1; }
	public boolean delete	(CoursCollectif obj) { return false; }
	public boolean update	(CoursCollectif obj) { return false; }

	// On cherche une CoursCollectif gr�ce � son id
	/**
		Objectif : Retourner un enregistrement de la DB par rapport � sa cl� primaire.
		@version Finale 1.3.3
		@param la valeur de la cl� primaire.
		@return Une instance de l'objet initialis�e avec les valeurs issue de la DB.
	 */
	@Override public CoursCollectif find(int id) {
		CoursCollectif CC = new CoursCollectif();
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM CoursCollectif INNER JOIN Cours ON CoursCollectif.numCoursCollectif = Cours.numCours WHERE numCoursCollectif = ?;";
			pst = this.connect.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet res_cou_col = pst.executeQuery();
			while (res_cou_col.next()) {
				// int numCours, String nomSport, double prix, int minEleve, int maxEleve, String periodeCours, String categorieAge, String niveauCours
				CC.setNumCoursCollectif(res_cou_col.getInt("numCoursCollectif"));
				CC.setCategorieAge(res_cou_col.getString("categorieAge"));
				CC.setNiveauCours(res_cou_col.getString("niveauCours"));
				CC.setNumCours(res_cou_col.getInt("numCours"));
				CC.setNomSport(res_cou_col.getString("nomSport"));
				CC.setPrix(res_cou_col.getInt("prix"));
				CC.setMinEl(res_cou_col.getInt("minEleve"));
				CC.setMaxEl(res_cou_col.getInt("maxEleve"));
				CC.setPeriodeCours(res_cou_col.getString("periodeCours"));
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst != null) {
				try { pst.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return CC;
	}

	/**
		Objectif : Retourner la liste compl�te des enregistrements contenu dans une table
		@version Finale 1.3.3
		@return La liste compl�te des utilisateurs.
	 */
	@Override public ArrayList<CoursCollectif> getList() {
		ArrayList<CoursCollectif> liste = new ArrayList<CoursCollectif>();
		
		PreparedStatement pst = null;
		try {
			String sql = "SELECT * FROM CoursCollectif INNER JOIN Cours ON Cours.numCours = CoursCollectif.numCoursCollectif";
			pst = this.connect.prepareStatement(sql);
			ResultSet res_cou_col = pst.executeQuery();
			while (res_cou_col.next()) {
				CoursCollectif CC = new CoursCollectif();
				CC.setNumCoursCollectif(res_cou_col.getInt("numCoursCollectif"));
				CC.setCategorieAge(res_cou_col.getString("categorieAge"));
				CC.setNiveauCours(res_cou_col.getString("niveauCours"));
				CC.setNumCours(res_cou_col.getInt("numCours"));
				CC.setNomSport(res_cou_col.getString("nomSport"));
				CC.setPrix(res_cou_col.getInt("prix"));
				CC.setMinEl(res_cou_col.getInt("minEleve"));
				CC.setMaxEl(res_cou_col.getInt("maxEleve"));
				CC.setPeriodeCours(res_cou_col.getString("periodeCours"));
				liste.add(CC);
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
	
	/**
	Objectif : Retourner une liste filtr�e de cours collectifs.
	@version Finale 1.3.3
	@param Le num�ro du moniteur pour afficher les cours selon les accr�ditations.
	@param Le num�ro de la semaine pour v�rifier les disponibilit�s 
	@xparam Le num�ro de l'�l�ves pour coller � sa cat�gorie
	@param La p�riode pour les disponibilit�s
	@return La liste filtr�e des cours collectifs.
 */
	@Override public ArrayList<CoursCollectif> getMyListSelonID(int idMoniteur, long numSemaine, int idEleve, String periode){
		PreparedStatement pst_lst_cou1 = null;
		PreparedStatement pst_lst_cou2 = null;
		ArrayList<CoursCollectif> listSelonId = new ArrayList<CoursCollectif>();
		try {
			String sql1 = "SELECT * from Cours "
			+ "INNER JOIN CoursCollectif ON CoursCollectif.numCoursCollectif = Cours.numCours "
			+ "WHERE PeriodeCours = ? AND CoursCollectif.categorieAge in "
			+ "(SELECT Categorie from ELEVE Where numEleve = ?) "
			+ "AND nomSport in "
			+ "(SELECT nomAccreditation from accreditation where numAccreditation in "
			+ "(SELECT numAccreditation from ligneAccreditation where numMoniteur = ?)) "
			
			+ "AND   Cours.numCours IN "
            + "(SELECT CoursMoniteur.numCours FROM CoursMoniteur "
           	+ "INNER JOIN CoursSemaine ON CoursSemaine.numCours = CoursMoniteur.numCours "         
           	+ "WHERE  CoursSemaine.numCours IN "
            + "(SELECT CoursSemaine.numCours FROM Cours WHERE CoursSemaine.numSemaine = ? AND periodeCours = ? AND numMoniteur = ?));";
			pst_lst_cou1 = this.connect.prepareStatement(sql1);
			pst_lst_cou1.setString(1, periode);
			pst_lst_cou1.setInt(2, idEleve);
			pst_lst_cou1.setInt(3, idMoniteur);
			pst_lst_cou1.setLong(4, numSemaine);
			pst_lst_cou1.setString(5, periode);
			pst_lst_cou1.setInt(6, idMoniteur);
			ResultSet res_cou_col = pst_lst_cou1.executeQuery();
			while (res_cou_col.next()) {
				// S'il y a d�j� une r�servation, on r�duit la liste des cours dpropos�s � un seul.
				CoursCollectif CC = new CoursCollectif();
				CC.setNumCoursCollectif(res_cou_col.getInt("numCoursCollectif"));
				CC.setCategorieAge(res_cou_col.getString("categorieAge"));
				CC.setNiveauCours(res_cou_col.getString("niveauCours"));
				CC.setNumCours(res_cou_col.getInt("numCours"));
				CC.setNomSport(res_cou_col.getString("nomSport"));
				CC.setPrix(res_cou_col.getInt("prix"));
				CC.setMinEl(res_cou_col.getInt("minEleve"));
				CC.setMaxEl(res_cou_col.getInt("maxEleve"));
				CC.setPeriodeCours(res_cou_col.getString("periodeCours"));
				listSelonId.add(CC);
			}
			if (listSelonId.isEmpty()){
				// On affiche tous les cours correspondants aux variables, car il n'y a pas de r�servations
				String sql2 = "SELECT * from Cours "
						+ "INNER JOIN CoursCollectif ON CoursCollectif.numCoursCollectif = Cours.numCours "
						+ "WHERE PeriodeCours = ? AND CoursCollectif.categorieAge in "
						+ "(SELECT Categorie from ELEVE Where numEleve = ?) "
						+ "AND nomSport in "
						+ "(SELECT nomAccreditation from accreditation where numAccreditation in "
						+ "(SELECT numAccreditation from ligneAccreditation where numMoniteur = ?));";
				pst_lst_cou2 = this.connect.prepareStatement(sql2);
				pst_lst_cou2.setString(1, periode);
				pst_lst_cou2.setInt(2, idEleve);
				pst_lst_cou2.setInt(3, idMoniteur);
						ResultSet res_cou_col2 = pst_lst_cou2.executeQuery();
						while (res_cou_col2.next()) {
							CoursCollectif CC = new CoursCollectif();
							CC.setNumCoursCollectif(res_cou_col2.getInt("numCoursCollectif"));
							CC.setCategorieAge(res_cou_col2.getString("categorieAge"));
							CC.setNiveauCours(res_cou_col2.getString("niveauCours"));
							CC.setNumCours(res_cou_col2.getInt("numCours"));
							CC.setNomSport(res_cou_col2.getString("nomSport"));
							CC.setPrix(res_cou_col2.getInt("prix"));
							CC.setMinEl(res_cou_col2.getInt("minEleve"));
							CC.setMaxEl(res_cou_col2.getInt("maxEleve"));
							CC.setPeriodeCours(res_cou_col2.getString("periodeCours"));
							listSelonId.add(CC);
						}
						pst_lst_cou2.close();
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst_lst_cou1 != null) {
				try { pst_lst_cou1.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return listSelonId;
	}
	
	/**
		Objectif : R�cup�rer un instance d'un objet compl�tement initialis�e correspondant aux valeurs entr�es en param�tre.
		@version Finale 1.3.3
		@param Des valeurs ins�r�es dans un objet permettant d'identifier une seule personne dans la DB.
		@return instance d'un objet compl�tement initialis�e correspondant aux valeurs entr�es en param�tre.
	 */
	@Override public CoursCollectif getId(CoursCollectif obj) {
		PreparedStatement pst = null;
		CoursCollectif CC = new CoursCollectif();
		try {
			String sql = "SELECT * FROM cours "
					+ "INNER JOIN CoursCollectif ON numCoursCollectif = numCours "
					+ "WHERE nomSport = ? AND periodeCours = ? AND categorieAge = ? AND niveauCours = ?;";
			pst = this.connect.prepareStatement(sql);
			pst.setString(1, obj.getNomSport());
			pst.setString(2, obj.getPeriodeCours());
			pst.setString(3, obj.getCategorieAge());
			pst.setString(4, obj.getNiveauCours());
			ResultSet res_Rec_CC = pst.executeQuery();
			while (res_Rec_CC.next()) {
				CC.setNumCoursCollectif(res_Rec_CC.getInt("numCoursCollectif"));
				CC.setCategorieAge(res_Rec_CC.getString("categorieAge"));
				CC.setNiveauCours(res_Rec_CC.getString("niveauCours"));
				CC.setNumCours(res_Rec_CC.getInt("numCours"));
				CC.setNomSport(res_Rec_CC.getString("nomSport"));
				CC.setPrix(res_Rec_CC.getInt("prix"));
				CC.setMinEl(res_Rec_CC.getInt("minEleve"));
				CC.setMaxEl(res_Rec_CC.getInt("maxEleve"));
				CC.setPeriodeCours(res_Rec_CC.getString("periodeCours"));
				}
		}
		catch (SQLException e) { e.printStackTrace(); }
		finally {
			if (pst != null) {
				try { pst.close(); }
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		return CC;
	}

	@Override
	public ArrayList<CoursCollectif> getListSelonCriteres(CoursCollectif obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateAssurance(int numEleve, int numSemaine, String periode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int valeurReduction(int numSem, int numEleve, double prixCours) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String calculerPlaceCours(int numCours, long numSemaine, int idMoniteur) {
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

	@Override
	public long getDateDebutReserv(int numReserv) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<CoursCollectif> getReservationAnnulee(int numUtilisateur, int typeUtilisateur) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getCategorieReservation(int numMoniteur, int numSemaine, String periode) {
		// TODO Auto-generated method stub
		return null;
	}
}
