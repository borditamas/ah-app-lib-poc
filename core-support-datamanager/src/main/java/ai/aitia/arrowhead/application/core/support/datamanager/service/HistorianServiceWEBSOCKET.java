package ai.aitia.arrowhead.application.core.support.datamanager.service;

import java.util.List;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.CommunicationClient;
import ai.aitia.arrowhead.application.common.networking.Communicator;
import ai.aitia.arrowhead.application.common.networking.CommunicatorType;
import ai.aitia.arrowhead.application.common.networking.profile.InterfaceProfile;
import ai.aitia.arrowhead.application.common.networking.profile.Protocol;
import ai.aitia.arrowhead.application.common.networking.profile.websocket.WebsocketKey;
import ai.aitia.arrowhead.application.common.verification.Ensure;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.OperationModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceQueryModel;

public class HistorianServiceWEBSOCKET implements HistorianService {

	//=================================================================================================
	// members
	
	private final String name = "historian";
	
	private Communicator communicator;	
	
	private final String getDataOperation = "get-data";
	private CommunicationClient getDataWSClient;
	
	private final String putDataOperation = "put-data";
	private CommunicationClient putDataWSClient;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public HistorianServiceWEBSOCKET(final Communicator communicator) {
		Ensure.notNull(communicator, "Communicator is null");
		Ensure.isTrue(communicator.type() == CommunicatorType.WEBSOCKET, "Communicator is not for WEBSOCKET");
		Ensure.isTrue(communicator.isInitialized(), "Communicator is not initialized");
		this.communicator = communicator;
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
			Ensure.isTrue(operation.getInterfaceProfiles().containsKey(Protocol.WEBSOCKET), "operation have not WEBSOCKET profile");
			
			final InterfaceProfile interfaceProfile = operation.getInterfaceProfiles().get(Protocol.WEBSOCKET);
			if (operation.getOperation().equalsIgnoreCase(this.getDataOperation)) {
				Ensure.notEmpty(interfaceProfile.getAddress(), "get-data operation address is empty");
				Ensure.portRange(interfaceProfile.getPort());
				Ensure.isTrue(interfaceProfile.contains(WebsocketKey.PATH), "no path for get-data operation");
				this.getDataWSClient = communicator.client(interfaceProfile);
			}
			if (operation.getOperation().equalsIgnoreCase(this.putDataOperation)) {
				Ensure.notEmpty(interfaceProfile.getAddress(), "put-data operation address is empty");
				Ensure.portRange(interfaceProfile.getPort());
				Ensure.isTrue(interfaceProfile.contains(WebsocketKey.PATH), "no path for put-data operation");
				this.putDataWSClient = communicator.client(interfaceProfile);
			}
		}			
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public void verify() {
		// TODO Auto-generated method stub
		
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public List<String> getData() throws CommunicationException {
		return this.getDataWSClient.receive(List.class);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void putData(final List<String> senML) throws CommunicationException {
		this.putDataWSClient.send(senML);
	}
}
