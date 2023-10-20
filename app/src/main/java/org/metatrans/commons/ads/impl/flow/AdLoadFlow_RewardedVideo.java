package org.metatrans.commons.ads.impl.flow;


import android.os.Handler;

import org.metatrans.commons.ads.impl.sequence.IAdsContainerSequence;
import org.metatrans.commons.ads.impl.stat.model.AdsData;

import java.util.concurrent.ExecutorService;


public class AdLoadFlow_RewardedVideo extends AdLoadFlow_Base {


	public AdLoadFlow_RewardedVideo(String _adID, IAdsContainerSequence _containers_sequance, AdsData _adsData
			, Handler _uiHandler, ExecutorService _executor) {
		super(_adID, _containers_sequance, _adsData, _uiHandler, _executor);
	}
	
	
	@Override
	protected void retry() {
		super.retry();
		getCurrentContainer().requestRewardedVideo(this);
	}
	
	
	@Override
	public void cleanCurrent() {
		getCurrentContainer().removeRewardedVideo(getAdID(), this);
		super.cleanCurrent();
	}


	@Override
	public synchronized void loadOK() {

		super.loadOK();

		pause();
	}
}
