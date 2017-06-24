package com.EyVdeSW.TP.presentacion;

import java.util.ArrayList;
import java.util.List;

import com.EyVdeSW.TP.domainModel.Campania;
import com.EyVdeSW.TP.domainModel.Usuario;
import com.EyVdeSW.TP.services.CampañaService;
import com.EyVdeSW.TP.services.UsuarioService;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class PantallaMisCampañas extends VerticalLayout implements View {

	protected static final String NAME = "pantallaMisCampañas";
	
	CampañaService campañaService = CampañaService.getCampañaService();
	UsuarioService usuarioService = UsuarioService.getUsuarioService();
	List <Campania> campañas = new ArrayList<>();
	
	String username = "";
	
	public PantallaMisCampañas(){
		

		Label titulo = new Label("Mis Campañas");
		titulo.setStyleName(ValoTheme.LABEL_H1);
		HorizontalLayout hlTitulo = new HorizontalLayout(titulo);
		addComponent(hlTitulo);
		setComponentAlignment(hlTitulo, Alignment.MIDDLE_CENTER);
		
		Button editar = new Button("Editar campaña seleccionada");
		Button borrar = new Button("Borrar campaña seleccionada");
		
		Usuario usuario = usuarioService.getUsuarioPorMail(username);
		campañas = campañaService.getCampañasDeUsuario(usuario);
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		String usernameMail = String.valueOf(getSession().getAttribute("user"));
		username=usernameMail;
		
	}

	
	
}
