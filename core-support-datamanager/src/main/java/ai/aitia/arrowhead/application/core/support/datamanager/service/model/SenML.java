package ai.aitia.arrowhead.application.core.support.datamanager.service.model;

public class SenML {

	//=================================================================================================
	// members
	
	private String bn;
	private Double bt;
	private String bu;
	private Double bv;
	private Double bs;
	private Short bver;
	private String n;
	private String u;
	private Double v = null;
	private String vs = null;
	private Boolean vb = null;
	private String vd = null;
	private Double s = null;
	private Double t = null;
	private Double ut = null;

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public String getBn() { return bn; }
	public Double getBt() { return bt; }
	public String getBu() { return bu; }
	public Double getBv() { return bv; }
	public Double getBs() { return bs; }
	public Short getBver() { return bver; }
	public String getN() { return n; }
	public String getU() { return u; }
	public Double getV() { return v; }
	public String getVs() { return vs; }
	public Boolean getVb() { return vb; }
	public String getVd() { return vd; }
	public Double getS() { return s; }
	public Double getT() { return t; }
	public Double getUt() { return ut; }
	
	//-------------------------------------------------------------------------------------------------
	public void setBn(final String bn) { this.bn = bn; }
	public void setBt(final Double bt) { this.bt = bt; }
	public void setBu(final String bu) { this.bu = bu; }
	public void setBv(final Double bv) { this.bv = bv; }
	public void setBs(final Double bs) { this.bs = bs; }
	public void setBver(final Short bver) { this.bver = bver; }
	public void setN(final String n) { this.n = n; }
	public void setU(final String u) { this.u = u; }
	public void setV(final Double v) { this.v = v; }
	public void setVs(final String vs) { this.vs = vs; }
	public void setVb(final Boolean vb) { this.vb = vb; }
	public void setVd(final String vd) { this.vd = vd; }
	public void setS(final Double s) { this.s = s; }
	public void setT(final Double t) { this.t = t; }
	public void setUt(final Double ut) { this.ut = ut; }
}
