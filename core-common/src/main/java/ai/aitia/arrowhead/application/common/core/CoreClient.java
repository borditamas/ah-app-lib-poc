package ai.aitia.arrowhead.application.common.core;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.CommunicatorType;

public interface CoreClient {
	
	CommunicatorType getCommunicatorType();
	void initialize();
	boolean isInitialized();
	void verifyInitialization();
	boolean isAvailable() throws CommunicationException;
}
