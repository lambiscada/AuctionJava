package es.udc.is.isg019.subastador.model.puja;

import java.util.List;

import es.udc.is.isg019.subastador.model.util.exceptions.PujaByIdUsuarioOrderByFechaVencimientoNotFoundException;
import es.udc.pojo.modelutil.dao.GenericDao;

public interface PujaDao extends GenericDao<Puja, Long> {

	public List<Puja> findPujasByIdUsuarioOrderByFechaVencimiento(Long idUsuario, int startIndex,
			int count) throws PujaByIdUsuarioOrderByFechaVencimientoNotFoundException;

}
