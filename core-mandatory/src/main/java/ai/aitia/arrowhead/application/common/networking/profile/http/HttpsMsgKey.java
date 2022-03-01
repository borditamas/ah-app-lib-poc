package ai.aitia.arrowhead.application.common.networking.profile.http;

import ai.aitia.arrowhead.application.common.networking.profile.MessageKey;
import ai.aitia.arrowhead.application.common.networking.profile.model.PathVariables;
import ai.aitia.arrowhead.application.common.networking.profile.model.QueryParams;

public enum HttpsMsgKey implements MessageKey {

	PATH_VARIABLES(PathVariables.class),
	QUERY_PARAMETERS(QueryParams.class);
	
	private Class<?> type;

	private HttpsMsgKey(final Class<?> type) {
		this.type = type;
	}

	@Override
	public Class<?> getType() { return type; }	
}
