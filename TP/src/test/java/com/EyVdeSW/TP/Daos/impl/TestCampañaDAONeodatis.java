package com.EyVdeSW.TP.Daos.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.EyVdeSW.TP.Daos.impl.CampañaDAONeodatis;
import com.EyVdeSW.TP.Daos.impl.NeodatisLocalTestConnector;
import com.EyVdeSW.TP.domainModel.Campania;
import com.EyVdeSW.TP.domainModel.Campania.EstadoCampania;
import com.EyVdeSW.TP.domainModel.Usuario;

import properties.Parametros;

public class TestCampañaDAONeodatis {

	private static String dbFilePath;
	private static CampañaDAONeodatis campañaDAO;

	@BeforeClass
	public static void setUpClass() {
		campañaDAO = new CampañaDAONeodatis();
		campañaDAO.setBdConnector(new NeodatisLocalTestConnector());
		dbFilePath = Parametros.getProperty(Parametros.dbTestPath);
		File f = new File(dbFilePath);
		if (f.exists())
			f.delete();
	}
	
	@AfterClass
	public static void tearDownClass() {		
		File f = new File(dbFilePath);
		if (f.exists())
			f.delete();
	}

	@Before
	public void LimpiarBD() throws Exception {
		campañaDAO.traerTodos().forEach(c -> campañaDAO.borrar(c));
	}

	@Test(expected = IllegalArgumentException.class)
	public void crearCampañaUserException() {
		Usuario userInvalido = new Usuario("pepe", "unUsuario", "usuario@asd.com", "1234",
				Usuario.TipoUsuario.ANALISTACOMERCIAL);
		new Campania(userInvalido, "unNombre", "unaDescripcion");
	}

	@Test(expected = IllegalArgumentException.class)
	public void crearCampañaUserException2() {
		Usuario userInvalido = new Usuario("pepe", "unUsuario", "usuario@asd.com", "1234",
				Usuario.TipoUsuario.ANALISTATECNICO);
		new Campania(userInvalido, "unNombre", "unaDescripcion");
	}
	
	//TODO fechaDeInicio<FechaDeFin
	//@Test(expected = IllegalArgumentException.class)	
	public void crearCampañaUserException3() {
		Usuario userInvalido = new Usuario("pepe", "unUsuario", "usuario@asd.com", "1234",
				Usuario.TipoUsuario.CLIENTE);
		@SuppressWarnings("unused")
		Campania c = new Campania(userInvalido, "unNombre", "unaDescripcion");		
	}
	
	@Test
	public void crearCampañaUser() {
		Usuario userValido = new Usuario("pepe", "unUsuario", "usuario@asd.com", "1234", Usuario.TipoUsuario.CLIENTE);
		new Campania(userValido, "unNombre", "unaDescripcion");
	}

	@Test
	public void testExiste() {
		Usuario userValido = new Usuario("pepe", "unUsuario", "usuario@asd.com", "1234", Usuario.TipoUsuario.CLIENTE);
		Campania c1 = new Campania(userValido, "unaCampaña", "Soy una descripcion");
		campañaDAO.guardar(c1);

		assertEquals(true, campañaDAO.existe("unaCampaña"));
		assertEquals(false, campañaDAO.existe("unaCampañaInexistente"));

	}

	@Test
	public void getCampañaPorId(){
		Usuario userValido = new Usuario("pepe", "unUsuario", "usuario@asd.com", "1234", Usuario.TipoUsuario.CLIENTE);
		Campania c1 = new Campania(userValido, "unaCampaña", "Soy una descripcion");
		campañaDAO.guardar(c1);
		assertEquals(c1, campañaDAO.getCampañaPorId(c1.getIdCampania()));		
	}
	
	
	
	@Test (expected = IllegalArgumentException.class)
	public void getCampañaPorIdException(){
		Usuario userValido = new Usuario("pepe", "unUsuario", "usuario@asd.com", "1234", Usuario.TipoUsuario.CLIENTE);
		Campania c1 = new Campania(userValido, "unaCampaña", "Soy una descripcion");
		Campania c2 = new Campania(userValido, "unaCampaña", "Soy una descripcion");
		campañaDAO.guardar(c1);
		campañaDAO.getCampañaPorId(c2.getIdCampania());
	}
	
	@Test
	public void modificarSimple() {
		Usuario userValido = new Usuario("pepe", "unUsuario", "usuario@asd.com", "1234", Usuario.TipoUsuario.CLIENTE);
		Campania c1 = new Campania(userValido, "unaCampaña", "Soy una descripcion");
		c1.setEstado(EstadoCampania.PLANIFICADA);
		campañaDAO.guardar(c1);

		Campania c2 = campañaDAO.getCampañaPorNombre("unaCampaña");

		assertEquals(c1, c2);

		c2.setNombre("sarasa");
		c2.setDescripcion("descripcion");

		campañaDAO.modificar(c1, c2);

		assertEquals(campañaDAO.getCampañaPorNombre("sarasa"), c2);
	}
	
	@Test
	public void modificarPorId() {
		Usuario userValido = new Usuario("pepe", "unUsuario", "usuario@asd.com", "1234", Usuario.TipoUsuario.CLIENTE);
		Campania c1 = new Campania(userValido, "unaCampaña", "Soy una descripcion");
		c1.setEstado(EstadoCampania.PLANIFICADA);
		campañaDAO.guardar(c1);

		Campania c2 = campañaDAO.getCampañaPorNombre("unaCampaña");

		assertEquals(c1, c2);

		c2.setNombre("sarasa");
		c2.setDescripcion("descripcion");

		campañaDAO.modificar(c2.getIdCampania(), c2);

		assertEquals(campañaDAO.getCampañaPorNombre("sarasa"), c2);
	}

	@Test
	public void traerTodos() {
		agregarDatosDePrueba(instanciaCampañas());
		ArrayList<Campania> campanias = (ArrayList<Campania>) campañaDAO.traerTodos();
		assertEquals(campanias.size(), 5);
	}

	@Test
	public void consultarPorNombre() {
		agregarDatosDePrueba(instanciaCampañas());
		ArrayList<Campania> campanias = (ArrayList<Campania>) campañaDAO.consultarPorNombre("c");
		assertEquals(campanias.size(), 5);
		campanias = (ArrayList<Campania>) campañaDAO.consultarPorNombre("c2");
		assertEquals(campanias.size(), 1);
	}
	
	
	@Test (expected = IllegalArgumentException.class)
	public void getOwnerException() {
		agregarDatosDePrueba(instanciaCampañas());
		campañaDAO.getOwner("unaCampañaInexistente");		
	}
	
	@Test
	public void getOwner() {
		ArrayList<Campania> campañas = instanciaCampañas();
		agregarDatosDePrueba(campañas);
		assertEquals(campañas.get(0).getUsuario(),campañaDAO.getOwner("c1"));
		assertNotEquals(campañas.get(0).getUsuario(),campañaDAO.getOwner("c2"));
		assertEquals(campañas.get(1).getUsuario(),campañaDAO.getOwner("c2"));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void getCampañasDeException() {
		agregarDatosDePrueba(instanciaCampañas());
		campañaDAO.getCampañasDe(new Usuario("UnUsuarioInexistente", null, null, null, null));		
	}
	
	@Test
	public void getCampañasDe() {
		ArrayList<Campania> campañas = instanciaCampañas();
		agregarDatosDePrueba(campañas);
		Collection<Campania> campañasDePepe = campañaDAO.getCampañasDe(campañas.get(0).getUsuario());
		assertEquals(3,campañasDePepe.size());
		assertTrue(campañasDePepe.contains(campañas.get(0)));
		assertFalse(campañasDePepe.contains(campañas.get(1)));
		assertTrue(campañasDePepe.contains(campañas.get(2)));
		
		Collection<Campania> campañasDePepe2 = campañaDAO.getCampañasDe(campañas.get(1).getUsuario());
		assertEquals(2,campañasDePepe2.size());
		assertFalse(campañasDePepe2.contains(campañas.get(0)));
		assertTrue(campañasDePepe2.contains(campañas.get(1)));
		assertFalse(campañasDePepe2.contains(campañas.get(2)));
		assertTrue(campañasDePepe2.contains(campañas.get(3)));
	}
	
	@Test
	public void getCampañasVigentes() {
		ArrayList<Campania> campañas = instanciaCampañasVigentes();
		agregarDatosDePrueba(campañas);
		Collection<Campania> campañasVigentes = campañaDAO.getCampañasVigentes();
		assertEquals(3, campañasVigentes.size());
		assertFalse(campañasVigentes.contains(campañas.get(2)));
		assertFalse(campañasVigentes.contains(campañas.get(4)));
		assertTrue(campañasVigentes.contains(campañas.get(0)));
		assertTrue(campañasVigentes.contains(campañas.get(1)));
		assertTrue(campañasVigentes.contains(campañas.get(3)));
		System.out.println(Campania.convertirDateAString(new Date()));
	}


	private void agregarDatosDePrueba(ArrayList<Campania> instancia) {
		instancia.forEach(t -> campañaDAO.guardar(t));
	}

	private ArrayList<Campania> instanciaCampañas() {		
		Usuario pepe = new Usuario("pepe", "unUsuario", "usuario@asd.com", "1234", Usuario.TipoUsuario.CLIENTE);
		Usuario pepe2 = new Usuario("pepe2", "unUsuario2", "usuario2@asd.com", "1234", Usuario.TipoUsuario.CLIENTE);

		ArrayList<Campania> ret = new ArrayList<>();
		ret.add(new Campania(pepe, "c1", "d1"));
		ret.add(new Campania(pepe2, "c2", "d2"));
		ret.add(new Campania(pepe, "c3", "d3"));
		ret.add(new Campania(pepe2, "c4", "d4"));
		ret.add(new Campania(pepe, "c5", "d5"));
		return ret;
	}
	
	private ArrayList<Campania> instanciaCampañasVigentes() {		
		Usuario pepe = new Usuario("pepe", "unUsuario", "usuario@asd.com", "1234", Usuario.TipoUsuario.CLIENTE);
		Usuario pepe2 = new Usuario("pepe2", "unUsuario2", "usuario2@asd.com", "1234", Usuario.TipoUsuario.CLIENTE);

		Campania c1 = new Campania(pepe, "c1", "d1");
		c1.setEstado(Campania.EstadoCampania.PLANIFICADA);
		Campania c2 = new Campania(pepe, "c2", "d2");
		c2.setEstado(Campania.EstadoCampania.PLANIFICADA);
		Campania c3 = new Campania(pepe, "c3", "d3");
		c3.setEstado(Campania.EstadoCampania.CANCELADA);
		Campania c4 = new Campania(pepe2, "c4", "d4");
		c4.setEstado(Campania.EstadoCampania.PLANIFICADA);
		Campania c5 = new Campania(pepe2, "c5", "d5");
		c5.setEstado(Campania.EstadoCampania.PRELIMINAR);
		
		ArrayList<Campania> ret = new ArrayList<>();
		ret.add(c1);
		ret.add(c2);
		ret.add(c3);
		ret.add(c4);
		ret.add(c5);
		
		return ret;
	}

}
