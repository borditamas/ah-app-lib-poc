package ai.aitia.arrowhead.application.common.core;

import ai.aitia.arrowhead.application.common.networking.profile.CommunicatorProfile;

public abstract class AbstractCoreClient implements CoreClient {
	
	//=================================================================================================
	// members
	
	protected final CommunicatorProfile communicatorProfile;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	protected AbstractCoreClient(final CommunicatorProfile communicatorProfile) {
		this.communicatorProfile = communicatorProfile;
	}
}
