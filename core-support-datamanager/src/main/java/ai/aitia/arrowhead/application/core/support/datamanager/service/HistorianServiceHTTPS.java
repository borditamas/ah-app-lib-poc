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
import ai.aitia.arrowhead.application.common.networking.profile.http.HttpsMsgKey;
import ai.aitia.arrowhead.application.common.networking.profile.model.PathVariables;
import ai.aitia.arrowhead.application.common.verification.Ensure;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.OperationModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceQueryModel;
import ai.aitia.arrowhead.application.core.support.datamanager.service.model.SenML;

public class HistorianServiceHTTPS implements HistorianService {

	//=================================================================================================
	// members
	
	private Communicator communicator;	
	
	private final String getDataOperation = "get-data";
	private CommunicationClient getDataHTTPClient;
	
	private final String putDataOperation = "put-data";
	private CommunicationClient putDataHTTPClient;
	
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
				interfaceProfile.verifyForHTTP(true);
				this.getDataHTTPClient = communicator.client(interfaceProfile);
			}
			if (operation.getOperation().equalsIgnoreCase(this.putDataOperation)) {
				interfaceProfile.verifyForHTTP(true);
				this.putDataHTTPClient = communicator.client(interfaceProfile);
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
	public SenML getMeasurements(final String systemName, final String serviceName, final boolean terminate) throws CommunicationException, PayloadDecodingException {
		final MessageProperties props = new MessageProperties();
		props.add(HttpsMsgKey.PATH_VARIABLES, new PathVariables(List.of(systemName, serviceName)));
		this.putDataHTTPClient.send(props);
		
		final PayloadResolver resolver = new PayloadResolver(MediaType.JSON);
		this.getDataHTTPClient.receive(resolver);
		
		if (terminate) {
			this.getDataHTTPClient.terminate(); //TODO should be called before or after send?
		}
		
		if (resolver.isClientError()) {
			throw new CommunicationException(resolver.getClientErrorMsg());
		}
		return resolver.getPayload(SenML.class);
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public void putMeasurements(final String systemName, final String serviceName, final List<SenML> measurements, final boolean terminate) throws CommunicationException {
		final MessageProperties props = new MessageProperties();
		props.add(HttpsMsgKey.PATH_VARIABLES, new PathVariables(List.of(systemName, serviceName)));
		this.putDataHTTPClient.send(props, measurements);
		if (terminate) {
			this.putDataHTTPClient.terminate(); //TODO should be called before or after send?
		}
		this.putDataHTTPClient.receive(new PayloadResolver(MediaType.EMPTY));
	}
}
