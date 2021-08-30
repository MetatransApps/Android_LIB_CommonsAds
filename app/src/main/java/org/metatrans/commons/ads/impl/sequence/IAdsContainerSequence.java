package org.metatrans.commons.ads.impl.sequence;


import java.util.List;

import org.metatrans.commons.ads.impl.IAdsContainer;


public interface IAdsContainerSequence {
	
	public IAdsContainer next();
	
	public void reset();
	
	public List<IAdsContainer> getAdsContainers();
}
