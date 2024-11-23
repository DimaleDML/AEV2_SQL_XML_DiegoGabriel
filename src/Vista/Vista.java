package Vista;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JPasswordField;

public class Vista extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUser;
	private JButton btnLogin;
	private JLabel lblUser;
	private JLabel lblPassword;
	private JPasswordField txtPassword;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Vista frame = new Vista();
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
	public Vista() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtUser = new JTextField();
		txtUser.setBounds(69, 102, 144, 36);
		contentPane.add(txtUser);
		txtUser.setColumns(10);
		
		btnLogin = new JButton("LOGIN");
		btnLogin.setBounds(69, 226, 144, 36);
		contentPane.add(btnLogin);
		
		lblUser = new JLabel("User");
		lblUser.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblUser.setBounds(69, 78, 46, 14);
		contentPane.add(lblUser);
		
		lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPassword.setBounds(69, 149, 87, 14);
		contentPane.add(lblPassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(68, 174, 145, 36);
		contentPane.add(txtPassword);
		this.setVisible(true);
	}
	
	public JButton getButtonLogin() {
		return btnLogin;
	}
	
	public JTextField getTextFieldUser() {
		return txtUser;
	}
	
	public JPasswordField getTextFieldPassword() {
		return txtPassword;
	}
	
	public JLabel getLabelUser() {
		return lblUser;
	}
	
	public JLabel getLabelPassword() {
		return lblPassword;
	}
	
	public void modoVista(boolean valor) {
		this.setVisible(valor);
	}
	
}
