package es.udc.is.isg019.subastador.model.producto;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("productoDao")
public class ProductoDaoHibernate extends GenericDaoHibernate<Producto, Long>
		implements ProductoDao {

	@SuppressWarnings("unchecked")
	public List<Producto> findProducto(List<String> palabras, Long idCategoria,
			int startIndex, int count) {
	
		String consulta = "SELECT p FROM Producto p";
		Iterator<String> palabrasIter = palabras.iterator();
		boolean palabraVacia = true;

		if (palabrasIter.hasNext()) {
			consulta = consulta + " WHERE p.nombre";
			palabraVacia = false;
		}

		while (palabrasIter.hasNext()) {
			consulta = consulta + " LIKE '%" + palabrasIter.next()
					+ "%' AND p.nombre";
		}

		if (!palabraVacia)
			consulta = consulta.substring(0, consulta.length() - 13);

		if (palabraVacia)
			consulta = consulta + " WHERE";
		else
			consulta = consulta + " AND";
		consulta = consulta + " p.fechaVencimiento >= :fechaActual";

		if (idCategoria != null) {
			consulta = consulta
					+ " AND p.categoria.idCategoria = :categoria ORDER BY p.nombre";
			return getSession().createQuery(consulta)
					.setParameter("fechaActual", Calendar.getInstance())
					.setParameter("categoria", idCategoria)
					.setFirstResult(startIndex).setMaxResults(count).list();

		} else {
			consulta = consulta + " ORDER BY p.nombre";
			return getSession().createQuery(consulta)
					.setParameter("fechaActual", Calendar.getInstance())
					.setFirstResult(startIndex).setMaxResults(count).list();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Producto> findProductosByIdUsuario(Long idUsuario,
			int startIndex, int count) {
		return getSession()
				.createQuery(
						"SELECT p FROM Producto p WHERE p.usuario.idUsuario = :idUsuario "
								+ "ORDER BY p.idProducto DESC")
				.setParameter("idUsuario", idUsuario)
				.setFirstResult(startIndex).setMaxResults(count).list();
	}


}