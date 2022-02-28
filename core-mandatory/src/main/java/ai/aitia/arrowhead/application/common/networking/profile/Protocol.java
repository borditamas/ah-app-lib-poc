package ai.aitia.arrowhead.application.common.networking.profile;

import ai.aitia.arrowhead.application.common.networking.CommunicatorType;

public enum Protocol {

	HTTP, WEBSOCKET, MQTT;
	
	public static Protocol valueOf(CommunicatorType communicatorType) {
		switch (communicatorType) {
		case HTTPS:
			return Protocol.HTTP;
			
		case WEBSOCKET:
			return Protocol.WEBSOCKET;
			
		case MQTT:
			return Protocol.MQTT;

		default:
			// TODO throw;
			return null;
		}
	}
}
