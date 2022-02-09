package ai.aitia.arrowhead.application.common.core;

import ai.aitia.arrowhead.application.common.networking.CommunicationType;

public interface CoreClient {
	
	CommunicationType getCommunicationType();
	void initialize();
	boolean isInitialized();
	void verifyInitialization();
	boolean isAvailable();
}
