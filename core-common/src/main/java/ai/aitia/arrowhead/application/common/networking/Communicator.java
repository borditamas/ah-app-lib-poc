package ai.aitia.arrowhead.application.common.networking;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.profile.InterfaceProfile;

public interface Communicator {

	public CommunicatorType getType();
	public void initialize();
	public boolean isInitialized();
	public <T> T send(final InterfaceProfile interfaceProfile, final Class<T> responseType) throws CommunicationException;
	public <T,P> T send(final InterfaceProfile interfaceProfile, final Class<T> responseType, final P payload) throws CommunicationException;
}
