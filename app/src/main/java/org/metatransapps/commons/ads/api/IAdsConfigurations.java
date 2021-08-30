package org.metatransapps.commons.ads.api;


import org.metatransapps.commons.ads.impl.IAdsContainer;

import android.content.Context;

public interface IAdsConfigurations {
	public int[] getProvidersOfBanners();
	public int[] getProvidersOfInterstitials();
	public IAdsConfiguration getProviderConfiguration(int providerID);
	public IAdsContainer getProviderContainer(int providerID, Context context);
}
