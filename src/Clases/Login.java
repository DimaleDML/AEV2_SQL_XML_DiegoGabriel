package Clases;

public class Login {

	private String usuario;
	private String contrasenya;
	private String tipo;
	
	public Login(String usuario, String contrasenya, String tipo) {
		this.usuario = usuario;
		this.contrasenya = contrasenya;
		this.tipo = tipo;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getUsuario() {
		return usuario;
	}

	public String getContrasenya() {
		return contrasenya;
	}
	
	public String getTipo() {
		return tipo;
	}
}
