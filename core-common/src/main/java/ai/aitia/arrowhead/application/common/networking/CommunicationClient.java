package ai.aitia.arrowhead.application.common.networking;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.profile.InterfaceProfile;

public interface CommunicationClient {
	
	void initialize();
	boolean isInitialized();
	<T,P> T send(final InterfaceProfile interfaceProfile, final Class<T> responseType, final P payload) throws CommunicationException;
}
