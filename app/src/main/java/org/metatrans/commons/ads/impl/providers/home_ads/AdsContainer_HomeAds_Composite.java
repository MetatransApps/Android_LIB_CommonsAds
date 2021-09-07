package org.metatrans.commons.ads.impl.providers.home_ads;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.metatrans.commons.ads.api.IAdsProviders;
import org.metatrans.commons.cfg.publishedapp.IHomeAdInfo;

import java.util.List;


public class AdsContainer_HomeAds_Composite extends AdsContainer_HomeAds_BaseImpl {


	private List<AdsContainer_HomeAds_BaseImpl> containers;
	private int currentContainer_index = 0;


	public AdsContainer_HomeAds_Composite(Context context, List<AdsContainer_HomeAds_BaseImpl> _containers) {

		super(context, null);

		containers = _containers;

		if (containers == null || containers.size() == 0) {
			throw new IllegalStateException("HomeAds Containers list's size should be more tha zero containers=" + containers);
		}
	}


	@Override
	public int getProviderID() {
		return IAdsProviders.ID_HOME_ADS;
	}


	@Override
	protected AdsContainer_HomeAds_BaseImpl getCurrentHomeAdsSubContainer() {
		return containers.get(currentContainer_index);
	}


	@Override
	protected IHomeAdInfo getNextHomeAdInfo() {

		AdsContainer_HomeAds_BaseImpl currentContainer = containers.get(currentContainer_index);
		IHomeAdInfo adInfo = currentContainer.getNextHomeAdInfo();

		currentContainer_index++;
		if (currentContainer_index >= containers.size()) {
			currentContainer_index = 0;
		}

		return adInfo;
	}
	
	
	@Override
	protected boolean canWorkOffline() {
		return containers.get(currentContainer_index).canWorkOffline();
	}


	@Override
	protected boolean openTarget(IHomeAdInfo promoted) {
		return containers.get(currentContainer_index).openTarget(promoted);
	}


	@Override
	protected Intent createInterstitialIntent(Activity currentActivity) {
		return containers.get(currentContainer_index).createInterstitialIntent(currentActivity);
	}
}
