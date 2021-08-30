package org.metatransapps.commons.ads.impl.flow;


import java.util.concurrent.ExecutorService;

import org.metatransapps.commons.ads.impl.sequence.IAdsContainerSequence;
import org.metatransapps.commons.ads.impl.stat.model.AdsData;

import android.os.Handler;


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
