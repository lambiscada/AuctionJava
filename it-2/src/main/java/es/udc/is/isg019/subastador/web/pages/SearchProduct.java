package es.udc.is.isg019.subastador.web.pages;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import es.udc.is.isg019.subastador.web.util.UserSession;

public class SearchProduct {
	@Property
	private String searchName;

	@SessionState(create = false)
	private UserSession userSession;
		    
	@Inject
	private Messages messages;
	
	@InjectPage
	private ShowProducts products;
	

	Object onSuccess() {
		products.setSearchName(searchName);
		return products;
	}

}
