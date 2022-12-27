package org.metatrans.commons.ads.impl.providers.dummy;


import org.metatrans.commons.ads.api.IAdsConfiguration;


public class AdsConfiguration_Dummy implements IAdsConfiguration {


	private static final String CONTAINER_CLASS_DUMMY = AdsContainer_Dummy.class.getName();


	@Override
	public String getContainerClass() {

		return CONTAINER_CLASS_DUMMY;
	}


	@Override
	public String[] getUnitIDs_Banners() {

		return new String[] {"FIXED_STRING_getUnitIDs_Banners"};
	}


	@Override
	public String[] getUnitIDs_Interstitial() {

		return new String[] {"FIXED_STRING_getUnitIDs_Interstitial"};
	}


	@Override
	public String getUnitID(String adID) {

		throw new UnsupportedOperationException();
	}
}
