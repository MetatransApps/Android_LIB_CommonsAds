package org.metatrans.commons.ads.api;


public interface IAdsConfigurations {

	int[] getProvidersOfBanners();

	int[] getProvidersOfInterstitials();

	IAdsConfiguration getProviderConfiguration(int providerID);
}
