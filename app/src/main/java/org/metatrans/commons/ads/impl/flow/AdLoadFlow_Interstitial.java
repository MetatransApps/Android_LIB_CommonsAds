package org.metatrans.commons.ads.impl.flow;


import java.util.concurrent.ExecutorService;

import org.metatrans.commons.DebugTags;
import org.metatrans.commons.ads.impl.sequence.IAdsContainerSequence;
import org.metatrans.commons.ads.impl.stat.model.AdsData;

import android.os.Handler;


public class AdLoadFlow_Interstitial extends AdLoadFlow_Base {
	
	
	public AdLoadFlow_Interstitial(String _adID, IAdsContainerSequence _containers_sequance, AdsData _adsData
			, Handler _uiHandler, ExecutorService _executor) {

		super(_adID, _containers_sequance, _adsData, _uiHandler, _executor);

		System.out.println(DebugTags.ADS_FLOWS + "AdLoadFlow_Interstitial.constructor(): _containers_sequance=" + _containers_sequance.getAdsContainers());
	}
	
	
	@Override
	protected void retry() {

		System.out.println(DebugTags.ADS_FLOWS + "AdLoadFlow_Interstitial.retry(): called");

		super.retry();

		getCurrentContainer().requestInterstitial(this);
	}
	
	
	@Override
	protected void cleanCurrent() {

		System.out.println(DebugTags.ADS_FLOWS + "AdLoadFlow_Interstitial.cleanCurrent(): called");

		getCurrentContainer().removeInterstitial(getAdID(), this);

		super.cleanCurrent();
	}

	@Override
	public synchronized void loadOK() {

		System.out.println(DebugTags.ADS_FLOWS + "AdLoadFlow_Interstitial.loadOK(): called");

		super.loadOK();

		pause();
	}
}
