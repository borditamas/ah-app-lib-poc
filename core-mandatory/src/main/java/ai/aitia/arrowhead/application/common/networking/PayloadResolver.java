package ai.aitia.arrowhead.application.common.networking;

import ai.aitia.arrowhead.application.common.verification.Ensure;

public class PayloadResolver {
	
	//=================================================================================================
	// members
		
	private PayloadDecoder decoder;
	private String payloadStr;
	private byte[] payloadBytes;
	private Object fullMessage;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public <P>P getPayload(Class<P> type) {
		try {
			if (this.payloadStr != null && !this.payloadStr.isBlank()) {
				return this.decoder.decode(this.payloadStr, type);
			}
			if (this.payloadBytes != null && this.payloadBytes.length != 0) {
				return this.decoder.decode(this.payloadBytes, type);
			}
			
		} catch (final Exception ex) {
			Ensure.fail("Payload cannot be decoded as " + type.getSimpleName()+ ". " + ex.getMessage());
		}
		return null;
	}
	
	//-------------------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public <F>F getFullMessage(final Class<F> type) {
		Ensure.isTrue(type.isAssignableFrom(this.fullMessage.getClass()), "Full message is not assignable to " + type.getSimpleName());
		return (F)this.fullMessage;
	}
	
	//-------------------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public void add(final PayloadDecoder decoder, final String data, final Object fullMessage) {
		Ensure.notNull(decoder, "PayloadDecoder is null");
		Ensure.notNull(fullMessage, "fullMessage is null");
		this.decoder = decoder;
		this.payloadStr = data;
		this.fullMessage = fullMessage;
	}
	
	//-------------------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public void add(final PayloadDecoder decoder, final byte[] data, final Object fullMessage) {
		Ensure.notNull(decoder, "PayloadDecoder is null");
		Ensure.notNull(fullMessage, "fullMessage is null");
		this.decoder = decoder;
		this.payloadBytes = data;
		this.fullMessage = fullMessage;
	}
	
	//-------------------------------------------------------------------------------------------------
	public void add(final Object fullMessage) {
		Ensure.notNull(fullMessage, "fullMessage is null");
		this.fullMessage = fullMessage;
	}
}
