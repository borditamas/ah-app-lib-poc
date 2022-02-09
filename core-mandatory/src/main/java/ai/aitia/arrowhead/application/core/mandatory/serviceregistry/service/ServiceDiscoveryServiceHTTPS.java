package ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service;

import java.util.List;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.HttpsService;
import ai.aitia.arrowhead.application.common.networking.properties.HttpMethod;
import ai.aitia.arrowhead.application.common.networking.properties.HttpsKey;
import ai.aitia.arrowhead.application.common.service.model.OperationModel;
import ai.aitia.arrowhead.application.common.service.model.ServiceModel;
import ai.aitia.arrowhead.application.common.service.model.ServiceQueryModel;
import ai.aitia.arrowhead.application.common.verification.Ensure;

public class ServiceDiscoveryServiceHTTPS implements ServiceDiscoveryService {

	//=================================================================================================
	// members
	
	private final String name = "service-discovery";
	
	private final HttpsService https;
	private final String address;
	private final int port;
	
	private final String queryPath;
	private final HttpMethod queryMethod;
	
	private final String registerOperation = "register";
	private String registerPath;
	private HttpMethod registerMethod;
	
	private final String unregisterOperation = "unregister";
	private String unregisterPath;
	private HttpMethod unregisterMethod;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public ServiceDiscoveryServiceHTTPS(final HttpsService https, final String address, final int port, final String queryPath, final HttpMethod queryMethod) {
		Ensure.isTrue(https.isInitialized(), "https is not initialized");
		
		this.https = https;
		this.address = address;
		this.port = port;
		this.queryPath = queryPath;
		this.queryMethod = queryMethod;
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
			if (operation.getOperation().equalsIgnoreCase(this.registerOperation)) {
				this.registerPath = operation.getHttpsProperties().get(HttpsKey.PATH);
				this.registerMethod = HttpMethod.valueOf(operation.getHttpsProperties().get(HttpsKey.METHOD));
			}
			if (operation.getOperation().equalsIgnoreCase(this.unregisterOperation)) {
				this.unregisterPath = operation.getHttpsProperties().get(HttpsKey.PATH);
				this.unregisterMethod = HttpMethod.valueOf(operation.getHttpsProperties().get(HttpsKey.METHOD));
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
	public ServiceModel register(final ServiceModel service) throws CommunicationException {
		// TODO Auto-generated method stub
		return null;
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public boolean unregister(final ServiceModel service) throws CommunicationException {
		// TODO Auto-generated method stub
		return false;
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public List<ServiceModel> query(final ServiceQueryModel form) throws CommunicationException {
		// TODO Auto-generated method stub
		return null;
	}
}
