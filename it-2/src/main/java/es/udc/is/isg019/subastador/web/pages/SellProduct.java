package es.udc.is.isg019.subastador.web.pages;

import java.util.Calendar;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.is.isg019.subastador.model.auctionservice.AuctionService;
import es.udc.is.isg019.subastador.model.auctionservice.UserNotSellerException;
import es.udc.is.isg019.subastador.model.userservice.UserService;
import es.udc.is.isg019.subastador.model.usuario.Usuario;
import es.udc.is.isg019.subastador.web.services.AuthenticationPolicy;
import es.udc.is.isg019.subastador.web.services.AuthenticationPolicyType;
import es.udc.is.isg019.subastador.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class SellProduct {

    @Property
    private String productName;

    @Property
    private String description;
   

    @Property
    private double price;
    
    @Property
    private int minutes;

    @Property 
    private Long idTarjeta;
    
    @Property
    private String deliveryInfo;

    @SessionState(create=false)
    private UserSession userSession;

    @Inject
    private UserService userService;

    @Inject
    private AuctionService auctionService;

    @Component
    private Form sellProductForm;
    
    @Inject
    private Messages messages;

    private Calendar fechaAnuncio = Calendar.getInstance();
    
      
    void onActivate() throws InstanceNotFoundException{
    	if (userService.findUserProfile(userSession.getUserProfileId()).getTarjetaBanco()!=null){
    	 idTarjeta = userService.findUserProfile(userSession.getUserProfileId()).getTarjetaBanco().getIdTarjeta();
    	} else { idTarjeta = null; }
    }

    void onValidateFromSellProductForm() throws InstanceNotFoundException  {
    	
        if (!sellProductForm.isValid()) {
            return;
        }
        try {
			auctionService.anunciarProducto(productName,description, deliveryInfo, price, fechaAnuncio, minutes,userSession.getUserProfileId(),auctionService.getCategorias().get(0).getIdCategoria());		
		} catch (UserNotSellerException e) {
			sellProductForm.recordError(messages.get("error-UserIsNotSeller"));
		}
      
    }

    Object onSuccess() {
    	return SuccessfulOperation.class;

    }

}
