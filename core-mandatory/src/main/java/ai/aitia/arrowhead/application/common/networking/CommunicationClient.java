package ai.aitia.arrowhead.application.common.networking;

import ai.aitia.arrowhead.application.common.exception.CommunicationException;
import ai.aitia.arrowhead.application.common.networking.profile.MessageProperties;

public interface CommunicationClient {
	
	public void send(final Object payload) throws CommunicationException;
	public void send(final MessageProperties props, final Object payload) throws CommunicationException;
	public <T>T receive(final Class<T> type) throws CommunicationException;
	public void terminate() throws CommunicationException;
}
