package ai.aitia.arrowhead.application.common.networking.profile;

import java.util.HashMap;

import ai.aitia.arrowhead.application.common.verification.Ensure;

public class InterfaceProfile {

	private final Protocol protocol;
	private final String address;
	private final int port;
	private final HashMap<String,Object> properties = new HashMap<String,Object>();
	
	
	public InterfaceProfile(final Protocol protocol, final String address, final int port) {
		this.protocol = protocol;
		this.address = address;
		this.port = port;
	}

	public Protocol getProtocol() { return protocol; }
	public String getAddress() { return address; }
	public int getPort() { return port; }
	
	public void put(final ProtocolKey key, final Object value) {
		this.properties.put(key.name(), value);
	}
	
	public boolean contains(final ProtocolKey key) {
		return this.properties.containsKey(key.name());
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(final Class<T> castTo, final ProtocolKey key) {
		final Object o = this.properties.get(key.name());
		Ensure.notNull(o, "No value for " + key.name());
		Ensure.isTrue(castTo.isAssignableFrom(o.getClass()), "Value for key " + key.name() + "cannot cast to" + castTo.getSimpleName());
		return (T)o;
	}
}
