package es.udc.is.isg019.subastador.model.usuario;

import es.udc.pojo.modelutil.dao.GenericDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface UsuarioDao extends GenericDao<Usuario, Long>{

    /**
     * Returns an Usuario by login (not user identifier)
     *
     * @param login the user identifier
     * @return the Usuario
     */
    public Usuario findByLogin(String login) throws InstanceNotFoundException;
}
