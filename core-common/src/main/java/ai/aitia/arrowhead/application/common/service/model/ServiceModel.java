package ai.aitia.arrowhead.application.common.service.model;

import java.util.List;

public class ServiceModel {
	
	private final String serviceName;
	private final List<OperationModel> operations;
	
	public ServiceModel(final String serviceName, final List<OperationModel> operations) {
		this.serviceName = serviceName;
		this.operations = operations;
	}

	public String getServiceName() { return serviceName; }
	public List<OperationModel> getOperations() { return operations; }
}
