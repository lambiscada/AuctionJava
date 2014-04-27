package es.udc.is.isg019.subastador.model.auctionservice;

@SuppressWarnings("serial")
public class ExpiredProductDateException extends Exception {
	private String nombreProducto;

	public ExpiredProductDateException(String nombreProducto) {
		super("Product date expired => nombreProducto = " + nombreProducto);
		this.nombreProducto = nombreProducto;
	}

	public String getNombreProducto() {
		return nombreProducto;
	}

}
