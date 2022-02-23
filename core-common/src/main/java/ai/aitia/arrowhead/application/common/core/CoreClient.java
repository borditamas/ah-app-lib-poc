package ai.aitia.arrowhead.application.common.core;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;

/*default*/ interface CoreClient {
	
	void initialize();
	boolean isInitialized();
	void verifyInitialization();
	boolean isAvailable() throws CommunicationException;
}
