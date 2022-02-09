package ai.aitia.arrowhead.application.common.service;

import ai.aitia.arrowhead.application.common.service.model.ServiceQueryModel;

public interface ArrowheadService {

	String getServiceName();
	ServiceQueryModel getServiceQueryForm();
	void verify();
}
