package ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model;

import java.util.List;

public class ServiceModel {
	
	//=================================================================================================
	// members
	
	private final String name;
	private final List<OperationModel> operations;
	private final SystemModel system;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public ServiceModel(final String name, final List<OperationModel> operations, final SystemModel system) {
		this.name = name;
		this.operations = operations;
		this.system = system;
	}

	//-------------------------------------------------------------------------------------------------
	public String getName() { return name; }
	public List<OperationModel> getOperations() { return operations; }
	public SystemModel getSystem() { return system; }
}
