package Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class Vista3 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnAnyadirRegistroBD;
	private JButton btnImportarCSV;
	private JTextField txtLogin;
	private JPasswordField txtPassword;
	private JPasswordField txtConfPassword;
	private JLabel lblLogin;
	private JLabel lblContrasenya;
	private JLabel lblConfContrasenya;
	private JTextArea txtInfoAdmin;
	private JButton btnVolver;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Vista3 frame = new Vista3();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public Vista3() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 584, 535);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnAnyadirRegistroBD = new JButton("Añadir BD");
		btnAnyadirRegistroBD.setBounds(30, 231, 121, 56);
		contentPane.add(btnAnyadirRegistroBD);
		
		txtLogin = new JTextField();
		txtLogin.setBounds(30, 83, 121, 20);
		contentPane.add(txtLogin);
		txtLogin.setColumns(10);
		
		lblLogin = new JLabel("Login");
		lblLogin.setBounds(77, 58, 46, 14);
		contentPane.add(lblLogin);
		
		lblContrasenya = new JLabel("Contrasenya");
		lblContrasenya.setBounds(55, 114, 68, 14);
		contentPane.add(lblContrasenya);
		
		lblConfContrasenya = new JLabel("Confirmació Contrasenya");
		lblConfContrasenya.setBounds(30, 167, 121, 14);
		contentPane.add(lblConfContrasenya);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(30, 136, 121, 20);
		contentPane.add(txtPassword);
		
		txtConfPassword = new JPasswordField();
		txtConfPassword.setBounds(30, 192, 121, 20);
		contentPane.add(txtConfPassword);
		
		btnImportarCSV = new JButton("Importar CSV");
		btnImportarCSV.setBounds(176, 429, 382, 56);
		contentPane.add(btnImportarCSV);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(176, 11, 382, 396);
		contentPane.add(scrollPane);
		
		txtInfoAdmin = new JTextArea();
		txtInfoAdmin.setEditable(false);
		scrollPane.setViewportView(txtInfoAdmin);
		
		btnVolver = new JButton("Volver");
		btnVolver.setBounds(30, 9, 121, 31);
		contentPane.add(btnVolver);
	}
	public void modoVista(boolean valor) {
		this.setVisible(valor);
	}
	
	public JButton getButtonAnyadirRegistroBD() {
		return btnAnyadirRegistroBD;
	}
	
	public JPasswordField getTxtPassword() {
		return txtPassword;
	}
	
	public JPasswordField getTxtConfPassword() {
		return txtConfPassword;
	}
	
	public JTextField getTxtLogin() {
		return txtLogin;
	}
	
	public JButton getButtonImportarCSV() {
		return btnImportarCSV;
	}
	public JTextArea getTextAreaInfoAdmin() {
		return txtInfoAdmin;
	}
	public JButton getButtonVolver() {
		return btnVolver;
	}
}
