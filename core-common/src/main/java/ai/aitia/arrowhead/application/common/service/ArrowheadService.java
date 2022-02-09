package ai.aitia.arrowhead.application.common.service;

import ai.aitia.arrowhead.application.common.service.model.ServiceQueryModel;

public interface ArrowheadService {

	ServiceQueryModel getServiceQueryForm();
	void verify();
}
