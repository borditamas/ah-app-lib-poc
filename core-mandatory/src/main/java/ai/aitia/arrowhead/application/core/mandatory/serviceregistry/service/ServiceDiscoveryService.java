package ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service;

import java.util.List;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.service.ArrowheadService;
import ai.aitia.arrowhead.application.common.service.model.ServiceModel;
import ai.aitia.arrowhead.application.common.service.model.ServiceQueryModel;

public interface ServiceDiscoveryService extends ArrowheadService {
	
	public static final String NAME = "service-discovery";

	ServiceModel register(final ServiceModel service) throws CommunicationException;
	boolean unregister(final ServiceModel service) throws CommunicationException;
	List<ServiceModel> query(final ServiceQueryModel from) throws CommunicationException;
}
