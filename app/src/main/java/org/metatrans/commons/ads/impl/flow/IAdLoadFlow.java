package org.metatrans.commons.ads.impl.flow;


public interface IAdLoadFlow {
	
	public String getAdID();
	public void resume();
	public void pause();
	
	public void cleanCurrent();
	
	public boolean isActive();
}