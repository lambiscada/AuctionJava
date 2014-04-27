package es.udc.is.isg019.subastador.test.experiments;

import org.hibernate.Transaction;

import es.udc.is.isg019.subastador.model.userservice.util.PasswordEncrypter;
import es.udc.is.isg019.subastador.model.usuario.Usuario;
import es.udc.is.isg019.subastador.model.usuario.UsuarioDao;
import es.udc.is.isg019.subastador.model.usuario.UsuarioDaoHibernate;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class DaoExperiments {

	public static void main(String[] args) {

		UsuarioDaoHibernate userProfileDaoHibernate = new UsuarioDaoHibernate();
		userProfileDaoHibernate.setSessionFactory(HibernateUtil
				.getSessionFactory());
		UsuarioDao userProfileDao = userProfileDaoHibernate;

		Transaction tx = HibernateUtil.getSessionFactory().getCurrentSession()
				.beginTransaction();
		try {

			// Register user.
			Usuario userProfile = new Usuario("daoUser",
					PasswordEncrypter.crypt("userPassword"), "name",
					"lastName", "user@udc.es");
			userProfileDao.save(userProfile);
			Long userId = userProfile.getIdUsuario();
			System.out.println("User with userId '" + userId
					+ "' has been created");
			System.out.println(userProfile);

			// Find user.
			userProfile = userProfileDao.find(userId);
			System.out.println("User with userId '" + userId
					+ "' has been retrieved");
			System.out.println(userProfile);
			
			// ... proceed in the same way for other entities / methods / use cases

			tx.commit();

		} catch (RuntimeException e) {
			e.printStackTrace();
			tx.rollback();
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
			tx.commit();
		} finally {
			HibernateUtil.getSessionFactory().getCurrentSession().close();
		}

		HibernateUtil.shutdown();

	}

}
