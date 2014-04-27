package es.udc.is.isg019.subastador.model.auctionservice;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.is.isg019.subastador.model.categoria.Categoria;
import es.udc.is.isg019.subastador.model.categoria.CategoriaDao;
import es.udc.is.isg019.subastador.model.producto.Producto;
import es.udc.is.isg019.subastador.model.producto.ProductoDao;
import es.udc.is.isg019.subastador.model.puja.Puja;
import es.udc.is.isg019.subastador.model.puja.PujaDao;
import es.udc.is.isg019.subastador.model.usuario.Usuario;
import es.udc.is.isg019.subastador.model.usuario.UsuarioDao;
import es.udc.is.isg019.subastador.model.util.exceptions.PujaByIdUsuarioOrderByFechaVencimientoNotFoundException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("auctionService")
@Transactional
public class AuctionServiceImpl implements AuctionService {

	@Autowired
	private ProductoDao productoDao;

	@Autowired
	private UsuarioDao usuarioDao;

	@Autowired
	private PujaDao pujaDao;

	@Autowired
	private CategoriaDao categoriaDao;

	public Producto anunciarProducto(String nombre, String descripcion,
			String infoEnvio, double precioSalida, Calendar fechaAnuncio,
			int minutos, Long idUsuario, Long idCategoria)
			throws InstanceNotFoundException, UserNotSellerException {
		Usuario usuario = usuarioDao.find(idUsuario);
		if (usuario.getTarjetaBanco() == null)
			throw new UserNotSellerException(usuario.getLogin());

		Categoria categoria = categoriaDao.find(idCategoria);
		Calendar fechaVencimiento = Calendar.getInstance();
		fechaVencimiento.add(Calendar.MINUTE, minutos);

		Producto producto = new Producto(nombre, descripcion, infoEnvio,
				precioSalida, precioSalida, fechaVencimiento, fechaAnuncio, usuario, categoria);
		productoDao.save(producto);
		return producto;
	}

	@Transactional(readOnly = true)
	public List<Categoria> getCategorias() {
		return categoriaDao.findCategorias();
	}

	@Transactional(readOnly = true)
	public ProductoBlock findProductos(String cadenaBusqueda, Long idCategoria,
			int startIndex, int count) {
		List<Producto> productosEncontrados = null;
		List<String> palabras = new LinkedList<String>();
		StringTokenizer st = new StringTokenizer(cadenaBusqueda.replace("'", " "));
		while (st.hasMoreTokens()) {
			palabras.add(st.nextToken());
		}
		productosEncontrados = productoDao.findProducto(palabras, idCategoria,
				startIndex, count + 1);

		boolean existMoreProductos = productosEncontrados.size() == (count + 1);
		if (existMoreProductos) {
			productosEncontrados.remove(productosEncontrados.size() - 1);
		}

		return new ProductoBlock(productosEncontrados, existMoreProductos);

	}

	public Puja pujar(double cantidadMaxima, Long idUsuario, Long idProducto)
			throws ExpiredProductDateException, InstanceNotFoundException,
			InsufficientCantidadMaximaException {
		Calendar fechaActual = Calendar.getInstance();
		Producto producto = productoDao.find(idProducto);
		Usuario usuario = usuarioDao.find(idUsuario);
		Puja pujaGanadora = productoDao.find(idProducto).getPujaGanadora();
		double precioActual = productoDao.find(idProducto).getPrecioSalida();
		double maximaPuja = precioActual;
		Puja puja = null;

		if (fechaActual.getTime().compareTo(
				producto.getFechaVencimiento().getTime()) > 0)
			throw new ExpiredProductDateException(producto.getNombre());

		if ((pujaGanadora != null)) {
			if (cantidadMaxima <= precioActual) {
				throw new InsufficientCantidadMaximaException();
			}
			maximaPuja = pujaGanadora.getCantidadMaxima();
			if ((cantidadMaxima >= precioActual)
					&& (cantidadMaxima < maximaPuja)) {
				puja = new Puja(cantidadMaxima, fechaActual, usuario, producto);
				pujaDao.save(puja);
			}

			if (maximaPuja > cantidadMaxima) {
				precioActual = cantidadMaxima + 0.5;
				producto.setPrecioActual(precioActual);

				return pujaGanadora;
			} else if (maximaPuja == cantidadMaxima) {
				precioActual = cantidadMaxima;
				producto.setPrecioActual(precioActual);

				return pujaGanadora;
			} else {
				precioActual = pujaGanadora.getCantidadMaxima() + 0.5;
				if (pujaGanadora.getUsuario().getIdUsuario() == usuario
						.getIdUsuario())
					pujaDao.remove(pujaGanadora.getIdPuja());

				puja = new Puja(cantidadMaxima, fechaActual, usuario, producto);
				pujaDao.save(puja);
				producto.setPujaGanadora(puja);
				producto.setPrecioActual(precioActual);

				return puja;
			}
		} else {
			if (cantidadMaxima >= maximaPuja) {
				puja = new Puja(cantidadMaxima, fechaActual, usuario, producto);
				pujaDao.save(puja);
				producto.setPrecioActual(producto.getPrecioSalida());
				producto.setPujaGanadora(puja);
				return puja;
			} else
				throw new InsufficientCantidadMaximaException();
		}
	}

	@Transactional(readOnly = true)
	public PujasBlock findPujasByIdUsuario(Long idUsuario, int startIndex,
			int count) {
		List<Puja> pujasEncontradas = null;
		try {
			pujasEncontradas = pujaDao
					.findPujasByIdUsuarioOrderByFechaVencimiento(idUsuario,
							startIndex, count + 1);
		} catch (PujaByIdUsuarioOrderByFechaVencimientoNotFoundException e) {
			e.printStackTrace();
		}
		boolean existMorePujas = pujasEncontradas.size() == (count + 1);
		if (existMorePujas) {
			pujasEncontradas.remove(pujasEncontradas.size() - 1);
		}

		return new PujasBlock(pujasEncontradas, existMorePujas);

	}

	@Transactional(readOnly = true)
	public ProductoBlock findProductosByIdUsuario(Long idUsuario,
			int startIndex, int count) {
		List<Producto> productosEncontrados = productoDao
				.findProductosByIdUsuario(idUsuario, startIndex, count + 1);
		boolean existMoreProductos = productosEncontrados.size() == (count + 1);
		if (existMoreProductos) {
			productosEncontrados.remove(productosEncontrados.size() - 1);
		}

		return new ProductoBlock(productosEncontrados, existMoreProductos);
	}

	@Transactional(readOnly = true)
	public Producto findProductoByIdProducto(Long idProducto)
			throws InstanceNotFoundException {
		return productoDao.find(idProducto);
	}

}
