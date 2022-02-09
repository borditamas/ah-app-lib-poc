package ai.aitia.arrowhead.application.common.networking;

import javax.naming.CommunicationException;

public class HttpsService implements Communication{

	@Override
	public CommunicationType getType() {
		return CommunicationType.HTTPS;
	}
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	public <T> T send(final String address, final int port, final String path, final Class<T> responseType) throws CommunicationException {
		return send(address, port, path, null, responseType);
	}

	public <T, P> T send(final String address, final int port, final String path, P payload, final Class<T> responseType) throws CommunicationException {
		try {
			// TODO Auto-generated method stub
			return null;
			
		} catch (final Exception ex) {
			// log
			throw new CommunicationException(ex.getMessage());
		}
	}
}
