package es.udc.is.isg019.subastador.model.auctionservice;

@SuppressWarnings("serial")
public class UserNotSellerException extends Exception {

	private String login;

	public UserNotSellerException(String login) {
		super("User is not seller => login = " + login);
		this.login = login;
	}

	public String getLogin() {
		return login;
	}

}
