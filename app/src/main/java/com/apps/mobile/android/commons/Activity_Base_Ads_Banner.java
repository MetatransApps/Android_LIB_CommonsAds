package com.apps.mobile.android.commons;


import com.apps.mobile.android.commons.ads.impl.flow.IAdLoadFlow;
import com.apps.mobile.android.commons.app.Application_Base;
import com.apps.mobile.android.commons.app.Application_Base_Ads;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public abstract class Activity_Base_Ads_Banner extends com.apps.mobile.android.commons.Activity_Base {
	
	
	private IAdLoadFlow current_adLoadFlow;
	private boolean isBannerAttached;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
	}
	
	
	protected abstract String getBannerName();
	
	
	protected abstract FrameLayout getFrame();
	
	
	protected int getGravity() {
		return Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
	}
	
	/*
	@Override
	protected void onDestroy() {
		
		if (getAutoMode_BannerAttachDetach()) detachBanner();
		
		current_adLoadFlow = null;
		
		super.onDestroy();
	}
	 */	
	
	
	@Override
	protected void onPause() {
		
		System.out.println("Activity_Base_Ads_Banner: onPause()");
		
		detachBanner();
		
		super.onPause();
	}
	
	
	@Override
	protected void onResume() {
		
		System.out.println("Activity_Base_Ads_Banner: onResume()");
		
		super.onResume();
		
		attachBanner();
	}
	
	
	protected boolean isBannerAttached() {
		return isBannerAttached;
	}
	
	
	protected void detachBanner() {
		
		try {
	        
			if (current_adLoadFlow != null) {
				current_adLoadFlow.pause();
				current_adLoadFlow = null;
			}
			
			isBannerAttached = false;
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	protected void attachBanner() {
		
		try {
			
	        ViewGroup frame = getFrame();
	        
	        if (Application_Base.getInstance().isTestMode()) {
	            if (frame == null) {
	        		throw new IllegalStateException("Frame is null");
	        	}
	        }
	        
	        if (frame != null) {
	        	
				//System.out.println("Activity_Base_Ads_Banner: attachBanner() frame.isShown = " + frame.isShown());
				
				if (current_adLoadFlow != null) {
					throw new IllegalStateException("current_adLoadFlow is NOT null");
				}
				
				if (getBannerName() != null /*&& DeviceUtils.isConnectedOrConnecting(this)*/) {
					current_adLoadFlow = ((Application_Base_Ads)getApplication()).getAdsManager().createFlow_Banner_Mixed(frame, getBannerName(), getGravity());
				}				
				
				if (current_adLoadFlow != null) {
					
					System.out.println("Activity_Base_Ads_Banner: attachBanner() - resume add");
					
					current_adLoadFlow.resume();
					
					isBannerAttached = true;
				}
	        }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
