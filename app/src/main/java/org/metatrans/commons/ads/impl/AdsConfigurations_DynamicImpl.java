package org.metatrans.commons.ads.impl;


import org.metatrans.commons.ads.api.IAdsConfiguration;
import org.metatrans.commons.ads.api.IAdsConfigurations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdsConfigurations_DynamicImpl implements IAdsConfigurations {


	private Map<Integer, IAdsConfiguration> ads_cfgs;


	public AdsConfigurations_DynamicImpl() {

		ads_cfgs = new HashMap<>();
	}
	

	public void addProviderConfig(int provider_id, IAdsConfiguration provider_cfg) {

		ads_cfgs.put(provider_id, provider_cfg);
	}


	@Override
	public IAdsConfiguration getProviderConfiguration(int providerID) {

		IAdsConfiguration cfg = ads_cfgs.get(providerID);

		if (cfg == null) {

			throw new IllegalStateException("ProviderID=" + providerID);
		}

		return cfg;
	}


	@Override
	public int[] getProvidersOfBanners() {


		List<Integer> all_providers_ids = new ArrayList<Integer>();


		for (Integer provider_id: ads_cfgs.keySet()) {

			IAdsConfiguration cfg = ads_cfgs.get(provider_id);

			String[] ids = cfg.getUnitIDs_Banners();

			if (ids != null && ids.length > 0) {

				all_providers_ids.add(provider_id);
			}
		}


		int[] result = new int[all_providers_ids.size()];

		for (int i = 0; i < all_providers_ids.size(); i++) {

			Integer provider_id = all_providers_ids.get(i);

			result[i] = provider_id;
		}


		return result;
	}


	@Override
	public int[] getProvidersOfInterstitials() {


		List<Integer> all_providers_ids = new ArrayList<Integer>();


		for (Integer provider_id: ads_cfgs.keySet()) {

			IAdsConfiguration cfg = ads_cfgs.get(provider_id);

			String[] ids = cfg.getUnitIDs_Interstitial();

			if (ids != null && ids.length > 0) {

				all_providers_ids.add(provider_id);
			}
		}


		int[] result = new int[all_providers_ids.size()];

		for (int i = 0; i < all_providers_ids.size(); i++) {

			Integer provider_id = all_providers_ids.get(i);

			result[i] = provider_id;
		}


		return result;
	}


	public int[] getProvidersOfRewardedVideos() {


		List<Integer> all_providers_ids = new ArrayList<Integer>();


		for (Integer provider_id: ads_cfgs.keySet()) {

			IAdsConfiguration cfg = ads_cfgs.get(provider_id);

			String[] ids = cfg.getUnitIDs_RewardedVideo();

			if (ids != null && ids.length > 0) {

				all_providers_ids.add(provider_id);
			}
		}


		int[] result = new int[all_providers_ids.size()];

		for (int i = 0; i < all_providers_ids.size(); i++) {

			Integer provider_id = all_providers_ids.get(i);

			result[i] = provider_id;
		}


		return result;
	}
}
