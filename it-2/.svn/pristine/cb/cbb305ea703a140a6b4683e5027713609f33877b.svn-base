package es.udc.is.isg019.subastador.web.pages;

import java.util.List;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import es.udc.is.isg019.subastador.model.auctionservice.AuctionService;
import es.udc.is.isg019.subastador.model.auctionservice.ProductoBlock;
import es.udc.is.isg019.subastador.model.categoria.Categoria;
import es.udc.is.isg019.subastador.model.producto.Producto;
import es.udc.is.isg019.subastador.model.usuario.Usuario;


public class ShowProducts {
	private final static int PRODUCTS_PER_PAGE = 10;

	
	private String searchName;
	private int startIndex = 0;
	private Producto product;
	private ProductoBlock productBlock;

	@Inject
	private AuctionService auctionService;
	@InjectPage
	private ProductDetails productDetails;
	
	@InjectComponent
	private Zone output;

	Categoria categoria = auctionService.getCategorias().get(0);

	public String getSearchName() {
		return searchName;
	}

	public Usuario getSeller(){
		return product.getUsuario();
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public Producto getProduct() {
		return product;
	}

	public void setProduct(Producto product) {
		this.product = product;
	}
	public List<Producto> getProducts() {
		return productBlock.getProductos();
	}
	
	public long getMinutes(){
		return product.getRemainingTime();
	}
	

	
	public Object[] getPreviousLinkContext() {
		
		if (startIndex-PRODUCTS_PER_PAGE >= 0) {
			return new Object[] {searchName, startIndex-PRODUCTS_PER_PAGE};
		} else {
			return null;
		}
		
	}
	
	public Object[] getNextLinkContext() {
		
		if (productBlock.getExistMoreProductos()) {
			return new Object[] {searchName, startIndex+PRODUCTS_PER_PAGE};
		} else {
			return null;
		}
		
	}
	
	Object[] onPassivate() {
		return new Object[] {searchName, startIndex};
	}
	

	void onActivate(String searchName, int startIndex) {
		this.searchName = searchName;
		this.startIndex = startIndex;
		if (searchName == null) {
			searchName="";
		}	
			productBlock = auctionService.findProductos(searchName, categoria.getIdCategoria(), startIndex, PRODUCTS_PER_PAGE);
		
	}

	Object onActionFromUpdate(){
		return output.getBody();
	}
	

}
