package FENETRE;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import DAO.AbstractDAOFactory;
import DAO.ClientDAO;
import DAO.DAO;
import POJO.Client;
import POJO.Eleve;
import POJO.Utilisateur;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;

import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JCheckBox;

public class F_Client extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					F_Client frame = new F_Client(-1);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public F_Client(int idClient) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 220, 340);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Instanciation
		JLabel labStatut = new JLabel(""+idClient);
		JLabel lblClient = new JLabel("Client");
		JButton btn_sajouterCli = new JButton("S'ajouter comme eleve");
		JButton btnNewButton = new JButton("Deconnexion");
		JSeparator separator = new JSeparator();
		JButton btn_ajoutEleve = new JButton("Ajouter un \u00E9l\u00E8ve");
		JButton btn_reserverCours = new JButton("R\u00E9server un cours");
		JButton btn_afficherFacture = new JButton("Afficher la facture");
		btn_afficherFacture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		// Fonts
		lblClient.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));

		// Bounds
		labStatut.setBounds(68, 13, 122, 14);
		btn_sajouterCli.setBounds(10, 38, 180, 30);
		lblClient.setBounds(10, 11, 46, 14);
		btnNewButton.setBounds(10, 250, 180, 30);
		separator.setBounds(10, 34, 53, 14);
		btn_afficherFacture.setBounds(12, 207, 178, 30);
		btn_reserverCours.setBounds(10, 124, 180, 30);
		btn_ajoutEleve.setBounds(10, 81, 180, 30);

		// ADD
		contentPane.add(lblClient);
		contentPane.add(btn_sajouterCli);
		contentPane.add(labStatut);
		contentPane.add(btnNewButton);
		contentPane.add(separator);
		contentPane.add(btn_ajoutEleve);
		contentPane.add(btn_reserverCours);
		contentPane.add(btn_afficherFacture);
		
		JButton btnAfficherMesCours = new JButton("Afficher mes cours");
		btnAfficherMesCours.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Affiche F_AfficherRDV
				setVisible(false);
				F_AfficherRDV frame = new F_AfficherRDV(idClient);
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
		btnAfficherMesCours.setBounds(12, 167, 178, 30);
		contentPane.add(btnAfficherMesCours);

		// ONCLICK
		// S'ajouter en tant qu'�l�ve
		btn_sajouterCli.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				try {
					Client C = null;
					C = C.rechercherClient(idClient);
					System.out.println(C.getAdresse());
					if(C != null){
						System.out.println("F_Client -> ajout eleve");
						Eleve E = new Eleve(C.getNumPersonne(), C.getNom(), C.getPre(), C.getAdresse(), C.getSexe(), C.getDateNaissance());
						if (E.createEleve() != -1) labStatut.setText("Vous avez �t�s ajout�s en tant qu'�l�ve.");
						else labStatut.setText("Verifiez vos donnees");
					}
					else{ labStatut.setText("ID reli� � aucune personne."); }
				}
				catch (Exception e) {
					labStatut.setText("Verifiez vos donnees");
					e.printStackTrace();
				}

			}
		});
		
		/*btn_sajouterCli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});*/

		// Ajouter un �l�ve
		btn_ajoutEleve.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// Affiche F_ajoutEleve
				setVisible(false);
				F_AjoutEleve frame = new F_AjoutEleve(idClient);
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
		
		// Ajouter RDV
		btn_reserverCours.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				setVisible(false);
				F_AjoutRdv frame = new F_AjoutRdv(idClient);
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
		
		
		
		// Deconnexion
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// deconnexion
				setVisible(false);
				F_Connexion frame = new F_Connexion();
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}
}
