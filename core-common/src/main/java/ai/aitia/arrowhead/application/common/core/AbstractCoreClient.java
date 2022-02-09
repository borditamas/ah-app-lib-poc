package ai.aitia.arrowhead.application.common.core;

import ai.aitia.arrowhead.application.common.networking.Communication;
import ai.aitia.arrowhead.application.common.networking.CommunicationType;

public abstract class AbstractCoreClient implements CoreClient {
	
	protected String address;
	protected int port;
	
	protected final Communication communicationService;
	protected final CommunicationType communicationType;
	
	protected AbstractCoreClient(final Communication communicationService) {
		this.communicationService = communicationService; 
		this.communicationType = this.communicationService.getType();
	}

	protected void setNetworkAddress(final String address, final int port) {
		this.address = address;
		this.port = port;
	}
}
