package com.roadhouse.boxheadonline;

import com.badlogic.gdx.utils.Timer.Task;

public class SpawnFriendlies extends Task {

	private int level;
	 
	public SpawnFriendlies (int level){
		
	}
	
	@Override
	public void run() {
		Boxhead.roamingFriendlies.add(Friendly.friendlySpawn(level));
		// TODO Auto-generated method stub
		
	}

}

