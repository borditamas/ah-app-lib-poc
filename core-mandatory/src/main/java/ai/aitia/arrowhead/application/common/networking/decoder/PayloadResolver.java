package ai.aitia.arrowhead.application.common.networking.decoder;

import java.nio.ByteBuffer;

import ai.aitia.arrowhead.application.common.networking.decoder.exception.PayloadDecodingException;
import ai.aitia.arrowhead.application.common.verification.Ensure;

public class PayloadResolver { // TODO partial/chunked message feature
	
	//=================================================================================================
	// members
	
	private final MediaType media;
		
	private PayloadDecoder decoder;
	private String payloadStr;
	private byte[] payloadBytes;
	private Object fullMessage;
	private boolean partial = false;
	private boolean clientError = false;
	private String clientErrorMsg;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public PayloadResolver(final MediaType media) {
		Ensure.notNull(media, "MediaType is null");
		this.media = media;
	}

	//-------------------------------------------------------------------------------------------------
	public boolean isPartial() { return partial; }
	public boolean isClientError() { return clientError; }
	public String getClientErrorMsg() { return clientErrorMsg; }

	//-------------------------------------------------------------------------------------------------
	public void setPartial(final boolean partial) { this.partial = partial; }
	public void setClientError(final boolean clientError) { this.clientError = clientError; }
	public void setClientErrorMsg(final String clientErrorMsg) { this.clientErrorMsg = clientErrorMsg; }

	//-------------------------------------------------------------------------------------------------
	public <P> P getPayload(final Class<P> type) throws PayloadDecodingException {
		if (this.partial) {
			throw new PayloadDecodingException("Payload is partial yet");
		}
		
		if (this.media == MediaType.EMPTY) {
			return null;
		}
		
		try {
			if (this.payloadStr != null && !this.payloadStr.isBlank()) {
				return this.decoder.decode(this.media, this.payloadStr, type);
			}
			if (this.payloadBytes != null && this.payloadBytes.length != 0) {
				return this.decoder.decode(this.media, this.payloadBytes, type);
			}
			
		} catch (final Exception ex) {
			throw new PayloadDecodingException("Payload cannot be decoded as " + type.getSimpleName(), ex);
		}
		throw new PayloadDecodingException("No data to decode");
	}
	
	//-------------------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public <F>F getFullMessage(final Class<F> type) {
		Ensure.isTrue(type.isAssignableFrom(this.fullMessage.getClass()), "Full message is not assignable to " + type.getSimpleName());
		return (F)this.fullMessage;
	}
	
	//-------------------------------------------------------------------------------------------------
	public void add(final PayloadDecoder decoder, final String data, final Object fullMessage) {
		Ensure.notNull(decoder, "PayloadDecoder is null");
		Ensure.notNull(fullMessage, "fullMessage is null");
		
		this.decoder = decoder;
		this.fullMessage = fullMessage;
		this.payloadStr = this.payloadStr == null || this.payloadStr.isBlank() ? data : this.payloadStr + data;
	}
	
	//-------------------------------------------------------------------------------------------------
	public void add(final PayloadDecoder decoder, final byte[] data, final Object fullMessage) {
		Ensure.notNull(decoder, "PayloadDecoder is null");
		Ensure.notNull(fullMessage, "fullMessage is null");		
		
		this.decoder = decoder;
		this.fullMessage = fullMessage;
		if (this.payloadBytes == null || this.payloadBytes.length == 0) {
			this.payloadBytes = data;			
		} else {
			this.payloadBytes = ByteBuffer.allocate(this.payloadBytes.length + data.length).put(this.payloadBytes).put(data).array();
		}		
	}
	
	//-------------------------------------------------------------------------------------------------
	public void add(final Object fullMessage) {
		Ensure.notNull(fullMessage, "fullMessage is null");
		this.fullMessage = fullMessage;
	}
}
