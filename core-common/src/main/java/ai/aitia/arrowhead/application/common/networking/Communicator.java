package ai.aitia.arrowhead.application.common.networking;

import ai.aitia.arrowhead.application.common.networking.profile.InterfaceProfile;

public interface Communicator {

	CommunicatorType type();
	void properties(final CommunicationProperties props);
	void initialize();
	boolean isInitialized();
	CommunicationClient client(final InterfaceProfile interfaceProfile);
}
