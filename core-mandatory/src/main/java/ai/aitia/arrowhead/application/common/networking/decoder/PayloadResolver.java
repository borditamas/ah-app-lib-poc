package ai.aitia.arrowhead.application.common.networking.decoder;

import ai.aitia.arrowhead.application.common.networking.decoder.exception.PayloadDecodingException;
import ai.aitia.arrowhead.application.common.verification.Ensure;

public class PayloadResolver {
	
	//=================================================================================================
	// members
	
	private final MediaType media;
		
	private PayloadDecoder decoder;
	private String payloadStr;
	private byte[] payloadBytes;
	private Object fullMessage;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public PayloadResolver(final MediaType media) {
		Ensure.notNull(media, "MediaType is null");
		this.media = media;
	}

	//-------------------------------------------------------------------------------------------------
	public <P> P getPayload(final Class<P> type) throws PayloadDecodingException {
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
		// TODO throw ex when already has value --> final!
		Ensure.notNull(decoder, "PayloadDecoder is null");
		Ensure.notNull(fullMessage, "fullMessage is null");
		this.decoder = decoder;
		this.payloadStr = data;
		this.fullMessage = fullMessage;
	}
	
	//-------------------------------------------------------------------------------------------------
	public void add(final PayloadDecoder decoder, final byte[] data, final Object fullMessage) {
		// TODO throw ex when already has value --> final!
		Ensure.notNull(decoder, "PayloadDecoder is null");
		Ensure.notNull(fullMessage, "fullMessage is null");		
		this.decoder = decoder;
		this.payloadBytes = data;
		this.fullMessage = fullMessage;
	}
	
	//-------------------------------------------------------------------------------------------------
	public void add(final Object fullMessage) {
		// TODO throw ex when already has value --> final!
		Ensure.notNull(fullMessage, "fullMessage is null");
		this.fullMessage = fullMessage;
	}
}
