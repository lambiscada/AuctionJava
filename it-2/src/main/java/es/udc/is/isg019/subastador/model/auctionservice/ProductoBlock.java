package es.udc.is.isg019.subastador.model.auctionservice;

import java.util.List;

import es.udc.is.isg019.subastador.model.producto.Producto;

public class ProductoBlock {
	private List<Producto> productos;
	private boolean existMoreProductos;

	public ProductoBlock(List<Producto> productos, boolean existMoreProductos) {

		this.productos = productos;
		this.existMoreProductos = existMoreProductos;

	}

	public List<Producto> getProductos() {
		return productos;
	}

	public boolean getExistMoreProductos() {
		return existMoreProductos;
	}

}
