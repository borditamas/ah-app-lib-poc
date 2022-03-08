package ai.aitia.arrowhead.application.core.support.datamanager.service;

import java.util.List;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.decoder.exception.PayloadDecodingException;
import ai.aitia.arrowhead.application.common.service.ArrowheadService;

public interface HistorianService extends ArrowheadService {
	
	public static final String NAME = "historian";

	public List<String> getData(final String systemName, final String serviceName, final boolean terminate) throws CommunicationException, PayloadDecodingException; //TODO change to proper output
	public void putData(final String systemName, final String serviceName, final List<String> senML, final boolean terminate) throws CommunicationException; //TODO change to proper input
}
