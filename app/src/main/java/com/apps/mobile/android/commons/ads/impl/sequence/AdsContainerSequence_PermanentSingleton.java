package com.apps.mobile.android.commons.ads.impl.sequence;


import java.util.ArrayList;
import java.util.List;

import com.apps.mobile.android.commons.ads.impl.IAdsContainer;


public class AdsContainerSequence_PermanentSingleton implements IAdsContainerSequence {
	
	
	private IAdsContainer adsContainer;
	
	
	public AdsContainerSequence_PermanentSingleton(IAdsContainer _adsContainer) {
		adsContainer = _adsContainer;
	}
	
	
	@Override
	public IAdsContainer next() {
		return adsContainer;
	}


	@Override
	public void reset() {
		//Do nothing
	}
	
	public List<IAdsContainer> getAdsContainers() {
		 List<IAdsContainer> result = new ArrayList<IAdsContainer>();
		 result.add(adsContainer);
		 return result;
	}
}
