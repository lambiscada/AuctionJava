package es.udc.is.isg019.subastador.model.puja;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("pujaDao")
public class PujaDaoHibernate extends GenericDaoHibernate<Puja, Long> implements
		PujaDao {

	@SuppressWarnings("unchecked")
	public List<Puja> findPujasByIdUsuarioOrderByFechaVencimiento(Long idUsuario, int startIndex,
			int count) {
		return getSession()
				.createQuery("SELECT (p) FROM Puja p WHERE p.idPuja IN (SELECT MAX(idPuja) FROM Puja pu WHERE pu.usuario.idUsuario = :idUsuario GROUP BY pu.producto.idProducto) ORDER BY p.producto.fechaVencimiento DESC")
				.setParameter("idUsuario", idUsuario)
				.setFirstResult(startIndex).setMaxResults(count).list();
	}
}