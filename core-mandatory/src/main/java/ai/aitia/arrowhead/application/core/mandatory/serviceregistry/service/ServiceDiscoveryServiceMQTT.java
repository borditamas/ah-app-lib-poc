package ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service;

import java.util.List;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.exception.InitializationException;
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
import ai.aitia.arrowhead.application.common.networking.profile.mqtt.MqttKey;
import ai.aitia.arrowhead.application.common.networking.profile.mqtt.MqttMsgKey;
import ai.aitia.arrowhead.application.common.verification.Ensure;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.OperationModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceQueryModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.dto.RegisterServiceRequestJSON;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.dto.RegisterServiceResponseJSON;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.dto.ServiceQueryRequestJSON;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.dto.ServiceQueryResponseJSON;

public class ServiceDiscoveryServiceMQTT implements ServiceDiscoveryService {

	//=================================================================================================
	// members
	
	private final Communicator communicator;
	
	//private final String queryOperation = "query";
	private final CommunicationClient queryMqttClient;
	
	private final String registerOperation = "register";
	private CommunicationClient registerMqttClient;
	
	private final String unregisterOperation = "unregister";
	private CommunicationClient unregisterMqttClient;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public ServiceDiscoveryServiceMQTT(final Communicator communicator, final InterfaceProfile queryInterfaceProfile) {
		Ensure.notNull(communicator, "Communicator is null");
		Ensure.isTrue(communicator.type() == CommunicatorType.MQTT, "Communicator is not for MQTT");
		Ensure.isTrue(communicator.isInitialized(), "Communicator is not initialized");
		
		Ensure.isTrue(queryInterfaceProfile.getProtocol() == Protocol.MQTT, "queryInterfaceProfile is not for MQTT");
		Ensure.notEmpty(queryInterfaceProfile.get(String.class, MqttKey.TOPIC_PUBLISH), "publishTopic for query is empty");
		Ensure.notEmpty(queryInterfaceProfile.get(String.class, MqttKey.TOPIC_SUBSCRIBE), "subscribeTopic for query is empty");
		
		this.communicator = communicator;
		this.queryMqttClient = communicator.client(queryInterfaceProfile);
	}
	
	//-------------------------------------------------------------------------------------------------	
	@Override
	public String getServiceName() {
		return ServiceDiscoveryService.NAME;
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
		Ensure.isTrue(service.getName().equalsIgnoreCase(ServiceDiscoveryService.NAME), "Service name missmatch");
		Ensure.notEmpty(service.getOperations(), "operation list is empty");
		
		for (final OperationModel operation : service.getOperations()) {
			Ensure.notNull(operation, "operation is null");
			Ensure.isTrue(operation.getInterfaceProfiles().containsKey(Protocol.MQTT), "operation have no MQTT profile");
			
			final InterfaceProfile interfaceProfile = operation.getInterfaceProfiles().get(Protocol.HTTP);
			if (operation.getOperation().equalsIgnoreCase(this.registerOperation)) {
				interfaceProfile.verifyForMQTT(true, true);
				this.registerMqttClient = communicator.client(interfaceProfile);
			}
			if (operation.getOperation().equalsIgnoreCase(this.unregisterOperation)) {
				interfaceProfile.verifyForMQTT(true, true);
				this.unregisterMqttClient = communicator.client(interfaceProfile);
			}
		}				
	}

	//-------------------------------------------------------------------------------------------------	
	@Override
	public void verify() {
		if (this.queryMqttClient == null) {
			throw new InitializationException("queryMqttClient is null");
		}
		if (this.registerMqttClient == null) {
			throw new InitializationException("registerMqttClient is null");
		}
		if (this.unregisterMqttClient == null) {
			throw new InitializationException("unregisterMqttClient is null");
		}
	}

	//-------------------------------------------------------------------------------------------------	
	@Override
	public ServiceModel register(final ServiceModel service) throws CommunicationException, PayloadDecodingException {
		final PathVariables subscribePathVars = new PathVariables();
		subscribePathVars.add(service.getName());
		final MessageProperties props = new MessageProperties();
		props.add(MqttMsgKey.PATH_VARIABLES_SUBSCRIBE, subscribePathVars);
		props.add(MqttMsgKey.RECEIVE_TIMEOUT, true);
		
		this.registerMqttClient.send(props, new RegisterServiceRequestJSON(service));
		final PayloadResolver resolver = new PayloadResolver(MediaType.JSON);
		this.registerMqttClient.receive(resolver);
		
		final ServiceModel response = resolver.getPayload(RegisterServiceResponseJSON.class).convertToServiceModel();
		this.registerMqttClient.terminate();
		return response;
	}

	//-------------------------------------------------------------------------------------------------	
	@Override
	public boolean unregister(final ServiceModel service) throws CommunicationException {
		final PathVariables subscribePathVars = new PathVariables();
		subscribePathVars.add(service.getName());
		final MessageProperties props = new MessageProperties();
		props.add(MqttMsgKey.PATH_VARIABLES_SUBSCRIBE, subscribePathVars);
		props.add(MqttMsgKey.RECEIVE_TIMEOUT, true);
		
		this.unregisterMqttClient.send(props, service.getName());
		final PayloadResolver resolver = new PayloadResolver(MediaType.TEXT);
		this.unregisterMqttClient.receive(resolver);
		
		try {
			final boolean response = resolver.getPayload(Boolean.class);
			this.unregisterMqttClient.terminate();
			return response;
			
		} catch (final PayloadDecodingException ex) {
			throw new CommunicationException("Payload cannot be decoded", ex);
		}
	}

	//-------------------------------------------------------------------------------------------------	
	@Override
	public List<ServiceModel> query(final ServiceQueryModel from) throws CommunicationException, PayloadDecodingException {
		final PathVariables subscribePathVars = new PathVariables();
		//subscribePathVars.add(service.getRequesterSystemName());
		final MessageProperties props = new MessageProperties();
		props.add(MqttMsgKey.PATH_VARIABLES_SUBSCRIBE, subscribePathVars);
		props.add(MqttMsgKey.RECEIVE_TIMEOUT, true);
		
		this.queryMqttClient.send(new ServiceQueryRequestJSON(from));
		final PayloadResolver resolver = new PayloadResolver(MediaType.JSON);
		this.queryMqttClient.receive(resolver);
		
		final List<ServiceModel> response = resolver.getPayload(ServiceQueryResponseJSON.class).convertToServiceModelList();
		this.queryMqttClient.terminate();
		return response;
	}
}
