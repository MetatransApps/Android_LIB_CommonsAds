package com.apps.mobile.android.commons.ads.api;


import android.content.Context;

import com.apps.mobile.android.commons.ads.impl.IAdsContainer;

public interface IAdsConfigurations {
	public int[] getProvidersOfBanners();
	public int[] getProvidersOfInterstitials();
	public IAdsConfiguration getProviderConfiguration(int providerID);
	public IAdsContainer getProviderContainer(int providerID, Context context);
}
