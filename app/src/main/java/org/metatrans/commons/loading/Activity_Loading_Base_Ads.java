package org.metatrans.commons.loading;


import android.view.View;
import android.view.ViewGroup;

import org.metatrans.commons.R;
import org.metatrans.commons.ads.impl.flow.IAdLoadFlow;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.loading.Activity_Loading_Base;


public abstract class Activity_Loading_Base_Ads extends Activity_Loading_Base {
	
	
	private int HOUSE_AD_VIEW_ID = 571437512;
	
	
	//private IAdLoadFlow current_adLoadFlow_Banner;
	private IAdLoadFlow current_adLoadFlow_Interstitial;
	
	
	protected abstract String getBannerName();
	protected abstract String getInterstitialName();
	@Override
	protected abstract IConfigurationColours getColoursCfg();
	
	
	@Override
	protected void onDestroy() {
		
		/*try {
			
			if (current_adLoadFlow_Banner != null) current_adLoadFlow_Banner.pause();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		current_adLoadFlow_Banner = null;*/
		
		super.onDestroy();
	}
	
	
	@Override
	public void onPause() {
		
		detachBanners();
		
		super.onPause();
	}
	
	
	@Override
	protected void onResume() {
		
		super.onResume();
			
		attachBanners();
			
	}
	
	
	private void detachBanners() {
		try {
			
			ViewGroup frame = (ViewGroup) findViewById(R.id.commons_layout_loading);
			if (frame != null) {
		       View old = frame.findViewById(HOUSE_AD_VIEW_ID);
		       if (old != null) {
		    	   frame.removeView(old);
		       }
			}
			
			/*if (current_adLoadFlow_Banner != null) current_adLoadFlow_Banner.pause();
			
			current_adLoadFlow_Banner = null;*/
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void attachBanners() {
		
		try {
			
	        ViewGroup frame = (ViewGroup) findViewById(R.id.commons_layout_loading);
	        
	        if (Application_Base.getInstance().isTestMode()) {
	            if (frame == null) {
	        		throw new IllegalStateException("Frame is null");
	        	}
	        }
	        
	        /*if (frame != null) {
					
				if (getBannerName() != null) {
					
					current_adLoadFlow_Banner = ((Application_Base_Ads)getApplication()).getAdsManager().createFlow_Banner_Mixed(frame, getBannerName(), Gravity.TOP);
				}
				
				if (current_adLoadFlow_Banner != null) {
					
					current_adLoadFlow_Banner.resume();
				}
	        }*/
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void openInterstitial() {
		
		try {
			
			System.out.println("Activity_Loading_Base_Ads openInterstitial called");
			
			if (current_adLoadFlow_Interstitial != null) {
				System.out.println("Activity_Loading_Base_Ads openInterstitial RESUMED");
				current_adLoadFlow_Interstitial.resume();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
