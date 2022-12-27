package org.metatrans.commons.ads.impl.providers.dummy;


import org.metatrans.commons.ads.api.IAdsProviders;
import org.metatrans.commons.ads.impl.IAdsContainer;
import org.metatrans.commons.ads.impl.flow.AdLoadFlow_Banner;
import org.metatrans.commons.ads.impl.flow.AdLoadFlow_Interstitial;

import android.content.Context;


public class AdsContainer_Dummy implements IAdsContainer {
	

	public AdsContainer_Dummy(Context context) {

	}


	@Override
	public int getProviderID() {
		return IAdsProviders.ID_DUMMY;
	}
	
	
	@Override
	public void onCreate_Container(Context _app_context) {

		//Do nothing
	}
	
	
	@Override
	public void removeInterstitial(String adID, AdLoadFlow_Interstitial flow) {

		//Do nothing
	}
	
	
	@Override
	public void attach(AdLoadFlow_Banner flow) {

		flow.loadOK();
		flow.pause();
	}
	
	
	@Override
	public void detach(AdLoadFlow_Banner flow) {

		//Do nothing
	}
	
	
	@Override
	public void requestInterstitial(AdLoadFlow_Interstitial flow) {

		flow.loadOK();
		flow.pause();
	}


	@Override
	public void initInterstitial(AdLoadFlow_Interstitial flow) {

		//Do nothing
	}
}
