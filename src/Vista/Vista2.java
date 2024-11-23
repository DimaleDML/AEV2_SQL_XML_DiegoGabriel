package Vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;

public class Vista2 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnAnyadirUsuario;
	private JTextField txtConsulta;
	private JButton btnConsulta;
	private JEditorPane editorPane;
	private JButton btnLogout;
	private JButton btnExportarCSV;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Vista2 frame = new Vista2();
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
	public Vista2() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 670, 609);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnAnyadirUsuario = new JButton("Añadir Usuario");
		btnAnyadirUsuario.setBounds(486, 523, 125, 41);
		contentPane.add(btnAnyadirUsuario);
		
		JLabel lblSelect = new JLabel("Introducir Consulta Select");
		lblSelect.setBounds(10, 11, 132, 14);
		contentPane.add(lblSelect);
		
		txtConsulta = new JTextField();
		txtConsulta.setBounds(10, 28, 464, 20);
		contentPane.add(txtConsulta);
		txtConsulta.setColumns(10);
		
		btnConsulta = new JButton("Ejecutar Consulta");
		btnConsulta.setBounds(10, 54, 117, 23);
		contentPane.add(btnConsulta);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(20, 88, 591, 429);
		contentPane.add(scrollPane_1);
		
		editorPane = new JEditorPane();
		scrollPane_1.setViewportView(editorPane);
		
		btnLogout = new JButton("Log out");
		btnLogout.setBounds(25, 528, 206, 31);
		contentPane.add(btnLogout);
		
		btnExportarCSV = new JButton("Exportar CSV");
		btnExportarCSV.setBounds(486, 11, 125, 66);
		contentPane.add(btnExportarCSV);

	}
	
	public void modoVista(boolean valor) {
		this.setVisible(valor);
	}
	
	public JButton getButtonAnyadirUsuario() {
		return btnAnyadirUsuario;
	}
	public JButton getButtonConsulta() {
		return btnConsulta;
	}
	public JTextField getTxtConsulta() {
		return txtConsulta;
	}
	public JEditorPane getEditorPane() {
		return editorPane;
	}
	public JButton getButtonLogout() {
		return btnLogout;
	}
	
	public JButton getButtonExportarCSV() {
		return btnExportarCSV;
	}
}
