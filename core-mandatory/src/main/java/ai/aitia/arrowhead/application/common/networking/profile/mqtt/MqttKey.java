package ai.aitia.arrowhead.application.common.networking.profile.mqtt;

import ai.aitia.arrowhead.application.common.networking.profile.ProtocolKey;

public enum MqttKey implements ProtocolKey {
	TOPIC_PUBLISH, TOPIC_SUBSCRIBE, QOS, RETAINED;
}
