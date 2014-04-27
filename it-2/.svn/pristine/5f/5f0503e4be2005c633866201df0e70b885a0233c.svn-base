package es.udc.is.isg019.subastador.web.pages;

import java.text.DateFormat;
import java.util.Locale;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.is.isg019.subastador.model.auctionservice.AuctionService;
import es.udc.is.isg019.subastador.model.categoria.Categoria;
import es.udc.is.isg019.subastador.model.producto.Producto;
import es.udc.is.isg019.subastador.model.usuario.Usuario;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ProductDetails {
	private Producto product;
	private Long idProducto;
	private boolean vencida=false;

	@Inject
	private Locale locale;
	@Inject
	private AuctionService auctionService;
	
	@InjectPage
	private Bid bid;
	
	@InjectComponent
	private Zone output;
	
	Categoria categoria = auctionService.getCategorias().get(0);
	
	
	public Categoria getCategoria() {
		return categoria;
	}
	public Producto getProduct() {
		return product;
	}

	public void setProduct(Producto product) {
		this.product = product;
	}
	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}
	public Usuario getSeller(){
		return product.getUsuario();
	}
	public boolean isVencida() {

		if (product.getRemainingTime()<0) {
			vencida = true;
		}
		return vencida;
	}

	
	public Usuario getWinner(){
		
		if (product.getPujaGanadora()!=null){
			return product.getPujaGanadora().getUsuario();
		}
		else {
			return new Usuario();
		}
	}
	
	public DateFormat getDateFormat() {
		return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
	}


	Long onPassivate() {
		return idProducto;
	}
	
	void onActivate(Long idProducto) {	
		this.idProducto=idProducto;
			
		try {
				product = auctionService.findProductoByIdProducto(idProducto);
		} catch (InstanceNotFoundException e) {
				e.printStackTrace();
			}		
	}
	Object onActionFromUpdate(){
		return output.getBody();
	}
}
