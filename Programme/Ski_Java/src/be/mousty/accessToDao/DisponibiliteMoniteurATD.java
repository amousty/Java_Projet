package be.mousty.accessToDao;

import java.sql.Date;
import java.util.ArrayList;

import be.mousty.dao.AbstractDAOFactory;
import be.mousty.dao.DAO;
import be.mousty.pojo.DisponibiliteMoniteur;
import be.mousty.pojo.Semaine;

public class DisponibiliteMoniteurATD {
	// VARIABLES
	private boolean disponible;

	// CONSTRUCTEURS
	public DisponibiliteMoniteurATD(){}
	public DisponibiliteMoniteurATD(boolean disponible){ this.disponible 	= disponible; }
	public DisponibiliteMoniteurATD(DisponibiliteMoniteur DM){ this.disponible 	= DM.getDisponible(); }

	
	// APPEL AUX METHODES DAO DANS LES CLASSES METIER
	AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<DisponibiliteMoniteur> DisponibiliteMoniteurDAO = adf.getDisponibiliteMoniteurDAO();
	public int						create	(DisponibiliteMoniteur d) 		{ return DisponibiliteMoniteurDAO.create(d); 	}
	public boolean 					delete	()	 							{ return DisponibiliteMoniteurDAO.delete(null); }
	public DisponibiliteMoniteur 	getId	(DisponibiliteMoniteur d) 		{ return DisponibiliteMoniteurDAO.getId(d); 	}
	public boolean 					update	(DisponibiliteMoniteur d) 		{ return DisponibiliteMoniteurDAO.update(d); 	}
	public DisponibiliteMoniteur 	find	(int id) 						{ return DisponibiliteMoniteurDAO.find(id); 	} 
	public ArrayList<DisponibiliteMoniteur> getListCP 			() 					{ return DisponibiliteMoniteurDAO.getList(); 	} 
	public ArrayList<DisponibiliteMoniteur> getMyListSelonID	(int idMoniteur)	{ return DisponibiliteMoniteurDAO.getMyListSelonID(idMoniteur, -1, -1, ""); 	}
	public void creerTouteDisponibilites						()					{ DisponibiliteMoniteurDAO.creerTouteDisponibilites();		}
	public void creerTouteDisponibilitesSelonMoniteur			(int numMoniteur) 	{ DisponibiliteMoniteurDAO.creerTouteDisponibilitesSelonMoniteur(numMoniteur); }
	public boolean getListSelonCriteres(DisponibiliteMoniteur obj) {
		ArrayList<DisponibiliteMoniteur> listBoolean = DisponibiliteMoniteurDAO.getListSelonCriteres(obj);
		boolean valRetour = listBoolean == null ? true : false; 
		return valRetour; // Si true, �a a fonctionn�.
	}
	
	
	// FONCTION SURCHARGEE
	@Override public String toString() { return  "Le moniteur est disponible : " + disponible + "."; }
	
	// METHODES
	public ArrayList<DisponibiliteMoniteurATD> getListDispo(int numMoniteur){
		ArrayList<DisponibiliteMoniteur> listDispo  = getMyListSelonID(numMoniteur);
		ArrayList<DisponibiliteMoniteurATD> listDispoATD = new ArrayList<DisponibiliteMoniteurATD>();
		for(int i = 0; i < listDispo.size(); i++){
			DisponibiliteMoniteurATD DMATD = new DisponibiliteMoniteurATD();
			//DMATD.setNom(A.get(i).getNomAccreditation());
			DMATD.setDisponible(listDispo.get(i).getDisponible());
			listDispoATD.add(DMATD);
		}
		return listDispoATD;
	}
	
	public boolean updateDispo(Date dateDebut, int numMoniteur){
		SemaineATD SATD = new SemaineATD();
		Semaine S = new Semaine();
		S.setDateDebut(dateDebut);
		S = SATD.getId(S); // num�ro de semaine
		DisponibiliteMoniteur DM = new DisponibiliteMoniteur();
		DM.setNumSemaine(S.getNumSemaine());
		DM.setNumMoniteur(numMoniteur);
		return getListSelonCriteres(DM);
	}

	// PROPRIETE
	public boolean 	getDisponible	() 						{ return disponible; 			}
	public void 	setDisponible	(boolean disponible)	{ this.disponible = disponible; }
}
