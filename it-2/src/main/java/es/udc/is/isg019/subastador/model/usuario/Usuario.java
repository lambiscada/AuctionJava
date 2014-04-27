package es.udc.is.isg019.subastador.model.usuario;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import es.udc.is.isg019.subastador.model.tarjetabanco.TarjetaBanco;

@Entity
public class Usuario {

	private Long idUsuario;
	private String login;
	private String password;
	private String nombre;
	private String apellidos;
	private String email;
	private TarjetaBanco tarjeta;

	public Usuario() {
	}

	public Usuario(String login, String password, String nombre,
			String apellidos, String email) {

		/**
		 * NOTE: "idUsuario" and "version" *must* be left as "null" since its
		 * value is automatically generated.
		 */

		this.login = login;
		this.password = password;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idTarjeta")
	public TarjetaBanco getTarjetaBanco() {
		return tarjeta;
	}

	public void setTarjetaBanco(TarjetaBanco tarjeta) {
		this.tarjeta = tarjeta;
	}

}
