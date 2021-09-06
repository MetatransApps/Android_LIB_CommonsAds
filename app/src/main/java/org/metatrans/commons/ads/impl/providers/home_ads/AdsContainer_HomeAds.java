package org.metatrans.commons.ads.impl.providers.home_ads;


import java.util.List;

import org.metatrans.commons.ads.api.IAdsConfiguration;
import org.metatrans.commons.ads.api.IAdsProviders;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.publishedapp.IHomeAdInfo;
import org.metatrans.commons.cfg.publishedapp.IPublishedApplication;
import org.metatrans.commons.cfg.publishedapp.PublishedApplication_Utils;
import org.metatrans.commons.marketing.Activity_Marketing_AppList;
import org.metatrans.commons.web.WebUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


public class AdsContainer_HomeAds extends AdsContainer_HomeAds_BaseImpl {
	
	
	public AdsContainer_HomeAds(Context _activity, IAdsConfiguration adsConf) {
		super(_activity, adsConf);
	}
	
	
	@Override
	public int getProviderID() {
		return IAdsProviders.ID_HOME_ADS;
	}
	
	
	@Override
	protected boolean canWorkOffline() {
		return true;
	}


	@Override
	protected IHomeAdInfo getNextHomeAdInfo() {
		return getApp_Random();
	}


	@Override
	protected boolean openTarget(IHomeAdInfo promoted) {
		return WebUtils.openApplicationStorePage(getActivity(), (IPublishedApplication) promoted);
	}


	@Override
	protected Intent createInterstitialIntent(Activity currentActivity) {
		return new Intent(currentActivity, Activity_Marketing_AppList.class);
	}


	//Used in FREE versions to obtain home add. Should not be called from Paid version
	private IPublishedApplication getApp_Random() {

		IPublishedApplication app = null;

		List<IPublishedApplication> apps = PublishedApplication_Utils.getStoreApps_FreeOnly(Application_Base.getInstance().getAppStore());

		if (apps != null && apps.size() > 1) {

			do {
				int random_index = (int) ((Math.random() * 10.0 * ((double)apps.size())) % apps.size());
				app = apps.get(random_index);
			} while (app.getPackage().equals(Application_Base.getInstance().getPackageName()));
		}

		//Fix for new apps, which still don't have url into the store
		if (app != null && app.getMarketURL() == null) {
			app = null;
		}

		return app;
	}
}
