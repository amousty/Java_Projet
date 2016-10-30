package DAO;

import POJO.Accreditation;
import POJO.Client;
import POJO.Eleve;
import POJO.Moniteur;
import POJO.Personne;
import POJO.Semaine;
import POJO.Utilisateur;

public abstract class AbstractDAOFactory {
	public static final int DAO_FACTORY 	= 0;
	public static final int XML_DAO_FACTORY = 1;

	public abstract DAO<Utilisateur> 	getUtilisateurDAO();
	public abstract DAO<Personne> 		getPersonneDAO();
	public abstract DAO<Client> 		getClientDAO();
	public abstract DAO<Moniteur> 		getMoniteurDAO();
	public abstract DAO<Eleve> 			getEleveDAO();
	public abstract DAO<Accreditation> 	getAccreditationDAO();
	public abstract DAO<Semaine> 		getSemaineDAO();
	//public abstract DAO<Professeur> getProfesseurDAO();
	
	//public abstract DAO<Matiere> getMatiereDAO();

	public static AbstractDAOFactory getFactory(int type){
		switch(type){
			case DAO_FACTORY: return new DAOFactory();
			default: return null;
		}
	}
	
}
