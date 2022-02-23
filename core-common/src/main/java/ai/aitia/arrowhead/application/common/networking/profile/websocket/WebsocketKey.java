package ai.aitia.arrowhead.application.common.networking.profile.websocket;

import ai.aitia.arrowhead.application.common.networking.profile.ProtocolKey;

public enum WebsocketKey implements ProtocolKey {

	PATH, PATH_VARIABLES, QUERY_PARAMETERS,
	PARTIAL_MSG_SUPPORT;
}
