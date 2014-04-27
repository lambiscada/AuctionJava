package es.udc.is.isg019.subastador.test.model.auctionservice;

import static es.udc.is.isg019.subastador.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.is.isg019.subastador.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.is.isg019.subastador.model.auctionservice.AuctionService;
import es.udc.is.isg019.subastador.model.auctionservice.ExpiredProductDateException;
import es.udc.is.isg019.subastador.model.auctionservice.InsufficientCantidadMaximaException;
import es.udc.is.isg019.subastador.model.auctionservice.ProductoBlock;
import es.udc.is.isg019.subastador.model.auctionservice.PujasBlock;
import es.udc.is.isg019.subastador.model.auctionservice.UserNotSellerException;
import es.udc.is.isg019.subastador.model.categoria.Categoria;
import es.udc.is.isg019.subastador.model.categoria.CategoriaDao;
import es.udc.is.isg019.subastador.model.producto.Producto;
import es.udc.is.isg019.subastador.model.puja.Puja;
import es.udc.is.isg019.subastador.model.puja.PujaDao;
import es.udc.is.isg019.subastador.model.tarjetabanco.TarjetaBanco;
import es.udc.is.isg019.subastador.model.tarjetabanco.TarjetaBancoDao;
import es.udc.is.isg019.subastador.model.usuario.Usuario;
import es.udc.is.isg019.subastador.model.usuario.UsuarioDao;
import es.udc.is.isg019.subastador.model.util.exceptions.PujaByIdUsuarioOrderByFechaVencimientoNotFoundException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class AuctionServiceTest {

	private final long NON_EXISTENT_USUARIO_ID = -1;
	private final long NON_EXISTENT_PRODUCTO_ID = -1;
	private final long NON_EXISTENT_PUJA_ID = -1;
	private final long NON_EXISTENT_CATEGORIA_ID = -1;

	@Autowired
	private AuctionService auctionService;

	@Autowired
	private UsuarioDao usuarioDao;

	@Autowired
	private CategoriaDao categoriaDao;

	@Autowired
	private TarjetaBancoDao tarjetaBancoDao;

	@Autowired
	private PujaDao pujaDao;

	@Test
	public void testFindProductoByIdProducto() throws InstanceNotFoundException {
		Usuario usuarioVendedor = createUsuarioVendedor("login");
		Producto producto = createProducto(usuarioVendedor);
		Producto producto2 = auctionService.findProductoByIdProducto(producto
				.getIdProducto());
		assertEquals(producto, producto2);

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testFindProductoWithNonExistentIdProducto()
			throws InstanceNotFoundException {
		auctionService.findProductoByIdProducto(NON_EXISTENT_PRODUCTO_ID);

	}

	@Test
	public void testFindPujaByIdPuja() throws InstanceNotFoundException {
		Usuario usuarioVendedor = createUsuarioVendedor("login");
		Puja puja = createPuja(100, createProducto(usuarioVendedor),
				usuarioVendedor);
		Puja puja2 = auctionService.findPujasByIdUsuario(usuarioVendedor.getIdUsuario(), 0, 1).getPujas().get(0);
		assertEquals(puja, puja2);

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testFindPujaWithNonExistentIdPuja()
			throws InstanceNotFoundException, PujaByIdUsuarioOrderByFechaVencimientoNotFoundException {
		pujaDao.remove(NON_EXISTENT_PUJA_ID);	
	}

	@Test
	public void testFindCategorias() {
		List<Categoria> categorias = createCategorias();
		List<Categoria> categoria2 = auctionService.getCategorias();
		assertEquals(categorias, categoria2);
	}

	@Test
	public void testFindProductosByIdUsuarioAndExistMoreProducts() {
		Usuario usuarioVendedor = createUsuarioVendedor("login");
		createProducto(usuarioVendedor);
		createProducto(usuarioVendedor);
		createProducto(usuarioVendedor);
		ProductoBlock productoBlock = auctionService.findProductosByIdUsuario(
				usuarioVendedor.getIdUsuario(), 0, 2);
		assertTrue(productoBlock.getExistMoreProductos());
		assertTrue(productoBlock.getProductos().size() == 2);
	}

	@Test
	public void testFindProductosByIdUsuarioAndNotExistMoreProducts() {
		Usuario usuarioVendedor = createUsuarioVendedor("login");
		createProducto(usuarioVendedor);
		createProducto(usuarioVendedor);
		createProducto(usuarioVendedor);
		ProductoBlock productoBlock = auctionService.findProductosByIdUsuario(
				usuarioVendedor.getIdUsuario(), 0, 3);
		assertFalse(productoBlock.getExistMoreProductos());
		assertTrue(productoBlock.getProductos().size() == 3);
	}

	@Test
	public void testFindProductosByIdUsuarioWithNonExistentIdUsuario() {
		ProductoBlock productoBlock = auctionService.findProductosByIdUsuario(
				NON_EXISTENT_USUARIO_ID, 0, 3);
		assertFalse(productoBlock.getExistMoreProductos());
		assertTrue(productoBlock.getProductos().size() == 0);

	}

	@Test
	public void testFindProductosByIdUsuarioAndNotFromOtherUser() {
		Usuario usuarioVendedor1 = createUsuarioVendedor("login1");
		Usuario usuarioVendedor2 = createUsuarioVendedor("login2");
		Producto producto1 = createProducto(usuarioVendedor1);
		Producto producto2 = createProducto(usuarioVendedor1);
		Producto producto3 = createProducto(usuarioVendedor2);
		ProductoBlock productoBlock = auctionService.findProductosByIdUsuario(
				usuarioVendedor1.getIdUsuario(), 0, 3);
		assertFalse(productoBlock.getExistMoreProductos());
		assertTrue(productoBlock.getProductos().size() == 2);
		assertTrue(productoBlock.getProductos().contains(producto1));
		assertTrue(productoBlock.getProductos().contains(producto2));
		assertFalse(productoBlock.getProductos().contains(producto3));
	}

	@Test
	public void testFindPujasByIdUsuarioAndExistMorePujas() {
		Usuario usuario = createUsuario("login");
		Usuario usuarioVendedor = createUsuarioVendedor("loginVendedor");
		Producto producto1 = createProducto(usuarioVendedor);
		Producto producto2 = createProducto(usuarioVendedor);
		createPuja(100, producto1, usuario);
		createPuja(90, producto2, usuario);
		PujasBlock pujaBlock = auctionService.findPujasByIdUsuario(
				usuario.getIdUsuario(), 0, 1);
		assertTrue(pujaBlock.getExistMorePujas());
		assertTrue(pujaBlock.getPujas().size() == 1);
	}

	@Test
	public void testFindPujasByIdUsuarioAndNotExistMorePujas() {
		Usuario usuario = createUsuario("login");
		Usuario usuarioVendedor = createUsuarioVendedor("loginVendedor");
		Producto producto1 = createProducto(usuarioVendedor);
		Producto producto2 = createProducto(usuarioVendedor);
		createPuja(100, producto1, usuario);
		createPuja(90, producto2, usuario);
		PujasBlock pujaBlock = auctionService.findPujasByIdUsuario(
				usuario.getIdUsuario(), 0, 2);
		assertFalse(pujaBlock.getExistMorePujas());
		assertTrue(pujaBlock.getPujas().size() == 2);
	}

	@Test
	public void testFindPujasByIdUsuarioWithNonExistentIdUsuario() {
		PujasBlock pujasBlock = auctionService.findPujasByIdUsuario(
				NON_EXISTENT_USUARIO_ID, 0, 3);
		assertFalse(pujasBlock.getExistMorePujas());
		assertTrue(pujasBlock.getPujas().size() == 0);

	}

	@Test
	public void testFindPujasByIdUsuarioVencidas() {
		Usuario usuario = createUsuario("login");
		Usuario usuarioVendedor = createUsuarioVendedor("loginVendedor");
		Producto producto1 = createProducto(usuarioVendedor);
		createPujaVencida(100, producto1, usuario);
		PujasBlock pujaBlock = auctionService.findPujasByIdUsuario(
				usuario.getIdUsuario(), 0, 2);
		assertFalse(pujaBlock.getExistMorePujas());
		assertTrue(pujaBlock.getPujas().size() == 0);
	}

	@Test
	public void testFindPujasByIdUsuarioAndNotFromOther() {
		Usuario usuario1 = createUsuario("login1");
		Usuario usuario2 = createUsuario("login2");
		Usuario usuarioVendedor = createUsuarioVendedor("loginVendedor");
		Producto producto1 = createProducto(usuarioVendedor);
		Puja puja1 = createPuja(90, producto1, usuario1);
		Puja puja2 = createPuja(100, producto1, usuario2);
		PujasBlock pujaBlock = auctionService.findPujasByIdUsuario(
				usuario1.getIdUsuario(), 0, 1);
		assertFalse(pujaBlock.getExistMorePujas());
		assertTrue(pujaBlock.getPujas().size() == 1);
		assertTrue(pujaBlock.getPujas().contains(puja1));
		assertFalse(pujaBlock.getPujas().contains(puja2));

	}

	@Test
	public void testAnunciarProducto() throws InstanceNotFoundException,
			UserNotSellerException {
		Usuario usuarioVendedor = createUsuarioVendedor("login");
		Categoria categoria = createCategoria("Joyas");
		Producto producto1 = auctionService.anunciarProducto("nombre",
				"descripcion", "infoEnvio", 25, Calendar.getInstance(), 10,
				usuarioVendedor.getIdUsuario(), categoria.getIdCategoria());
		Producto producto2 = auctionService.findProductoByIdProducto(producto1
				.getIdProducto());
		assertEquals(producto1, producto2);
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testAnunciarProductoWithNonExistentIdUsuario()
			throws InstanceNotFoundException, UserNotSellerException {
		Categoria categoria = createCategoria("Joyas");
		auctionService.anunciarProducto("nombre", "descripcion", "infoEnvio",
				25, Calendar.getInstance(), 10, NON_EXISTENT_USUARIO_ID,
				categoria.getIdCategoria());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testAnunciarProductoWithNonExistentIdCategoria()
			throws InstanceNotFoundException, UserNotSellerException {
		Usuario usuarioVendedor = createUsuarioVendedor("login");
		auctionService.anunciarProducto("nombre", "descripcion", "infoEnvio",
				25, Calendar.getInstance(), 10, usuarioVendedor.getIdUsuario(),
				NON_EXISTENT_CATEGORIA_ID);
	}

	@Test(expected = UserNotSellerException.class)
	public void testAnunciarProductoButUserNotSeller()
			throws InstanceNotFoundException, UserNotSellerException {
		Usuario usuarioNotVendedor = createUsuario("login");
		Categoria categoria = createCategoria("Joyas");
		auctionService.anunciarProducto("nombre", "descripcion", "infoEnvio",
				25, Calendar.getInstance(), 10,
				usuarioNotVendedor.getIdUsuario(), categoria.getIdCategoria());

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testPujaWithNotCorrectIdUsuario()
			throws InstanceNotFoundException, ExpiredProductDateException,
			InsufficientCantidadMaximaException {
		Usuario usuarioVendedor = createUsuarioVendedor("login");
		Producto producto = createProducto(usuarioVendedor);
		auctionService.pujar(100, NON_EXISTENT_USUARIO_ID,
				producto.getIdProducto());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testPujaWithNotCorrectIdProducto()
			throws InstanceNotFoundException, ExpiredProductDateException,
			InsufficientCantidadMaximaException {
		Usuario usuarioVendedor = createUsuarioVendedor("login");
		auctionService.pujar(100, usuarioVendedor.getIdUsuario(),
				NON_EXISTENT_PRODUCTO_ID);
	}

	@Test(expected = ExpiredProductDateException.class)
	public void testPujaWithIncorrectFechaVencimiento()
			throws InstanceNotFoundException, ExpiredProductDateException,
			InsufficientCantidadMaximaException {
		Usuario usuarioVendedor = createUsuarioVendedor("login");
		Producto producto = createProducto(usuarioVendedor);
		Calendar fechaVencimiento = producto.getFechaVencimiento();
		fechaVencimiento.set(2001, 12, 2);
		producto.setFechaVencimiento(fechaVencimiento);
		auctionService.pujar(100, usuarioVendedor.getIdUsuario(),
				producto.getIdProducto());
	}

	@Test(expected = InsufficientCantidadMaximaException.class)
	public void testPujaWithInsufficientCantidadMaxima()
			throws InstanceNotFoundException, ExpiredProductDateException,
			InsufficientCantidadMaximaException {
		Usuario usuarioVendedor = createUsuarioVendedor("login");
		Producto producto = createProducto(usuarioVendedor);
		auctionService.pujar(40, usuarioVendedor.getIdUsuario(),
				producto.getIdProducto());
	}

	@Test
	public void testPujaTwoTimesAndCheckExistsOnlyOne()
			throws InstanceNotFoundException, ExpiredProductDateException,
			InsufficientCantidadMaximaException {
		Usuario usuarioVendedor = createUsuarioVendedor("login");
		Producto producto = createProducto(usuarioVendedor);
		auctionService.pujar(100, usuarioVendedor.getIdUsuario(),
				producto.getIdProducto());
		Puja puja2 = auctionService.pujar(110, usuarioVendedor.getIdUsuario(),
				producto.getIdProducto());
		
		PujasBlock pujasBlock = auctionService.findPujasByIdUsuario(
				usuarioVendedor.getIdUsuario(), 0, 2);
		assertTrue(pujasBlock.getPujas().size() == 1);
		assertTrue(pujasBlock.getPujas().contains(puja2));
	}

	@Test
	public void testPujaEjemplo1() throws InstanceNotFoundException,
			ExpiredProductDateException, InsufficientCantidadMaximaException {
		Usuario usuarioVendedor = createUsuarioVendedor("login");
		Producto producto = createProducto(usuarioVendedor);
		Usuario usuario = createUsuario("login1");

		Puja puja = auctionService.pujar(52, usuario.getIdUsuario(),
				producto.getIdProducto());
		//PujaDetails pujaDetails = auctionService
			//	.showPujaAndPrecioActual(producto.getIdProducto());
		assertEquals(puja, producto.getPujaGanadora());
		assertEquals(50, producto.getPrecioActual(), 0.1);
	}

	@Test
	public void testPujaEjemplo2() throws InstanceNotFoundException,
			ExpiredProductDateException, InsufficientCantidadMaximaException {
		Usuario usuarioVendedor = createUsuarioVendedor("login");
		Producto producto = createProducto(usuarioVendedor);
		Usuario compradorA = createUsuario("compradorA");
		Usuario compradorB = createUsuario("compradorB");

		Puja puja1 = auctionService.pujar(52, compradorA.getIdUsuario(),
				producto.getIdProducto());
		auctionService.pujar(51, compradorB.getIdUsuario(),
				producto.getIdProducto());
		//PujaDetails pujaDetails = auctionService
			//	.showPujaAndPrecioActual(producto.getIdProducto());
		//assertEquals(puja1, pujaDetails.getPujaGanadora());
		//assertEquals(51.5, pujaDetails.getPrecioActual(), 0.1);
		assertEquals(puja1, producto.getPujaGanadora());
		assertEquals(51.5, producto.getPrecioActual(), 0.1);
	}

	@Test
	public void testPujaEjemplo3() throws InstanceNotFoundException,
			ExpiredProductDateException, InsufficientCantidadMaximaException {
		Usuario usuarioVendedor = createUsuarioVendedor("login");
		Producto producto = createProducto(usuarioVendedor);
		Usuario compradorA = createUsuario("compradorA");
		Usuario compradorB = createUsuario("compradorB");

		auctionService.pujar(52, compradorA.getIdUsuario(),
				producto.getIdProducto());
		Puja puja2 = auctionService.pujar(54, compradorB.getIdUsuario(),
				producto.getIdProducto());
		/*PujaDetails pujaDetails = auctionService
				.showPujaAndPrecioActual(producto.getIdProducto());
		assertEquals(puja2, pujaDetails.getPujaGanadora());
		assertEquals(52.5, pujaDetails.getPrecioActual(), 0.1);*/
		assertEquals(puja2, producto.getPujaGanadora());
		assertEquals(52.5, producto.getPrecioActual(), 0.1);
	}

	@Test
	public void testPujaTwoPujasWithSameAmountFirstWins()
			throws InstanceNotFoundException, ExpiredProductDateException,
			InsufficientCantidadMaximaException {
		Usuario usuarioVendedor = createUsuarioVendedor("login");
		Producto producto = createProducto(usuarioVendedor);
		Usuario compradorA = createUsuario("compradorA");
		Usuario compradorB = createUsuario("compradorB");

		Puja puja1 = auctionService.pujar(52, compradorA.getIdUsuario(),
				producto.getIdProducto());
		auctionService.pujar(52, compradorB.getIdUsuario(),
				producto.getIdProducto());
		/*PujaDetails pujaDetails = auctionService
				.showPujaAndPrecioActual(producto.getIdProducto());
		assertEquals(puja1, pujaDetails.getPujaGanadora());
		assertEquals(52, pujaDetails.getPrecioActual(), 0.1);*/
		assertEquals(puja1, producto.getPujaGanadora());
		assertEquals(52, producto.getPrecioActual(), 0.1);
	}

	@Test
	public void testFindProductosUnaPalabra() {
		List<Producto> productos = createProductos();
		ProductoBlock productoBlock = auctionService.findProductos("rendición",
				null, 0, 3);
		
		assertTrue(productoBlock.getProductos().contains(productos.get(1)));
		
	}

	@Test
	public void testFindProductosDosPalabra() {
		List<Producto> productos = createProductos();
		ProductoBlock productoBlock = auctionService.findProductos("ver de",
				null, 0, 3);
		assertTrue(productoBlock.getProductos().contains(productos.get(5)));
		assertTrue(productoBlock.getProductos().contains(productos.get(7)));
	}

	@Test
	public void testFindProductosTrozo() {
		List<Producto> productos = createProductos();
		ProductoBlock productoBlock = auctionService.findProductos("frag",
				null, 0, 3);
		assertTrue(productoBlock.getProductos().contains(productos.get(2)));
	}

	@Test
	public void testFindProductosMayusculas() {
		List<Producto> productos = createProductos();
		ProductoBlock productoBlock = auctionService.findProductos("SILL",
				null, 0, 3);
		assertTrue(productoBlock.getProductos().contains(productos.get(6)));
	}

	@Test
	public void testFindProductosMayusculasAndMinusculas() {
		List<Producto> productos = createProductos();
		ProductoBlock productoBlock = auctionService.findProductos("ReLoJeS",
				null, 0, 3);
		assertTrue(productoBlock.getProductos().contains(productos.get(0)));
	}



	@Test
	public void testFindProductosCadenaVaciaAndCategoria() {
		List<Producto> productos = createProductos();
		Categoria categoriaCuadros = productos.get(0).getCategoria();
		ProductoBlock productoBlock = auctionService.findProductos("",
				categoriaCuadros.getIdCategoria(), 0, 9);
		assertTrue(productoBlock.getProductos().contains(productos.get(0)));
		assertTrue(productoBlock.getProductos().contains(productos.get(1)));
		assertTrue(productoBlock.getProductos().contains(productos.get(2)));
		assertTrue(productoBlock.getProductos().contains(productos.get(3)));

	}

	@Test
	public void testFindProductosMismoNombreSinCategoria() {
		List<Producto> productos = createProductos();
		ProductoBlock productoBlock = auctionService.findProductos("mona lisa",
				null, 0, 9);
		assertTrue(productoBlock.getProductos().contains(productos.get(3)));
		assertTrue(productoBlock.getProductos().contains(productos.get(4)));

	}

	@Test
	public void testFindProductosMismoNombreConCategoria() {
		List<Producto> productos = createProductos();
		Categoria categoriaCuadros = productos.get(0).getCategoria();
		ProductoBlock productoBlock = auctionService.findProductos("mona lisa",
				categoriaCuadros.getIdCategoria(), 0, 9);
		assertTrue(productoBlock.getProductos().contains(productos.get(3)));
		assertFalse(productoBlock.getProductos().contains(productos.get(4)));
	}

	/*
	 * Métodos privados para la creación de Productos , Usuario, UsuarioVendedor
	 * , Categorias
	 */

	private Usuario createUsuario(String login) {
		Usuario usuario = new Usuario(login, "password", "nombre", "apellidos",
				" email");
		usuarioDao.save(usuario);
		return usuario;
	}

	private Usuario createUsuarioVendedor(String login) {
		Usuario usuario = createUsuario(login);
		TarjetaBanco tarjetaBanco = new TarjetaBanco(new Long(
				"1111222233334444"), Calendar.getInstance());
		tarjetaBancoDao.save(tarjetaBanco);
		usuario.setTarjetaBanco(tarjetaBanco);
		return usuario;
	}

	private Categoria createCategoria(String nombre) {
		Categoria categoria = new Categoria();
		categoria.setNombre(nombre);
		categoriaDao.save(categoria);
		return categoria;
	}

	private List<Categoria> createCategorias() {
		List<Categoria> categorias = new ArrayList<Categoria>(3);
		categorias.add(createCategoria("Cuadros"));
		categorias.add(createCategoria("Joyas"));
		categorias.add(createCategoria("Muebles"));
		return categorias;
	}

	private Producto createProducto(Usuario usuarioVendedor) {
		Categoria categoria = createCategoria("cuadros");
		try {
			return auctionService.anunciarProducto("nombre", "descripcion",
					"infoEnvio", 50, Calendar.getInstance(), 10,
					usuarioVendedor.getIdUsuario(), categoria.getIdCategoria());
		} catch (InstanceNotFoundException | UserNotSellerException e) {
			throw new RuntimeException(e);
		}

	}

	private Puja createPuja(double cantidad, Producto producto, Usuario usuario) {
		Puja puja;
		try {
			puja = auctionService.pujar(cantidad, usuario.getIdUsuario(),
					producto.getIdProducto());
		} catch (InstanceNotFoundException | ExpiredProductDateException
				| InsufficientCantidadMaximaException e) {
			throw new RuntimeException(e);
		}
		return puja;
	}

	private Puja createPujaVencida(double cantidad, Producto producto,
			Usuario usuario) {
		Calendar fechaVencida = Calendar.getInstance();
		fechaVencida.set(2000, 12, 12);
		Puja puja = new Puja(100, fechaVencida, usuario, producto);
		return puja;
	}

	private List<Producto> createProductos() {
		Usuario usuarioVendedor = createUsuarioVendedor("login");
		Categoria categoriaCuadro = createCategoria("cuadros");
		Categoria categoriaFoto = createCategoria("fotos");
		Categoria categoriaMueble = createCategoria("muebles");
		List<Producto> productos = new ArrayList<Producto>(8);

		try {
			productos.add(auctionService.anunciarProducto(
					"Los relojes blandos", "Cuadro de Dalí", "infoEnvio", 150,
					Calendar.getInstance(), 3, usuarioVendedor.getIdUsuario(),
					categoriaCuadro.getIdCategoria()));
			productos.add(auctionService.anunciarProducto(
					"La rendición de Breda", "descripcion", "infoEnvio", 500,
					Calendar.getInstance(), 1, usuarioVendedor.getIdUsuario(),
					categoriaCuadro.getIdCategoria()));
			productos.add(auctionService.anunciarProducto(
					"La fragua de Vulcano", "descripcion", "infoEnvio", 10,
					Calendar.getInstance(), 2, usuarioVendedor.getIdUsuario(),
					categoriaCuadro.getIdCategoria()));
			productos.add(auctionService.anunciarProducto("La Mona Lisa",
					"Cuadro La Mona Lisa", "infoEnvio", 500,
					Calendar.getInstance(), 3, usuarioVendedor.getIdUsuario(),
					categoriaCuadro.getIdCategoria()));
			productos.add(auctionService.anunciarProducto("La Mona Lisa",
					"Copia del cuadro La Mona Lisa", "infoEnvio", 10,
					Calendar.getInstance(), 3, usuarioVendedor.getIdUsuario(),
					categoriaFoto.getIdCategoria()));
			productos.add(auctionService.anunciarProducto(
					"La niña de los ojos verdes", "Steve McCurry", "infoEnvio",
					100, Calendar.getInstance(), 5,
					usuarioVendedor.getIdUsuario(),
					categoriaFoto.getIdCategoria()));
			productos.add(auctionService.anunciarProducto("Silla del Rey Sol",
					"descripcion", "infoEnvio", 25, Calendar.getInstance(), 3,
					usuarioVendedor.getIdUsuario(),
					categoriaMueble.getIdCategoria()));
			productos.add(auctionService.anunciarProducto(
					"Escritorio de Julio Verne", "descripcion", "infoEnvio",
					300, Calendar.getInstance(), 2,
					usuarioVendedor.getIdUsuario(),
					categoriaMueble.getIdCategoria()));
			return productos;
		} catch (InstanceNotFoundException | UserNotSellerException e) {
			throw new RuntimeException(e);
		}

	}

}