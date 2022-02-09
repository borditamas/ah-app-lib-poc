package ai.aitia.arrowhead.application.common.networking.properties;

import java.util.HashMap;

public class HttpsProperties extends HashMap<HttpsKey,String> {

	private static final long serialVersionUID = 793842136737553676L;
	
	public String getAddress() {
		return this.get(HttpsKey.ADDRESS);
	}
	
	public Integer getPort() {
		return Integer.valueOf(this.get(HttpsKey.PORT));
	}
	
	public String getPath() {
		return this.get(HttpsKey.PATH);
	}
	
	public HttpMethod getMethod() {
		return HttpMethod.valueOf(this.get(HttpsKey.METHOD));
	}
}
