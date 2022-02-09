package ai.aitia.arrowhead.application.common.service;

import java.util.List;

import ai.aitia.arrowhead.application.common.service.model.OperationModel;
import ai.aitia.arrowhead.application.common.service.model.ServiceQueryModel;

public interface ArrowheadService {

	
	String getServiceName();
	ServiceQueryModel getServiceQueryForm();
	void initialize(List<OperationModel> operations);
	void verify();
}
