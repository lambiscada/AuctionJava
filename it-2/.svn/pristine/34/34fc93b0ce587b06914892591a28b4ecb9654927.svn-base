package es.udc.is.isg019.subastador.model.auctionservice;

import java.util.Calendar;
import java.util.List;

import es.udc.is.isg019.subastador.model.categoria.Categoria;
import es.udc.is.isg019.subastador.model.producto.Producto;
import es.udc.is.isg019.subastador.model.puja.Puja;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface AuctionService {
	public Producto anunciarProducto(String nombre, String descripcion,
			String infoEnvio, double precioSalida, Calendar fechaAnuncio,
			int minutos, Long idUsuario, Long idCategoria) throws InstanceNotFoundException, UserNotSellerException;

	public List<Categoria> getCategorias();

	public ProductoBlock findProductos(String cadenaBusqueda, Long idCategoria,
			int startIndex, int count);

	public Producto findProductoByIdProducto(Long idProducto) throws InstanceNotFoundException;
		
	public Puja pujar(double cantidadMaxima,Long idUsuario,
			Long idProducto) throws ExpiredProductDateException, InstanceNotFoundException, InsufficientCantidadMaximaException;

	public PujasBlock findPujasByIdUsuario(Long idUsuario, int startIndex,
			int count);

	public ProductoBlock findProductosByIdUsuario(Long idUsuario,
			int startIndex, int count);
}
