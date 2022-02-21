package ai.aitia.arrowhead.application.common.networking;

public interface Communicator<T> {

	CommunicatorType type();
	void properties(final CommunicationProperties props);
	void initialize();
	boolean isInitialized();
	T client();
}
