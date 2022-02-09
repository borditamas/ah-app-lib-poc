package ai.aitia.arrowhead.application.core.mandatory.serviceregistry.service;

import java.util.List;

import ai.aitia.arrowhead.application.common.networking.HttpsService;
import ai.aitia.arrowhead.application.common.service.model.ServiceModel;
import ai.aitia.arrowhead.application.common.service.model.ServiceQueryModel;

public class ServiceDiscoveryServiceHTTPS implements ServiceDiscoveryService {

	//=================================================================================================
	// members
	
	private final HttpsService https;

	private final String address;
	private final int port;
	private final String queryPath;
	
	private String registerPath;
	private String unregisterPath;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public ServiceDiscoveryServiceHTTPS(final HttpsService https, final String address, final int port, final String queryPath) {
		this.https = https;
		this.address = address;
		this.port = port;
		this.queryPath = queryPath;
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void verify() {
		// TODO Auto-generated method stub		
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public ServiceQueryModel getServiceQueryForm() {
		// TODO Auto-generated method stub
		return null;
	}

	//=================================================================================================
	// operations
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public ServiceModel register(final ServiceModel service) {
		// TODO Auto-generated method stub
		return null;
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public boolean unregister(final ServiceModel service) {
		// TODO Auto-generated method stub
		return false;
	}

	//-------------------------------------------------------------------------------------------------
	@Override
	public List<ServiceModel> query(final ServiceQueryModel form) {
		// TODO Auto-generated method stub
		return null;
	}
}
