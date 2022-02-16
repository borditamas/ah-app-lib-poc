package ai.aitia.arrowhead.application.common.networking;

import java.nio.file.Path;
import java.nio.file.Paths;

import ai.aitia.arrowhead.application.common.verification.Ensure;

public class CommunicationProperties {

	//=================================================================================================
	// members
	
	private final String keyStoreType;
	private final String keyStorePassword;
	private final Path keyStorePath;	
	private final String keyPassword;	
	
	private final String trustStorePassword;
	private final Path trustStorePath;
	
	private boolean disableHostnameVerifier = false;
	private int connectionTimeout = 30000;
	private int socketTimeout = 30000;
	private int connectionManagerTimeout = 10000;
	
	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public CommunicationProperties(final String keyStoreType, final String keyStorePassword, final String keyPassword, final String keyStorePath, final String trustStorePassword, final String trustStorePath) {
		Ensure.notEmpty(keyStoreType, "keyStoreType is empty");
		Ensure.notEmpty(keyStorePassword, "keyStorePassword is empty");
		Ensure.notEmpty(keyPassword, "keyPassword is empty");
		Ensure.notEmpty(keyStorePath, "keyStorePath is empty");
		Ensure.notEmpty(trustStorePassword, "trustStorePassword is empty");
		Ensure.notEmpty(trustStorePath, "trustStorePath is empty");
		
		this.keyStoreType = keyStoreType;
		this.keyStorePassword = keyStorePassword;
		this.keyPassword = keyPassword;
		this.trustStorePassword = trustStorePassword;
		this.keyStorePath = Paths.get(keyStorePath, new String[0]);
		this.trustStorePath = Paths.get(trustStorePath, new String[0]);		
	}
	
	//-------------------------------------------------------------------------------------------------
	public String getKeyStoreType() { return keyStoreType; }
	public String getKeyStorePassword() { return keyStorePassword; }
	public Path getKeyStorePath() { return keyStorePath; }
	public String getKeyPassword() { return keyPassword; }
	public String getTrustStorePassword() { return trustStorePassword; }
	public Path getTrustStorePath() { return trustStorePath; }
	public boolean isDisableHostnameVerifier() { return disableHostnameVerifier; }
	public int getConnectionTimeout() { return connectionTimeout; }
	public int getSocketTimeout() { return socketTimeout; }

	//-------------------------------------------------------------------------------------------------
	public void setDisableHostnameVerifier(final boolean disableHostnameVerifier) { this.disableHostnameVerifier = disableHostnameVerifier; }
	public void setConnectionTimeout(final int connectionTimeout) { this.connectionTimeout = connectionTimeout; } 
	public void setSocketTimeout(final int socketTimeout) { this.socketTimeout = socketTimeout; }
	public int getConnectionManagerTimeout() { return connectionManagerTimeout; }
	public void setConnectionManagerTimeout(final int connectionManagerTimeout) { this.connectionManagerTimeout = connectionManagerTimeout; }
}
