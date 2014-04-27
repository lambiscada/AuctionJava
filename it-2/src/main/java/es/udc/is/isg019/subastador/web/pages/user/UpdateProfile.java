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
public class UpdateProfile {

    @Property
    private String firstName;

    @Property
    private String lastName;

    @Property
    private String email;
    
    @Property
	private Long creditCardNumber;
   
    @SessionState(create=false)
    private UserSession userSession;

    @Inject
    private UserService userService;

    
    void onPrepareForRender() throws InstanceNotFoundException{

        Usuario userProfile = userService.findUserProfile(userSession
                .getUserProfileId());
        firstName = userProfile.getNombre();
        lastName = userProfile.getApellidos();
        email = userProfile.getEmail();
        
        if (userProfile.getTarjetaBanco() != null) {
			creditCardNumber = userProfile.getTarjetaBanco().getNumero();
        }
    }

    Object onSuccess() throws InstanceNotFoundException, IncorrectNumeroTarjetaBancoException {
        userService.updateUserProfileDetails(
                userSession.getUserProfileId(), new UserProfileDetails(
                        firstName, lastName, email));
        userSession.setFirstName(firstName);
        return Index.class;

    }

}