package ai.aitia.arrowhead.application.common.exception;

public class CommunicationException extends Exception {

	private static final long serialVersionUID = -22868013731893292L;

	public CommunicationException(final String message) {
		super(message);
	}

	public CommunicationException(final String message, final Throwable cause) {
		super(message, cause);
	}
}
