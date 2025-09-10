package org.metatrans.commons.ads.impl.flow;


import android.os.Handler;

import org.metatrans.commons.DebugTags;
import org.metatrans.commons.ads.impl.sequence.IAdsContainerSequence;
import org.metatrans.commons.ads.impl.stat.model.AdsData;

import java.util.concurrent.ExecutorService;


public class AdLoadFlow_RewardedVideo extends AdLoadFlow_Base {


	public AdLoadFlow_RewardedVideo(String _adID, IAdsContainerSequence _containers_sequence, AdsData _adsData,
									Handler _uiHandler, ExecutorService _executor) {

		super(_adID, _containers_sequence, _adsData, _uiHandler, _executor);

		System.out.println(DebugTags.ADS_FLOWS + "AdLoadFlow_RewardedVideo.constructor(): _containers_sequence=" + _containers_sequence.getAdsContainers());
	}
	
	
	@Override
	protected void retry() {

		System.out.println(DebugTags.ADS_FLOWS + "AdLoadFlow_RewardedVideo.retry(): called");

		super.retry();

		getCurrentContainer().initRewardedVideo(this);
	}
	
	
	@Override
	public void cleanCurrent() {

		System.out.println(DebugTags.ADS_FLOWS + "AdLoadFlow_RewardedVideo.cleanCurrent(): called");

		if (getCurrentContainer() != null) getCurrentContainer().removeRewardedVideo(getAdID(), this);

		super.cleanCurrent();
	}


	@Override
	public synchronized void loadOK() {

		System.out.println(DebugTags.ADS_FLOWS + "AdLoadFlow_RewardedVideo.loadOK(): called");

		super.loadOK();

		getCurrentContainer().requestRewardedVideo(this);

		pause();
	}
}
