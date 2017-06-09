package com.EyVdeSW.TP.Daos.impl;

import java.util.Collection;

import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.core.query.nq.SimpleNativeQuery;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

import com.EyVdeSW.TP.Daos.CampañaDAO;
import com.EyVdeSW.TP.Daos.UsuarioDAO;
import com.EyVdeSW.TP.domainModel.Campania;
import com.EyVdeSW.TP.domainModel.Usuario;

public class CampañaDAONeodatis extends DAONeodatis<Campania> implements CampañaDAO {

	@Override
	public boolean existe(String nombreCampaña) {
		boolean ret = false;
		Objects<Campania> resultadoQuery = null;
		odb = null;
		try {
			odb = bdConnector.getBDConnection();
			resultadoQuery = odb.getObjects(new CriteriaQuery(Campania.class, Where.equal("nombre", nombreCampaña)));
			ret = resultadoQuery.size() != 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (odb != null)
				odb.close();
		}
		return ret;

	}

	@Override
	public Collection<Campania> traerTodos() {
		return consultar(new CriteriaQuery(Campania.class));
	}

	@Override
	public Collection<Campania> consultarPorNombre(String nombre) {
		IQuery query = new CriteriaQuery(Campania.class, Where.like("nombre", "%" + nombre + "%"));
		return consultar(query);
	}

	@Override
	public Campania getCampañaPorNombre(String nombreCampaña) {
		Campania campaña = null;
		Objects<Campania> resultadoQuery = null;
		odb = null;
		try {
			odb = bdConnector.getBDConnection();
			resultadoQuery = odb.getObjects(new CriteriaQuery(Campania.class, Where.equal("nombre", nombreCampaña)));
			if (resultadoQuery.size() != 0)
				campaña = resultadoQuery.getFirst();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (odb != null)
				odb.close();
		}
		return campaña;
	}

	@Override
	public void modificar(Campania original, Campania modificacion) {
		odb = null;
		try {
			odb = bdConnector.getBDConnection();
			IQuery query = new CriteriaQuery(Campania.class, Where.like("nombre", original.getNombre()));
			Objects<Campania> resultadoQuery = odb.getObjects(query);

			Campania t = resultadoQuery.getFirst();
			t.setMensaje(modificacion.getMensaje());
			t.setAccionesPublicitarias(modificacion.getAccionesPublicitarias());
			t.setNombre(modificacion.getNombre());
			t.setDescripcion(modificacion.getDescripcion());
			t.setFechaDeInicio(modificacion.getFechaDeInicio());

			odb.store(t);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (odb != null)
				odb.close();
		}
	}

	@Override
	public Usuario getOwner(String nombreCampaña) {
		Usuario ret = null;
		if (existe(nombreCampaña)) {
			ret = consultar(new CriteriaQuery(Campania.class, Where.equal("nombre", nombreCampaña))).getFirst()
					.getUsuario();
		} else {
			throw new IllegalArgumentException("No existe una campaña con el nombre: " + nombreCampaña);
		}
		return ret;
	}

	@Override
	public Collection<Campania> getCampañasDe(Usuario user) {
		Collection<Campania> ret = null;
		UsuarioDAO usuarioDAO = new UsuarioDAONeodatis();
		((DAONeodatis<Usuario>) usuarioDAO).setBdConnector(bdConnector);

		if (usuarioDAO.existeUsuario(user.getNombreUsuario())) {
			ret = consultar(new SimpleNativeQuery() {
				public boolean match(Campania campaña) {
					return campaña.getUsuario().equals(user);
				}
			});
		} else {
			throw new IllegalArgumentException("No existe el usuario de nombre: " + user.getNombreUsuario());
		}
		return ret;
	}

}
