package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import Model.Model;
import Vista.Vista;
import Vista.Vista2;
import Vista.Vista3;

public class Controlador {
	Model model;
	Vista vista;
	Vista2 vista2;
	Vista3 vista3;

	ActionListener actionListenerBotoLogin;
	ActionListener actionListenerBotoAnyadirUsuario;
	ActionListener actionListenerBotoAnyadirBD;
	ActionListener actionListenerBotoImportarCSV;
	ActionListener actionListenerBtnConsulta;
	ActionListener actionListenerBtnVolver;
	ActionListener actionListenerBtnLogout;
	ActionListener actionListenerBotoExportarCSV;

	public Controlador(Model model, Vista vista, Vista2 vista2, Vista3 vista3) {
		this.model = model;
		this.vista = vista;
		this.vista2 = vista2;
		this.vista3 = vista3;
		initEventHandlers();
	}

	public void initEventHandlers() {
		actionListenerBotoLogin = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String usuario = vista.getTextFieldUser().getText();

				System.out.println("usuario: " + usuario);

				char[] arrPassword = vista.getTextFieldPassword().getPassword();

				String contrasenyacifrada = model.cifraMD5(new String(arrPassword));

				System.out.println("contraenya cifrada: " + contrasenyacifrada);

				if (!model.buscaUsuarioValido(usuario, contrasenyacifrada)) {
					JOptionPane.showMessageDialog(null, "ERROR CON LOS DATOS, asegurate de 	que el login y la contraseña son correctos.", "ERROR EN EL LOGIN", JOptionPane.ERROR_MESSAGE);
				} else {
					//JOptionPane.showMessageDialog(null, "TODO GOOD", ":D", JOptionPane.WARNING_MESSAGE);
					if (!model.esAdmin(usuario, contrasenyacifrada)) {
						vista2.getButtonAnyadirUsuario().setEnabled(false);
					}
					vista3.modoVista(false);
					vista.modoVista(false);					
					vista2.modoVista(true);
					
				}
			}
		};
		
		vista.getButtonLogin().addActionListener(actionListenerBotoLogin);
		

		actionListenerBotoAnyadirUsuario = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vista2.modoVista(false);
				vista3.modoVista(true);
				vista2.getTxtConsulta().setText("");
				vista2.getEditorPane().setText("");
			}
		};
		vista2.getButtonAnyadirUsuario().addActionListener(actionListenerBotoAnyadirUsuario);
		
		
		actionListenerBotoAnyadirBD= new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String login , contrasenya , confContrasenya, contrasenyaEncriptada;
				
				login = vista3.getTxtLogin().getText();
				char[] arrPassword = vista3.getTxtPassword().getPassword();
				contrasenya = new String(arrPassword);
				char[] arrConfPassword = vista3.getTxtConfPassword().getPassword();
				confContrasenya = new String(arrConfPassword);
				
				if(login.equals("")) {
					JOptionPane.showMessageDialog(null, "TEXTO VACIO EN EL LOGIN", "LOGIN VACIO", JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(contrasenya.equals("")) {
					JOptionPane.showMessageDialog(null, "TEXTO VACIO DE LA CONTRASEÑA" , "CONTRASEÑA VACIA", JOptionPane.WARNING_MESSAGE);
					return;
				}			
				if(confContrasenya.equals("")) {
					JOptionPane.showMessageDialog(null, "TEXTO VACIO DE LA CONFIRMACION DE CONTRASEÑA" , "CONFIRMACION CONTRASEÑA VACIA", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				if(!contrasenya.equals(confContrasenya)) {
					JOptionPane.showMessageDialog(null, "LAS CONTRASEÑAS NO COINCIDEN" , "ERROR CONTRASEÑAS", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				contrasenyaEncriptada = model.cifraMD5(contrasenya);
				
				if (!model.createUser(login, contrasenyaEncriptada, "client")) {
					JOptionPane.showMessageDialog(null, "NOMBRE DE USUARIO YA REGISTRADO" , "ERROR LOGIN", JOptionPane.ERROR_MESSAGE);
				}

				
			}
		};
		vista3.getButtonAnyadirRegistroBD().addActionListener(actionListenerBotoAnyadirBD);
		
		actionListenerBotoImportarCSV = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.eliminarTablaPopulation();

				vista3.getTextAreaInfoAdmin().setText(model.importarCSV());
			}
		};
		vista3.getButtonImportarCSV().addActionListener(actionListenerBotoImportarCSV);
		
		actionListenerBtnConsulta = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String consulta = vista2.getTxtConsulta().getText();
				vista2.getEditorPane().setContentType("text/html");
				vista2.getEditorPane().setText(model.realizaSelect(consulta));
			}
		};
		vista2.getButtonConsulta().addActionListener(actionListenerBtnConsulta);
		
		actionListenerBtnVolver = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vista3.modoVista(false);
				vista3.getTxtLogin().setText("");
				vista3.getTxtPassword().setText("");
				vista3.getTextAreaInfoAdmin().setText("");
				vista3.getTxtConfPassword().setText("");
				vista.modoVista(false);					
				vista2.modoVista(true);
			}
		};
		vista3.getButtonVolver().addActionListener(actionListenerBtnVolver);
		
		actionListenerBtnLogout = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				vista3.getTxtLogin().setText("");
				vista3.getTxtPassword().setText("");
				vista3.getTxtConfPassword().setText("");
				vista2.getTxtConsulta().setText("");
				vista2.getEditorPane().setText("");
				vista.getTextFieldUser().setText("");
				vista.getTextFieldPassword().setText("");
				vista3.modoVista(false);				
				vista2.modoVista(false);
				vista.modoVista(true);
				model.logOut();
			}
		};
		vista2.getButtonLogout().addActionListener(actionListenerBtnLogout);
		
		actionListenerBotoExportarCSV = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(vista2.getTxtConsulta().getText().equals("")) {
					JOptionPane.showMessageDialog(null, "NO SE PUEDE EXPORTAR SI EL TEXTO ESTA VACIO" , "ERROR AL EXPORTAR", JOptionPane.ERROR_MESSAGE);
				}else {
					model.exportarCSV(vista2.getTxtConsulta().getText());
				}
			}
		};
		vista2.getButtonExportarCSV().addActionListener(actionListenerBotoExportarCSV);
	}
}
