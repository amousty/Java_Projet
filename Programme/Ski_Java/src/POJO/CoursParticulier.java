package POJO;

import DAO.AbstractDAOFactory;
import DAO.DAO;

public class CoursParticulier extends Cours{
	// VARIABLES
	private int 	numCoursParticulier;
	private int 	nombreHeures;
	AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<CoursParticulier> CoursParticulierDao = adf.getCoursParticulierDAO();
	
	// CONSTRUCTEURS
	public CoursParticulier (){}
	public CoursParticulier(int numCoursParticulier, int nombreHeures){
		this.numCoursParticulier 	= numCoursParticulier;
		this.nombreHeures 			= nombreHeures;
	}
	
	public CoursParticulier(int numCours, String nomSport, double prix, int minEleve, int maxEleve, String periodeCours, 
			int nombreHeures){
		super(numCours,nomSport, prix, minEleve, maxEleve, periodeCours);
		//this.numCoursParticulier 	= numCoursParticulier;
		this.nombreHeures 			= nombreHeures;
	}
	
	// METHODES
	public int createCoursParticulier					() 		 { return CoursParticulierDao.create(this); }
	public void deleteCoursParticulier					()		 { CoursParticulierDao.delete(null); }
	public CoursParticulier rechercherCoursParticulier	(int id) { return CoursParticulierDao.find(id); }
	
	// FONCTION SURCHARGEE
		@Override
		public String toString() { 
			return  
					super.toString()+ System.getProperty("line.separator") + 
					"Nombre d'heures ce cours : " + nombreHeures + System.getProperty("line.separator");
		}
	
	// PROPRIETES
	public int getNumCoursParticulier	() 							{ return numCoursParticulier; }
	public int getNombreHeures			() 							{ return nombreHeures; }
	public void setNumCoursParticulier	(int numCoursParticulier) 	{ this.numCoursParticulier = numCoursParticulier; }
	public void setNombreHeures			(int el) 					{ nombreHeures = el; }
}