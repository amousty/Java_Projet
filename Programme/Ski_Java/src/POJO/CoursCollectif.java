package POJO;

import DAO.AbstractDAOFactory;
import DAO.DAO;

public class CoursCollectif extends Cours{
	// VARIABLES
		private int 	numCoursCollectif;
		private String 	categorieAge;
		private String 	niveauCours;
		AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
		DAO<CoursCollectif> CoursCollectifDao = adf.getCoursCollectifDAO();
		
		// CONSTRUCTEURS
		public CoursCollectif (){}
		public CoursCollectif(String categorieAge, String niveauCours){
			this.categorieAge 	= categorieAge;
			this.niveauCours 	= niveauCours;
		}
		
		public CoursCollectif(int numCours, String nomSport, double prix, int minEleve, int maxEleve, String periodeCours, 
				String categorieAge, String niveauCours){
			super(numCours,nomSport, prix, minEleve, maxEleve, periodeCours);
			this.categorieAge 	= categorieAge;
			this.niveauCours 	= niveauCours;
		}
		
		// METHODES
		public int createCoursCollectif					() 		{ return CoursCollectifDao.create(this); }
		public void deleteCoursCollectif				()		{ CoursCollectifDao.delete(null); }
		public CoursCollectif rechercherCoursCollectif	(int id){ return CoursCollectifDao.find(id); }
		
		// FONCTION SURCHARGEE
			@Override
			public String toString() { 
				return  
						super.toString()+ System.getProperty("line.separator") + 
						"Cat�gorie d'age : " + categorieAge + System.getProperty("line.separator") + 
						"Niveau du cours : " + niveauCours + System.getProperty("line.separator");
			}
		
		// PROPRIETES
		public int getNumCoursCollectif		() 							{ return numCoursCollectif; }
		public String getCategorieAge		() 							{ return categorieAge; }
		public String getNiveauCours		() 							{ return niveauCours; }
		public void setNumCoursCollectif	(int numCoursCollectif) 	{ this.numCoursCollectif = numCoursCollectif; }
		public void setCategorieAge			(String el) 				{ categorieAge = el; }
		public void setNiveauCours			(String niveauCours) 		{ this.niveauCours = niveauCours; }
}