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
import ai.aitia.arrowhead.application.common.networking.profile.http.HttpMethod;
import ai.aitia.arrowhead.application.common.networking.profile.http.HttpsKey;
import ai.aitia.arrowhead.application.common.networking.profile.model.PathVariables;
import ai.aitia.arrowhead.application.common.networking.profile.websocket.WebsocketKey;
import ai.aitia.arrowhead.application.common.networking.profile.websocket.WebsocketMsgKey;
import ai.aitia.arrowhead.application.common.verification.Ensure;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.OperationModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceQueryModel;

public class HistorianServiceHTTPS implements HistorianService {

	//=================================================================================================
	// members
	
	private Communicator communicator;	
	
	private final String getDataOperation = "get-data";
	private CommunicationClient getDataWSClient;
	
	private final String putDataOperation = "put-data";
	private CommunicationClient putDataWSClient;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public HistorianServiceHTTPS(final Communicator communicator) {
		Ensure.notNull(communicator, "Communicator is null");
		Ensure.isTrue(communicator.type() == CommunicatorType.HTTPS, "Communicator is no for HTTPS");
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
	public void load(ServiceModel service) {
		Ensure.notNull(service, "service is null");
		Ensure.isTrue(service.getName().equalsIgnoreCase(HistorianService.NAME), "Service name missmatch");
		Ensure.notEmpty(service.getOperations(), "operation list is empty");
		
		for (final OperationModel operation : service.getOperations()) {
			Ensure.notNull(operation, "operation is null");
			Ensure.isTrue(operation.getInterfaceProfiles().containsKey(Protocol.HTTP), "operation have not HTTP profile");
			
			final InterfaceProfile interfaceProfile = operation.getInterfaceProfiles().get(Protocol.HTTP);
			if (operation.getOperation().equalsIgnoreCase(this.getDataOperation)) {
				Ensure.notNull(interfaceProfile.get(HttpMethod.class, HttpsKey.METHOD), "no method for get-data operation");
				Ensure.notEmpty(interfaceProfile.get(String.class, HttpsKey.ADDRESS), "get-data operation address is empty");
				Ensure.portRange(interfaceProfile.get(Integer.class, HttpsKey.PORT));
				Ensure.notEmpty(interfaceProfile.get(String.class, HttpsKey.PATH), "no path for get-data operation");
				this.getDataWSClient = communicator.client(interfaceProfile);
			}
			if (operation.getOperation().equalsIgnoreCase(this.putDataOperation)) {
				Ensure.notNull(interfaceProfile.get(HttpMethod.class, HttpsKey.METHOD), "no method for put-data operation");
				Ensure.notEmpty(interfaceProfile.get(String.class, HttpsKey.ADDRESS), "put-data operation address is empty");
				Ensure.portRange(interfaceProfile.get(Integer.class, HttpsKey.PORT));
				Ensure.notEmpty(interfaceProfile.get(String.class, HttpsKey.PATH), "no path for put-data operation");
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
		final MessageProperties props = new MessageProperties();
		props.add(WebsocketMsgKey.PATH_VARIABLES, new PathVariables(List.of(systemName, serviceName)));
		this.putDataWSClient.send(props);
		
		final PayloadResolver resolver = new PayloadResolver(MediaType.JSON);
		this.getDataWSClient.receive(resolver);
		
		if (terminate) {
			this.getDataWSClient.terminate();
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
		final MessageProperties props = new MessageProperties();
		props.add(WebsocketMsgKey.PATH_VARIABLES, new PathVariables(List.of(systemName, serviceName)));
		this.putDataWSClient.send(props, senML);
		if (terminate) {
			this.putDataWSClient.terminate();
		}
	}
}
