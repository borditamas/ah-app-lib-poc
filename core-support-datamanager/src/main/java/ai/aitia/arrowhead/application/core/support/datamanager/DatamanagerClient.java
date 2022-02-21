package ai.aitia.arrowhead.application.core.support.datamanager;

import java.util.List;

import ai.aitia.arrowhead.application.common.core.AbstractCoreClient;
import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.exception.InitializationException;
import ai.aitia.arrowhead.application.common.networking.Communicator;
import ai.aitia.arrowhead.application.common.networking.CommunicatorType;
import ai.aitia.arrowhead.application.common.networking.profile.Protocol;
import ai.aitia.arrowhead.application.common.service.MonitoringService;
import ai.aitia.arrowhead.application.common.service.MonitoringServiceHTTPS;
import ai.aitia.arrowhead.application.common.service.model.ServiceModel;
import ai.aitia.arrowhead.application.common.verification.Ensure;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.ServiceRegistryClient;

public class DatamanagerClient extends AbstractCoreClient {

	//=================================================================================================
	// members
	
	private final boolean initialized = false;
	private final ServiceRegistryClient srClient;
	
	private MonitoringService monitoringService;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public DatamanagerClient(final Communicator communicator, final ServiceRegistryClient srClient) {
		super(communicator);
		this.srClient = srClient;
		
		Ensure.notNull(super.communicator, "communicator is null.");
		Ensure.notNull(this.srClient, "srClient is null.");
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
			this.srClient.verifyInitialization();
			super.communicator.initialize();
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
		switch (super.communicatorType) {
		case HTTPS:
			this.monitoringService = createMonitoringService(new MonitoringServiceHTTPS(this.communicator));
			break;

		default:
			throw new InitializationException("Unsupported communicator type: " + super.communicatorType.name());
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	private MonitoringService createMonitoringService(final MonitoringService monitoring) {		
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
		
		//We use this init to set the network address of the system
		super.setNetworkAddress(serviceModel.getOperations().get(0).getInterfaceProfiles().get(Protocol.valueOf(super.communicatorType)).getAddress(),
								serviceModel.getOperations().get(0).getInterfaceProfiles().get(Protocol.valueOf(super.communicatorType)).getPort());
		
		return monitoring;
	}
}
