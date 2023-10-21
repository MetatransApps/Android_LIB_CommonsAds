package org.metatrans.commons.ads.impl.flow;


import java.util.concurrent.ExecutorService;

import org.metatrans.commons.DebugTags;
import org.metatrans.commons.ads.impl.sequence.IAdsContainerSequence;
import org.metatrans.commons.ads.impl.stat.model.AdsData;

import android.os.Handler;
import android.view.ViewGroup;


public class AdLoadFlow_Banner extends AdLoadFlow_Base {
	
	
	private ViewGroup frame;
	private int gravity;
	
	
	public AdLoadFlow_Banner(String _adID, ViewGroup _frame, int _gravity, IAdsContainerSequence _containers_sequance, AdsData _adsData, Handler _uiHandler, ExecutorService _executor) {

		super(_adID, _containers_sequance, _adsData, _uiHandler, _executor);
		
		frame = _frame;
		gravity = _gravity;

		System.out.println(DebugTags.ADS_FLOWS + "AdLoadFlow_Banner: constructor(): called, frame=" + frame + ", gravity=" + gravity);
	}
	
	
	public ViewGroup getFrame() {
		return frame;
	}


	public int getGravity() {
		return gravity;
	}
	
	
	protected void retry() {

		System.out.println(DebugTags.ADS_FLOWS + "AdLoadFlow_Banner: retry(): called");

		super.retry();

		getCurrentContainer().attach(this);
	}
	
	
	@Override
	public void cleanCurrent() {

		System.out.println(DebugTags.ADS_FLOWS + "AdLoadFlow_Banner: cleanCurrent(): called");

		getCurrentContainer().detach(this);

		super.cleanCurrent();
	}
}
