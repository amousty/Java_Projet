package be.mousty.fenetre;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import be.mousty.accessToDao.ClientATD;
import be.mousty.accessToDao.CoursATD;
import be.mousty.accessToDao.CoursCollectifATD;
import be.mousty.accessToDao.CoursParticulierATD;
import be.mousty.accessToDao.EleveATD;
import be.mousty.accessToDao.MoniteurATD;
import be.mousty.accessToDao.ReservationATD;
import be.mousty.accessToDao.SemaineATD;

public class F_AjoutRdv extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8692836574443387394L;
	private JPanel contentPane;
	private int numMoniteur;
	private int numCours;
	private int numEleve;
	private int numSemaine;
	private int numClient;
	private String periode = "09-12";
	private boolean pageChargee = false;


	JSeparator separator = new JSeparator();
	JSeparator separator_1 = new JSeparator();
	JLabel lbl_Reserv = new JLabel("R\u00E9servation");
	JLabel lbl_error = new JLabel("");
	JLabel lbl_moniteur = new JLabel("Moniteur");
	JLabel lbl_eleve = new JLabel("El\u00E8ve");
	JLabel lbl_horaire = new JLabel("Horaire");
	JLabel lbl_cours = new JLabel("Cours");
	JLabel lbl_infoCours = new JLabel("Il reste x places pour ce cours");
	JLabel lbl_placeMin = new JLabel("Il manque x places pour ce cours");

	JComboBox<ComboItem> cb_nomEleve = new JComboBox<ComboItem>();
	JComboBox<ComboItem> cb_nomMoniteur = new JComboBox<ComboItem>();
	JComboBox<ComboItem> cb_semaine = new JComboBox<ComboItem>();
	JComboBox<ComboItem> cb_cours = new JComboBox<ComboItem>();
	JRadioButton rdbtnCoursCollectif = new JRadioButton("Cours collectif");
	JRadioButton radbtn_touteLaJournee = new JRadioButton("x");
	JRadioButton rdbtnCoursParticulier = new JRadioButton("Cours particulier");
	JRadioButton rdbtnCoursMatin = new JRadioButton("Cours matin");
	JRadioButton rdbtnCoursAprem = new JRadioButton("Cours apres-midi");
	JButton btn_ret = new JButton("Retour");
	JButton btn_inscrip = new JButton("R\u00E9server");
	JCheckBox chkb_assur = new JCheckBox("Assurance (15\u20AC)");

	// ADF
	/*AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
	DAO<Reservation> 		ReservationDAO 		= adf.getReservationDAO();
	DAO<Client> 			ClientDAO 			= adf.getClientDAO();
	DAO<Eleve> 				EleveDAO 			= adf.getEleveDAO();
	DAO<Moniteur> 			MoniteurDAO 		= adf.getMoniteurDAO();
	DAO<Cours> 				CoursDAO 			= adf.getCoursDAO();
	DAO<Semaine> 			SemaineDAO 			= adf.getSemaineDAO();
	DAO<CoursCollectif> 	CoursCollectifDAO 	= adf.getCoursCollectifDAO();
	DAO<CoursParticulier> 	CoursParticulierDAO = adf.getCoursParticulierDAO();*/
	SemaineATD SATD = new SemaineATD();
	CoursATD CATD = new CoursATD();
	ReservationATD RATD = new ReservationATD();
	EleveATD EATD = new EleveATD();
	ClientATD CLIATD = new ClientATD();
	MoniteurATD MATD = new MoniteurATD();
	CoursCollectifATD CCATD = new CoursCollectifATD();
	CoursParticulierATD CPATD = new CoursParticulierATD();
	
	
	// lISTES UTILES AUX COMBOBOXS
	ArrayList<EleveATD> listEleve			= new ArrayList<EleveATD>();
	ArrayList<SemaineATD> listSemaine		= SATD.getListSemaineSelonDateDuJour();//SemaineDAO.getListSemaineSelonDateDuJour();

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					F_AjoutRdv frame = new F_AjoutRdv(118);
					frame.setVisible(true);
					//this.loadComboBox();
				}
				catch (Exception e) { e.printStackTrace(); }
			}
		});


	}

	/**
	 * Create the frame.
	 */
	public F_AjoutRdv(int idClient) {
		numClient = idClient;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 540, 417);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// New
		lbl_infoCours.setVerticalAlignment(SwingConstants.TOP);
		rdbtnCoursCollectif.setSelected(true);
		rdbtnCoursMatin.setSelected(true);
		radbtn_touteLaJournee.setVisible(false);

		// Alignement
		lbl_moniteur.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_eleve.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_horaire.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_cours.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_infoCours.setHorizontalAlignment(SwingConstants.LEFT);

		// Font
		lbl_Reserv.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));

		// Foreground color
		lbl_error.setForeground(Color.RED);

		// Bouds
		lbl_Reserv.setBounds			(10, 11, 193, 22);
		separator.setBounds				(10, 44, 105, 15);
		cb_nomEleve.setBounds			(214, 134, 250, 20);
		cb_nomMoniteur.setBounds		(214, 77, 250, 20);
		cb_semaine.setBounds			(214, 190, 250, 20);
		rdbtnCoursCollectif.setBounds	(6, 62, 109, 23);
		rdbtnCoursParticulier.setBounds	(6, 88, 109, 23);
		lbl_error.setBounds				(213, 18, 251, 14);
		lbl_moniteur.setBounds			(214, 45, 250, 14);
		lbl_eleve.setBounds				(214, 109, 250, 14);
		lbl_horaire.setBounds			(224, 165, 250, 14);
		lbl_cours.setBounds				(214, 221, 250, 14);
		cb_cours.setBounds				(214, 246, 250, 20);
		lbl_infoCours.setBounds			(214, 277, 250, 15);
		rdbtnCoursAprem.setBounds		(6, 161, 150, 23);
		rdbtnCoursMatin.setBounds		(6, 133, 150, 23);
		btn_inscrip.setBounds			(10, 310, 105, 23);
		btn_ret.setBounds				(10, 344, 105, 23);
		separator_1.setBounds			(10, 299, 105, 15);
		radbtn_touteLaJournee.setBounds	(6, 189, 150, 23);
		lbl_placeMin.setBounds(214, 303, 250, 15);
		chkb_assur.setBounds(6, 245, 150, 23);

		// Add
		contentPane.add(lbl_Reserv);
		contentPane.add(separator);
		contentPane.add(cb_nomEleve);
		contentPane.add(cb_nomMoniteur);
		contentPane.add(rdbtnCoursCollectif);
		contentPane.add(lbl_error);
		contentPane.add(cb_semaine);
		contentPane.add(rdbtnCoursParticulier);
		contentPane.add(cb_cours);
		contentPane.add(lbl_cours);
		contentPane.add(lbl_eleve);
		contentPane.add(lbl_moniteur);
		contentPane.add(lbl_horaire);
		contentPane.add(lbl_infoCours);
		contentPane.add(rdbtnCoursMatin);
		contentPane.add(rdbtnCoursAprem);
		contentPane.add(btn_inscrip);
		contentPane.add(btn_ret);
		contentPane.add(separator_1);
		contentPane.add(radbtn_touteLaJournee);
		contentPane.add(lbl_placeMin);
		contentPane.add(chkb_assur);

		// EVENEMENT CLICK SUR RADIOBUTTON
		// Valider la r�servation
		System.out.println("Num client : " + idClient);
		btn_inscrip.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(cb_nomEleve.getSelectedItem() != "" && cb_nomMoniteur.getSelectedItem() != "" && cb_semaine.getSelectedItem() != "" && cb_cours.getSelectedItem() != ""){
					String string = CATD.calculerPlaceCours(numCours, numSemaine, numMoniteur);
					//String string = CoursDAO.calculerPlaceCours(numCours, numSemaine, numMoniteur);
					String[] parts = string.split("-");
					String part1 = parts[0];
					if(Integer.parseInt(part1) > 0){
						//System.out.println("R�servation effectu�");
						String[] partsPer = periode.split("-");
						String heureDebut = partsPer[0];
						String heureFin = partsPer[1];
						boolean assurance = chkb_assur.isSelected();
						RATD = new ReservationATD();
						RATD.setHeureDebut(Integer.parseInt(heureDebut));
						RATD.setHeureFin(Integer.parseInt(heureFin));
						RATD.setNumReservation(-1);
						RATD.setAUneAssurance(assurance);
						RATD.setSemaine(SATD.find(numSemaine));
						RATD.setCours(CATD.find(numCours));
						RATD.setEleve(EATD.find(numEleve));
						RATD.setClient(CLIATD.find(idClient));
						RATD.setMoniteur(MATD.find(numMoniteur));
						int numReservation = RATD.createReservation();
						
						 
						if(numReservation != -1){
						// Update -> d�coche toutes les assurances pr�c�demment s�lectionn�es, pour faciliter le calcul d'assurance final.
						//ReservationDAO.updateAssurance(numEleve, numSemaine, periode);
						RATD.updateAssurance(numEleve, numSemaine, periode);
						System.out.println("Num reserv : " + numReservation);
						
						// Retour � l'ancien �cran
						setVisible(false);
						F_Client frame = new F_Client(idClient);
						frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
						frame.setVisible(true);
						}
						else { lbl_infoCours.setText("Reservation annul�e. (6 ans mini pour faire du snow)"); }
					}
					else { lbl_infoCours.setText("Vous ne pouvez plus r�server pour ce cours."); }
				}
			}
		});

		// Retour menu client
		btn_ret.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// Affiche F_ajoutEleve
				setVisible(false);
				F_Client frame = new F_Client(idClient);
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.setVisible(true);
			}
		});

		// COURS COLLECTIF SELECTIONNE
		rdbtnCoursCollectif.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				rdbtnCoursParticulier.setSelected(false);
				rdbtnCoursCollectif.setSelected	(true);
				
				periode = "09-12";

				rdbtnCoursMatin.setText("Cours matin");
				rdbtnCoursAprem.setText("Cours apres-midi");
				//radbtn_touteLaJournee.setText("Matin et apres-midi");
				radbtn_touteLaJournee.setVisible(false);
				if (pageChargee) { loadCbEleve(); loadCbCours();  }
			}
		});

		// COURS PARTICULIER SELECTIONNE
		rdbtnCoursParticulier.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				rdbtnCoursCollectif.setSelected	(false);
				rdbtnCoursParticulier.setSelected(true);

				periode = "12-13";

				rdbtnCoursMatin.setText("Cours de 12 � 13");
				rdbtnCoursAprem.setText("Cours de 13 � 14");
				radbtn_touteLaJournee.setText("Cours de 12 � 14");
				radbtn_touteLaJournee.setVisible(true);
				if (pageChargee) { loadCbEleve(); loadCbCours();  }
			}
		});

		// COURS DU MATIN
		rdbtnCoursMatin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (rdbtnCoursMatin.isSelected()){
					rdbtnCoursAprem.setSelected(false);
					radbtn_touteLaJournee.setSelected(false);
					switch(rdbtnCoursMatin.getText()){
					case "Cours de 12 � 13" : 
						periode = "12-13";
						break;
					case "Cours matin" : 
						periode = "09-12";
						break;
					}
				}

				if (pageChargee) {  loadCbEleve(); loadCbCours();}
			}
		});

		// COURS APREM
		rdbtnCoursAprem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (rdbtnCoursAprem.isSelected()){
					rdbtnCoursMatin.setSelected(false);
					radbtn_touteLaJournee.setSelected(false);
					switch(rdbtnCoursAprem.getText()){
					case "Cours de 13 � 14" : 
						periode = "13-14";
						break;
					case "Cours apres-midi" : 
						periode = "14-17";
						break;
					}
				}

				if (pageChargee) {  loadCbEleve(); loadCbCours(); }
			}
		});

		// LES DEUX HEURES
		radbtn_touteLaJournee.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (radbtn_touteLaJournee.isSelected()){
					rdbtnCoursMatin.setSelected(false);
					rdbtnCoursAprem.setSelected(false);
					switch(radbtn_touteLaJournee.getText()){
					case "Cours de 12 � 14" : 
						periode = "12-14";
						break;
						/*case "Matin et apres-midi" : 
						periode = "09-17";
						break;*/
					}
				}

				if (pageChargee) { loadCbEleve(); loadCbCours(); }
			}
		});

		// ON CHOISI IN NOUVEAU MONITEUR
		// -> Il faut charger les cours correspondants
		//Cours C = new Cours();
		cb_nomEleve.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					Object item = cb_nomEleve.getSelectedItem();
					int value = ((ComboItem)item).getValue();
					numEleve = value;
					System.out.println("Num �l�ve : " + value);
					if (pageChargee) loadCbCours();
				}
			}
		});

		cb_nomMoniteur.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					Object item = cb_nomMoniteur.getSelectedItem();
					int value = ((ComboItem)item).getValue();
					numMoniteur = value;
					System.out.println("Num moniteur : " + value);
					if (pageChargee) {  loadCbEleve(); loadCbCours();}
				}
			}
		});
		cb_semaine.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					Object item = cb_semaine.getSelectedItem();
					int value = ((ComboItem)item).getValue();
					numSemaine = value;
					System.out.println("Num semaine : " + value);
					if (pageChargee) loadCbEleve();
				}
			}
		});

		cb_cours.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					Object item = cb_cours.getSelectedItem();
					int value = ((ComboItem)item).getValue();
					numCours = value;
					System.out.println("Num cours : " + value);

					if (pageChargee) { loadInfoCoursText(); /*loadCbEleve();*/ }
				}
			}
		});

		if (!pageChargee) { loadComboBox(); }
		pageChargee = true;
	}

	public void loadComboBox(){
		try{
			// REMPLISSAGE DES COMBOBOX
			loadCbSemaine();
			loadCbMoniteur();
			
			loadCbEleve();
			loadCbCours();
			
			loadInfoCoursText();
		}
		catch(Exception Ex){
			Ex.getStackTrace();
		}
	}

	public void loadCbEleve(){
		// ELEVES
		
		listEleve = EATD.getListEleveSelonAccredProfEtCoursATD(numMoniteur, numSemaine, numClient, periode);
		cb_nomEleve.removeAllItems();
		//cb_nomEleve.removeAllItems();
		lbl_error.setText("");
		if (listEleve == null || listEleve.size() == 0){ lbl_error.setText("Pas d'�l�ves disponible"); }
		else { 
			for(EleveATD e : listEleve) {
				cb_nomEleve.addItem (new ComboItem(e.getNom().toUpperCase() + " " + e.getPre(), EATD.getIdATD(e)));
				}
			}
	}

	public void loadCbMoniteur(){
		// MONITEUR
		ArrayList<MoniteurATD> listMoniteur = MATD.getListDispoATD(numSemaine, periode);//MoniteurDAO.getListDispo(numSemaine, periode);
		if (listMoniteur != null){
			cb_nomMoniteur.removeAllItems();
			for(MoniteurATD m : listMoniteur)
				cb_nomMoniteur.addItem (new ComboItem(m.getNom().toUpperCase() + " " + m.getPre(), MATD.getIdATD(m)));
		}
		else lbl_error.setText("Erreur ajout moniteur");
	}

	public void loadCbSemaine(){
		// SEMAINE		
		if (listSemaine != null){
			cb_semaine.removeAllItems();
			for(SemaineATD s : listSemaine) 
				if (!s.getCongeScolaire()) cb_semaine.addItem (new ComboItem(s.getDateDebut().toString(), SATD.getIdATD(s)));
				else lbl_error.setText("Erreur semaine");
		}
	}

	public void loadCbCours(){
		// COURS
		cb_cours.removeAllItems();
		if(rdbtnCoursCollectif.isSelected()){ // Cours collectif
			ArrayList<CoursCollectifATD> listCoursColl = CCATD.getListCoursCollectifSelonIdATD(numMoniteur, numEleve, numSemaine, periode);
					//CoursCollectifDAO.getListCoursCollectifSelonId(numMoniteur, numEleve, periode, numSemaine);
			for(CoursCollectifATD cc : listCoursColl){
				cb_cours.addItem (new ComboItem("Niveau " + cc.getNiveauCours() + ", " + cc.getCategorieAge() + " (" + cc.getNomSport() + ") ", CCATD.getIdATD(cc)));
			}
		}
		else if(rdbtnCoursParticulier.isSelected()){ // Cours particulier
			//ArrayList<CoursParticulier>  listCoursPartic = CoursParticulierDAO.getListCoursParticulierSelonId(numMoniteur, periode, numSemaine);
			ArrayList<CoursParticulierATD> listCoursPartic = CPATD.getListCoursParticulierSelonIdATD(numMoniteur, periode, numSemaine);
			for(CoursParticulierATD cp : listCoursPartic){
				cb_cours.addItem (new ComboItem(cp.getNombreHeures() + " heure(s) de " + cp.getNomSport() + " (" + cp.getPeriodeCours() + ")", CPATD.getIdATD(cp)));
			}
		}
		else lbl_error.setText("Erreur disponibilit�s");
		loadInfoCoursText();
	}

	public void loadInfoCoursText(){
		//String string = CoursDAO.calculerPlaceCours(numCours, numSemaine, numMoniteur);
		String string = CATD.calculerPlaceCours(numCours, numSemaine, numMoniteur);
		String[] parts = string.split("-");
		String part1 = parts[0];
		String part2 = parts[1]; 
		lbl_infoCours.setText("Il reste " + part1 + " places disponibles.");
		lbl_placeMin.setText("Il manque " + part2 + " places pour que ce cours soit prest�.");
	}
}

// CLASSE UTILISEE POUR RECUPERER LID DE LA PERSONNE
class ComboItem
{
	private String key;
	private int value;

	public ComboItem(String key, int value)
	{
		this.key 	= key;
		this.value 	= value;
	}

	@Override
	public String toString	() { return key; } 
	public String getKey	() { return key; }
	public int getValue		() { return value; }
}