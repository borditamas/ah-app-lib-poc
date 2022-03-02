package ai.aitia.arrowhead.application.common.networking;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import ai.aitia.arrowhead.application.common.verification.Ensure;

public class PayloadResolver<P> {
	
	private P payload;
	private Object fullMessage;
	
	public P getPayload() {
		return this.payload;
	}	
	
	@SuppressWarnings("unchecked")
	public <F>F getFullMessage(final Class<F> type) {
		Ensure.isTrue(type.isAssignableFrom(this.fullMessage.getClass()), "Full message is not assignable to " + type.getSimpleName());
		return (F)this.fullMessage;
	}

	@SuppressWarnings("unchecked")
	public void read(final Object data, final Object fullMessage) {
		Ensure.isTrue(this.payload.getClass().isAssignableFrom(data.getClass()), "Data to be loaded is not assignable to " + this.payload.getClass().getSimpleName());
		this.payload = (P)data;
		this.fullMessage = fullMessage;
	}

	@SuppressWarnings("unchecked")
	public void read(final byte[] data, final Object fullMessage) {
		try {
			final ByteArrayInputStream bis = new ByteArrayInputStream(data);
			final ObjectInput in = new ObjectInputStream(bis);			
			this.payload = (P)in.readObject();
			this.fullMessage = fullMessage;
			
		} catch (final Exception ex) {
			Ensure.fail("Data to be loaded is not assignable to " + this.payload.getClass().getSimpleName() + ". " + ex.getMessage());
		}
	}
	
	public void read(final Object fullMessage) {
		this.fullMessage = fullMessage;
	}
}
