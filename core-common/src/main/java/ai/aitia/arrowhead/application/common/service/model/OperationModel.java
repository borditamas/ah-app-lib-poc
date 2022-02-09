package ai.aitia.arrowhead.application.common.service.model;

import ai.aitia.arrowhead.application.common.networking.properties.HttpsProperties;
import ai.aitia.arrowhead.application.common.networking.properties.WebsocketProperties;

public class OperationModel {

	private final String operation;
	private final HttpsProperties httpsProperties = new HttpsProperties();
	private final WebsocketProperties websocketProperties = new WebsocketProperties();
	
	public OperationModel(final String operation, final HttpsProperties httpsProperties) {
		this.operation = operation;
		this.httpsProperties.putAll(httpsProperties);
	}
	
	public OperationModel(final String operation, final WebsocketProperties websocketProperties) {
		this.operation = operation;
		this.websocketProperties.putAll(websocketProperties);
	}

	public String getOperation() { return operation; }
	public HttpsProperties getHttpsProperties() { return httpsProperties; }
	public WebsocketProperties getWebsocketProperties() { return websocketProperties; }
}
