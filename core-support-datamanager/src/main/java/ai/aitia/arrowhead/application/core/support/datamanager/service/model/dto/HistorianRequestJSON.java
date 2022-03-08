package ai.aitia.arrowhead.application.core.support.datamanager.service.model.dto;

import java.util.List;

import ai.aitia.arrowhead.application.core.support.datamanager.service.model.SenML;

public class HistorianRequestJSON {

	//=================================================================================================
	// members
	
	private String systemName;
	private String serviceName;
	private List<SenML> measurements;
	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public HistorianRequestJSON() {}
	
	//-------------------------------------------------------------------------------------------------
	public HistorianRequestJSON(final String systemName, final String serviceName, final List<SenML> measurements) {
		this.systemName = systemName;
		this.serviceName = serviceName;
		this.measurements = measurements;
	}

	//-------------------------------------------------------------------------------------------------
	public String getSystemName() { return systemName; }
	public String getServiceName() { return serviceName; }
	public List<SenML> getMeasurements() { return measurements; }

	//-------------------------------------------------------------------------------------------------
	public void setSystemName(final String systemName) { this.systemName = systemName; }
	public void setServiceName(final String serviceName) { this.serviceName = serviceName; }
	public void setMeasurements(final List<SenML> measurements) { this.measurements = measurements; }
}
