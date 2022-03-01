package ai.aitia.arrowhead.application.common.networking.profile.mqtt;

import ai.aitia.arrowhead.application.common.networking.profile.MessageKey;
import ai.aitia.arrowhead.application.common.networking.profile.model.PathVariables;

public enum MqttMsgKey implements MessageKey {

	PATH_VARIABLES_PUBLISH(PathVariables.class),
	PATH_VARIABLES_SUBSCRIBE(PathVariables.class),
	QOS(Integer.class),
	RETAINED(Boolean.class),
	RECEIVE_TIMEOUT(Boolean.class);
	
	private Class<?> type;

	private MqttMsgKey(final Class<?> type) {
		this.type = type;
	}

	@Override
	public Class<?> getType() { return type; }	
}
