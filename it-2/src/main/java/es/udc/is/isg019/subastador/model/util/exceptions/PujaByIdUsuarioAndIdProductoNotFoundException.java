package es.udc.is.isg019.subastador.model.util.exceptions;

public class PujaByIdUsuarioAndIdProductoNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	private Long idUsuario;
	private Long idProducto;

	public PujaByIdUsuarioAndIdProductoNotFoundException(Long idUsuario,
			Long idProducto) {
		super("No existe aun puja para el producto con ID:" + idProducto
				+ " realizada por el usuario con ID:" + idUsuario);
		this.idUsuario = idUsuario;
		this.idProducto = idProducto;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public Long getIdProducto() {
		return idProducto;
	}

}
