package es.udc.is.isg019.subastador.model.producto;



import java.util.List;
import es.udc.pojo.modelutil.dao.GenericDao;

public interface ProductoDao extends GenericDao<Producto, Long> {    
        
    /**
     * Returns a list of productos pertaining to a given user. If the user has
     * no products, an empty list is returned.
     *
     * @param palabras list of search words
     * @param idCategoria the category identifier
     * @param startIndex the index (starting from 0) of the first account to 
     *        return
     * @param count the maximum number of accounts to return
     * @return the list of products
     */
    public List<Producto> findProducto(List<String> palabras,Long idCategoria, int startIndex,
        	int count);
    /**
     * Returns a list of products pertaining to a given user. If the user has
     * no products, an empty list is returned.
     * @param idUsuario the user identifier
     * @param startIndex the index (starting from 0) of the first account to 
     *        return
     * @param count the maximum number of accounts to return
     * @return the list of products
     */
    public List<Producto> findProductosByIdUsuario(Long idUsuario, int startIndex,
        	int count);
   

}