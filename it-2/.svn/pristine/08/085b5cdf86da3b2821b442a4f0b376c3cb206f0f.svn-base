package es.udc.is.isg019.subastador.web.pages;

import java.text.DateFormat;
import java.util.List;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.is.isg019.subastador.model.auctionservice.AuctionService;
import es.udc.is.isg019.subastador.model.auctionservice.PujasBlock;
import es.udc.is.isg019.subastador.model.producto.Producto;
import es.udc.is.isg019.subastador.model.puja.Puja;
import es.udc.is.isg019.subastador.model.userservice.UserService;
import es.udc.is.isg019.subastador.model.usuario.Usuario;
import es.udc.is.isg019.subastador.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class ShowBid {
	private final static int PUJAS_PER_PAGE = 10;
	private int startIndex = 0;
	private PujasBlock pujasBlock;
	private Puja puja;
	private boolean vencida=false;
	private Producto producto;

	@Inject
	private AuctionService auctionService;
	@InjectPage
	private ProductDetails productDetails;
	@Inject
	private UserService userService;
	@SessionState(create = false)
	private UserSession userSession;

	@InjectComponent
	private Zone output;
	
	public Puja getPuja() {
		return puja;
	}

	public void setPuja(Puja puja) {
		this.puja = puja;
	}

	public List<Puja> getPujas() {
		return pujasBlock.getPujas();
	}

	public Producto getProducto() {
		return producto;
	}
	

	public Usuario getWinner() {
		if (puja.getProducto().getPujaGanadora() != null) {
			return puja.getProducto().getPujaGanadora().getUsuario();
		} else {
			return new Usuario();
		}
	}


	public DateFormat getDateFormat() {
		return DateFormat.getDateTimeInstance();
	}

	public boolean isVencida() {

		if (puja.getProducto().getRemainingTime()<0) {
			vencida = true;
		}
		return vencida;
	}

	public int getPreviousLinkContext() {

		if (startIndex - PUJAS_PER_PAGE >= 0) {
			return startIndex - PUJAS_PER_PAGE;
		} else {
			return 0;
		}

	}

	public int getNextLinkContext() {

		if (pujasBlock.getExistMorePujas()) {
			return startIndex + PUJAS_PER_PAGE;
		} else {
			return 0;
		}

	}	

	int onPassivate() {
		return startIndex;
	}

	void onActivate(int startIndex) throws InstanceNotFoundException {
		this.startIndex = startIndex;
		Usuario user = userService.findUserProfile(userSession
				.getUserProfileId());
		pujasBlock = auctionService.findPujasByIdUsuario(user.getIdUsuario(),
				startIndex, PUJAS_PER_PAGE);
	}

	Object onActionFromUpdate() {
		return output.getBody();
	}
}
