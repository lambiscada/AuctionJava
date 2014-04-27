package es.udc.is.isg019.subastador.web.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.is.isg019.subastador.model.auctionservice.AuctionService;
import es.udc.is.isg019.subastador.model.auctionservice.ExpiredProductDateException;
import es.udc.is.isg019.subastador.model.auctionservice.InsufficientCantidadMaximaException;
import es.udc.is.isg019.subastador.model.userservice.UserService;
import es.udc.is.isg019.subastador.web.services.AuthenticationPolicy;
import es.udc.is.isg019.subastador.web.services.AuthenticationPolicyType;
import es.udc.is.isg019.subastador.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class Bid {
	private Long idProducto;
	private double precio;
	private boolean bidMore = false;

	@SessionState(create = false)
	private UserSession userSession;

	@Inject
	private UserService userService;

	@Inject
	private AuctionService auctionService;

	@Inject
	private Messages messages;

	@Property
	private double amount;

	@Component
	private Form bidForm;

	public Long getIdProduct() {
		return idProducto;
	}

	public void setIdProduct(Long idProducto) {
		this.idProducto = idProducto;
	}

	public boolean isBidMore() {
		return bidMore;
	}

	public void setBidMore(boolean bidMore) {
		this.bidMore = bidMore;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	void onActivate(Long idProducto) throws InstanceNotFoundException {
		if (auctionService.findProductoByIdProducto(idProducto)
				.getPujaGanadora() != null) {
			setBidMore(true);
		} else {
			setBidMore(false);
		}
		setPrecio(auctionService.findProductoByIdProducto(idProducto)
				.getPrecioActual());

		this.idProducto = idProducto;
	}

	Long onPassivate() {
		return idProducto;

	}

	void onValidateFromBidForm() {
		if (!bidForm.isValid()) {
			return;
		}
		if ((bidMore) && (amount <= precio)) {
			bidForm.recordError(messages.get("incorrect-amount"));
		} else {
			if (amount < precio) {
				bidForm.recordError(messages.get("incorrect-amount"));
			}
		}
		try {
			auctionService.pujar(amount, userSession.getUserProfileId(),idProducto);

		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		} catch (ExpiredProductDateException e) {
			e.printStackTrace();
		} catch (InsufficientCantidadMaximaException e) {
			e.printStackTrace();
		}

	}

	Object onSuccess() {
		return SuccessfulOperation.class;
	}

}
