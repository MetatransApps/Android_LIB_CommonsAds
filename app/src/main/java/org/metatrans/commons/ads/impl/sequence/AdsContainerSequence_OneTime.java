package org.metatrans.commons.ads.impl.sequence;


import org.metatrans.commons.ads.impl.IAdsContainer;

import java.util.ArrayList;
import java.util.List;


public class AdsContainerSequence_OneTime implements IAdsContainerSequence {


	private int current;
	private List<IAdsContainer> adsContainers;


	public AdsContainerSequence_OneTime(List<IAdsContainer> _adsContainers) {
		this();
		for (int i = 0; i < _adsContainers.size(); i++) {
			adsContainers.add(_adsContainers.get(i));
		}
	}


	public AdsContainerSequence_OneTime() {
		current = 0;
		adsContainers = new ArrayList<IAdsContainer>();		
	}
	
	
	@Override
	public IAdsContainer next() {

		if (current >= adsContainers.size()) {

			return null;
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
