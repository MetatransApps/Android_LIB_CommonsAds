package org.metatransapps.commons.ads.impl.flow;


import java.util.concurrent.ExecutorService;

import org.metatransapps.commons.ads.impl.sequence.IAdsContainerSequence;
import org.metatransapps.commons.ads.impl.stat.model.AdsData;

import android.os.Handler;
import android.view.ViewGroup;


public class AdLoadFlow_Banner extends AdLoadFlow_Base {
	
	
	private ViewGroup frame;
	private int gravity;
	
	
	public AdLoadFlow_Banner(String _adID, ViewGroup _frame, int _gravity, IAdsContainerSequence _containers_sequance, AdsData _adsData, Handler _uiHandler, ExecutorService _executor) {
		super(_adID, _containers_sequance, _adsData, _uiHandler, _executor);
		
		frame = _frame;
		gravity = _gravity;
	}
	
	
	public ViewGroup getFrame() {
		return frame;
	}


	public int getGravity() {
		return gravity;
	}
	
	
	protected void retry() {
		super.retry();
		getCurrentContainer().attach(this);
	}
	
	
	@Override
	public void cleanCurrent() {
		getCurrentContainer().detach(this);
		super.cleanCurrent();
	}
}
