package com.roadhouse.boxheadonline;

public class ScoreUp {
	
	protected int currentScore;
	protected float x,y;
	protected int counter;
	protected int activeCounter;
	
	public ScoreUp(int currentScore, float x,float y){
		counter = 50;
		this.x = x;
		this.y = y;
		this.currentScore = currentScore;
		activeCounter = 0;
	}

	public boolean incrementOrRemove (){
		activeCounter++;
		return activeCounter > counter;
	}
}
