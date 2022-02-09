package ai.aitia.arrowhead.application.common.service;

import java.util.List;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.HttpsService;
import ai.aitia.arrowhead.application.common.service.model.OperationModel;
import ai.aitia.arrowhead.application.common.service.model.ServiceQueryModel;

public class MonitoringServiceHTTPS implements MonitoringService {
	
	//=================================================================================================
	// members
	
	private final HttpsService https;
	
	private final String address;
	private final int port;
	
	private String echoPath;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public MonitoringServiceHTTPS(final HttpsService https, final String address, final int port) {
		//Assert https init
		this.https = https;
		this.address = address;
		this.port = port;
	}	
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public String getServiceName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public ServiceQueryModel getServiceQueryForm() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//-------------------------------------------------------------------------------------------------
	@Override
	public void initialize(List<OperationModel> operations) {
		// TODO Auto-generated method stub
		
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
	public boolean echo() throws CommunicationException {
		try {
			https.send(address, port, echoPath, Void.class);
			return true;
		} catch (final Exception ex) {
			return false;
		}
	}
}
