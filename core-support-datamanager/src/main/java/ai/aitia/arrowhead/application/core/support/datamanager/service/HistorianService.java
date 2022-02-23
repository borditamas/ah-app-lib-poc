package ai.aitia.arrowhead.application.core.support.datamanager.service;

import java.util.List;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.service.ArrowheadService;

public interface HistorianService extends ArrowheadService {
	
	public static final String NAME = "historian";

	public List<String> getData() throws CommunicationException; //TODO change to proper output
	public void putData(final List<String> senML) throws CommunicationException; //TODO change to proper input
}
