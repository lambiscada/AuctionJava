package es.udc.is.isg019.subastador.model.categoria;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.pojo.modelutil.dao.GenericDaoHibernate;

@Repository("CategoriaDao")
public class CategoriaDaoHibernate extends GenericDaoHibernate<Categoria, Long>
		implements CategoriaDao {

	@SuppressWarnings("unchecked")
	public List<Categoria> findCategorias() {
		return getSession().createQuery("SELECT c FROM Categoria c").list();

	}

}