package ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service;

import java.util.List;

import ai.aitia.arrowhead.application.common.service.ArrowheadService;
import ai.aitia.arrowhead.application.common.service.model.ServiceModel;
import ai.aitia.arrowhead.application.common.service.model.ServiceQueryModel;

public interface ServiceDiscoveryService extends ArrowheadService {

	ServiceModel register(final ServiceModel service);
	boolean unregister(final ServiceModel service);
	List<ServiceModel> query(final ServiceQueryModel from);
}
