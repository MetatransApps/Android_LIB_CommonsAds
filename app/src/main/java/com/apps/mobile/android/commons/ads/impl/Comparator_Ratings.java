package com.apps.mobile.android.commons.ads.impl;


import java.util.Comparator;

import com.apps.mobile.android.commons.ads.impl.stat.model.AdsData;


public class Comparator_Ratings implements Comparator<IAdsContainer> {

	
	private AdsData data;
	
	
	public Comparator_Ratings(AdsData _data) {
		data = _data;
	}
	
	
	@Override
	public int compare(IAdsContainer c1, IAdsContainer c2) {
		
		if (c1 == c2 || c1.equals(c2)) {
			return 0;
		}
		
		double r1 = data.getAdData(c1.getProviderID()).getRating();
		double r2 = data.getAdData(c2.getProviderID()).getRating();
		
		double diff = r1 - r2;
		
		if (diff == 0) {
			return 0;
		} else if (diff < 0) {
			return 1;
		} else { //diff > 0
			return -1;
		}
	}
}
