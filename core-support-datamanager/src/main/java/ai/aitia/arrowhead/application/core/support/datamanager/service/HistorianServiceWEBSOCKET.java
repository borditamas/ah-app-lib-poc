package ai.aitia.arrowhead.application.core.support.datamanager.service;

import java.util.List;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.CommunicationClient;
import ai.aitia.arrowhead.application.common.networking.Communicator;
import ai.aitia.arrowhead.application.common.networking.CommunicatorType;
import ai.aitia.arrowhead.application.common.networking.decoder.MediaType;
import ai.aitia.arrowhead.application.common.networking.decoder.PayloadResolver;
import ai.aitia.arrowhead.application.common.networking.decoder.exception.PayloadDecodingException;
import ai.aitia.arrowhead.application.common.networking.profile.InterfaceProfile;
import ai.aitia.arrowhead.application.common.networking.profile.MessageProperties;
import ai.aitia.arrowhead.application.common.networking.profile.Protocol;
import ai.aitia.arrowhead.application.common.networking.profile.model.PathVariables;
import ai.aitia.arrowhead.application.common.networking.profile.websocket.WebsocketKey;
import ai.aitia.arrowhead.application.common.networking.profile.websocket.WebsocketMsgKey;
import ai.aitia.arrowhead.application.common.verification.Ensure;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.OperationModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceQueryModel;

public class HistorianServiceWEBSOCKET implements HistorianService {

	//=================================================================================================
	// members
	
	private Communicator communicator;	
	
	private final String getDataOperation = "get-data";
	private CommunicationClient getDataWSClient;
	private boolean getDataConnected = false;
	
	private final String putDataOperation = "put-data";
	private CommunicationClient putDataWSClient;
	private boolean putDataConnected = false;
	
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
		return HistorianService.NAME;
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
		Ensure.isTrue(service.getName().equalsIgnoreCase(HistorianService.NAME), "Service name missmatch");
		Ensure.notEmpty(service.getOperations(), "operation list is empty");
		
		for (final OperationModel operation : service.getOperations()) {
			Ensure.notNull(operation, "operation is null");
			Ensure.isTrue(operation.getInterfaceProfiles().containsKey(Protocol.WEBSOCKET), "operation have no WEBSOCKET profile");
			
			final InterfaceProfile interfaceProfile = operation.getInterfaceProfiles().get(Protocol.WEBSOCKET);
			if (operation.getOperation().equalsIgnoreCase(this.getDataOperation)) {
				Ensure.notEmpty(interfaceProfile.get(String.class, WebsocketKey.ADDRESS), "get-data operation address is empty");
				Ensure.portRange(interfaceProfile.get(Integer.class, WebsocketKey.PORT));
				Ensure.notEmpty(interfaceProfile.get(String.class, WebsocketKey.PATH), "no path for get-data operation");
				this.getDataWSClient = communicator.client(interfaceProfile);
			}
			if (operation.getOperation().equalsIgnoreCase(this.putDataOperation)) {
				Ensure.notEmpty(interfaceProfile.get(String.class, WebsocketKey.ADDRESS), "put-data operation address is empty");
				Ensure.portRange(interfaceProfile.get(Integer.class, WebsocketKey.PORT));
				Ensure.notEmpty(interfaceProfile.get(String.class, WebsocketKey.PATH), "no path for put-data operation");
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
	public List<String> getData(final String systemName, final String serviceName, final boolean terminate) throws CommunicationException {
		if (!this.getDataConnected) {
			final MessageProperties props = new MessageProperties();
			props.add(WebsocketMsgKey.PATH_VARIABLES, new PathVariables(List.of(systemName, serviceName)));
			this.getDataWSClient.send(props, null);
			this.getDataConnected = true;
		}
		
		final PayloadResolver resolver = new PayloadResolver(MediaType.JSON);
		this.getDataWSClient.receive(resolver);
		
		if (terminate) {
			this.getDataWSClient.terminate();
			this.getDataConnected = false;
		}
		
		try {
			return resolver.getPayload(List.class);
		} catch (final PayloadDecodingException ex) {
			throw new CommunicationException("Payload cannot be decoded", ex);
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void putData(final String systemName, final String serviceName, final List<String> senML, final boolean terminate) throws CommunicationException {
		if (!this.putDataConnected) {
			final MessageProperties props = new MessageProperties();
			props.add(WebsocketMsgKey.PATH_VARIABLES, new PathVariables(List.of(systemName, serviceName)));
			this.putDataWSClient.send(props, senML);
			this.putDataConnected = true;
		}
		
		this.putDataWSClient.send(senML);
		if (terminate) {
			this.putDataWSClient.terminate();
			this.putDataConnected = false;
		}
	}
}
