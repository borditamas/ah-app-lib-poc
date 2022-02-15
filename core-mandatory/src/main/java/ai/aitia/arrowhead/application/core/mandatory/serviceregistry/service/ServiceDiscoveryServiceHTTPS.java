package ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service;

import java.util.List;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.Communicator;
import ai.aitia.arrowhead.application.common.networking.CommunicatorType;
import ai.aitia.arrowhead.application.common.networking.profile.InterfaceProfile;
import ai.aitia.arrowhead.application.common.networking.profile.Protocol;
import ai.aitia.arrowhead.application.common.networking.profile.http.HttpMethod;
import ai.aitia.arrowhead.application.common.networking.profile.http.HttpsKey;
import ai.aitia.arrowhead.application.common.service.model.OperationModel;
import ai.aitia.arrowhead.application.common.service.model.ServiceModel;
import ai.aitia.arrowhead.application.common.service.model.ServiceQueryModel;
import ai.aitia.arrowhead.application.common.verification.Ensure;

public class ServiceDiscoveryServiceHTTPS implements ServiceDiscoveryService {

	//=================================================================================================
	// members
	
	private final String name = "service-discovery";
	
	private final Communicator https;
	
	private final String queryOperation = "query";
	private final InterfaceProfile queryInterfaceProfile;
	
	private final String registerOperation = "register";
	private InterfaceProfile registerInterfaceProfile;
	
	private final String unregisterOperation = "unregister";
	private InterfaceProfile unregisterInterfaceProfile;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public ServiceDiscoveryServiceHTTPS(final Communicator https, final InterfaceProfile queryInterfaceProfile) {
		Ensure.notNull(https, "Communicator is null");
		Ensure.isTrue(https.getType() == CommunicatorType.HTTPS, "Communicator is not for HTTPS");
		Ensure.isTrue(https.isInitialized(), "https is not initialized");
		
		Ensure.isTrue(queryInterfaceProfile.getProtocol() == Protocol.HTTP, "queryInterfaceProfile is not for HTTPS");
		Ensure.notEmpty(queryInterfaceProfile.getAddress(), "address is empty");
		Ensure.portRange(queryInterfaceProfile.getPort());
		Ensure.notEmpty(queryInterfaceProfile.get(String.class, HttpsKey.PATH), "queryPath is empty");
		Ensure.notNull(queryInterfaceProfile.get(HttpMethod.class, HttpsKey.METHOD), "queryMethod is null");
		
		this.https = https;
		this.queryInterfaceProfile = queryInterfaceProfile;
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
			if (operation.getOperation().equalsIgnoreCase(this.registerOperation)) {
				Ensure.isTrue(interfaceProfile.contains(HttpsKey.PATH), "no path for register operation");
				Ensure.isTrue(interfaceProfile.contains(HttpsKey.METHOD), "no http method for register operation");
				this.registerInterfaceProfile = interfaceProfile;
			}
			if (operation.getOperation().equalsIgnoreCase(this.unregisterOperation)) {
				Ensure.isTrue(interfaceProfile.contains(HttpsKey.PATH), "no path for unregister operation");
				Ensure.isTrue(interfaceProfile.contains(HttpsKey.METHOD), "no http method for unregister operation");
				this.unregisterInterfaceProfile = interfaceProfile;
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
