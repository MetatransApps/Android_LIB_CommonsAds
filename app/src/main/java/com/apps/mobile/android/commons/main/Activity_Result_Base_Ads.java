package com.apps.mobile.android.commons.main;


import com.apps.mobile.android.commons.Activity_Base_Ads_Banner;
import com.apps.mobile.android.commons.R;
import com.apps.mobile.android.commons.app.Application_Base;
import com.apps.mobile.android.commons.cfg.colours.ConfigurationUtils_Colours;
import com.apps.mobile.android.commons.cfg.colours.IConfigurationColours;
import com.apps.mobile.android.commons.engagement.social.View_Social_InviteFriends;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;


public abstract class Activity_Result_Base_Ads extends Activity_Base_Ads_Banner {
	
	
	private int MAIN_VIEW_ID = 876983464;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
	}
	
	
	@Override
	public void onResume() {
		
		setContentView(R.layout.activity_result);
		
		FrameLayout frame = getFrame();
		View_Result view = createView();
		view.setOnTouchListener(new OnTouchListener_Result(view));
		view.setId(MAIN_VIEW_ID);
		frame.addView(view);
		
		super.onResume();
		
		IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(((Application_Base)getApplication()).getUserSettings().uiColoursID);		
		View view_invite_friends = new View_Social_InviteFriends(this, view.getRectangle_InviteFriends(),
				((Application_Base)getApplication()).getEngagementProvider().getSocialProvider(), coloursCfg);
		frame.addView(view_invite_friends);
	}
	
	
	protected abstract View_Result createView();
	
	public abstract void startNewGame();
	
	
	protected FrameLayout getFrame() {
		return (FrameLayout) findViewById(R.id.layout_result_vertical);
	}
		
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}
