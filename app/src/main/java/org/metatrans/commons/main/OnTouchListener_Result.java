package org.metatrans.commons.main;


import org.metatrans.commons.Alerts_Base;
import org.metatrans.commons.ads.R;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.main.View_Result;
import org.metatrans.commons.marketing.Activity_Marketing_AppList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public class OnTouchListener_Result implements OnTouchListener {
	
	
	private View_Result view;
	
	
	public OnTouchListener_Result(View_Result _view) {
		view = _view;
	}
	
	
	@Override
	public boolean onTouch(View _view, MotionEvent event) {
		
		synchronized (view) {
			
			int action = event.getAction();
			
			if (action == MotionEvent.ACTION_DOWN) {

				processEvent_DOWN(event);

			} else if (action == MotionEvent.ACTION_MOVE) {
				
				processEvent_MOVE(event);
				
			} else if (action == MotionEvent.ACTION_UP
					|| action == MotionEvent.ACTION_CANCEL) {

				processEvent_UP(event);

			}
		}
		
		return true;
	}
	
	
	private void processEvent_DOWN(MotionEvent event) {
		
		
		float x = event.getX();
		float y = event.getY();
		
		
		if (view.isOverButton_Back(x, y)) {
			
			view.selectButton_Back();
			
		} else if (view.isOverButton_New(x, y)) {
			
			view.selectButton_New();
			
		} else if (view.isOverButton_MoreGames(x, y)) {
			
			view.selectButton_MoreGames();
		}
		
	}
	
	
	private void processEvent_MOVE(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		
		if (view.isOverButton_Back(x, y)) {
			view.selectButton_Back();
		} else {
			view.deselectButton_Back();
		}
		
		if (view.isOverButton_New(x, y)) {
			view.selectButton_New();
		} else {
			view.deselectButton_New();
		}
		
		if (view.isOverButton_MoreGames(x, y)) {
			view.selectButton_MoreGames();
		} else {
			view.deselectButton_MoreGames();
		}
		
	}
	
	
	private void processEvent_UP(MotionEvent event) {
		
		float x = event.getX();
		float y = event.getY();
		
		if (view.isOverButton_Back(x, y)) {
			
			view.deselectButton_Back();
			
			((Activity_Result_Base_Ads)view.getContext()).finish();
			
		} else if (view.isOverButton_New(x, y)) {
			
			view.deselectButton_New();
			
			AlertDialog.Builder adb = Alerts_Base.createAlertDialog_LoseGame(view.getContext(),

					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							Application_Base.getInstance().getSFXManager().playSound(R.raw.sfx_button_pressed_1);

							((Activity_Result_Base_Ads)view.getContext()).startNewGame();
							((Activity_Result_Base_Ads)view.getContext()).finish();
							
						}
					});

			adb.show();

			
		} else if (view.isOverButton_MoreGames(x, y)) {
			
			view.deselectButton_MoreGames();
			
			Intent i = new Intent(view.getContext(), Activity_Marketing_AppList.class);
			view.getContext().startActivity(i);
		}
	}
}
