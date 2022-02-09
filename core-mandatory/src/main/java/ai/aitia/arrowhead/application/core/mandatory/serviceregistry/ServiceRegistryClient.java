package ai.aitia.arrowhead.application.core.mandatory.serviceregistry;

import java.util.List;

import ai.aitia.arrowhead.application.common.core.AbstractCoreClient;
import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.exception.InitializationException;
import ai.aitia.arrowhead.application.common.networking.CommunicationType;
import ai.aitia.arrowhead.application.common.networking.HttpsService;
import ai.aitia.arrowhead.application.common.networking.properties.HttpMethod;
import ai.aitia.arrowhead.application.common.service.MonitoringService;
import ai.aitia.arrowhead.application.common.service.MonitoringServiceHTTPS;
import ai.aitia.arrowhead.application.common.service.model.ServiceModel;
import ai.aitia.arrowhead.application.common.verification.Ensure;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.ServiceDiscoveryService;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.ServiceDiscoveryServiceHTTPS;

public class ServiceRegistryClient extends AbstractCoreClient {

	//=================================================================================================
	// members
	
	private boolean initialized = false;
	private final String queryPath;
	private final HttpMethod queryMethod;
	
	private ServiceDiscoveryService serviceDiscoveryService;
	private MonitoringService monitoringService;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public ServiceRegistryClient(final HttpsService https, final String address, final int port, final String queryPath, final HttpMethod queryMethod) {
		super(https);
		super.setNetworkAddress(address, port);
		this.queryPath = queryPath;
		this.queryMethod = queryMethod;
		
		Ensure.notNull(super.communicationService, "communicationService is null.");
		Ensure.notEmpty(super.address, "address is null.");
		Ensure.portRange(port);
		Ensure.notNull(queryPath, "queryPath is null");
		Ensure.notNull(queryMethod, "queryMethod is null");
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
			super.communicationService.initialize();
			initializeServices();
			this.initialized = true;
			// TODO: info log
			
		} catch (final Exception ex) {
			// TODO: error log
			throw new InitializationException(ex.getMessage(), ex);
		}
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public boolean isInitialized() {
		return initialized;
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
		return this.monitoringService().echo();
	}
	
	//=================================================================================================
	// services
	
	//-------------------------------------------------------------------------------------------------
	public MonitoringService monitoringService() {
		verifyInitialization();
		return this.monitoringService;
	}
	
	//-------------------------------------------------------------------------------------------------
	public ServiceDiscoveryService serviceDiscoveryService() {
		verifyInitialization();
		return this.serviceDiscoveryService;
	}
	
	//=================================================================================================
	// assistant methods
	
	//-------------------------------------------------------------------------------------------------
	private void initializeServices() {
		switch (super.communicationType) {
		case HTTPS:
			final HttpsService https = (HttpsService)super.communicationService;
			this.serviceDiscoveryService = createServiceDiscoveryServiceHTTPS(https);
			this.monitoringService = createMonitoringServiceHTTPS(https);
			break;

		default:
			throw new InitializationException("Unsupported communication type: " + super.communicationType.name());
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	private ServiceDiscoveryService createServiceDiscoveryServiceHTTPS(final HttpsService https) {
		final ServiceDiscoveryServiceHTTPS serviceDiscovery = new ServiceDiscoveryServiceHTTPS(https, super.address, super.port, this.queryPath, this.queryMethod);
		
		List<ServiceModel> services;
		try {
			services = serviceDiscoveryService.query(serviceDiscovery.getServiceQueryForm());
			
		} catch (final CommunicationException ex) {
			throw new InitializationException("CommunicationException occured while querying " + serviceDiscovery.getServiceName() + " service", ex);
		}

		if (services.size() < 1) {
			throw new InitializationException(serviceDiscovery.getServiceName() + " service was not discovered.");
		}
		
		serviceDiscovery.load(services.get(0));		
		serviceDiscovery.verify();
		return serviceDiscovery;
	}
	
	//-------------------------------------------------------------------------------------------------
	private MonitoringService createMonitoringServiceHTTPS(final HttpsService https) {
		final MonitoringServiceHTTPS monitoring = new MonitoringServiceHTTPS(https);
		
		List<ServiceModel> services;
		try {
			services = serviceDiscoveryService.query(monitoring.getServiceQueryForm());
		} catch (final CommunicationException e) {
			throw new InitializationException("CommunicationException occured while querying " + monitoring.getServiceName() + " service");
		}
		
		if (services.size() < 1) {
			throw new InitializationException(monitoring.getServiceName() + " service was not discovered.");
		}
		
		monitoring.load(services.get(0));
		monitoring.verify();
		return monitoringService;
	}
}
