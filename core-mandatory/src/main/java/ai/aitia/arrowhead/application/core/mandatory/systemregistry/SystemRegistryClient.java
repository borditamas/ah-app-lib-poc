package ai.aitia.arrowhead.application.core.mandatory.systemregistry;

import java.util.List;

import ai.aitia.arrowhead.application.common.core.AbstractCoreClient;
import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.exception.InitializationException;
import ai.aitia.arrowhead.application.common.networking.Communicator;
import ai.aitia.arrowhead.application.common.networking.CommunicatorType;
import ai.aitia.arrowhead.application.common.networking.profile.CommunicationProfile;
import ai.aitia.arrowhead.application.common.service.MonitoringService;
import ai.aitia.arrowhead.application.common.service.MonitoringServiceHTTPS;
import ai.aitia.arrowhead.application.common.verification.Ensure;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.ServiceDiscoveryService;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceModel;

public class SystemRegistryClient extends AbstractCoreClient {
	
	//=================================================================================================
	// members
	
	private final ServiceDiscoveryService discovery;
	
	private MonitoringService monitoringService;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public SystemRegistryClient(final CommunicationProfile communicationProfile, final ServiceDiscoveryService discovery) {
		super(communicationProfile);
		this.discovery = discovery;
		
		Ensure.notNull(super.communicationProfile, "CommunicationProfile is null.");
		Ensure.notNull(this.discovery, "ServiceDiscoveryService is null.");
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void initialize() {
		try {
			this.discovery.verify();
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
		return this.monitoringService != null;
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
		if (super.communicationProfile.contains(MonitoringService.NAME)) {
			final Communicator communicator = super.communicationProfile.communicator(MonitoringService.NAME);
			if (communicator != null && communicator.type() == CommunicatorType.HTTPS) {
				this.monitoringService = createMonitoringService(new MonitoringServiceHTTPS(communicator));
			}			
		}		
	}
	
	//-------------------------------------------------------------------------------------------------
	private MonitoringServiceHTTPS createMonitoringService(final MonitoringServiceHTTPS monitoring) {		
		List<ServiceModel> services;
		try {
			services = this.discovery.query(monitoring.getServiceQueryForm());
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
		
		return monitoring;
	}
}
