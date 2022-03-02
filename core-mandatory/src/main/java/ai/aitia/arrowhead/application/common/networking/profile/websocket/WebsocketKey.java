package ai.aitia.arrowhead.application.common.networking.profile.websocket;

import ai.aitia.arrowhead.application.common.networking.profile.ProtocolKey;

public enum WebsocketKey implements ProtocolKey {

	ADDRESS(String.class),
	PORT(Integer.class),
	PATH(String.class),
	PARTIAL_MSG_SUPPORT(Boolean.class);
	
	private Class<?> type;

	private WebsocketKey(final Class<?> type) {
		this.type = type;
	}

	@Override
	public Class<?> getType() { return type; }
}
