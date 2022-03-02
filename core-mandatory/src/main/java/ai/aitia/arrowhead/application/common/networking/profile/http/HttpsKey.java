package ai.aitia.arrowhead.application.common.networking.profile.http;

import ai.aitia.arrowhead.application.common.networking.profile.http.HttpMethod;
import ai.aitia.arrowhead.application.common.networking.profile.ProtocolKey;

public enum HttpsKey implements ProtocolKey {
	
	ADDRESS(String.class),
	PORT(Integer.class),
	METHOD(HttpMethod.class),
	PATH(String.class);
	
	private Class<?> type;

	private HttpsKey(final Class<?> type) {
		this.type = type;
	}

	@Override
	public Class<?> getType() { return type; }
}
