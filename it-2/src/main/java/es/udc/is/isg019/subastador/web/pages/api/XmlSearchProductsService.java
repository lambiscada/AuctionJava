package es.udc.is.isg019.subastador.web.pages.api;

import java.util.List;

import org.apache.tapestry5.annotations.ContentType;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.is.isg019.subastador.model.auctionservice.AuctionService;
import es.udc.is.isg019.subastador.model.auctionservice.ProductoBlock;
import es.udc.is.isg019.subastador.model.producto.Producto;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@ContentType("text/xml")
public class XmlSearchProductsService {

	private final static int PRODUCTS_PER_PAGE = 10;

	private Producto product;
	
	@Property
	private ProductoBlock productBlock;

	@Inject
	private AuctionService auctionService;

	@Inject
	private org.apache.tapestry5.services.Request request;

	public Producto getProduct() {
		return product;
	}

	public void setProduct(Producto producto) {
		this.product = producto;
	}

	public List<Producto> getProducts() {
		return productBlock.getProductos();
	}

	void onActivate() throws InstanceNotFoundException {

		String nombre;
		int startIndex;

		nombre = request.getParameter("name");
		String startIndexAsString = request.getParameter("ind");

		
		if (nombre == null)
			nombre = "";

		if(startIndexAsString==null)
			startIndex=0;
		else
			startIndex = Integer.valueOf(startIndexAsString);

		productBlock = auctionService.findProductos(nombre, null, startIndex,PRODUCTS_PER_PAGE);


	}

}

