package ai.aitia.arrowhead.application.common.service.model;

import java.util.List;

public class ServiceModel {
	
	private final String name;
	private final List<OperationModel> operations;
	
	public ServiceModel(final String name, final List<OperationModel> operations) {
		this.name = name;
		this.operations = operations;
	}

	public String getName() { return name; }
	public List<OperationModel> getOperations() { return operations; }
}
