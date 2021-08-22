package com.apps.mobile.android.commons.ads.impl.flow;


import java.util.concurrent.ExecutorService;

import android.os.Handler;

import com.apps.mobile.android.commons.ads.impl.sequence.IAdsContainerSequence;
import com.apps.mobile.android.commons.ads.impl.stat.model.AdsData;


public class AdLoadFlow_Interstitial extends AdLoadFlow_Base {
	
	
	public AdLoadFlow_Interstitial(String _adID, IAdsContainerSequence _containers_sequance, AdsData _adsData
			, Handler _uiHandler, ExecutorService _executor) {
		super(_adID, _containers_sequance, _adsData, _uiHandler, _executor);
	}
	
	
	@Override
	protected void retry() {
		super.retry();
		getCurrentContainer().requestInterstitial(this);
	}
	
	
	@Override
	public void cleanCurrent() {
		getCurrentContainer().removeInterstitial(getAdID(), this);
		super.cleanCurrent();
	}
}
