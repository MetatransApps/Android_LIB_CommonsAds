package org.metatrans.commons.ads.api;


public interface IAdsConfigurations {

	int[] getProvidersOfBanners();

	int[] getProvidersOfInterstitials();

	int[] getProvidersOfRewardedVideos();

	IAdsConfiguration getProviderConfiguration(int providerID);
}
