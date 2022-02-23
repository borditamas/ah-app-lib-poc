package ai.aitia.arrowhead.application.common.networking;

import ai.aitia.arrowhead.application.common.networking.profile.InterfaceProfile;

public interface Communicator {

	public CommunicatorType type();
	public void properties(final CommunicationProperties props);
	public void initialize();
	public boolean isInitialized();
	public CommunicationClient client(final InterfaceProfile interfaceProfile);
}
