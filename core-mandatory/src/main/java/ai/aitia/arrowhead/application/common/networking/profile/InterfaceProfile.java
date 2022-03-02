package ai.aitia.arrowhead.application.common.networking.profile;

import java.util.HashMap;

import ai.aitia.arrowhead.application.common.verification.Ensure;

public class InterfaceProfile {

	//=================================================================================================
	// members
	
	private final Protocol protocol;
	private final HashMap<String,Object> properties = new HashMap<String,Object>();
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public InterfaceProfile(final Protocol protocol) {
		this.protocol = protocol;
	}

	//-------------------------------------------------------------------------------------------------
	public Protocol getProtocol() { return protocol; }
	
	//-------------------------------------------------------------------------------------------------
	public void put(final ProtocolKey key, final Object value) {
		Ensure.isTrue(key.getType().isAssignableFrom(value.getClass()), "Value for key " + key.name() + "has invalid type. Must be " + key.getType().getSimpleName());
		this.properties.put(key.name(), value);
	}
	
	//-------------------------------------------------------------------------------------------------
	public boolean contains(final ProtocolKey key) {
		return this.properties.containsKey(key.name());
	}
	
	//-------------------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public <T> T get(final Class<T> castTo, final ProtocolKey key) {
		final Object o = this.properties.get(key.name());
		Ensure.notNull(o, "No value for " + key.name());
		Ensure.isTrue(castTo.isAssignableFrom(o.getClass()), "Value for key " + key.name() + "cannot cast to" + castTo.getSimpleName());
		return (T)o;
	}
	
	//-------------------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public <T> T getOrDefault(final Class<T> castTo, final ProtocolKey key, final T defaultValue) {
		final Object o = this.properties.get(key.name());
		if (o == null) {
			return defaultValue;
		}
		Ensure.isTrue(castTo.isAssignableFrom(o.getClass()), "Value for key " + key.name() + "cannot cast to" + castTo.getSimpleName());
		return (T)o;
	}
}
