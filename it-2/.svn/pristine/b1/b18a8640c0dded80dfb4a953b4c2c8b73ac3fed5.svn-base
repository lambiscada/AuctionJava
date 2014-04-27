package es.udc.is.isg019.subastador.model.producto;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import es.udc.is.isg019.subastador.model.categoria.Categoria;
import es.udc.is.isg019.subastador.model.puja.Puja;
import es.udc.is.isg019.subastador.model.usuario.Usuario;

@Entity		
public class Producto {
	
	private Long idProducto;
	private String nombre;
	private String descripcion;
	private String infoEnvio;
	private double precioSalida;
	private double precioActual;
	private Calendar fechaVencimiento;
	private Calendar fechaAnuncio;
	private Usuario usuario;
	private Categoria categoria;
	private Puja pujaGanadora;
	private long version;

	public Producto() {
	}

	public Producto(String nombre, String descripcion, String infoEnvio,
			double precioSalida,double precioActual,Calendar fechaVencimiento,Calendar fechaAnuncio, Usuario usuario,
			Categoria categoria) {
		/**
		 * NOTE: "idProducto" *must* be left as "null" since its value is
		 * automatically generated.
		 */
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.infoEnvio = infoEnvio;
		this.precioSalida = precioSalida;
		this.precioActual = precioSalida;
		this.fechaVencimiento = fechaVencimiento;
		this.fechaAnuncio = fechaAnuncio;
		this.usuario = usuario;
		this.categoria = categoria;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getInfoEnvio() {
		return infoEnvio;
	}

	public void setInfoEnvio(String infoEnvio) {
		this.infoEnvio = infoEnvio;
	}

	public double getPrecioSalida() {
		return precioSalida;
	}

	public void setPrecioSalida(double precioSalida) {
		this.precioSalida = precioSalida;
	}
	
	public double getPrecioActual() {
		return precioActual;
	}

	public void setPrecioActual(double precioActual) {
		this.precioActual = precioActual;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Calendar fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getFechaAnuncio() {
		return fechaAnuncio;
	}

	public void setFechaAnuncio(Calendar fechaAnuncio) {
		this.fechaAnuncio = fechaAnuncio;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "idUsuario")
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "idCategoria")
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idPuja")
	public Puja getPujaGanadora() {
		return pujaGanadora;
	}

	public void setPujaGanadora(Puja pujaGanadora) {
		this.pujaGanadora = pujaGanadora;
	}

	@Version
	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	@Transient
	public Long getRemainingTime() {
		long remainingTimeInMillis = fechaVencimiento.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
		return remainingTimeInMillis/(1000*60);
		
	}

}
