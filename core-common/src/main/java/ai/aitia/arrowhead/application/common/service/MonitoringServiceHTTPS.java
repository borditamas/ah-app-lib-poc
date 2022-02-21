package ai.aitia.arrowhead.application.common.service;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.CommunicationClient;
import ai.aitia.arrowhead.application.common.networking.Communicator;
import ai.aitia.arrowhead.application.common.networking.CommunicatorType;
import ai.aitia.arrowhead.application.common.networking.profile.InterfaceProfile;
import ai.aitia.arrowhead.application.common.networking.profile.Protocol;
import ai.aitia.arrowhead.application.common.networking.profile.http.HttpsKey;
import ai.aitia.arrowhead.application.common.service.model.OperationModel;
import ai.aitia.arrowhead.application.common.service.model.ServiceModel;
import ai.aitia.arrowhead.application.common.service.model.ServiceQueryModel;
import ai.aitia.arrowhead.application.common.verification.Ensure;

public class MonitoringServiceHTTPS implements MonitoringService {
	
	//=================================================================================================
	// members
	
	private final String name = "monitoring";
	
	private CommunicationClient httpsClient;	
	
	private final String echoOperation = "echo";
	private InterfaceProfile echoInterfaceProfile;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public MonitoringServiceHTTPS(final Communicator<CommunicationClient> communicator) {
		Ensure.notNull(communicator, "Communicator is null");
		Ensure.isTrue(communicator.type() == CommunicatorType.HTTPS, "Communicator is not for HTTPS");
		Ensure.isTrue(communicator.isInitialized(), "https is not initialized");
		this.httpsClient = communicator.client();
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public String getServiceName() {
		return this.name;
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
		Ensure.isTrue(service.getName().equalsIgnoreCase(this.name), "Service name missmatch");
		Ensure.notEmpty(service.getOperations(), "operation list is empty");
		
		for (final OperationModel operation : service.getOperations()) {
			Ensure.notNull(operation, "operation is null");
			Ensure.isTrue(operation.getInterfaceProfiles().containsKey(Protocol.HTTP), "operation have not HTTP profile");
			
			final InterfaceProfile interfaceProfile = operation.getInterfaceProfiles().get(Protocol.HTTP);
			if (operation.getOperation().equalsIgnoreCase(this.echoOperation)) {
				Ensure.notEmpty(interfaceProfile.getAddress(), "echo operation address is empty");
				Ensure.portRange(interfaceProfile.getPort());
				Ensure.isTrue(interfaceProfile.contains(HttpsKey.PATH), "no path for echo operation");
				Ensure.isTrue(interfaceProfile.contains(HttpsKey.METHOD), "no http method for echo operation");
				this.echoInterfaceProfile = interfaceProfile;
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
			httpsClient.send(this.echoInterfaceProfile, Void.class, null);
			return true;
		} catch (final CommunicationException ex) {
			return false;
		}
	}
}
