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
import ai.aitia.arrowhead.application.common.networking.profile.http.HttpMethod;
import ai.aitia.arrowhead.application.common.networking.profile.http.HttpsKey;
import ai.aitia.arrowhead.application.common.networking.profile.http.HttpsMsgKey;
import ai.aitia.arrowhead.application.common.networking.profile.model.QueryParams;
import ai.aitia.arrowhead.application.common.verification.Ensure;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.OperationModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.ServiceQueryModel;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.dto.RegisterServiceRequestJSON;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.dto.RegisterServiceResponseJSON;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.dto.ServiceQueryRequestJSON;
import ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service.model.dto.ServiceQueryResponseJSON;

public class ServiceDiscoveryServiceHTTPS implements ServiceDiscoveryService {

	//=================================================================================================
	// members
	
	private final Communicator communicator;
	
	//private final String queryOperation = "query";
	private final CommunicationClient queryHttpsClient;
	
	private final String registerOperation = "register";
	private CommunicationClient registerHttpsClient;
	
	private final String unregisterOperation = "unregister";
	private CommunicationClient unregisterHttpsClient;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public ServiceDiscoveryServiceHTTPS(final Communicator communicator, final InterfaceProfile queryInterfaceProfile) {
		Ensure.notNull(communicator, "Communicator is null");
		Ensure.isTrue(communicator.type() == CommunicatorType.HTTPS, "Communicator is not for HTTPS");
		Ensure.isTrue(communicator.isInitialized(), "Communicator is not initialized");
		
		Ensure.isTrue(queryInterfaceProfile.getProtocol() == Protocol.HTTP, "queryInterfaceProfile is not for HTTPS");
		Ensure.notEmpty(queryInterfaceProfile.get(String.class, HttpsKey.ADDRESS), "address is empty");
		Ensure.portRange(queryInterfaceProfile.get(Integer.class, HttpsKey.PORT));
		Ensure.notEmpty(queryInterfaceProfile.get(String.class, HttpsKey.PATH), "queryPath is empty");
		Ensure.notNull(queryInterfaceProfile.get(HttpMethod.class, HttpsKey.METHOD), "queryMethod is null");
		
		this.communicator = communicator;
		this.queryHttpsClient = communicator.client(queryInterfaceProfile);
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
			Ensure.isTrue(operation.getInterfaceProfiles().containsKey(Protocol.HTTP), "operation have no HTTP profile");
			
			final InterfaceProfile interfaceProfile = operation.getInterfaceProfiles().get(Protocol.HTTP);
			if (operation.getOperation().equalsIgnoreCase(this.registerOperation)) {
				Ensure.isTrue(interfaceProfile.contains(HttpsKey.PATH), "no path for register operation");
				Ensure.isTrue(interfaceProfile.contains(HttpsKey.METHOD), "no http method for register operation");
				this.registerHttpsClient = communicator.client(interfaceProfile);
			}
			if (operation.getOperation().equalsIgnoreCase(this.unregisterOperation)) {
				Ensure.isTrue(interfaceProfile.contains(HttpsKey.PATH), "no path for unregister operation");
				Ensure.isTrue(interfaceProfile.contains(HttpsKey.METHOD), "no http method for unregister operation");
				this.unregisterHttpsClient = communicator.client(interfaceProfile);
			}
		}		
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void verify() {
		if (this.queryHttpsClient == null) {
			throw new InitializationException("queryHttpsClient is null");
		}
		if (this.registerHttpsClient == null) {
			throw new InitializationException("registerHttpsClient is null");
		}
		if (this.unregisterHttpsClient == null) {
			throw new InitializationException("unregisterHttpsClient is null");
		}
	}

	//=================================================================================================
	// operations
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public ServiceModel register(final ServiceModel service) throws CommunicationException {
		this.registerHttpsClient.send(new RegisterServiceRequestJSON(service));
		final PayloadResolver resolver = new PayloadResolver(MediaType.JSON);
		this.registerHttpsClient.receive(resolver);
		try {
			return resolver.getPayload(RegisterServiceResponseJSON.class).convertToServiceModel();
			
		} catch (final PayloadDecodingException ex) {
			throw new CommunicationException("Payload cannot be decoded", ex);
		}
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public boolean unregister(final ServiceModel service) throws CommunicationException {
		final QueryParams queryParams = new QueryParams();
		queryParams.add("system_name", service.getSystem().getName());
		queryParams.add("service_name", service.getName());
		final MessageProperties msgProps = new MessageProperties();
		msgProps.add(HttpsMsgKey.QUERY_PARAMETERS, queryParams);
		this.unregisterHttpsClient.send(msgProps, null);		
		this.unregisterHttpsClient.receive(new PayloadResolver(MediaType.EMPTY));
		return true;
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public List<ServiceModel> query(final ServiceQueryModel form) throws CommunicationException {
		this.queryHttpsClient.send(new ServiceQueryRequestJSON(form));
		final PayloadResolver resolver = new PayloadResolver(MediaType.JSON);
		this.queryHttpsClient.receive(resolver);
		try {
			return resolver.getPayload(ServiceQueryResponseJSON.class).convertToServiceModelList();
		} catch (final PayloadDecodingException ex) {
			throw new CommunicationException("Payload cannot be decoded", ex);
		}
	}
}
