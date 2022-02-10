package ai.aitia.arrowhead.application.common.core;

import ai.aitia.arrowhead.application.common.networking.Communicator;
import ai.aitia.arrowhead.application.common.networking.CommunicatorType;

public abstract class AbstractCoreClient implements CoreClient {
	
	//=================================================================================================
	// members
	
	protected String address;
	protected int port;
	
	protected final Communicator communicator;
	protected final CommunicatorType communicatorType;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	protected AbstractCoreClient(final Communicator communicatior) {
		this.communicator = communicatior; 
		this.communicatorType = this.communicator.getType();
	}

	//-------------------------------------------------------------------------------------------------
	protected void setNetworkAddress(final String address, final int port) {
		this.address = address;
		this.port = port;
	}
}
