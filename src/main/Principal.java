package main;

import Controlador.Controlador;
import Model.Model;
import Vista.Vista;
import Vista.Vista2;
import Vista.Vista3;

public class Principal {

	public static void main(String[] args) {
		Vista vista = new Vista();
		Vista2 vista2 = new Vista2();
		Vista3 vista3 = new Vista3();
		Model model = new Model();
		Controlador controlador = new Controlador(model, vista, vista2, vista3);

	}

}