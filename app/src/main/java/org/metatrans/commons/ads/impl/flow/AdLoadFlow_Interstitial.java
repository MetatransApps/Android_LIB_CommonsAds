package org.metatrans.commons.ads.impl.flow;


import java.util.concurrent.ExecutorService;

import org.metatrans.commons.ads.impl.sequence.IAdsContainerSequence;
import org.metatrans.commons.ads.impl.stat.model.AdsData;

import android.os.Handler;


public class AdLoadFlow_Interstitial extends AdLoadFlow_Base {
	
	
	public AdLoadFlow_Interstitial(String _adID, IAdsContainerSequence _containers_sequance, AdsData _adsData
			, Handler _uiHandler, ExecutorService _executor) {
		super(_adID, _containers_sequance, _adsData, _uiHandler, _executor);

		System.out.println("AdLoadFlow_Interstitial.constructor(): _containers_sequance=" + _containers_sequance.getAdsContainers());
	}
	
	
	@Override
	protected void retry() {
		super.retry();
		getCurrentContainer().requestInterstitial(this);
	}
	
	
	@Override
	protected void cleanCurrent() {
		getCurrentContainer().removeInterstitial(getAdID(), this);
		super.cleanCurrent();
	}

	@Override
	public synchronized void loadOK() {

		super.loadOK();

		pause();
	}
}
