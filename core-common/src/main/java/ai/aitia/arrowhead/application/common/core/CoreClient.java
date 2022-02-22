package ai.aitia.arrowhead.application.common.core;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;

public interface CoreClient {
	
	void initialize();
	boolean isInitialized();
	void verifyInitialization();
	boolean isAvailable() throws CommunicationException;
}
