package org.metatransapps.commons.ads.impl.sequence;


import java.util.List;

import org.metatransapps.commons.ads.impl.IAdsContainer;


public interface IAdsContainerSequence {
	
	public IAdsContainer next();
	
	public void reset();
	
	public List<IAdsContainer> getAdsContainers();
}
