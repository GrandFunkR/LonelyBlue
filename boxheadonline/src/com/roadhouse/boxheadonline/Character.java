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
	
	
	
	public int getDirection(double a){
		if (a >= -22.5 && a < 22.5) return Boxhead.RIGHT;
		else if (a >= 22.5 && a < 67.5) return Boxhead.DOWN_RIGHT;
		else if (a >= 67.5 && a < 112.5) return Boxhead.DOWN;
		else if (a >= 112.5 && a < 157.5) return Boxhead.DOWN_LEFT;
		else if (a >= 157.5 || a < -157.5) return Boxhead.LEFT;
		else if (a >= -157.5 && a < -112.5) return Boxhead.UP_LEFT;
		else if (a >= -112.5 && a < -67.5) return Boxhead.UP;
		else if (a >= -67.5 && a < -22.5) return Boxhead.UP_RIGHT;
		
		return 0;
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
	
	public float getDistance(Collidable o){
		float cx = character.x;
		float cy = character.y;
		float ox = o.getControl().x;
		float oy = o.getControl().y;
		float diffX = 0, diffY = 0;
		if (cx < ox) diffX = ox - cx;
		else if (cx > ox) diffX = cx - ox;
		if (cy < oy) diffY = oy - cy;
		else if (cy > oy) diffY = cy - oy;
		
		return (float) (Math.sqrt((double)(diffX*diffX + diffY*diffY)));
		 
		
	}
	
	public String toString(){
		return "CHARACTER;" + character.x + ";" + character.y;
	}
	
	

	
	
	
}
