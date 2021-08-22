package com.apps.mobile.android.commons.ads.impl.sequence;


import java.util.List;

import com.apps.mobile.android.commons.ads.impl.IAdsContainer;


public interface IAdsContainerSequence {
	
	public IAdsContainer next();
	
	public void reset();
	
	public List<IAdsContainer> getAdsContainers();
}
