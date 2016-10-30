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
					F_Client frame = new F_Client(105);
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
		setBounds(100, 100, 220, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JLabel labStatut = new JLabel(""+idClient);

		JButton btn_sajouterCli = new JButton("S'ajouter comme eleve");
		btn_sajouterCli.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// S'ajouter en tant qu'�l�ve
				try {
					//AbstractDAOFactory adf = AbstractDAOFactory.getFactory(AbstractDAOFactory.DAO_FACTORY);
					//DAO<Eleve> EleveDao = adf.getEleveDAO();
					//DAO<Client> ClientDao = adf.getClientDAO();
					
					//Client C = ClientDao.find(idClient);
					Client C = null;
					C = C.rechercherClient(idClient);
					System.out.println(C.getAdresse());
					if(C != null){
						System.out.println("F_Client -> ajout eleve");
						Eleve E = new Eleve(C.getNumPersonne(), C.getNom(), C.getPre(), C.getAdresse(), C.getSexe(), C.getDateNaissance());
						if (E.createEleve() != -1)
							labStatut.setText("Vous avez �t�s ajout�s en tant qu'�l�ve.");
						else
							labStatut.setText("Verifiez vos donnees");
					}
					else{
						labStatut.setText("ID reli� � aucune personne.");
					}
				}
			catch (Exception e) {
				labStatut.setText("Verifiez vos donnees");
				e.printStackTrace();
			}

		}
	});
		btn_sajouterCli.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btn_sajouterCli.setBounds(10, 38, 180, 30);
		contentPane.add(btn_sajouterCli);

		JLabel lblClient = new JLabel("Client");
		lblClient.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
		lblClient.setBounds(10, 11, 46, 14);
		contentPane.add(lblClient);


		labStatut.setBounds(68, 13, 122, 14);
		contentPane.add(labStatut);

		;

		JButton btnNewButton = new JButton("Deconnexion");
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
		btnNewButton.setBounds(10, 210, 180, 30);
		contentPane.add(btnNewButton);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 34, 53, 14);
		contentPane.add(separator);
		
		JButton btn_ajoutEleve = new JButton("Ajouter un \u00E9l\u00E8ve");
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
		btn_ajoutEleve.setBounds(10, 81, 180, 30);
		contentPane.add(btn_ajoutEleve);
		
		JButton btn_reserverCours = new JButton("R\u00E9server un cours");
		btn_reserverCours.setBounds(10, 124, 180, 30);
		contentPane.add(btn_reserverCours);
		
		JButton btn_afficherFacture = new JButton("Afficher la facture");
		btn_afficherFacture.setBounds(12, 167, 178, 30);
		contentPane.add(btn_afficherFacture);
}
}
