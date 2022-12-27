package org.metatrans.commons.ads.impl;


import org.metatrans.commons.ads.impl.stat.model.AdsData;

import java.util.Comparator;


public class Comparator_Impressions implements Comparator<IAdsContainer> {


	private AdsData data;


	public Comparator_Impressions(AdsData _data) {
		data = _data;
	}
	
	
	@Override
	public int compare(IAdsContainer c1, IAdsContainer c2) {
		
		if (c1 == c2 || c1.equals(c2)) {

			return 0;
		}
		
		double r1 = data.getAdData(c1.getProviderID()).getImpressions();
		double r2 = data.getAdData(c2.getProviderID()).getImpressions();

		return (int) (r1 - r2);
	}
}
