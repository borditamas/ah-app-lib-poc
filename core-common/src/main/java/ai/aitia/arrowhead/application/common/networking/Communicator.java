package ai.aitia.arrowhead.application.common.networking;

public interface Communicator {

	public CommunicatorType getType();
	public void initialize();
	public boolean isInitialized();
}
