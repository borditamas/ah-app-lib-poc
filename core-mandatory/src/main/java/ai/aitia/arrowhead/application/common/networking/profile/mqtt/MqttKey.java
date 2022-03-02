package ai.aitia.arrowhead.application.common.networking.profile.mqtt;

import ai.aitia.arrowhead.application.common.networking.profile.ProtocolKey;

public enum MqttKey implements ProtocolKey {
	
	TOPIC_PUBLISH(String.class),
	TOPIC_SUBSCRIBE(String.class);
	
	private Class<?> type;

	private MqttKey(final Class<?> type) {
		this.type = type;
	}

	@Override
	public Class<?> getType() { return type; }
}
