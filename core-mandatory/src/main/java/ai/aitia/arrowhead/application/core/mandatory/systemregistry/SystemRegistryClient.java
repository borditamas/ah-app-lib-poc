package ai.aitia.arrowhead.application.core.mandatory.systemregistry;

import java.util.List;

import ai.aitia.arrowhead.application.common.core.AbstractCoreClient;
import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.exception.InitializationException;
import ai.aitia.arrowhead.application.common.networking.Communicator;
import ai.aitia.arrowhead.application.common.networking.CommunicatorType;
import ai.aitia.arrowhead.application.common.networking.profile.CommunicatorProfile;
import ai.aitia.arrowhead.application.common.service.MonitoringService;
import ai.aitia.arrowhead.application.common.service.MonitoringServiceHTTPS;
import ai.aitia.arrowhead.application.common.service.model.ServiceModel;
import ai.aitia.arrowhead.application.common.verification.Ensure;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.ServiceRegistryClient;

public class SystemRegistryClient extends AbstractCoreClient {
	
	//=================================================================================================
	// members
	
	private final ServiceRegistryClient srClient;
	
	private MonitoringService monitoringService;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public SystemRegistryClient(final CommunicatorProfile communicatorProfile, final ServiceRegistryClient srClient) {
		super(communicatorProfile);
		this.srClient = srClient;
		
		Ensure.notNull(super.communicatorProfile, "communicatorProfile is null.");
		Ensure.notNull(this.srClient, "srClient is null.");
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void initialize() {
		try {
			this.srClient.verifyInitialization();
			initializeServices();
			// TODO: info log
			
		} catch (final Exception ex) {
			// TODO: error log
			throw new InitializationException(ex.getMessage(), ex);
		}
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
		if (super.communicatorProfile.contains(MonitoringService.NAME)) {
			final Communicator communicator = super.communicatorProfile.communicator(MonitoringService.NAME);
			if (communicator != null && communicator.type() == CommunicatorType.HTTPS) {
				this.monitoringService = createMonitoringService(new MonitoringServiceHTTPS(communicator));
			}			
		}		
	}
	
	//-------------------------------------------------------------------------------------------------
	private MonitoringServiceHTTPS createMonitoringService(final MonitoringServiceHTTPS monitoring) {		
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
		
		return monitoring;
	}
}
