package ai.aitia.arrowhead.application.common.service;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;

public interface MonitoringService extends ArrowheadService {
	
	boolean echo() throws CommunicationException;
}
