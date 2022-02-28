package ai.aitia.arrowhead.application.common.service.model;

import java.util.Map;

import ai.aitia.arrowhead.application.common.networking.profile.InterfaceProfile;
import ai.aitia.arrowhead.application.common.networking.profile.Protocol;

public class OperationModel {

	//=================================================================================================
	// members
	
	private final String operation;
	private final Map<Protocol,InterfaceProfile> interfaceProfiles;
	
	//-------------------------------------------------------------------------------------------------
	public OperationModel(final String operation, final Map<Protocol,InterfaceProfile> interfaceProfiles) {
		this.operation = operation;
		this.interfaceProfiles = interfaceProfiles;
	}
	
	//-------------------------------------------------------------------------------------------------
	public String getOperation() { return operation; } 
	public Map<Protocol,InterfaceProfile> getInterfaceProfiles() { return interfaceProfiles; }
}
