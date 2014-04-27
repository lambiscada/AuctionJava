package es.udc.is.isg019.subastador.model.userservice;

import java.util.Calendar;

import es.udc.is.isg019.subastador.model.tarjetabanco.TarjetaBanco;
import es.udc.is.isg019.subastador.model.usuario.Usuario;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface UserService {

    public Usuario registerUser(String loginName, String clearPassword,
            UserProfileDetails userProfileDetails)
            throws DuplicateInstanceException;

    public Usuario login(String loginName, String password,
            boolean passwordIsEncrypted) throws InstanceNotFoundException,
            IncorrectPasswordException;

    public Usuario findUserProfile(Long idUsuario)
            throws InstanceNotFoundException;

    public void updateUserProfileDetails(Long idUsuario,
            UserProfileDetails userProfileDetails)
            throws InstanceNotFoundException;

    public void changePassword(Long idUsuario, String oldClearPassword,
            String newClearPassword) throws IncorrectPasswordException,
            InstanceNotFoundException;
    
    public TarjetaBanco registerTarjetaBanco(Long idUsuario, Long numero, Calendar fechaExpiracion)
            throws DuplicateInstanceException, InstanceNotFoundException, IncorrectNumeroTarjetaBancoException;

    public void updateTarjetaBanco(Long idTarjeta, Long numero,
			Calendar fechaExpiracion) throws InstanceNotFoundException, IncorrectNumeroTarjetaBancoException;
    
    public TarjetaBanco findTarjetaBanco(Long idTarjeta) throws InstanceNotFoundException;

}
