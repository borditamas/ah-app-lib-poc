package ai.aitia.arrowhead.application.common.networking.decoder;

public interface PayloadDecoder {

	public <T>T decode(final MediaType media, final byte[] payload, final Class<T> type) throws Exception;
	public <T>T decode(final MediaType media, final String payload, final Class<T> type) throws Exception;
}
