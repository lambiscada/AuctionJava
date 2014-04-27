package es.udc.is.isg019.subastador.web.pages.user;

import java.util.Calendar;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.is.isg019.subastador.model.userservice.IncorrectNumeroTarjetaBancoException;
import es.udc.is.isg019.subastador.model.userservice.UserService;
import es.udc.is.isg019.subastador.model.usuario.Usuario;
import es.udc.is.isg019.subastador.web.pages.SuccessfulOperation;
import es.udc.is.isg019.subastador.web.services.AuthenticationPolicy;
import es.udc.is.isg019.subastador.web.services.AuthenticationPolicyType;
import es.udc.is.isg019.subastador.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class RegisterCredit {

	@Property
	private Long creditCardNumber;

	@Property
	private Calendar expirationDate;

	@SessionState(create = false)
	private UserSession userSession;

	@Inject
	private UserService userService;

	@Component
	private Form registrationCreditForm;

	@Component(id = "creditCardNumber")
	private TextField creditCardNumberField;
	    
	@Inject
	private Messages messages;
	private Usuario userProfile;

	void onValidateFromRegistrationCreditForm() throws InstanceNotFoundException {
	
		if (!registrationCreditForm.isValid()) {
			return;}
		userProfile = userService.findUserProfile(userSession
                .getUserProfileId());
		
		try {
			userService.registerTarjetaBanco(userProfile.getIdUsuario(),
					creditCardNumber, expirationDate);
		} catch (DuplicateInstanceException e) {
			registrationCreditForm.recordError(creditCardNumberField,messages
					.get("error_duplicateCredit"));
		} catch (IncorrectNumeroTarjetaBancoException e) {
			registrationCreditForm.recordError(creditCardNumberField,messages
					.get("error_incorrectNumber"));
		}
		
	}
	

	Object onSuccess() {
		return SuccessfulOperation.class;
	}

}
