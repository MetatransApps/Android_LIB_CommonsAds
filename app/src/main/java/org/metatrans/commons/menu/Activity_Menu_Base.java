package org.metatrans.commons.menu;


import java.util.ArrayList;
import java.util.List;

import org.metatrans.commons.Activity_Base;
import org.metatrans.commons.Activity_Base_Ads_Banner;
import org.metatrans.commons.R;
import org.metatrans.commons.app.Application_Base;
import org.metatrans.commons.cfg.colours.ConfigurationUtils_Colours;
import org.metatrans.commons.cfg.colours.IConfigurationColours;
import org.metatrans.commons.cfg.menu.IConfigurationMenu_Main;
import org.metatrans.commons.model.BitmapCache_Base;
import org.metatrans.commons.ui.Toast_Base;
import org.metatrans.commons.ui.list.ListViewFactory;
import org.metatrans.commons.ui.list.RowItem_IdTD;
import org.metatrans.commons.ui.utils.BitmapUtils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


public abstract class Activity_Menu_Base extends Activity_Base_Ads_Banner {
	
	
	private List<IConfigurationMenu_Main> entries;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		try {
			
			entries = getEntries();
			
			LayoutInflater inflater = LayoutInflater.from(this);

			IConfigurationColours coloursCfg = ConfigurationUtils_Colours.getConfigByID(((Application_Base) getApplication()).getUserSettings().uiColoursID);

			int color_background = coloursCfg.getColour_Background();

			ViewGroup frame = ListViewFactory.create_ITD_ByXML(this, inflater, buildRows(entries), -1, color_background, new OnItemClickListener_Menu());
			
			setContentView(frame);
			
			setBackgroundPoster(R.id.commons_listview_frame, 55);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	@Override
	public void onPause() {
		
		System.out.println("Activity_Menu_Main: onPause()");
		
		super.onPause();
		
	}
	
	
	@Override
	public void onResume() {
		
		System.out.println("Activity_Menu_Main: onResume()");
		
		super.onResume();

		Application_Base.getInstance().showRateAppDialog(this);
	}
	
	
	protected abstract List<IConfigurationMenu_Main> getEntries();
	
	
	private List<RowItem_IdTD> buildRows(List<IConfigurationMenu_Main> entries) {
		
		List<RowItem_IdTD> rowItems = new ArrayList<RowItem_IdTD>();
		
		for (int i = 0; i < entries.size(); i++) {
			
			IConfigurationMenu_Main entry = entries.get(i);
			
			String title = entry.getName_String();
			String description = entry.getDescription_String();
			/*if ("".equals(description.trim())) {
				description = entry.getDescription() == 0 ? "" : getString(entry.getDescription());
			}*/

			int bitmap_id = entry.getIconResID();
			Bitmap bitmap = null;
			//The try-catch block is for backward compatibility until all apps start using I2DBitmapCache
			//and its immutable bitmap IDs. In contrasts the bitmap IDs in R.drawable could change because of Android build.
			try {

				bitmap = BitmapCache_Base.STATIC.getInstance(BitmapCache_Base.BITMAP_ID_COMMON).get(bitmap_id);
				bitmap = BitmapUtils.createScaledBitmap(bitmap, getIconSize(), getIconSize());

			} catch(Exception e) {

				//The old way ...
				bitmap = BitmapUtils.fromResource(Application_Base.getInstance(), bitmap_id, getIconSize());
			}


			Drawable drawable = BitmapUtils.createDrawable(this, bitmap);
			
			RowItem_IdTD item = new RowItem_IdTD(drawable, title, description);

			rowItems.add(item);
		}
		
		
		return rowItems;
	}

	
	private class OnItemClickListener_Menu implements
			AdapterView.OnItemClickListener {
		
		
		private OnItemClickListener_Menu() {
		}
		
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			
			Runnable action = entries.get(position).getAction();
			
			if (action == null) {
				Toast_Base.showToast_InCenter_Long(Activity_Menu_Base.this, "Clicked menu item with id=" + id + ", but no action found ...");
			} else {
				action.run();
			}
		}
	}
}
