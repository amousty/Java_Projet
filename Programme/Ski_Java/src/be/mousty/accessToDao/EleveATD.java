package be.mousty.accessToDao;

import java.sql.Date;
import java.util.ArrayList;

import be.mousty.dao.AbstractDAOFactory;
import be.mousty.dao.DAO;
import be.mousty.pojo.Eleve;

public class EleveATD extends PersonneATD{
	// VARIABLES
	private String categorie;

	// CONSTRUCTEURS
	public EleveATD(){}

	public EleveATD(String categorie, String nom, String pre, String adresse, String sexe, Date dateNaissance){
		super(nom, pre, adresse, sexe, dateNaissance);
		this.categorie = categorie;
	}

	public EleveATD(String nom, String pre, String adresse, String sexe, Date dateNaissance){
		super(nom, pre, adresse, sexe, dateNaissance);
		this.categorie = attributerCategorie();
	}
	
	public EleveATD(Eleve E){
		super(E.getNom(), E.getPre(), E.getAdresse(), E.getSexe(), E.getDateNaissance());
		this.categorie = attributerCategorie();
	}

	// APPEL AUX METHODES DAO DANS LES CLASSES METIER
	AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<Eleve> EleveDAO = adf.getEleveDAO();
	public int					create				(Eleve e) 	{ return EleveDAO.create(e); 	}
	public boolean 				delete				()	 		{ return EleveDAO.delete(null); }
	public Eleve 				getId				(Eleve e) 	{ return EleveDAO.getId(e); 	}
	public boolean 				update				(Eleve e) 	{ return EleveDAO.update(e); 	}
	public Eleve 				find				(int id) 	{ return EleveDAO.find(id); 	} 
	public ArrayList<Eleve> 	getListEl			() 			{ return EleveDAO.getList(); 	} 
	public ArrayList<Eleve> 	getListSelonCriteres(Eleve e) 	{ return EleveDAO.getListSelonCriteres(e); 	}
	public ArrayList<Eleve> getListEleveSelonAccredProfEtCours(int numMoniteur, int numSemaine, int numClient, String periode)
	{ return EleveDAO.getMyListSelonID(numMoniteur, numSemaine, numClient,  periode); }
	

	// METHODES
	public int inscriptionEleve(int numClient){
		Eleve E = new Eleve();
		E.setNumClient(numClient);
		E.setNumEleve(-1);
		E.setDateNaissance(getDateNaissance());
		E.setCategorie(this.attributerCategorie());
		E.setNom(getNom());
		E.setPre(getPre());
		E.setAdresse(getAdresse());
		E.setSexe(getSexe());
		return create(E);
	}
	public ArrayList<EleveATD> getListEleveSelonAccredProfEtCoursATD(int numMoniteur, int numSemaine, int numClient, String periode){
		ArrayList<Eleve>  listE = getListEleveSelonAccredProfEtCours(numMoniteur, numSemaine, numClient, periode);
		ArrayList<EleveATD> listEATD = new ArrayList<EleveATD>();
		for (int i = 0; i < listE.size(); i++) {
			EleveATD EATD = new EleveATD();
			EATD.setAdresse(listE.get(i).getAdresse());
			EATD.setCategorie(listE.get(i).getCategorie());
			EATD.setDateNaissance(listE.get(i).getDateNaissance());
			EATD.setNom(listE.get(i).getNom());
			EATD.setPre(listE.get(i).getPre());
			EATD.setSexe(listE.get(i).getSexe());
			//System.out.println(EATD.getSexe());
			listEATD.add(EATD);
		}
		return listEATD;
	}
	
	public int getIdATD(EleveATD EATD){
		Eleve E = new Eleve();
		E.setAdresse(EATD.getAdresse());
		E.setCategorie(EATD.getCategorie());
		E.setDateNaissance(EATD.getDateNaissance());
		E.setNom(EATD.getNom());
		E.setPre(EATD.getPre());
		E.setSexe(EATD.getSexe());
		E.setNumEleve(-1);
		E.setNumPersonne(-1);
		return getId(E).getNumEleve();
	}
	
	
	// METHODEs SURCHARGEEs
	@Override
	public String toString() { 
		return 
				super.toString()+ System.getProperty("line.separator")
				+ "ELEVE, cat�gorie " + categorie + System.getProperty("line.separator");
	}

	// PROPRIETE
	public String 	getCategorie () 				{ return categorie; }
	public void 	setCategorie (String categorie) { this.categorie = categorie; }
}
