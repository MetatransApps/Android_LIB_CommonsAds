package org.metatrans.commons.ads.impl.flow;


public interface IAdLoadFlow {
	
	public String getAdID();

	public boolean isActive();

	public void resume();

	public void pause();
	
	//public void cleanCurrent();
}