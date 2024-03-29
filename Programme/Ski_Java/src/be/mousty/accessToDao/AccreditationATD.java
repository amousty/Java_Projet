package be.mousty.accessToDao;
/**
	Classe m�tier relatif li�e � la classe Accreditation et AccreditationDAO.
	@author Adrien MOUSTY
	@version Finale 1.3.3
	@category M�tier
*/
import java.util.ArrayList;
import be.mousty.dao.AbstractDAOFactory;
import be.mousty.dao.DAO;
import be.mousty.pojo.Accreditation;

public class AccreditationATD {
	// VARIABLES
	private String nom;

	// CONSTRUCTEURS
	public AccreditationATD(){}
	public AccreditationATD(String nom){ this.nom = nom; }
	public AccreditationATD(Accreditation A){ this.nom = A.getNomAccreditation(); }
	
	

	// APPEL AUX METHODES DAO DANS LES CLASSES METIER
	AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<Accreditation> AccreditationDAO = adf.getAccreditationDAO();
	public Accreditation 			getId		(Accreditation a) 	{ return AccreditationDAO.getId(a);  }
	public Accreditation 			find		(int id) 			{ return AccreditationDAO.find(id);  } 
	public ArrayList<Accreditation> getListAccr	() 					{ return AccreditationDAO.getList(); } 
	
	// Transformation
	public static ArrayList<AccreditationATD> changeTypeAccredilist(ArrayList<Accreditation> A){
		ArrayList<AccreditationATD> LA = new ArrayList<AccreditationATD>();
		for(int i = 0; i < A.size(); i++){
			AccreditationATD AATD = new AccreditationATD();
			AATD.setNom(A.get(i).getNomAccreditation());
			LA.add(AATD);
		}
		return LA;
	}
	
	public static ArrayList<Accreditation> changeTypeAccredilistEnATD(ArrayList<AccreditationATD> A){
		ArrayList<Accreditation> LA = new ArrayList<Accreditation>();
		for(int i = 0; i < A.size(); i++){
			Accreditation AATD = new Accreditation();
			AATD.setNomAccreditation((A.get(i).getNom()));
			switch(AATD.getNomAccreditation()){
			case "Snowboard" : AATD.setNumAccreditation(1);
				break;
			case "Ski" : AATD.setNumAccreditation(2);
				break;
			case "Ski de fond" : AATD.setNumAccreditation(3);
				break;
			case "Telemark" : AATD.setNumAccreditation(4);
				break;
			case "Enfant" :AATD.setNumAccreditation(5);
				break;
			case "Adulte" :AATD.setNumAccreditation(6);
				break;
			}
			LA.add(AATD);
		}
		return LA;
	}

	public ArrayList<AccreditationATD> getFullAccredATD(){ return changeTypeAccredilist(getListAccr()); }
	// PROPRIETE
	public String 	getNom () 			{ return nom; 		}
	public void 	setNom (String nom) { this.nom = nom; 	}
}
