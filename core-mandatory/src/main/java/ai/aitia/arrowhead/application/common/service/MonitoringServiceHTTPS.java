package ai.aitia.arrowhead.application.common.service;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.CommunicationClient;
import ai.aitia.arrowhead.application.common.networking.Communicator;
import ai.aitia.arrowhead.application.common.networking.CommunicatorType;
import ai.aitia.arrowhead.application.common.networking.profile.InterfaceProfile;
import ai.aitia.arrowhead.application.common.networking.profile.Protocol;
import ai.aitia.arrowhead.application.common.networking.profile.http.HttpMethod;
import ai.aitia.arrowhead.application.common.networking.profile.http.HttpsKey;
import ai.aitia.arrowhead.application.common.verification.Ensure;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.OperationModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceQueryModel;

public class MonitoringServiceHTTPS implements MonitoringService {
	
	//=================================================================================================
	// members
	
	private Communicator communicator;	
	
	private final String echoOperation = "echo";
	private CommunicationClient echoHttpsClient;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public MonitoringServiceHTTPS(final Communicator communicator) {
		Ensure.notNull(communicator, "Communicator is null");
		Ensure.isTrue(communicator.type() == CommunicatorType.HTTPS, "Communicator is not for HTTPS");
		Ensure.isTrue(communicator.isInitialized(), "https is not initialized");
		this.communicator = communicator;
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public String getServiceName() {
		return MonitoringService.NAME;
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public ServiceQueryModel getServiceQueryForm() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void load(final ServiceModel service) {
		Ensure.notNull(service, "service is null");
		Ensure.isTrue(service.getName().equalsIgnoreCase(MonitoringService.NAME), "Service name missmatch");
		Ensure.notEmpty(service.getOperations(), "operation list is empty");
		
		for (final OperationModel operation : service.getOperations()) {
			Ensure.notNull(operation, "operation is null");
			Ensure.isTrue(operation.getInterfaceProfiles().containsKey(Protocol.HTTP), "operation have not HTTP profile");
			
			final InterfaceProfile interfaceProfile = operation.getInterfaceProfiles().get(Protocol.HTTP);
			if (operation.getOperation().equalsIgnoreCase(this.echoOperation)) {
				Ensure.notEmpty(interfaceProfile.get(String.class, HttpsKey.ADDRESS), "echo operation address is empty");
				Ensure.portRange(interfaceProfile.get(Integer.class, HttpsKey.PORT));
				Ensure.notNull(interfaceProfile.get(HttpMethod.class, HttpsKey.METHOD), "no http method for echo operation");
				this.echoHttpsClient = communicator.client(interfaceProfile);
			}
		}		
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public void verify() {
		// TODO Auto-generated method stub
		
	}

	//=================================================================================================
	// operations
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public boolean echo() {
		try {
			this.echoHttpsClient.send(null);
			this.echoHttpsClient.receive(null); // Later it will contain some data
			return true;
		} catch (final CommunicationException ex) {
			return false;
		}
	}
}
