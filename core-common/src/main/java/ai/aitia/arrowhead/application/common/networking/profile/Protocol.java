package ai.aitia.arrowhead.application.common.networking.profile;

import ai.aitia.arrowhead.application.common.networking.CommunicatorType;

public enum Protocol {

	HTTP, WEBSOCKET;
	
	public static Protocol valueOf(CommunicatorType communicatorType) {
		switch (communicatorType) {
		case HTTPS:
			return Protocol.HTTP;
			
		case WEBSOCKET:
			return Protocol.WEBSOCKET;

		default:
			// TODO throw;
			return null;
		}
	}
}
