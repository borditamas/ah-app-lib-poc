package ai.aitia.arrowhead.application.common.networking;

import java.io.IOException;
import java.io.InputStream;

import ai.aitia.arrowhead.application.common.verification.Ensure;

public class SSLProperties {

	//=================================================================================================
	// members
	
	private final String keyStoreType;
	private final String keyStorePassword;
	private byte[] keyStore;	
	private final String keyPassword;	
	
	private final String trustStorePassword;
	private byte[] trustStore;
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public SSLProperties(final String keyStoreType, final String keyStorePassword, final String keyPassword, final String trustStorePassword) {
		this.keyStoreType = keyStoreType;
		this.keyStorePassword = keyStorePassword;
		this.keyPassword = keyPassword;
		this.trustStorePassword = trustStorePassword;
	}
	
	//-------------------------------------------------------------------------------------------------
	public String getKeyStoreType() { return keyStoreType; }
	public String getKeyStorePassword() { return keyStorePassword; }
	public byte[] getKeyStore() { return keyStore; }
	public String getKeyPassword() { return keyPassword; }
	public String getTrustStorePassword() { return trustStorePassword; }
	public byte[] getTrustStore() { return trustStore; }

	//-------------------------------------------------------------------------------------------------
	public void readKeyStore(final InputStream inputStream) throws IOException {
		this.keyStore = inputStream.readAllBytes();
	}
	
	//-------------------------------------------------------------------------------------------------
	public void readTrustStore(final InputStream inputStream) throws IOException {
		this.trustStore = inputStream.readAllBytes();
	}
	
	//-------------------------------------------------------------------------------------------------
	public void verify() {
		Ensure.notEmpty(keyStoreType, "keyStoreType is empty");
		Ensure.notEmpty(keyStorePassword, "keyStorePassword is empty");
		Ensure.notEmpty(keyPassword, "keyPassword is empty");
		Ensure.notEmpty(trustStorePassword, "trustStorePassword is empty");
		
		Ensure.notEmpty(keyStore, "keyStore is empty");
		Ensure.notEmpty(trustStore, "trustStore is empty");
	}
}
