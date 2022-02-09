package ai.aitia.arrowhead.application.core.mandatory.systemregistry;

import java.util.List;

import ai.aitia.arrowhead.application.common.core.AbstractCoreClient;
import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.exception.InitializationException;
import ai.aitia.arrowhead.application.common.networking.Communication;
import ai.aitia.arrowhead.application.common.networking.CommunicationType;
import ai.aitia.arrowhead.application.common.networking.HttpsService;
import ai.aitia.arrowhead.application.common.service.MonitoringService;
import ai.aitia.arrowhead.application.common.service.MonitoringServiceHTTPS;
import ai.aitia.arrowhead.application.common.service.model.ServiceModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.ServiceRegistryClient;

public class SystemRegistryClient extends AbstractCoreClient {
	
	//=================================================================================================
	// members
	
	private final boolean initialized = false;
	private final ServiceRegistryClient srClient;
	
	private MonitoringService monitoringService;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public SystemRegistryClient(final Communication communicationService, final ServiceRegistryClient srClient) {
		super(communicationService);
		this.srClient = srClient;
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public CommunicationType getCommunicationType() {
		return super.communicationType;
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public void initialize() {
		try {
			this.srClient.verifyInitialization();
			super.communicationService.initialize();
			initializeServices();
			// TODO: info log
			
		} catch (final Exception ex) {
			// TODO: error log
			throw new InitializationException(ex.getMessage());
		}
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public boolean isInitialized() {
		return this.initialized;
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void verifyInitialization() {
		if (!isInitialized()) {
			throw new InitializationException("Not initialized");
		}		
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public boolean isAvailable() throws CommunicationException {
		return monitoringService().echo();
	}
	
	//=================================================================================================
	// services
	
	//-------------------------------------------------------------------------------------------------
	public MonitoringService monitoringService() {
		verifyInitialization();
		return this.monitoringService;
	}
	
	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------
	private void initializeServices() {
		switch (super.communicationType) {
		case HTTPS:
			final HttpsService https = (HttpsService)super.communicationService;
			this.monitoringService = createMonitoringServiceHTTPS(https);
			break;
			
		case WEBSOCKET:
			//TODO
			break;

		default:
			throw new InitializationException("Unsupported communication type: " + super.communicationType.name());
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	private MonitoringService createMonitoringServiceHTTPS(final HttpsService https) {
		final MonitoringServiceHTTPS monitoring = new MonitoringServiceHTTPS(https, super.address, super.port);
		
		List<ServiceModel> services;
		try {
			services = srClient.serviceDiscoveryService().query(monitoring.getServiceQueryForm());
		} catch (final InitializationException ex) {
			throw new InitializationException("Service Registry is not initialized.");
			
		} catch (final CommunicationException ex) {
			throw new InitializationException("CommunicationException occured while querying " + monitoring.getServiceName() + " service", ex);
		}
		
		if (services.size() < 1) {
			throw new InitializationException(monitoring.getServiceName() + " service was not discovered.");
		}
		
		monitoring.initialize(services.get(0).getOperations());
		monitoring.verify();
		return monitoringService;
	}
}
