package ai.aitia.arrowhead.application.common.networking.profile;

import java.util.HashMap;
import java.util.Map;

import ai.aitia.arrowhead.application.common.verification.Ensure;

public class MessageProperties {

	private final Map<String,Object> map = new HashMap<>();
	
	public void add(final ProtocolKey key, final Object value) {
		this.map.put(key.name(), value);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(final Class<T> castTo, final ProtocolKey key) {
		final Object o = this.map.get(key.name());
		if (o == null) {
			return null;
		}
		Ensure.isTrue(castTo.isAssignableFrom(o.getClass()), "Value for key " + key.name() + "cannot cast to" + castTo.getSimpleName());
		return (T)o;
	}
}
