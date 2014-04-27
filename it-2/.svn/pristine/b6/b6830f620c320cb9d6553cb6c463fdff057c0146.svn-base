package es.udc.is.isg019.subastador.model.usuario;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Repository("usuarioDao")
public class UsuarioDaoHibernate extends
		GenericDaoHibernate<Usuario, Long> implements UsuarioDao {

	public Usuario findByLogin(String login) throws InstanceNotFoundException {

    	Usuario usuario = (Usuario) getSession().createQuery(
    			"SELECT u FROM Usuario u WHERE u.login = :login")
    			.setParameter("login", login)
    			.uniqueResult();
    	if (usuario == null) {
   			throw new InstanceNotFoundException(login, Usuario.class.getName());
    	} else {
    		return usuario;
    	}

	}

}