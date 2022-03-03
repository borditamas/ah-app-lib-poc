package ai.aitia.arrowhead.application.common.networking.decoder.exception;

public class PayloadDecodingException extends Exception {

	private static final long serialVersionUID = 3341256167461196770L;

	public PayloadDecodingException(String message, Throwable cause) {
		super(message, cause);
	}

	public PayloadDecodingException(String message) {
		super(message);
	}

}
