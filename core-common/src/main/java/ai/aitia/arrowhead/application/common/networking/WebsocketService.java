package ai.aitia.arrowhead.application.common.networking;

public class WebsocketService implements Communication {

	@Override
	public CommunicationType getType() {
		return CommunicationType.WEBSOCKET;
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
