package ai.aitia.arrowhead.application.common.networking;

public class WebsocketCommunicator implements Communicator {

	@Override
	public CommunicatorType getType() {
		return CommunicatorType.WEBSOCKET;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

}
