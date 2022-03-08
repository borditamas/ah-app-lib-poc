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
import ai.aitia.arrowhead.application.common.networking.profile.mqtt.MqttMsgKey;
import ai.aitia.arrowhead.application.common.verification.Ensure;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.OperationModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceQueryModel;
import ai.aitia.arrowhead.application.core.support.datamanager.service.model.SenML;
import ai.aitia.arrowhead.application.core.support.datamanager.service.model.dto.HistorianRequestJSON;

public class HistorianServiceMQTT implements HistorianService  {

	//=================================================================================================
	// members
	
	private Communicator communicator;	
	
	private final String getDataOperation = "get-data";
	private CommunicationClient getDataMQTTClient;
	private boolean getDataSubscribed = false;
	
	private final String putDataOperation = "put-data";
	private CommunicationClient putDataMQTTClient;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public HistorianServiceMQTT(final Communicator communicator) {
		Ensure.notNull(communicator, "Communicator is null");
		Ensure.isTrue(communicator.type() == CommunicatorType.MQTT, "Communicator is not for MQTT");
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
			Ensure.isTrue(operation.getInterfaceProfiles().containsKey(Protocol.MQTT), "operation have no MQTT profile");
			
			final InterfaceProfile interfaceProfile = operation.getInterfaceProfiles().get(Protocol.WEBSOCKET);
			if (operation.getOperation().equalsIgnoreCase(this.getDataOperation)) {
				interfaceProfile.verifyForMQTT(true, false);
				this.getDataMQTTClient = communicator.client(interfaceProfile);
			}
			if (operation.getOperation().equalsIgnoreCase(this.putDataOperation)) {
				interfaceProfile.verifyForMQTT(false, true);
				this.putDataMQTTClient = communicator.client(interfaceProfile);
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
	public SenML getMeasurements(final String systemName, final String serviceName, final boolean terminate) throws CommunicationException, PayloadDecodingException { // TODO return an object where all info is conatined (decoding error flag, etc...)
		if (!this.getDataSubscribed) {
			final MessageProperties props = new MessageProperties();
			props.add(MqttMsgKey.PATH_VARIABLES_SUBSCRIBE, new PathVariables(List.of(systemName, serviceName)));
			this.getDataMQTTClient.send(props, null);
			this.getDataSubscribed = true;
		}
		
		final PayloadResolver resolver = new PayloadResolver(MediaType.JSON);
		this.getDataMQTTClient.receive(resolver);
		
		if (terminate) {
			this.getDataMQTTClient.terminate();
			this.getDataSubscribed = false;
		}

		if (resolver.isClientError()) {
			throw new CommunicationException(resolver.getClientErrorMsg());
		}
		return resolver.getPayload(SenML.class);
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void putMeasurements(final String systemName, final String serviceName, final List<SenML> measurements, final boolean terminate) throws CommunicationException { // TODO return an object where all info is conatined (decoding error flag, etc...)
		final MessageProperties props = new MessageProperties();
		final HistorianRequestJSON message = new HistorianRequestJSON(systemName, serviceName, measurements);
		this.putDataMQTTClient.send(props, message);
		// 'terminate' is intentionally ignored
	}
}
