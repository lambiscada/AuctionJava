package es.udc.is.isg019.subastador.model.tarjetabanco;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class TarjetaBanco {
	private Long idTarjeta;
    private Calendar fechaExpiracion;
    private Long numero;

    public TarjetaBanco() {}
    
    public TarjetaBanco(Long numero, Calendar fechaExpiracion) {
    	this.numero = numero;
    	this.fechaExpiracion = fechaExpiracion;

    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public Long getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(Long idTarjeta) {
        this.idTarjeta = idTarjeta;
    }
    
    @Temporal(TemporalType.TIMESTAMP)
    public Calendar getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Calendar fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

}
