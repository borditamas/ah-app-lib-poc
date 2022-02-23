package ai.aitia.arrowhead.application.common.networking;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.profile.model.QueryParams;

public interface CommunicationClient {
	
	public void send(final Object payload) throws CommunicationException;
	public void send(final QueryParams params, final Object payload) throws CommunicationException;
	public <T>T receive(final Class<T> type) throws CommunicationException;
	public void disconnect() throws CommunicationException;
}
