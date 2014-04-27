package es.udc.is.isg019.subastador.web.pages.user;

import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.is.isg019.subastador.model.tarjetabanco.TarjetaBanco;
import es.udc.is.isg019.subastador.model.userservice.IncorrectNumeroTarjetaBancoException;
import es.udc.is.isg019.subastador.model.userservice.UserProfileDetails;
import es.udc.is.isg019.subastador.model.userservice.UserService;
import es.udc.is.isg019.subastador.model.usuario.Usuario;
import es.udc.is.isg019.subastador.web.pages.Index;
import es.udc.is.isg019.subastador.web.services.AuthenticationPolicy;
import es.udc.is.isg019.subastador.web.services.AuthenticationPolicyType;
import es.udc.is.isg019.subastador.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class UpdateCredit {

	@Property
	private Long creditCardNumber;

	@Property
	private Date expirationDate;

	@SessionState(create = false)
	private UserSession userSession;

	@Inject
	private UserService userService;

	void onPrepareForRender() throws InstanceNotFoundException {

		Usuario userProfile = userService.findUserProfile(userSession
				.getUserProfileId());

		if (userProfile.getTarjetaBanco() != null) {
			creditCardNumber = userProfile.getTarjetaBanco().getNumero();
			expirationDate = userProfile.getTarjetaBanco().getFechaExpiracion()
					.getTime();
		}

	}

	Object onSuccess() throws InstanceNotFoundException,
			IncorrectNumeroTarjetaBancoException {
		Usuario userProfile = userService.findUserProfile(userSession
				.getUserProfileId());

		Calendar cal = Calendar.getInstance();
		cal.setTime(expirationDate);
		userService.updateTarjetaBanco(userProfile.getTarjetaBanco()
				.getIdTarjeta(), creditCardNumber, cal);

		return Index.class;

	}

}