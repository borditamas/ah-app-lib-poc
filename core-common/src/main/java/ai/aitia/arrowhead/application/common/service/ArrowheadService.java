package ai.aitia.arrowhead.application.common.service;

import ai.aitia.arrowhead.application.common.service.model.ServiceModel;
import ai.aitia.arrowhead.application.common.service.model.ServiceQueryModel;

public interface ArrowheadService {
	
	public String getServiceName();
	public ServiceQueryModel getServiceQueryForm();
	public void load(final ServiceModel service);
	public void verify();
}
