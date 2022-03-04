package ai.aitia.arrowhead.application.core.mandatory.serviceregistry;

import java.util.List;

import ai.aitia.arrowhead.application.common.core.AbstractCoreClient;
import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.exception.InitializationException;
import ai.aitia.arrowhead.application.common.networking.Communicator;
import ai.aitia.arrowhead.application.common.networking.profile.CommunicationProfile;
import ai.aitia.arrowhead.application.common.networking.profile.InterfaceProfile;
import ai.aitia.arrowhead.application.common.service.MonitoringService;
import ai.aitia.arrowhead.application.common.service.MonitoringServiceHTTPS;
import ai.aitia.arrowhead.application.common.verification.Ensure;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.ServiceDiscoveryService;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.ServiceDiscoveryServiceHTTPS;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.ServiceDiscoveryServiceMQTT;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceModel;

public class ServiceRegistryClient extends AbstractCoreClient {

	//=================================================================================================
	// members
	
	private final InterfaceProfile queryInterfaceProfile;
	
	private ServiceDiscoveryService serviceDiscoveryService;
	private MonitoringService monitoringService;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public ServiceRegistryClient(final CommunicationProfile communicationProfile, final InterfaceProfile queryInterfaceProfile) {
		super(communicationProfile);
		this.queryInterfaceProfile = queryInterfaceProfile;
		
		Ensure.notNull(super.communicationProfile, "communicationProfile is null.");
		Ensure.notNull(this.queryInterfaceProfile, "queryInterfaceProfile is null.");
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void initialize() {
		try {
			initializeServices();
			// TODO: info log
			
		} catch (final Exception ex) {
			// TODO: error log
			throw new InitializationException(ex.getMessage(), ex);
		}
		verifyInitialization();
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public boolean isInitialized() {
		return this.serviceDiscoveryService != null && this.monitoringService != null;
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
		if (super.communicationProfile.contains(ServiceDiscoveryService.NAME)) {
			final Communicator communicator = super.communicationProfile.communicator(ServiceDiscoveryService.NAME);
			if (communicator != null ) {
				switch (communicator.type()) {
				case HTTPS:
					this.serviceDiscoveryService = createServiceDiscoveryService(new ServiceDiscoveryServiceHTTPS(communicator, this.queryInterfaceProfile));
					break;
				case MQTT:
					this.serviceDiscoveryService = createServiceDiscoveryService(new ServiceDiscoveryServiceMQTT(communicator, this.queryInterfaceProfile));
					break;
				default:
					throw new InitializationException("Communicator type not supported");
				}
			}			
		}
		
		if (super.communicationProfile.contains(MonitoringService.NAME)) {
			final Communicator communicator = super.communicationProfile.communicator(MonitoringService.NAME);
			if (communicator != null) {
				switch (communicator.type()) {
				case HTTPS:
					this.monitoringService = createMonitoringService(new MonitoringServiceHTTPS(communicator));
					break;
				case MQTT:
					//this.monitoringService = createMonitoringService(new MonitoringServiceMQTT(communicator));
					break;
				default:
					throw new InitializationException("Communicator type not supported");
				}
			}			
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	private ServiceDiscoveryService createServiceDiscoveryService(final ServiceDiscoveryService serviceDiscovery) {
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
		return serviceDiscovery;
	}
	
	//-------------------------------------------------------------------------------------------------
	private MonitoringService createMonitoringService(final MonitoringService monitoring) {
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
