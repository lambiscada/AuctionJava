package es.udc.is.isg019.subastador.web.pages.user;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

import es.udc.is.isg019.subastador.model.userservice.IncorrectPasswordException;
import es.udc.is.isg019.subastador.model.userservice.UserService;
import es.udc.is.isg019.subastador.model.usuario.Usuario;
import es.udc.is.isg019.subastador.web.pages.Index;
import es.udc.is.isg019.subastador.web.services.AuthenticationPolicy;
import es.udc.is.isg019.subastador.web.services.AuthenticationPolicyType;
import es.udc.is.isg019.subastador.web.util.CookiesManager;
import es.udc.is.isg019.subastador.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.NON_AUTHENTICATED_USERS)
public class Login {

    @Property
    private String loginName;

    @Property
    private String password;

    @Property
    private boolean rememberMyPassword;

    @SessionState(create=false)
    private UserSession userSession;

    @Inject
    private Cookies cookies;

    @Component
    private Form loginForm;

    @Inject
    private Messages messages;

    @Inject
    private UserService userService;

    private Usuario userProfile = null;


    void onValidateFromLoginForm() {

        if (!loginForm.isValid()) {
            return;
        }

        try {
            userProfile = userService.login(loginName, password, false);
        } catch (InstanceNotFoundException e) {
            loginForm.recordError(messages.get("error-authenticationFailed"));
        } catch (IncorrectPasswordException e) {
            loginForm.recordError(messages.get("error-authenticationFailed"));
        }

    }

    Object onSuccess() {

    	userSession = new UserSession();
        userSession.setUserProfileId(userProfile.getIdUsuario());
        userSession.setFirstName(userProfile.getNombre());

        if (rememberMyPassword) {
            CookiesManager.leaveCookies(cookies, loginName, userProfile
                    .getPassword());
        }
        return Index.class;

    }

}
