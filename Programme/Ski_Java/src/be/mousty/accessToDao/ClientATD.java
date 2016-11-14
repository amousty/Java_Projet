package be.mousty.accessToDao;

import java.sql.Date;
import java.util.ArrayList;

import be.mousty.dao.AbstractDAOFactory;
import be.mousty.dao.DAO;
import be.mousty.pojo.Client;
import be.mousty.pojo.Eleve;

public class ClientATD extends UtilisateurATD{
	// VARIABLES
	private String 	adresseFacturation;
	
	// CONSTRUCTEURS
	public ClientATD(){}
	public ClientATD(Client C){
			super(C.getNom(), C.getPre(), C.getAdresse(), C.getSexe(), C.getDateNaissance(), C.getPseudo(),
					C.getMdp(), C.getTypeUtilisateur());
			this.adresseFacturation = C.getAdresseFacturation();
	}
	
	public ClientATD(String nom, String pre, String adresse, String sexe, Date dateNaissance,
			String pseudo, String mdp, int typeUtilisateur, String adresseFacturation){
		super(nom, pre, adresse, sexe, dateNaissance, pseudo, mdp, typeUtilisateur);
		this.adresseFacturation = adresseFacturation;
	}
	
	// APPEL AUX METHODES DAO DANS LES CLASSES METIER
	AbstractDAOFactory adf 	= AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<Client> ClientDAO 	= adf.getClientDAO();
	public int					create				(Client c) 		{ return ClientDAO.create(c); 	 	}
	public boolean 				delete				()	 			{ return ClientDAO.delete(null); 	}
	public Client 				getId				(Client c) 		{ return ClientDAO.getId(c); 		}
	public boolean 				update				(Client c) 		{ return ClientDAO.update(c); 		}
	public Client 				find				(int id) 		{ return ClientDAO.find(id); 		} 
	public ArrayList<Client> 	getListCli			() 				{ return ClientDAO.getList(); 		} 
	public ArrayList<Client> 	getListSelonCriteres(Client c) 		{ return ClientDAO.getListSelonCriteres(c); 			}
	
	public boolean changeClientToEleve(int idClient){
		Client C = find(idClient);
		if(C != null){
			System.out.println("F_Client -> ajout eleve");
			//= new Eleve(, , , , ,);
			Eleve E = new Eleve();
			E.setNumEleve(C.getNumPersonne());
			this.setDateNaissance(C.getDateNaissance());
			E.setCategorie(this.attributerCategorie());
			E.setNumClient(idClient);
			E.setNumPersonne(C.getNumPersonne());
			E.setNom(C.getNom());
			E.setPre(C.getPre());
			E.setDateNaissance( C.getDateNaissance());
			E.setAdresse(C.getAdresse());
			E.setSexe(C.getSexe());
			EleveATD EATD = new EleveATD(E);
			if(EATD.create(E) != -1) return true;
		}
		return false;
	}
	
	public int inscriptionClient(){
		Client C = new Client();
		C.setAdresseFacturation(this.getAdresseFacturation());
		C.setPseudo(this.getPseudo());
		C.setMdp(this.getMdp());
		C.setTypeUtilisateur(2);
		C.setNom(this.getNom());
		C.setPre(this.getPre());
		C.setDateNaissance(this.getDateNaissance());
		C.setAdresse(this.getAdresse());
		C.setSexe(this.getSexe());
		return create(C);
	}
	
	// PROPRIETES
	public String getAdresseFacturation	() 				{ return adresseFacturation; }
	public void setAdresseFacturation 	(String el) 	{ this.adresseFacturation  = el; }
}

