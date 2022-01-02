package org.metatrans.commons.ads.impl.providers.home_ads;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import org.metatrans.commons.ads.api.IAdsProviders;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.publishedapp.IHomeAdInfo;

import java.util.ArrayList;
import java.util.List;


public class AdsContainer_HomeAds_Composite extends AdsContainer_HomeAds_BaseImpl {


	private List<AdsContainer_HomeAds_BaseImpl> containers;
	private int currentContainer_index = 0;


	public AdsContainer_HomeAds_Composite(Context context, List<AdsContainer_HomeAds_BaseImpl> _containers) {

		super(context, null);

		containers = new ArrayList<AdsContainer_HomeAds_BaseImpl>();

		int storeID = ((Application_Base)context).getAppStore().getID();

		for(int i=0; i < _containers.size(); i++) {

			AdsContainer_HomeAds_BaseImpl cur = _containers.get(i);

			boolean hasAtLeast1Excluded = false;
			int[] curExcludedStores = cur.getExcludedStores();
			for (int j=0; j < curExcludedStores.length; j++) {
				if (curExcludedStores[j] == storeID) {
					hasAtLeast1Excluded = true;
					break;
				}
			}

			if (!hasAtLeast1Excluded) {
				containers.add(cur);
			}
		}

		if (containers.size() == 0) {
			throw new IllegalStateException("HomeAds Containers list's size should be more tha zero containers.size() = " + containers.size());
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
