package org.metatrans.commons.ads.impl.providers;


import java.util.HashSet;
import java.util.Set;

import org.metatrans.commons.DeviceUtils;
import org.metatrans.commons.ads.api.IAdsConfiguration;
import org.metatrans.commons.ads.impl.AdsManager;
import org.metatrans.commons.ads.impl.IAdsContainer;
import org.metatrans.commons.ads.impl.flow.AdLoadFlow_Banner;
import org.metatrans.commons.ads.impl.flow.AdLoadFlow_Interstitial;
import org.metatrans.commons.ads.utils.BannerUtils;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.app.Application_Base_Ads;

import android.app.Activity;
import android.content.Context;
import android.view.View;


public abstract class AdsContainer_Base implements IAdsContainer {
	
	
	private AdsStore_NoCache adsStore_Cache;
	
	private Context appContext;
	private IAdsConfiguration adsConf;
	
	
	public AdsContainer_Base(Context _appContext, IAdsConfiguration _adsConf) {
		appContext = _appContext;
		adsConf = _adsConf;
		adsStore_Cache = new AdsStore_NoCache(this);
	}
	
	
	@Override
	public void onCreate_Container(Context _app_context) {
	}
	
	
	public IAdsConfiguration getAdsConfiguration() {
		return adsConf;
	}
	
	protected boolean attachBanner_Initially() {
		return true;
	}
	
	protected boolean canWorkOffline() {
		return false;
	}
	
	protected abstract View createBanner(AdLoadFlow_Banner flow);
	protected abstract Object createBannerListener(AdLoadFlow_Banner flow);
	protected abstract void destroyBanner(Object ad);
	protected abstract void request_sync_banner(final View adview);
	
	protected void request_sync_banner(final View adview, AdLoadFlow_Banner flow) {
		request_sync_banner(adview);
	}
	
	
	protected abstract Object createInterstitial(AdLoadFlow_Interstitial flow);
	protected abstract Object createInterstitialListener(AdLoadFlow_Interstitial flow, Object interstitial);
	protected abstract void destroyInterstitial(Object ad);
	protected abstract void showInterstitial(Object ad);
	protected void showInterstitial(Object ad, AdLoadFlow_Interstitial flow) {
		showInterstitial(ad);
	}
	
	
	protected String[] getKeywords() {
		return Application_Base.getInstance().getKeywords();
	}
	
	
	protected Set<String> getKeywordsSet() {
		Set<String> result = new HashSet<String>();
		String[] keywords = getKeywords();
		for (int i=0; i<keywords.length; i++) {
			result.add(keywords[i]);
		}
		return result;
	}
	
	
	protected String getKeywordsLine() {
		String result = "";
		String[] keywords = getKeywords();
		for (int i=0; i<keywords.length; i++) {
			result += keywords[i];
			if (i != keywords.length - 1) {
				result += " ";
			}
		}
		return result;
	}
	
	
	protected Activity getActivity() {
		
		//if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) { //API 10 = SDK < 3.0
			//if (activity instanceof Application_Base_Ads) {
				Activity current = ((Application_Base_Ads)appContext).getCurrentActivity();
				if (current != null) {
					return current;
				} else {
					return null;//(Activity) appContext//new Activity();
				}
			//}
	    //}
		
		//return activity;
	}
	
	
	public void initInterstitial(AdLoadFlow_Interstitial flow) {
		
		System.out.println("AdsContainer_Base.initInterstitial(flow) : called");
		
		if (!DeviceUtils.isConnectedOrConnecting()) {
			
			if (!canWorkOffline()) {
				
				System.out.println("AdsContainer_Base.initInterstitial(flow) : This container " + this + " cannot work offline. Continue with next. Current flow is " + flow);
				
				flow.loadFailed();
				
				return;
			}
		}
		
		//Create the Ad at the first call
		final Object result = adsStore_Cache.getInterstitial(flow);
		
		System.out.println("AdsContainer_Base.initInterstitial(flow) : This container " + result + " was initialized for the interstitial ads.");
	}
	
	
	public void requestInterstitial(final AdLoadFlow_Interstitial flow) {
		
		System.out.println("AdsContainer_Base.requestInterstitial(flow) : called");
		
		if (!DeviceUtils.isConnectedOrConnecting()) {
			
			if (!canWorkOffline()) {
				
				System.out.println("AdsContainer_Base.requestInterstitial(flow) : This container " + this + " cannot work offline. Continue with next. Current flow is " + flow);
				
				flow.loadFailed();
				
				return;
			}
		}
		
		final Object result = adsStore_Cache.getInterstitial(flow);
		
		System.out.println("AdsContainer_Base.requestInterstitial(flow) : ad = " + result);
		
		AdsManager.getSingleton().getUiHandler().post(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					
					showInterstitial(result, flow);
					
				} catch(Exception e) {
					
					e.printStackTrace();
					
					flow.loadFailed();
				}
			    
			}
		});
	}
	
	
	public void removeInterstitial(String adID, AdLoadFlow_Interstitial flow) {
		adsStore_Cache.returnInterstitial(adID, flow);
	}
	
	
	public void detach(AdLoadFlow_Banner flow) {
		adsStore_Cache.returnBanner(flow.getAdID());
	}
	
	
	public void attach(final AdLoadFlow_Banner flow) {
		
		if (!DeviceUtils.isConnectedOrConnecting()) {
			
			if (!canWorkOffline()) {
				
				flow.loadFailed();
				
				System.out.println("AdsContainer_Base.attach(flow) : This container " + this + " cannot work offline. Continue with next. Current flow is " + flow);
				
				return;
			}
		}
		
		View banner = adsStore_Cache.getBanner(flow);
		
		if (attachBanner_Initially()) {
			//This is workaround for admob_gps banners - if attached before first load, the banner is sometimes invisible.
			flow.getFrame().addView(banner);
		}
		
		final View adview = banner.findViewById(BannerUtils.AD_BANNER_VIEW_ID);
		
		AdsManager.getSingleton().getUiHandler().post(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					
					request_sync_banner(adview, flow);
					
				} catch(Exception e) {

					e.printStackTrace();
					
					flow.loadFailed();
				}
			}
		});
	}
}
