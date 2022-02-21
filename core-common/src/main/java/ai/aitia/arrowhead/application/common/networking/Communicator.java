package ai.aitia.arrowhead.application.common.networking;

import ai.aitia.arrowhead.application.common.networking.profile.InterfaceProfile;

public interface Communicator<T> {

	CommunicatorType type();
	void properties(final CommunicationProperties props);
	void initialize();
	boolean isInitialized();
	T client(final InterfaceProfile interfaceProfile);
}
