package ai.aitia.arrowhead.application.common.networking;

public interface PayloadDecoder {

	public <T>T decode(final byte[] payload, final Class<T> type) throws Exception;
	public <T>T decode(final String payload, final Class<T> type) throws Exception;
}
