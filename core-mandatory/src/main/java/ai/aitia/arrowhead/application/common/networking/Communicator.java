package ai.aitia.arrowhead.application.common.networking;

import ai.aitia.arrowhead.application.common.networking.decoder.PayloadDecoder;
import ai.aitia.arrowhead.application.common.networking.profile.InterfaceProfile;

public interface Communicator {

	public CommunicatorType type();
	public void properties(final CommunicationProperties props);
	public void decoder(final PayloadDecoder decoder);
	public void initialize();
	public boolean isInitialized();
	public CommunicationClient client(final InterfaceProfile interfaceProfile);
}
