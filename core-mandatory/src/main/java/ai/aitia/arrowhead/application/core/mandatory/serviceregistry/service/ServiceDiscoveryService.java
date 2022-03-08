package ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service;

import java.util.List;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.decoder.exception.PayloadDecodingException;
import ai.aitia.arrowhead.application.common.service.ArrowheadService;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceQueryModel;

public interface ServiceDiscoveryService extends ArrowheadService {
	
	public static final String NAME = "service-discovery";

	ServiceModel register(final ServiceModel service) throws CommunicationException, PayloadDecodingException;
	boolean unregister(final ServiceModel service) throws CommunicationException, PayloadDecodingException;
	List<ServiceModel> query(final ServiceQueryModel from) throws CommunicationException, PayloadDecodingException;
}
