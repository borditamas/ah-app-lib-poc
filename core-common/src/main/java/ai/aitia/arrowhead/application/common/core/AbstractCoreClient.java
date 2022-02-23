package ai.aitia.arrowhead.application.common.core;

import ai.aitia.arrowhead.application.common.networking.profile.CommunicationProfile;

public abstract class AbstractCoreClient implements CoreClient {
	
	//=================================================================================================
	// members
	
	protected final CommunicationProfile communicationProfile;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	protected AbstractCoreClient(final CommunicationProfile communicationProfile) {
		this.communicationProfile = communicationProfile;
	}
}
