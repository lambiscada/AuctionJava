package es.udc.is.isg019.subastador.test.model.userservice;

import static es.udc.is.isg019.subastador.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.is.isg019.subastador.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.is.isg019.subastador.model.tarjetabanco.TarjetaBanco;
import es.udc.is.isg019.subastador.model.userservice.IncorrectNumeroTarjetaBancoException;
import es.udc.is.isg019.subastador.model.userservice.IncorrectPasswordException;
import es.udc.is.isg019.subastador.model.userservice.UserProfileDetails;
import es.udc.is.isg019.subastador.model.userservice.UserService;
import es.udc.is.isg019.subastador.model.usuario.Usuario;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE, SPRING_CONFIG_TEST_FILE })
@Transactional
public class UserServiceTest {

	private final long NON_EXISTENT_USER_PROFILE_ID = -1;
	private final long NON_EXISTENT_TARJETA_BANCO_ID = -1;

	@Autowired
	private UserService userService;

	@Test
	public void testRegisterUserAndFindUserProfile()
			throws DuplicateInstanceException, InstanceNotFoundException {

		/* Register user and find profile. */
		Usuario usuario = userService.registerUser("user", "userPassword",
				new UserProfileDetails("name", "lastName", "user@udc.es"));

		Usuario usuario2 = userService.findUserProfile(usuario.getIdUsuario());

		/* Check data. */
		assertEquals(usuario, usuario2);

	}

	@Test(expected = DuplicateInstanceException.class)
	public void testRegisterDuplicatedUser() throws DuplicateInstanceException,
			InstanceNotFoundException {

		String loginName = "user";
		String clearPassword = "userPassword";
		UserProfileDetails userProfileDetails = new UserProfileDetails("name",
				"lastName", "user@udc.es");

		userService.registerUser(loginName, clearPassword, userProfileDetails);

		userService.registerUser(loginName, clearPassword, userProfileDetails);

	}

	@Test
	public void testLoginClearPassword() throws IncorrectPasswordException,
			InstanceNotFoundException {

		String clearPassword = "userPassword";
		Usuario usuario = registerUser("user", clearPassword);

		Usuario usuario2 = userService.login(usuario.getLogin(), clearPassword,
				false);

		assertEquals(usuario, usuario2);

	}

	@Test
	public void testLoginEncryptedPassword() throws IncorrectPasswordException,
			InstanceNotFoundException {

		Usuario usuario = registerUser("user", "clearPassword");

		Usuario usuario2 = userService.login(usuario.getLogin(),
				usuario.getPassword(), true);

		assertEquals(usuario, usuario2);

	}

	@Test(expected = IncorrectPasswordException.class)
	public void testLoginIncorrectPasword() throws IncorrectPasswordException,
			InstanceNotFoundException {

		String clearPassword = "userPassword";
		Usuario usuario = registerUser("user", clearPassword);

		userService.login(usuario.getLogin(), 'X' + clearPassword, false);

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testLoginWithNonExistentUser()
			throws IncorrectPasswordException, InstanceNotFoundException {

		userService.login("user", "userPassword", false);

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentUser() throws InstanceNotFoundException {

		userService.findUserProfile(NON_EXISTENT_USER_PROFILE_ID);

	}

	@Test
	public void testUpdate() throws InstanceNotFoundException,
			IncorrectPasswordException {

		/* Update profile. */
		String clearPassword = "userPassword";
		Usuario usuario = registerUser("user", clearPassword);

		UserProfileDetails newUserProfileDetails = new UserProfileDetails(
				'X' + usuario.getNombre(), 'X' + usuario.getApellidos(),
				'X' + usuario.getEmail());

		userService.updateUserProfileDetails(usuario.getIdUsuario(),
				newUserProfileDetails);

		/* Check changes. */
		userService.login(usuario.getLogin(), clearPassword, false);
		Usuario usuario2 = userService.findUserProfile(usuario.getIdUsuario());

		assertEquals(newUserProfileDetails.getFirstName(), usuario2.getNombre());
		assertEquals(newUserProfileDetails.getLastName(),
				usuario2.getApellidos());
		assertEquals(newUserProfileDetails.getEmail(), usuario2.getEmail());

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testUpdateWithNonExistentUser()
			throws InstanceNotFoundException {

		userService.updateUserProfileDetails(NON_EXISTENT_USER_PROFILE_ID,
				new UserProfileDetails("name", "lastName", "user@udc.es"));

	}

	@Test
	public void testChangePassword() throws InstanceNotFoundException,
			IncorrectPasswordException {

		/* Change password. */
		String clearPassword = "userPassword";
		Usuario usuario = registerUser("user", clearPassword);
		String newClearPassword = 'X' + clearPassword;

		userService.changePassword(usuario.getIdUsuario(), clearPassword,
				newClearPassword);

		/* Check new password. */
		userService.login(usuario.getLogin(), newClearPassword, false);

	}

	@Test(expected = IncorrectPasswordException.class)
	public void testChangePasswordWithIncorrectPassword()
			throws InstanceNotFoundException, IncorrectPasswordException {

		String clearPassword = "userPassword";
		Usuario usuario = registerUser("user", clearPassword);

		userService.changePassword(usuario.getIdUsuario(), 'X' + clearPassword,
				'Y' + clearPassword);

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testChangePasswordWithNonExistentUser()
			throws InstanceNotFoundException, IncorrectPasswordException {

		userService.changePassword(NON_EXISTENT_USER_PROFILE_ID,
				"userPassword", "XuserPassword");

	}

	private Usuario registerUser(String loginName, String clearPassword) {

		UserProfileDetails userProfileDetails = new UserProfileDetails("name",
				"lastName", "user@udc.es");

		try {

			return userService.registerUser(loginName, clearPassword,
					userProfileDetails);

		} catch (DuplicateInstanceException e) {
			throw new RuntimeException(e);
		}

	}

	@Test
	public void testRegisterTarjetaBancoAndFindTarjetaBanco()
			throws DuplicateInstanceException, InstanceNotFoundException,
			NumberFormatException, IncorrectNumeroTarjetaBancoException {

		/* Register and find tarjeta banco. */
		Usuario usuario = userService.registerUser("user", "userPassword",
				new UserProfileDetails("name", "lastName", "user@udc.es"));

		TarjetaBanco tarjetaBanco = userService.registerTarjetaBanco(
				usuario.getIdUsuario(), new Long("1111222233334444"),
				Calendar.getInstance());

		TarjetaBanco tarjetaBanco2 = usuario.getTarjetaBanco();

		/* Check data. */
		assertEquals(tarjetaBanco, tarjetaBanco2);

	}

	@Test(expected = IncorrectNumeroTarjetaBancoException.class)
	public void testRegisterTarjetaBancoWithIncorrectNumber()
			throws DuplicateInstanceException, InstanceNotFoundException,
			NumberFormatException, IncorrectNumeroTarjetaBancoException {

		Usuario usuario = userService.registerUser("user", "userPassword",
				new UserProfileDetails("name", "lastName", "user@udc.es"));
		userService.registerTarjetaBanco(usuario.getIdUsuario(), new Long(
				"111122223333"), Calendar.getInstance());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testRegisterTarjetaBancoWithNonExstentUsuario()
			throws DuplicateInstanceException, InstanceNotFoundException,
			NumberFormatException, IncorrectNumeroTarjetaBancoException {

		userService.registerTarjetaBanco(NON_EXISTENT_USER_PROFILE_ID,
				new Long("1111222233334444"), Calendar.getInstance());

	}

	@Test(expected = DuplicateInstanceException.class)
	public void testRegisterTwoTarjetaBanco()
			throws DuplicateInstanceException, InstanceNotFoundException,
			NumberFormatException, IncorrectNumeroTarjetaBancoException {

		Usuario usuario = userService.registerUser("user", "userPassword",
				new UserProfileDetails("name", "lastName", "user@udc.es"));

		userService.registerTarjetaBanco(usuario.getIdUsuario(), new Long(
				"1111222233334444"), Calendar.getInstance());
		userService.registerTarjetaBanco(usuario.getIdUsuario(), new Long(
				"1111222233334444"), Calendar.getInstance());

	}

	@Test
	public void testUpdateTarjetaBanco() throws InstanceNotFoundException,
			DuplicateInstanceException, IncorrectNumeroTarjetaBancoException {

		Long nuevoNumero = new Long("4444333322221111");
		Calendar nuevaFechaExpiracion = Calendar.getInstance();
		Usuario usuario = userService.registerUser("user", "userPassword",
				new UserProfileDetails("name", "lastName", "user@udc.es"));

		TarjetaBanco tarjetaBanco = userService.registerTarjetaBanco(
				usuario.getIdUsuario(), new Long("1111222233334444"),
				Calendar.getInstance());

		userService.updateTarjetaBanco(tarjetaBanco.getIdTarjeta(),
				nuevoNumero, nuevaFechaExpiracion);

		TarjetaBanco tarjetaBanco2 = userService.findUserProfile(
				usuario.getIdUsuario()).getTarjetaBanco();

		assertEquals(nuevoNumero, tarjetaBanco2.getNumero());
		assertEquals(nuevaFechaExpiracion, tarjetaBanco2.getFechaExpiracion());
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testUpdateNonExistentTarjetaBanco()
			throws InstanceNotFoundException, DuplicateInstanceException,
			IncorrectNumeroTarjetaBancoException {

		userService.updateTarjetaBanco(NON_EXISTENT_TARJETA_BANCO_ID, new Long(
				"1111222233334444"), Calendar.getInstance());
	}

	@Test(expected = IncorrectNumeroTarjetaBancoException.class)
	public void testUpdateTarjetaBancoWithShorterNumbre()
			throws InstanceNotFoundException, DuplicateInstanceException,
			IncorrectNumeroTarjetaBancoException {

		Long numeroIncorrecto = new Long("444433332222");
		Calendar nuevaFechaExpiracion = Calendar.getInstance();
		Usuario usuario = userService.registerUser("user", "userPassword",
				new UserProfileDetails("name", "lastName", "user@udc.es"));

		TarjetaBanco tarjetaBanco = userService.registerTarjetaBanco(
				usuario.getIdUsuario(), new Long("1111222233334444"),
				Calendar.getInstance());

		userService.updateTarjetaBanco(tarjetaBanco.getIdTarjeta(),
				numeroIncorrecto, nuevaFechaExpiracion);

	}

	@Test(expected = IncorrectNumeroTarjetaBancoException.class)
	public void testUpdateTarjetaBancoWithLongerNumbre()
			throws InstanceNotFoundException, DuplicateInstanceException,
			IncorrectNumeroTarjetaBancoException {

		Long numeroIncorrecto = new Long("4444333322221111000");
		Calendar nuevaFechaExpiracion = Calendar.getInstance();
		Usuario usuario = userService.registerUser("user", "userPassword",
				new UserProfileDetails("name", "lastName", "user@udc.es"));

		TarjetaBanco tarjetaBanco = userService.registerTarjetaBanco(
				usuario.getIdUsuario(), new Long("1111222233334444"),
				Calendar.getInstance());

		userService.updateTarjetaBanco(tarjetaBanco.getIdTarjeta(),
				numeroIncorrecto, nuevaFechaExpiracion);

	}

	@Test
	public void testIsVendedor() throws InstanceNotFoundException,
			DuplicateInstanceException, IncorrectNumeroTarjetaBancoException {

		Usuario usuario = userService.registerUser("user", "userPassword",
				new UserProfileDetails("name", "lastName", "user@udc.es"));

		userService.registerTarjetaBanco(usuario.getIdUsuario(), new Long(
				"1111222233334444"), Calendar.getInstance());
		assertTrue(usuario.getTarjetaBanco()!=null);

	}

	@Test(expected = InstanceNotFoundException.class)
	public void testIsVendedorWithNonExistentUser() throws InstanceNotFoundException,
			DuplicateInstanceException, IncorrectNumeroTarjetaBancoException {

		userService.registerTarjetaBanco(NON_EXISTENT_USER_PROFILE_ID,
				new Long("1111222233334444"), Calendar.getInstance());

	}

	@Test
	public void testIsNotVendedor() throws InstanceNotFoundException,
			DuplicateInstanceException, IncorrectNumeroTarjetaBancoException {

		Usuario usuario = userService.registerUser("user", "userPassword",
				new UserProfileDetails("name", "lastName", "user@udc.es"));

		assertFalse(usuario.getTarjetaBanco()!=null);
	}
}
