package be.mousty.fenetre;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import be.mousty.accessToDao.EleveATD;
import be.mousty.utilitaire.DateLabelFormatter;

public class F_AjoutEleve extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String sexe = "H"; 


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					F_AjoutEleve frame = new F_AjoutEleve(-1);
					frame.setVisible(true);
				}
				catch (Exception e) { e.printStackTrace(); }
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public F_AjoutEleve(int numClient) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 192, 379);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// News
		JLabel lblInscription 		= new JLabel("Inscription \u00E9l\u00E8ve");
		JLabel lblNom 				= new JLabel("Nom");
		JLabel lblPre 				= new JLabel("Prenom");
		JLabel lblDateNaiss 		= new JLabel("Date de naissance");
		JLabel lblAdresse 			= new JLabel("Adresse");
		JRadioButton rdbtnH 		= new JRadioButton("Homme");
		JRadioButton rdbtnF 		= new JRadioButton("Femme");


		JTextField txtF_nom 		= new JTextField();
		JTextField txtF_pre 		= new JTextField();
		JTextField txtF_adresse 	= new JTextField();
		JButton btn_inscrip 		= new JButton("Enregistrement");
		JButton btn_ret 			= new JButton("Retour");

		// Set tooltip
		txtF_nom.setToolTipText		("Nom");
		txtF_pre.setToolTipText		("Prenom");
		txtF_adresse.setToolTipText	("Adresse");

		// Fonts
		lblInscription.setFont	(new Font("Yu Gothic UI", Font.PLAIN, 13));
		lblNom.setFont			(new Font("Yu Gothic UI", Font.PLAIN, 11));
		lblPre.setFont			(new Font("Yu Gothic UI", Font.PLAIN, 11));
		lblDateNaiss.setFont	(new Font("Yu Gothic UI", Font.PLAIN, 11));
		lblAdresse.setFont		(new Font("Yu Gothic UI", Font.PLAIN, 11));

		// Bounds 
		lblInscription.setBounds	(10, 11, 102, 14);
		lblNom.setBounds			(10, 29, 46, 14);
		lblPre.setBounds			(10, 74, 130, 14);
		lblDateNaiss.setBounds		(10, 124, 130, 14);
		lblAdresse.setBounds		(10, 172, 130, 14);
		rdbtnH.setBounds			(98, 234, 67, 23);
		rdbtnF.setBounds			(10, 234, 67, 23);
		txtF_nom.setBounds			(10, 43, 155, 20);
		txtF_pre.setBounds			(10, 90, 155, 20);
		txtF_adresse.setBounds		(10, 187, 155, 40);
		btn_inscrip.setBounds		(10, 264, 155, 20);
		btn_ret.setBounds			(10, 286, 155, 20);

		// Add
		contentPane.add(lblInscription);
		contentPane.add(lblNom);
		contentPane.add(lblPre);
		contentPane.add(lblDateNaiss);
		contentPane.add(lblAdresse);
		contentPane.add(rdbtnH);
		contentPane.add(rdbtnF);
		contentPane.add(txtF_nom);
		contentPane.add(txtF_pre);
		contentPane.add(txtF_adresse);
		contentPane.add(btn_ret);
		contentPane.add(btn_inscrip);

		// Set columns
		txtF_nom.setColumns(10);
		txtF_pre.setColumns(10);
		txtF_adresse.setColumns(10);


		// Datepicker
		SqlDateModel model = new SqlDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		datePicker.getJFormattedTextField().setToolTipText("Date de naissance");

		datePicker.setBounds(10, 138, 155, 23);
		contentPane.add(datePicker);


		rdbtnH.setSelected(true);

		// Action sur les boutons/
		rdbtnH.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				rdbtnH.setSelected(true);
				sexe = "H";
				rdbtnF.setSelected(false);
			}
		});

		rdbtnF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				rdbtnH.setSelected(false);
				sexe = "F";
				rdbtnF.setSelected(true);
			}
		});



		btn_inscrip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(!txtF_nom.getText().equals("") && !txtF_pre.getText().equals("") && !txtF_adresse.getText().equals("")){
						String dateNaissance = datePicker.getJFormattedTextField().getText();
						DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
						java.util.Date ud = df.parse(dateNaissance);
						java.sql.Date sd = new java.sql.Date(ud.getTime());
						//String nom, String pre, String adresse, String sexe, Date dateNaissance
						EleveATD EATD = new EleveATD(txtF_nom.getText(), txtF_pre.getText(), txtF_adresse.getText(), sexe, sd);
						int numEleve = EATD.inscriptionEleve(numClient);
						if (numEleve != -1){
							// Afficher la fenetre client
							setVisible(false); 
							F_Client frame = new F_Client(numClient);
							frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
							frame.setVisible(true);
						}
						else { JOptionPane.showMessageDialog(contentPane, "V�rifiez vos donn�es.");}
					} else { JOptionPane.showMessageDialog(contentPane, "Des champs sont vides."); } 

				} 
				catch (ParseException e) {
					JOptionPane.showMessageDialog(contentPane, "V�rifiez vos donn�es.");
					e.printStackTrace();
				}
			}
		});

		btn_ret.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				setVisible(false);
				F_Client frame = new F_Client(numClient);
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}
