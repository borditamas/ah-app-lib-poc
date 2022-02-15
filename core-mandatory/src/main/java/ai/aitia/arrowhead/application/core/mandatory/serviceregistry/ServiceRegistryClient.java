package ai.aitia.arrowhead.application.core.mandatory.serviceregistry;

import java.util.List;

import ai.aitia.arrowhead.application.common.core.AbstractCoreClient;
import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.exception.InitializationException;
import ai.aitia.arrowhead.application.common.networking.Communicator;
import ai.aitia.arrowhead.application.common.networking.CommunicatorType;
import ai.aitia.arrowhead.application.common.networking.profile.InterfaceProfile;
import ai.aitia.arrowhead.application.common.networking.profile.Protocol;
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
	private final InterfaceProfile queryInterfaceProfile;
	
	private ServiceDiscoveryService serviceDiscoveryService;
	private MonitoringService monitoringService;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public ServiceRegistryClient(final Communicator communicator, final InterfaceProfile queryInterfaceProfile) {
		super(communicator);
		this.queryInterfaceProfile = queryInterfaceProfile;
		
		Ensure.notNull(super.communicator, "communicator is null.");
		Ensure.notNull(this.queryInterfaceProfile, "queryInterfaceProfile is null.");
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public CommunicatorType getCommunicatorType() {
		return super.communicatorType;
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public void initialize() {
		try {
			super.communicator.initialize();
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
	public boolean isAvailable() {
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
		switch (super.communicatorType) {
		case HTTPS:
			this.serviceDiscoveryService = createServiceDiscoveryService(new ServiceDiscoveryServiceHTTPS(super.communicator, this.queryInterfaceProfile));
			this.monitoringService = createMonitoringService(new MonitoringServiceHTTPS(super.communicator));
			break;

		default:
			throw new InitializationException("Unsupported communicator type: " + super.communicatorType.name());
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	private ServiceDiscoveryService createServiceDiscoveryService(final ServiceDiscoveryServiceHTTPS serviceDiscovery) {
		List<ServiceModel> services;
		try {
			services = serviceDiscoveryService.query(serviceDiscovery.getServiceQueryForm());
			
		} catch (final CommunicationException ex) {
			throw new InitializationException("CommunicationException occured while querying " + serviceDiscovery.getServiceName() + " service", ex);
		}

		if (services.size() < 1) {
			throw new InitializationException(serviceDiscovery.getServiceName() + " service was not discovered.");
		}
		
		final ServiceModel serviceModel = services.get(0);
		serviceDiscovery.load(serviceModel);		
		serviceDiscovery.verify();
		
		//We use this init to set the network address of the system
		super.setNetworkAddress(serviceModel.getOperations().get(0).getInterfaceProfiles().get(Protocol.valueOf(super.communicatorType)).getAddress(),
								serviceModel.getOperations().get(0).getInterfaceProfiles().get(Protocol.valueOf(super.communicatorType)).getPort());
		
		return serviceDiscovery;
	}
	
	//-------------------------------------------------------------------------------------------------
	private MonitoringService createMonitoringService(final MonitoringServiceHTTPS monitoring) {
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
		return monitoring;
	}
}
