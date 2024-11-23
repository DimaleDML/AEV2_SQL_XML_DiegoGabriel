package Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Clases.Login;

public class Model {

	Connection con;
	Login log;

	
	/**
	 * Cifra contraseña a formato MD5
	 * @param input contraseña sin cifrar
	 * @return contraseña cifrada
	 */
	public String cifraMD5(String input) {

		String output = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest(input.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : digest) {
				sb.append(String.format("%02x", b));
			}
			output = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return output;
	}

	/**
	 * Comprueba si el usuario especificado es valido para conectarse a la base de datos SQL
	 * @param usuario usuario especificado
	 * @param contrasenya contraseña especificada
	 * @return boolean indicando si es valido (true) o no (false)
	 */
	public boolean buscaUsuarioValido(String usuario, String contrasenya) {
		boolean flag = true;
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", usuario, contrasenya);
			if (con != null && !con.isClosed()) {
				System.out.println("La conexión está abierta.");
				con.isClosed();
			} else {
				System.out.println("La conexión está cerrada.");
				con.isClosed();
				flag = false;
			}

		} catch (SQLException e) {
			return false;
			// e.printStackTrace();
		}
		String tipo = "";
		if (esAdmin(usuario, contrasenya)) {
			tipo = "admin";
		} else {
			tipo = "client";
		}
		log = new Login(usuario, contrasenya, tipo);
		if (log.getTipo().equals("admin")) {
			System.out.println("------------------ > es admin");
		} else {
			System.out.println("------------------ > no es admin");
		}
		return flag;

	}

	/**
	 * Inserta los datos del nuevo usuario en la BBDD y llama a otra funcion que se encarga de la creacion
	 * @param login nombre del usuario a ser creado
	 * @param contrasenya contraseña del usuario a ser creado
	 * @param tipo tipo del usuario especificado (client por defecto) (client/admin)
	 * @return boolean indicando si se ha añadido correctamente (true) o no (false)
	 */
	public boolean createUser(String login, String contrasenya, String tipo) {

		PreparedStatement psInsertar;
		try {
			if (usuarioRepetido(login)) {
				return false;
			}
			psInsertar = con.prepareStatement("INSERT INTO users (login,password,TYPE) VALUES (?,?,?)");
			psInsertar.setString(1, login);
			psInsertar.setString(2, contrasenya);
			psInsertar.setString(3, tipo);

			int filasAfectadas = psInsertar.executeUpdate();
			if (filasAfectadas == 1) {
				generateUser(login, contrasenya);
				grantPermisions(login);
			}
			System.out.println("Registro insertado correctamente. Filas afectadas: " + filasAfectadas);
			psInsertar.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;

	}

	/**
	 * Genera el usuario en la BBDD
	 * @param login Nombre del usuario especificado
	 * @param contrasenya Contraseña del usuario especificado
	 */
	public void generateUser(String login, String contrasenya) {
		String consulta;
		Statement stmt = null;
		try {
			stmt = con.createStatement();

			consulta = "CREATE USER '" + login + "' IDENTIFIED BY '" + contrasenya + "'";

			int filasAfectadas = stmt.executeUpdate(consulta);

			System.out.println("Registro insertado correctamente. Filas afectadas: " + filasAfectadas);
			stmt.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Da permisos al usuario especificado
	 * @param login nombre del usuario al que se le añaden los permisos
	 */
	public void grantPermisions(String login) {
		String consultaPermisos;
		Statement stmt = null;
		try {
			stmt = con.createStatement();

			consultaPermisos = "GRANT SELECT ON population.population TO '" + login + "'";

			stmt.executeUpdate(consultaPermisos);
			System.out.println("Permisos otorgados correctamente.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Crea tabla population, y la rellena con datos provenientes de la funcion writeXmlFile
	 * @return devuelve los datos extraidos del fichero csv en forma de string  
	 */
	public String importarCSV() {
		String StringAmostrar = "";
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", log.getUsuario(),
					log.getContrasenya());
			File ficheroCSV = new File("src/BD_CSV/AE02_population.csv");
			FileReader fr = new FileReader(ficheroCSV, StandardCharsets.UTF_8);
			BufferedReader br = new BufferedReader(fr);

			String linea = br.readLine();

			Statement stmt = con.createStatement();

			String[] nombresTabla = linea.split(";");

			String consultaTabla = "CREATE TABLE IF NOT EXISTS population (\r\n" + nombresTabla[0]
					+ " VARCHAR(30) NOT NULL,\r\n" + nombresTabla[1] + " VARCHAR(30) NOT NULL,\r\n" + nombresTabla[2]
					+ " VARCHAR(30) NOT NULL,\r\n" + nombresTabla[3] + " VARCHAR(30) NOT NULL,\r\n" + nombresTabla[4]
					+ " VARCHAR(30) NOT NULL,\r\n" + nombresTabla[5] + " VARCHAR(30) NOT NULL,\r\n" + nombresTabla[6]
					+ " VARCHAR(30) NOT NULL,\r\n" + nombresTabla[7] + " VARCHAR(30) NOT NULL,\r\n" + " PRIMARY KEY ("
					+ nombresTabla[0] + ")\r\n" + ");";

			stmt.executeUpdate(consultaTabla);

			linea = br.readLine();
			StringAmostrar = "";

			while (linea != null) {
				String[] elementosCSV = linea.split(";");

				writeXmlFile(elementosCSV);
				StringAmostrar += creaStringConcatenado(elementosCSV);

				String[] listaAinsertar = recorrerLista(elementosCSV[0]);

				PreparedStatement psInsertar;
				psInsertar = con.prepareStatement(
						"INSERT INTO population (country,population,density,area,fertility,age,urban,share) VALUES (?,?,?,?,?,?,?,?)");
				psInsertar.setString(1, listaAinsertar[0]);
				psInsertar.setString(2, listaAinsertar[1]);
				psInsertar.setString(3, listaAinsertar[2]);
				psInsertar.setString(4, listaAinsertar[3]);
				psInsertar.setString(5, listaAinsertar[4]);
				psInsertar.setString(6, listaAinsertar[5]);
				psInsertar.setString(7, listaAinsertar[6]);
				psInsertar.setString(8, listaAinsertar[7]);

				int filasAfectadas = psInsertar.executeUpdate();
				if (filasAfectadas == 1) {
					System.out.println("Fila insertada correctamente: ");
					System.out.println("-->" + psInsertar.toString());
				}
				psInsertar.close();
				linea = br.readLine();
			}
			br.close();
			fr.close();

		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return StringAmostrar;
	}

	/**
	 * Elimina tabla population
	 */
	public void eliminarTablaPopulation() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", log.getUsuario(),
					log.getContrasenya());
			Statement stmt = con.createStatement();
			String consultaTabla = "DROP TABLE IF EXISTS population";
			stmt.executeUpdate(consultaTabla);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Crea un fichero xml a traves de un array de Strings
	 * @param elementosCSV array de strings con los datos a escribir
	 */
	public static void writeXmlFile(String[] elementosCSV) {
		try {
			DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
			DocumentBuilder build = dFact.newDocumentBuilder();
			Document doc = build.newDocument();
			Element raiz = doc.createElement("Country");
			doc.appendChild(raiz);

			Element country = doc.createElement("country");
			country.appendChild(doc.createTextNode(String.valueOf(elementosCSV[0])));
			raiz.appendChild(country);
			Element population = doc.createElement("population");
			population.appendChild(doc.createTextNode(String.valueOf(elementosCSV[1])));
			raiz.appendChild(population);
			Element density = doc.createElement("density");
			density.appendChild(doc.createTextNode(String.valueOf(elementosCSV[2])));
			raiz.appendChild(density);
			Element area = doc.createElement("area");
			area.appendChild(doc.createTextNode(String.valueOf(elementosCSV[3])));
			raiz.appendChild(area);
			Element fertility = doc.createElement("fertility");
			fertility.appendChild(doc.createTextNode(String.valueOf(elementosCSV[4])));
			raiz.appendChild(fertility);
			Element age = doc.createElement("age");
			age.appendChild(doc.createTextNode(String.valueOf(elementosCSV[5])));
			raiz.appendChild(age);
			Element urban = doc.createElement("urban");
			urban.appendChild(doc.createTextNode(String.valueOf(elementosCSV[6])));
			raiz.appendChild(urban);
			Element share = doc.createElement("share");
			share.appendChild(doc.createTextNode(String.valueOf(elementosCSV[7])));
			raiz.appendChild(share);

			TransformerFactory tranFactory = TransformerFactory.newInstance();
			Transformer aTransformer = tranFactory.newTransformer();
			aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
			aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			try {
				File ficheroXML = new File("src/xml/" + elementosCSV[0] + ".xml");
				FileWriter fw = new FileWriter(ficheroXML);
				StreamResult result = new StreamResult(fw);
				aTransformer.transform(source, result);
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (TransformerException ex) {
			//System.out.println("Error escrivint el documento");
		} catch (ParserConfigurationException ex) {
			//System.out.println("Error construint el document");
		}
	}

	/**
	 * Da formato al contenido extraido de un fichero csv
	 * @param elementosCSV array de strings con el contenido de un fichero csv
	 * @return elementos formateados dentro de un string
	 */
	public static String creaStringConcatenado(String[] elementosCSV) {
		String lineaPais = "=>";
		for (String elemento : elementosCSV) {
			lineaPais += " - " + elemento;
		}
		lineaPais += "\n";

		return lineaPais;
	}

	/**
	 * devuelve un array que contiene los datos de un pais concreto
	 * @param nombrePais al cual queremos sacarle los datos
	 * @return array de strings con la informacion requerida
	 */
	public static String[] recorrerLista(String nombrePais) {
		String[] datos = new String[8];
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(new File("src/xml/" + nombrePais + ".xml"));
			Element raiz = document.getDocumentElement();
			NodeList nodeList = document.getElementsByTagName("Country");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				System.out.println("");
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;

					datos[0] = eElement.getElementsByTagName("country").item(0).getTextContent();
					datos[1] = eElement.getElementsByTagName("population").item(0).getTextContent();
					datos[2] = eElement.getElementsByTagName("density").item(0).getTextContent();
					datos[3] = eElement.getElementsByTagName("area").item(0).getTextContent();
					datos[4] = eElement.getElementsByTagName("fertility").item(0).getTextContent();
					datos[5] = eElement.getElementsByTagName("age").item(0).getTextContent();
					datos[6] = eElement.getElementsByTagName("urban").item(0).getTextContent();
					datos[7] = eElement.getElementsByTagName("share").item(0).getTextContent();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return datos;
	}

	/**
	 * comprueba si el usuario especificado por nombre, ya existe en la base de datos
	 * @param usuario especificado
	 * @return boolean indicando si existe un usuario con el nombre especificado
	 */
	public boolean usuarioRepetido(String usuario) {

		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", log.getUsuario(),
					log.getContrasenya());
			Statement stmt = con.createStatement();

			String query = "SELECT * FROM users WHERE login = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, usuario);

			ResultSet rs = pstmt.executeQuery();

			int rowCount = 0;
			while (rs.next()) {
				rowCount++;
			}

			if (rowCount == 0) {
				return false;
			}

			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return true;
	}

	/**
	 * Comprueba si el usuario especificado es admin o no realizando una consulta sobre una tabla protegida
	 * @param usuario usuario especificado
	 * @param contra contraseña del usuario especificado
	 * @return boolean indicando si el usuario es admin(true) o no (false)
	 */
	public boolean esAdmin(String usuario, String contra) {

		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", usuario, contra);
			Statement stmt = con.createStatement();

			String query = "SELECT * FROM users WHERE login = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, usuario);

			ResultSet rs = pstmt.executeQuery();

			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			return false;
			// System.out.println(e);
		}
		return true;
	}

	/**
	 * Realizas sentencia selecto sobre la base de datos y muestra tabla con el resultado
	 * @param consulta consulta realizada por el usuario
	 * @return string con codigo HTML que contiene la tabla con los resultados de la consulta
	 */
	public String realizaSelect(String consulta) {
		String html = "<TABLE>";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", log.getUsuario(),
					log.getContrasenya());
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(consulta);

			int columnCount = rs.getMetaData().getColumnCount();

			html += "<TR>";
			for (int conta1 = 1; conta1 <= columnCount; conta1++) {
				html += "<TH>" + rs.getMetaData().getColumnName(conta1) + "</TH>";
			}
			html += "</TR>";

			while (rs.next()) {
				html += "<TR>";
				for (int conta = 1; conta <= columnCount; conta++) {
					html += "<TD>" + rs.getString(conta) + "</TD>";
				}
				html += "</TR>";
			}
			html += "</TABLE>";

			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
			return e.toString();
		}
		return html;
	}

	/**
	 * Realiza consulta select i lo exporta a un fichero CSV
	 * @param consulta consulta realizada por el usuario
	 */
	public void exportarCSV(String consulta) {
		String contenido = "";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/population", log.getUsuario(),
					log.getContrasenya());
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(consulta);

			int columnCount = rs.getMetaData().getColumnCount();

			for (int conta1 = 1; conta1 <= columnCount; conta1++) {
				if (conta1 == columnCount) {
					contenido += rs.getMetaData().getColumnName(conta1) + "";
				} else {
					contenido += rs.getMetaData().getColumnName(conta1) + ";";
				}

			}

			contenido += "\n";

			while (rs.next()) {
				for (int conta = 1; conta <= columnCount; conta++) {
					if (conta == columnCount) {
						contenido += rs.getString(conta) + "";
					} else {
						contenido += rs.getString(conta) + ";";
					}
				}
				contenido += "\n";
			}
			crearCSV(contenido);
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Crea fichero CSV con nombre unico
	 * @param contenidoFichero contenido del fichero a ser creado
	 */
	public void crearCSV(String contenidoFichero) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
			String date = sdf.format(new Date());
			FileWriter writer = new FileWriter("consultaSelect"+date+".csv");

			writer.write(contenidoFichero);

			writer.close();

			System.out.println("Successfully wrote text to file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	/**
	 * Vuelve a la pantalla de login y elimina todos los datos relacionados con el login anterior
	 */
	public void logOut() {
		log.setUsuario(null);
		log.setContrasenya(null);
		log.setTipo(null);
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
