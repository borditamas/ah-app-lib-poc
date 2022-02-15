package ai.aitia.arrowhead.application.common.networking;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.profile.http.HttpMethod;

public class HttpsCommunicator implements Communicator {

	@Override
	public CommunicatorType getType() {
		return CommunicatorType.HTTPS;
	}
	
	@Override
	public void initialize() {
		// TODO certificate
	}
	
	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public <T> T send(final HttpMethod method, final String address, final int port, final String path, final Class<T> responseType) throws CommunicationException {
		return send(method, address, port, path, null, responseType);
	}

	public <T, P> T send(final HttpMethod method, final String address, final int port, final String path, P payload, final Class<T> responseType) throws CommunicationException {
		try {
			// TODO Auto-generated method stub
			return null;
			
		} catch (final Exception ex) {
			// log
			throw new CommunicationException(ex.getMessage());
		}
	}
}
