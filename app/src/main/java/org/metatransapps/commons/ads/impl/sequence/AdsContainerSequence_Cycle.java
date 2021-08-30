package org.metatransapps.commons.ads.impl.sequence;


import java.util.ArrayList;
import java.util.List;

import org.metatransapps.commons.ads.impl.IAdsContainer;


public class AdsContainerSequence_Cycle implements IAdsContainerSequence {
	
	
	private int current;
	private List<IAdsContainer> adsContainers;
	
	
	public AdsContainerSequence_Cycle(List<IAdsContainer> _adsContainers) {
		this();
		for (int i = 0; i < _adsContainers.size(); i++) {
			adsContainers.add(_adsContainers.get(i));
		}
	}
	
	
	public AdsContainerSequence_Cycle() {
		current = 0;
		adsContainers = new ArrayList<IAdsContainer>();		
	}
	
	
	@Override
	public IAdsContainer next() {
		if (current >= adsContainers.size()) {
			current = 0;
		}
		return adsContainers.get(current++);
	}
	
	
	public void addContainer(IAdsContainer adsContainer) {
		adsContainers.add(adsContainer);
	}


	public List<IAdsContainer> getAdsContainers() {
		return adsContainers;
	}


	@Override
	public void reset() {
		current = 0;
	}
}
