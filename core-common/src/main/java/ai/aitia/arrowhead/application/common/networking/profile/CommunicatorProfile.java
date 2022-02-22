package ai.aitia.arrowhead.application.common.networking.profile;

import java.util.HashMap;
import java.util.Map;

import ai.aitia.arrowhead.application.common.networking.Communicator;

public class CommunicatorProfile {

	//=================================================================================================
	// members
	
	private final Map<String,Communicator> profiles = new HashMap<>();
	
	public void put(final String serviceName, final Communicator communicator) {
		this.profiles.put(serviceName, communicator);
	}
	
	public Communicator communicator(final String serviceName) {
		return this.profiles.get(serviceName);
	}
	
	public boolean contains(final String serviceName) {
		return this.profiles.containsKey(serviceName);
	}
}
