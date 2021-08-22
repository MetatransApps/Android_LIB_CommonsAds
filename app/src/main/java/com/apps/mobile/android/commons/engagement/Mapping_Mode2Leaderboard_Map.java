package com.apps.mobile.android.commons.engagement;


import java.util.Map;


public class Mapping_Mode2Leaderboard_Map implements IMapping_Mode2Leaderboard {
	
	
	private Map<Integer, String> mapping_mode2leaderboard;
	 
	 
	public Mapping_Mode2Leaderboard_Map(Map<Integer, String> _mapping_mode2leaderboard) {
		mapping_mode2leaderboard = _mapping_mode2leaderboard;
	}


	@Override
	public String getLeaderboardID(int modeID) {
		return mapping_mode2leaderboard.get(modeID);
	}
}
