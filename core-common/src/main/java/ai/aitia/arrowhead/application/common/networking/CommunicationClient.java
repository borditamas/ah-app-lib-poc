package ai.aitia.arrowhead.application.common.networking;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.profile.model.QueryParams;

public interface CommunicationClient {
	
	void send(final Object payload) throws CommunicationException;
	void send(final QueryParams params, final Object payload) throws CommunicationException;
	<T>T receive(final Class<T> type) throws CommunicationException;
}
