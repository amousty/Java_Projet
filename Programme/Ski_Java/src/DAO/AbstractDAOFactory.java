package DAO;

import POJO.Accreditation;
import POJO.Client;
import POJO.Cours;
import POJO.CoursCollectif;
import POJO.CoursParticulier;
import POJO.Eleve;
import POJO.Moniteur;
import POJO.Personne;
import POJO.Semaine;
import POJO.Utilisateur;

public abstract class AbstractDAOFactory {
	public static final int DAO_FACTORY 	= 0;
	public static final int XML_DAO_FACTORY = 1;

	public abstract DAO<Utilisateur> 		getUtilisateurDAO();
	public abstract DAO<Personne> 			getPersonneDAO();
	public abstract DAO<Client> 			getClientDAO();
	public abstract DAO<Moniteur> 			getMoniteurDAO();
	public abstract DAO<Eleve> 				getEleveDAO();
	public abstract DAO<Accreditation> 		getAccreditationDAO();
	public abstract DAO<Semaine> 			getSemaineDAO();
	public abstract DAO<Cours> 				getCoursDAO();
	public abstract DAO<CoursParticulier> 	getCoursParticulierDAO();
	public abstract DAO<CoursCollectif> 	getCoursCollectifDAO();

	public static AbstractDAOFactory getFactory(int type){
		switch(type){
			case DAO_FACTORY: return new DAOFactory();
			default: return null;
		}
	}
	
}
