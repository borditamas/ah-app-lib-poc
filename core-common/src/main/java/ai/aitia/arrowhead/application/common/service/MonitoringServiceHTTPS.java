package ai.aitia.arrowhead.application.common.service;

import ai.aitia.arrowhead.application.common.networking.HttpsService;
import ai.aitia.arrowhead.application.common.service.model.ServiceQueryModel;

public class MonitoringServiceHTTPS implements MonitoringService {
	
	private final HttpsService https;
	
	private final String address;
	private final int port;
	
	private String echoPath;
	
	public MonitoringServiceHTTPS(final HttpsService https, final String address, final int port) {
		this.https = https;
		this.address = address;
		this.port = port;
		this.echoPath = echoPath;
	}
	
	@Override
	public void verify() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ServiceQueryModel getServiceQueryForm() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean echo() {
		try {
			https.send(address, port, echoPath, Void.class);
			return true;
		} catch (final Exception ex) {
			return false;
		}
	}
}
