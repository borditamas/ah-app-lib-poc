package ai.aitia.arrowhead.application.common.networking.profile.model;

import java.util.ArrayList;
import java.util.List;

public class PathVariables {

	final List<String> vars = new ArrayList<String>();
	
	public void add(final String value) {
		this.vars.add(value);
	}
	
	public List<String> getVariables() {
		return this.vars;
	}
}
