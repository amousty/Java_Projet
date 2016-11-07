package POJO;

public class Cours {
	// VARIABLES
	private int 	numCours;
	private String 	nomSport;
	private double 	prix;
	private int 	minEleve;
	private int 	maxEleve;
	private String 	periodeCours;
	//AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	//DAO<Cours> CoursDao = adf.getCoursDAO();
	
	// CONSTRUCTEURS
	public Cours (){}
	public Cours(int numCours, String nomSport, double prix, int minEleve, int maxEleve, String periodeCours){
		this.numCours 		= numCours;
		this.prix 			= prix;
		this.minEleve 		= minEleve;
		this.maxEleve 		= maxEleve;
		this.periodeCours 	= periodeCours;
		this.nomSport 		= nomSport;
	}
	
	// METHODES
	//public int createCours				() 		{ return CoursDao.create(this); }
	//public void deleteCours				()		{ CoursDao.delete(null); }
	//public Cours rechercherCours		(int id){ return CoursDao.find(id); }
	//public ArrayList<Cours> getListCours()		{ return CoursDao.getList(); }
	
	// FONCTION SURCHARGEE
		@Override
		public String toString() { 
			return  "Cours : " + nomSport + System.getProperty("line.separator")
					+ "P�riode de cours : " + periodeCours + System.getProperty("line.separator")
					+ "Prix : " + prix + "�" + System.getProperty("line.separator") 
					+  "Nombre minimum d'�l�ves pour ce cours : " + minEleve + System.getProperty("line.separator") 
					+  "Nombre maximum d'�l�ves pour ce cours : " + maxEleve + System.getProperty("line.separator");
		}
	
	// PROPRIETES
	public int getNumCours		() 				{ return numCours; }
	public double getPrix			() 				{ return prix; }
	public int getMinEl				() 				{ return minEleve; }
	public int getMaxEl				() 				{ return maxEleve; }
	public String getPeriodeCours	() 				{ return periodeCours; }
	public String getNomSport		() 				{ return nomSport; }
	public void setNumCours			(int numCours) 	{ this.numCours = numCours; }
	public void setPrix				(double el) 	{ prix = el; }
	public void setMinEl			(int el) 		{ minEleve = el; }
	public void setMaxEl			(int el) 		{ maxEleve = el; }
	public void setPeriodeCours		(String el) 	{ periodeCours = el; }
	public void setNomSport			(String el) 	{ nomSport = el; }
	
	
}
