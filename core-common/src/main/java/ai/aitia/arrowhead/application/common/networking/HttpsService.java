package ai.aitia.arrowhead.application.common.networking;

public class HttpsService implements Communication{

	@Override
	public CommunicationType getType() {
		return CommunicationType.HTTPS;
	}
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	public <T> T send(final String address, final int port, final String path, final Class<T> responseType) {
		// TODO Auto-generated method stub
		return null;
	}

	public <T, P> T send(final String address, final int port, final String path, P payload, final Class<T> responseType) {
		// TODO Auto-generated method stub
		return null;
	}
}
