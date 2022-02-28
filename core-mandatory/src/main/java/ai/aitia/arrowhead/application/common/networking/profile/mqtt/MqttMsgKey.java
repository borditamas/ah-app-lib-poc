package ai.aitia.arrowhead.application.common.networking.profile.mqtt;

import ai.aitia.arrowhead.application.common.networking.profile.MessageKey;

public enum MqttMsgKey implements MessageKey {

	PATH_VARIABLES_PUBLISH, PATH_VARIABLES_SUBSCRIBE, QOS, RETAINED;
}
