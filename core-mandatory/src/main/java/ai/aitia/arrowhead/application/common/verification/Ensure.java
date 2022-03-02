package ai.aitia.arrowhead.application.common.verification;

import java.util.Collection;

import ai.aitia.arrowhead.application.common.exception.DeveloperException;

public class Ensure {

	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public static void notNull(final Object o, final String msg) {
		if (o == null) {
			throw new DeveloperException(msg);
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	public static void notEmpty(final byte[] bytes, final String msg) {
		if (bytes.length == 0) {
			throw new DeveloperException(msg);
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	public static void notEmpty(final String str, final String msg) {
		if (str == null || str.isBlank()) {
			throw new DeveloperException(msg);
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	public static void notEmpty(final Collection<?> coll, final String msg) {
		if (coll == null || coll.isEmpty()) {
			throw new DeveloperException(msg);
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	public static void isTrue(final boolean expression, final String msg) {
		if (!expression) {
			throw new DeveloperException(msg);
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	public static void isFalse(final boolean expression, final String msg) {
		if (expression) {
			throw new DeveloperException(msg);
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	public static void portRange(final int port) {
		if (port < 0 || port > 65535) {
			throw new DeveloperException("port is out range");
		}
	}
	
	//-------------------------------------------------------------------------------------------------
	public static void fail(final String msg) {
		throw new DeveloperException(msg);
	}
}
