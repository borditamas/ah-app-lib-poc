package ai.aitia.arrowhead.application.common.networking;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.profile.InterfaceProfile;

public class WebsocketCommunicator implements Communicator {

	@Override
	public CommunicatorType getType() {
		return CommunicatorType.WEBSOCKET;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
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
	public <T, P> T send(final InterfaceProfile interfaceProfile, final Class<T> responseType, final P payload) throws CommunicationException {
		// TODO Auto-generated method stub
		return null;
	}
}
