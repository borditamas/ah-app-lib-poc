package ai.aitia.arrowhead.application.common.networking.profile.model;

import java.util.ArrayList;
import java.util.List;

import ai.aitia.arrowhead.application.common.verification.Ensure;

public class QueryParams {

	final List<String> params = new ArrayList<String>();
	
	public void add(final String key, final String value) {
		Ensure.notEmpty(key, "key is empty");
		Ensure.notEmpty(value, "value is empty");
		this.params.add(key);
		this.params.add(value);
	}
	
	public List<String> getParams() {
		return this.params;
	}
}
