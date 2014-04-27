package es.udc.is.isg019.subastador.test.experiments;

import org.hibernate.Session;
import org.hibernate.Transaction;

import es.udc.is.isg019.subastador.model.userservice.util.PasswordEncrypter;
import es.udc.is.isg019.subastador.model.usuario.Usuario;

public class SessionExperiments {

	public static void main(String[] args) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();

			// Register user.
			Usuario userProfile = new Usuario("sessionUser",
					PasswordEncrypter.crypt("userPassword"), "name",
					"lastName", "user@udc.es");
			session.saveOrUpdate(userProfile);
			Long userId = userProfile.getIdUsuario();
			System.out.println("User with userId '" + userId
					+ "' has been created");
			System.out.println(userProfile);

			// Find user.
			userProfile = (Usuario) session.get(Usuario.class, userId);
			if (userProfile != null) {
				System.out.println("User with userId '" + userId
						+ "' has been retrieved");
				System.out.println(userProfile);
			} else {
				System.out.println("User with userId '" + userId
						+ "' has not been found");
			}
			
			// ... proceed in the same way for other entities / methods /use cases

			tx.commit();

		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}

		HibernateUtil.shutdown();

	}
}
