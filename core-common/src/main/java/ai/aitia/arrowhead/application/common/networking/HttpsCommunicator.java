package ai.aitia.arrowhead.application.common.networking;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.profile.InterfaceProfile;

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
	
	@Override
	public <T> T send(final InterfaceProfile interfaceProfile, final Class<T> responseType) throws CommunicationException {
		return send(interfaceProfile, responseType, null);
	}

	@Override
	public <T,P> T send(final InterfaceProfile interfaceProfile, final Class<T> responseType, final P payload) throws CommunicationException {
		try {
			// TODO Auto-generated method stub
			return null;
			
		} catch (final Exception ex) {
			// log
			throw new CommunicationException(ex.getMessage());
		}
	}
}
