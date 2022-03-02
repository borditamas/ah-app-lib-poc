package ai.aitia.arrowhead.application.common.networking.profile.model;

import java.util.ArrayList;
import java.util.List;

import ai.aitia.arrowhead.application.common.verification.Ensure;

public class PathVariables {

	private List<String> vars = new ArrayList<String>();
	
	public PathVariables() {}
	
	public PathVariables(final List<String> variables) {
		for (final String var : variables) {
			Ensure.notEmpty(var, "path variable is empty");
			this.vars.add(var);
		}
	}
	
	public void add(final String variable) {
		Ensure.notEmpty(variable, "path variable is empty");
		this.vars.add(variable);
	}
	
	public List<String> getVariables() {
		return this.vars;
	}
}
