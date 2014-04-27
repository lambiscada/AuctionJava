package es.udc.is.isg019.subastador.model.userservice;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.is.isg019.subastador.model.tarjetabanco.TarjetaBanco;
import es.udc.is.isg019.subastador.model.tarjetabanco.TarjetaBancoDao;
import es.udc.is.isg019.subastador.model.userservice.util.PasswordEncrypter;
import es.udc.is.isg019.subastador.model.usuario.Usuario;
import es.udc.is.isg019.subastador.model.usuario.UsuarioDao;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UsuarioDao usuarioDao;

	@Autowired
	private TarjetaBancoDao tarjetaBancoDao;

	public Usuario registerUser(String loginName, String clearPassword,
			UserProfileDetails userProfileDetails)
			throws DuplicateInstanceException {

		try {
			usuarioDao.findByLogin(loginName);
			throw new DuplicateInstanceException(loginName,
					Usuario.class.getName());
		} catch (InstanceNotFoundException e) {
			String encryptedPassword = PasswordEncrypter.crypt(clearPassword);

			Usuario usuario = new Usuario(loginName, encryptedPassword,
					userProfileDetails.getFirstName(),
					userProfileDetails.getLastName(),
					userProfileDetails.getEmail());

			usuarioDao.save(usuario);
			return usuario;
		}

	}

	@Transactional(readOnly = true)
	public Usuario login(String loginName, String password,
			boolean passwordIsEncrypted) throws InstanceNotFoundException,
			IncorrectPasswordException {

		Usuario userProfile = usuarioDao.findByLogin(loginName);
		String storedPassword = userProfile.getPassword();

		if (passwordIsEncrypted) {
			if (!password.equals(storedPassword)) {
				throw new IncorrectPasswordException(loginName);
			}
		} else {
			if (!PasswordEncrypter.isClearPasswordCorrect(password,
					storedPassword)) {
				throw new IncorrectPasswordException(loginName);
			}
		}
		return userProfile;

	}

	@Transactional(readOnly = true)
	public Usuario findUserProfile(Long idUsuario)
			throws InstanceNotFoundException {

		return usuarioDao.find(idUsuario);
	}

	public void updateUserProfileDetails(Long idUsuario,
			UserProfileDetails userProfileDetails)
			throws InstanceNotFoundException {

		Usuario userProfile = usuarioDao.find(idUsuario);
		userProfile.setNombre(userProfileDetails.getFirstName());
		userProfile.setApellidos(userProfileDetails.getLastName());
		userProfile.setEmail(userProfileDetails.getEmail());

	}

	public void changePassword(Long idUsuario, String oldClearPassword,
			String newClearPassword) throws IncorrectPasswordException,
			InstanceNotFoundException {

		Usuario userProfile;
		userProfile = usuarioDao.find(idUsuario);

		String storedPassword = userProfile.getPassword();

		if (!PasswordEncrypter.isClearPasswordCorrect(oldClearPassword,
				storedPassword)) {
			throw new IncorrectPasswordException(userProfile.getLogin());
		}

		userProfile.setPassword(PasswordEncrypter.crypt(newClearPassword));

	}

	public TarjetaBanco registerTarjetaBanco(Long idUsuario, Long numero,
			Calendar fechaExpiracion) throws DuplicateInstanceException,
			InstanceNotFoundException, IncorrectNumeroTarjetaBancoException {
		Usuario usuario = usuarioDao.find(idUsuario);
		TarjetaBanco tarjetaBanco = usuario.getTarjetaBanco();
		if (tarjetaBanco != null) {
			throw new DuplicateInstanceException(tarjetaBanco.getNumero(),
					TarjetaBanco.class.getName());
		} else if (numero.toString().length() == 16) {
			tarjetaBanco = new TarjetaBanco(numero, fechaExpiracion);
			tarjetaBancoDao.save(tarjetaBanco);
			usuario.setTarjetaBanco(tarjetaBanco);
		} else
			throw new IncorrectNumeroTarjetaBancoException(numero);

		return tarjetaBanco;
	}

	public void updateTarjetaBanco(Long idTarjeta, Long numero,
			Calendar fechaExpiracion) throws InstanceNotFoundException,
			IncorrectNumeroTarjetaBancoException {
		TarjetaBanco tarjetaBanco = tarjetaBancoDao.find(idTarjeta);
		if (numero.toString().length() == 16) {
			tarjetaBanco.setNumero(numero);
			tarjetaBanco.setFechaExpiracion(fechaExpiracion);
		} else
			throw new IncorrectNumeroTarjetaBancoException(numero);

	}

	@Transactional(readOnly = true)
	public TarjetaBanco findTarjetaBanco(Long idTarjeta) throws InstanceNotFoundException {
		return tarjetaBancoDao.find(idTarjeta);
	}

}
