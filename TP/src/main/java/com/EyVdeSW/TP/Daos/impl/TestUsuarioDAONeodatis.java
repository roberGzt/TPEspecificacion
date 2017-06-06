package com.EyVdeSW.TP.Daos.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collection;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import com.EyVdeSW.TP.domainModel.Usuario;
import com.EyVdeSW.TP.domainModel.Usuario.TipoUsuario;


import properties.Parametros;

public class TestUsuarioDAONeodatis {

	private static String dbFilePath;
	private static UsuarioDAONeodatis usuarioDAO;
	
	@BeforeClass
	public static void setUpClass(){
		usuarioDAO = new UsuarioDAONeodatis();		
		usuarioDAO.setBdConnector( new NeodatisLocalConnector());
		dbFilePath = Parametros.getProperties().getProperty(Parametros.dbPath);
	}
	
	@Before
	public void setUp() throws Exception{
		File f = new File(dbFilePath);
		if (f.exists())
			f.delete();
	}
	
	public void limpiarBD(){
		Collection<Usuario> usuarios = usuarioDAO.traerTodosLosUsuarios();
		usuarios.forEach(t -> usuarioDAO.borrar(t));
		usuarios = usuarioDAO.traerTodosLosUsuarios();
	}
	
	@Test
	public void modificarSimple(){
		Usuario u1= new Usuario("soyUnNombreUsuario", "soyUnNombreReal","soyUnMail@gmail.com","soyUnaPassword",null);
		u1.setTipoUsuario(TipoUsuario.CLIENTE);
		usuarioDAO.guardar(u1);
		
		Usuario u2=usuarioDAO.getUsuarioPorNombreUsuario("soyUnNombreUsuario");
		
		assertEquals(u1, u2);
		
		u2.setNombreUsuario("fulano77");
		u2.setNombreReal("fulanito");
	
		
		usuarioDAO.modificar(u1, u2);
		
		assertEquals(usuarioDAO.getUsuarioPorNombreUsuario("fulano77"), u2);
	}
	
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
