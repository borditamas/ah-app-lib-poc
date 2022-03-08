package ai.aitia.arrowhead.application.core.support.datamanager.service;

import java.util.List;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.decoder.exception.PayloadDecodingException;
import ai.aitia.arrowhead.application.common.service.ArrowheadService;
import ai.aitia.arrowhead.application.core.support.datamanager.service.model.SenML;

public interface HistorianService extends ArrowheadService {
	
	public static final String NAME = "historian";

	public SenML getMeasurements(final String systemName, final String serviceName, final boolean terminate) throws CommunicationException, PayloadDecodingException; //TODO change to proper output
	public void putMeasurements(final String systemName, final String serviceName, final List<SenML> measurements, final boolean terminate) throws CommunicationException; //TODO change to proper input
}
