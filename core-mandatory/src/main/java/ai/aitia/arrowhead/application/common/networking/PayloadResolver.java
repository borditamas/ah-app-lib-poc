package ai.aitia.arrowhead.application.common.networking;

import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

import ai.aitia.arrowhead.application.common.verification.Ensure;

public class PayloadResolver<T> {
	
	private T payload;
	
	public T getPayload() {
		return this.payload;
	}
	
	@SuppressWarnings("unchecked")
	public void read(final Object data) {
		Ensure.isTrue(this.payload.getClass().isAssignableFrom(data.getClass()), "Data to be loaded is not assignable to " + this.payload.getClass().getSimpleName());
		this.payload = (T)data;
	}

	@SuppressWarnings("unchecked")
	public void read(final byte[] data) {
		try {
			final ByteArrayInputStream bis = new ByteArrayInputStream(data);
			final ObjectInput in = new ObjectInputStream(bis);			
			this.payload = (T)in.readObject();
			
		} catch (final Exception ex) {
			Ensure.fail("Data to be loaded is not assignable to " + this.payload.getClass().getSimpleName() + ". " + ex.getMessage());
		}
	}
}
