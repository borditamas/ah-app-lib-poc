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
import ai.aitia.arrowhead.application.common.verification.Ensure;
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
		
		Ensure.notNull(super.communicationService, "communicationService is null.");
		Ensure.notNull(this.srClient, "srClient is null.");
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
			
			Ensure.notEmpty(super.address, "address is empty");
			Ensure.portRange(super.port);
			// TODO: info log
			
		} catch (final Exception ex) {
			// TODO: error log
			throw new InitializationException(ex.getMessage(), ex);
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

		default:
			throw new InitializationException("Unsupported communication type: " + super.communicationType.name());
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	private MonitoringService createMonitoringServiceHTTPS(final HttpsService https) {
		final MonitoringServiceHTTPS monitoring = new MonitoringServiceHTTPS(https);
		
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
		
		final ServiceModel serviceModel = services.get(0);
		monitoring.load(serviceModel);
		monitoring.verify();
		
		super.setNetworkAddress(serviceModel.getOperations().get(0).getHttpsProperties().getAddress(),
								serviceModel.getOperations().get(0).getHttpsProperties().getPort().intValue());
		
		return monitoringService;
	}
}
