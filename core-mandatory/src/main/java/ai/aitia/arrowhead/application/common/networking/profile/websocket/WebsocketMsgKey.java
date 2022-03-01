package ai.aitia.arrowhead.application.common.networking.profile.websocket;

import ai.aitia.arrowhead.application.common.networking.profile.MessageKey;
import ai.aitia.arrowhead.application.common.networking.profile.model.PathVariables;
import ai.aitia.arrowhead.application.common.networking.profile.model.QueryParams;

public enum WebsocketMsgKey implements MessageKey {

	PATH_VARIABLES(PathVariables.class),
	QUERY_PARAMETERS(QueryParams.class);
	
	private Class<?> type;

	private WebsocketMsgKey(final Class<?> type) {
		this.type = type;
	}

	@Override
	public Class<?> getType() { return type; }
}
