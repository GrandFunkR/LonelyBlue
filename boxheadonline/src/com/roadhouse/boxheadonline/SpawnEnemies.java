package com.roadhouse.boxheadonline;

import com.badlogic.gdx.utils.Timer.Task;

public class SpawnEnemies extends Task {

	private int level;
	 
	public SpawnEnemies (int level){
		
	}
	
	@Override
	public void run() {
		Boxhead.enemies.add(Enemy.enemySpawn(level));
		// TODO Auto-generated method stub
		
	}

}

