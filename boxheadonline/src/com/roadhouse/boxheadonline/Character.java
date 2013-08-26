package com.roadhouse.boxheadonline;

import java.io.Serializable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;



public class Character extends Collidable implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -9008207712071753129L;
	final static double CHARACTER_SPEED = 0.1;
	final static int CHARACTER_HEALTH = 640;
	
	
	
	private Circle character;
	private int health;
	public Character(){
		super.setControl(new Circle());
		character = super.getControl();
		character.radius = 64;
		character.x = 1280 / 2;
		character.y = 720 / 2;
		setHealth(CHARACTER_HEALTH);
	}
	
	public Character(int x, int y){

	}

	public Circle getControl (){
		return character;
	}
	
	public void moveCharacter(double xDiff2, double yDiff2) {
		if (character.x >= 0 && character.x + 128 <= Boxhead.SCREEN_WIDTH) {
			character.x += xDiff2 * CHARACTER_SPEED;
		} else if (character.x < 0) {
			character.x = 0;
		} else if (character.x + character.radius * 2 > Boxhead.SCREEN_WIDTH) {
			character.x = (float) (Boxhead.SCREEN_WIDTH - 129);
		}

		if (character.y >= 0 && character.y + 128 <= Boxhead.SCREEN_HEIGHT) {

			character.y -= yDiff2 * CHARACTER_SPEED;
		} else if (character.y < 0) {
			character.y = 0;
		} else if (character.y + character.radius * 2 > Boxhead.SCREEN_HEIGHT) {
			character.y = (float) (Boxhead.SCREEN_HEIGHT - 129);
		}

	}

	@Override
	public boolean isColliding(Collidable secondObject, double bound) {
		// TODO Auto-generated method stub
		return false;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public void resetCharacter (){
		health = CHARACTER_HEALTH;
		character.x = 1280 / 2;
		character.y = 720 / 2;
	}
	
	public void decreaseHealth(){
		health--;
	}
	
	public void setPosition (float x, float y){
		character.x = x;
		character.y = y;
		
	}
	
	public String toString(){
		return "CHARACTER " + character.x + " " + character.y;
	}
	
	

	
	
	
}
