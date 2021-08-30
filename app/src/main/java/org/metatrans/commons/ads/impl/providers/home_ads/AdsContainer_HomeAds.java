package org.metatrans.commons.ads.impl.providers.home_ads;


import java.util.List;

import org.metatrans.commons.DeviceUtils;
import org.metatrans.commons.ads.api.IAdsConfiguration;
import org.metatrans.commons.ads.api.IAdsProviders;
import org.metatrans.commons.ads.impl.flow.AdLoadFlow_Banner;
import org.metatrans.commons.ads.impl.flow.AdLoadFlow_Interstitial;
import org.metatrans.commons.ads.impl.providers.AdsContainer_Base;
import org.metatrans.commons.ads.utils.BannerUtils;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.cfg.publishedapp.IPublishedApplication;
import org.metatrans.commons.cfg.publishedapp.PublishedApplication_Utils;
import org.metatrans.commons.events.api.IEvent_Base;
import org.metatrans.commons.events.api.IEventsManager;
import org.metatrans.commons.marketing.Activity_Marketing_AppList;
import org.metatrans.commons.web.WebUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;


public class AdsContainer_HomeAds extends AdsContainer_Base {
	
	
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
	protected View createBanner(final AdLoadFlow_Banner flow) {
		
		System.out.println("AdsContainer_HomeAds: createBanner called");
		
		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(Application_Base.getInstance().getUserSettings().uiColoursID);
		final IPublishedApplication promotedApp = getApp_Random();
		
		View bannerView = new BannerView(getActivity(), coloursCfg, promotedApp);
		
		bannerView.setId(BannerUtils.AD_BANNER_VIEW_ID);

        View wrapper = BannerUtils.createView(getActivity(), bannerView, flow.getGravity());
        
		return wrapper;
	}
	
	
	@Override
	protected Object createBannerListener(AdLoadFlow_Banner flow) {
		throw new UnsupportedOperationException();
	}
	
	
	@Override
	protected void destroyBanner(Object bannerView) {
		if (bannerView instanceof View) {
			((View)bannerView).destroyDrawingCache();
		}
	}
	
	
	@Override
	protected Object createInterstitial(AdLoadFlow_Interstitial flow) {
		
		System.out.println("AdsContainer_HomeAds: createInterstitial called");
		
		Object dummy = new Object();
		return dummy;
	}
	
	
	@Override
	protected Object createInterstitialListener(AdLoadFlow_Interstitial flow, Object interstitial) {
		throw new UnsupportedOperationException();
	}
	
	
	@Override
	protected void destroyInterstitial(Object ad) {
		//Do nothing
	}	
	
	
	@Override
	protected void request_sync_banner(final View adview, AdLoadFlow_Banner flow) {
		request_sync(adview, flow);
	}
	
	
	@Override
	protected void request_sync_banner(View adview) {
		throw new UnsupportedOperationException();
	}
	
	
	@Override
	protected void showInterstitial(Object dummy, AdLoadFlow_Interstitial flow) {
		request_sync(flow);
	}

	
	@Override
	protected void showInterstitial(Object ad) {
		throw new UnsupportedOperationException();
	}
	
	
	private void request_sync(final View bannerView, final AdLoadFlow_Banner flow) {
		
		System.out.println("AdsContainer_HomeAds: request_sync for banner called. bannerView=" + bannerView);
		
		//bannerView.setVisibility(View.VISIBLE);
		
		((BannerView)bannerView).setClickAction(new Runnable() {
			
			@Override
			public void run() {
            	flow.clicked();
            	
            	IPublishedApplication promoted = ((BannerView)bannerView).getPromotedApp();
            	
            	WebUtils.openApplicationStorePage(getActivity(), promoted);
            	
            	IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
        		eventsManager.register(getActivity(),
        				eventsManager.create(IEvent_Base.MARKETING, IEvent_Base.MARKETING_HOME_AD_PROVIDER_CLICKED, promoted.getID().hashCode(),
        				"MARKETING", "HOME_AD_PROVIDER_CLICKED", "" + promoted.getID()));
				
			}
		});
			
		
		flow.loadOK();
			
	}
	
	
	private void request_sync(AdLoadFlow_Interstitial flow) {
		
		System.out.println("AdsContainer_HomeAds: request_sync for interstitial called");
		
		int rand = (int) (Math.random() * 100d);
		
		Activity currentActivity = Application_Base.getInstance().getCurrentActivity();
		if (currentActivity == null) {
			System.out.println("AdsContainer_HomeAds: EXIT because current activity is null");
			flow.loadFailed();
			return;
		}
		
		final IPublishedApplication promoted = getApp_Random();
		
		if (rand <= 50 || promoted == null || !DeviceUtils.isConnected()) {
			
			System.out.println("AdsContainer_HomeAds: request_sync show APP_LIST");
			
			Intent i = new Intent(currentActivity, Activity_Marketing_AppList.class);
			currentActivity.startActivity(i);
    		
			flow.loadOK();
			
			//flow.cleanCurrent();
			
			try {
	        	IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
	        	Context context = (getActivity() != null) ? getActivity() : Application_Base.getInstance();
	    		eventsManager.register(context,
	    				eventsManager.create(IEvent_Base.MARKETING, IEvent_Base.MARKETING_HOME_AD_INTERSTITIAL_OPENED, "APP_LIST".hashCode(),
	    				"MARKETING", "HOME_AD_INTERSTITIAL_OPENED", "APP_LIST"));
	    	} catch(Exception e) {
	    		e.printStackTrace();
	    	}
    	
		} else {
			
			System.out.println("AdsContainer_HomeAds: request_sync promoted=" + promoted);
			
			if (promoted != null) {
				
				boolean ok = WebUtils.openApplicationStorePage(Application_Base.getInstance(), promoted, false);
				
				if (ok) {
					
					flow.loadOK();
					
					/**
					 * Clearer implementation will be to handle back button on the opened activity
					 * and than to cleanCurrent in order to set isDetached flag of flow to true 
					 */
					//flow.cleanCurrent();
					
					try {
		            	IEventsManager eventsManager = Application_Base.getInstance().getEventsManager();
		            	Context context = (getActivity() != null) ? getActivity() : Application_Base.getInstance();
		        		eventsManager.register(context,
		        				eventsManager.create(IEvent_Base.MARKETING, IEvent_Base.MARKETING_HOME_AD_INTERSTITIAL_OPENED, promoted.getID().hashCode(),
		        				"MARKETING", "HOME_AD_INTERSTITIAL_OPENED", "" + promoted.getID()));
			    	} catch(Exception e) {
			    		e.printStackTrace();
			    	}
					
				} else {
					flow.loadFailed();
				}
		
			} else {
				flow.loadFailed();
			}
		}
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
