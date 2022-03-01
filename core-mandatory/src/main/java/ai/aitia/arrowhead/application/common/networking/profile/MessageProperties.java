package ai.aitia.arrowhead.application.common.networking.profile;

import java.util.HashMap;
import java.util.Map;

import ai.aitia.arrowhead.application.common.verification.Ensure;

public class MessageProperties {

	private final Map<String,Object> map = new HashMap<>();
	
	public void add(final MessageKey key, final Object value) {
		Ensure.isTrue(value.getClass().isAssignableFrom(key.getType()), "Value for key " + key.name() + "has invalid type" + key.getType().getSimpleName());
		this.map.put(key.name(), value);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(final Class<T> castTo, final MessageKey key) {
		final Object o = this.map.get(key.name());
		if (o == null) {
			return null;
		}
		Ensure.isTrue(castTo.isAssignableFrom(o.getClass()), "Value for key " + key.name() + "cannot cast to" + castTo.getSimpleName());
		return (T)o;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getOrDefault(final Class<T> castTo, final MessageKey key, final T defaultValue) {
		final Object o = this.map.get(key.name());
		if (o == null) {
			return defaultValue;
		}
		Ensure.isTrue(castTo.isAssignableFrom(o.getClass()), "Value for key " + key.name() + "cannot cast to" + castTo.getSimpleName());
		return (T)o;
	}
}
