package es.udc.is.isg019.subastador.model.puja;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Immutable;

import es.udc.is.isg019.subastador.model.producto.Producto;
import es.udc.is.isg019.subastador.model.usuario.Usuario;

@Entity
@Immutable
@org.hibernate.annotations.BatchSize(size = 10)
public class Puja {
	/**
	 * NOTE: this entity class does not contain a "version" property since its
	 * instances are never updated after being persisted.
	 */

	private Long idPuja;
	private double cantidadMaxima;
	private Calendar fecha;
	private Usuario usuario;
	private Producto producto;


	public Puja() {
	}

	public Puja(double cantidadMaxima, Calendar fecha, Usuario usuario,
			Producto producto) {
		this.cantidadMaxima = cantidadMaxima;
		this.fecha = fecha;
		this.usuario = usuario;
		this.producto = producto;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getIdPuja() {
		return idPuja;
	}

	public void setIdPuja(Long idPuja) {
		this.idPuja = idPuja;
	}

	public double getCantidadMaxima() {
		return cantidadMaxima;
	}

	public void setCantidadMaxima(double cantidadMaxima) {
		this.cantidadMaxima = cantidadMaxima;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "idProducto")
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "idUsuario")
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	
}
