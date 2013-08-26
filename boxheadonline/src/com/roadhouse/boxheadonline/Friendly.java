package com.roadhouse.boxheadonline;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

public class Friendly extends Collidable {

	final static int NORTH_EAST = 0;
	final static int SOUTH_EAST = 1;
	final static int NORTH_WEST = 2;
	final static int SOUTH_WEST = 3;

	private Collidable attachedTo;
	
	private Circle friendly;
	private double speedX, speedY;

	final static int HEALTH_MAX = 15;
	final static int SPEED = 2;
	private int health;
	private double direction;
	
	
	
	
	private int attachedAt;

	private static Texture friendImg;
	
	public Friendly (){
		super.setControl(new Circle());
		this.friendly = super.getControl();
		speedX = 0.0;
		speedY = 0.0;

		health =HEALTH_MAX;
		setFriendImg(new Texture (Gdx.files.internal("testing/char.png")));
	}

	

	public static Friendly friendlySpawn(int level){
		int radius = 10 + (int) Math.sqrt (Math.pow (Boxhead.SCREEN_WIDTH/2, 2) + Math.pow (Boxhead.SCREEN_HEIGHT/2, 2)); // spawning radius (from center of map)
		int tempX, tempY;

		Random rand = new Random();

		tempX = rand.nextInt(radius);
		if (rand.nextInt(2) == 0){
			tempX *= -1;
		}
		tempY = (int) Math.sqrt (Math.pow (radius, 2) - Math.pow (tempX, 2)); // spawning y (from center of map)

		if (rand.nextInt(2) == 0){
			tempY *= -1;
		}
		Friendly newfriendly = new Friendly();
		newfriendly.getfriendly().radius = 64;
		newfriendly.getfriendly().x = Boxhead.SCREEN_WIDTH / 2 + tempX;
		newfriendly.getfriendly().y = Boxhead.SCREEN_HEIGHT / 2 + tempY;

		return newfriendly;
	}

	private void calculateSpeed (){
		speedX = SPEED*(Math.cos(direction));
		speedY = SPEED*(Math.sin(direction));
	}

	public void move (Circle character){
		this.calculateDirection(character);
		this.calculateSpeed();
		friendly.x -= speedX;
		friendly.y -= speedY;
	}

	private void calculateDirection (Circle character){
		double down, top;
		top = (character.x + character.radius) - (friendly.x+ friendly.radius);
		down = (character.y + character.radius) - (friendly.y + friendly.radius);
		Double angle = 0.0;
		angle = (Math.atan(top / down));


		if (character.x + character.radius < friendly.x + friendly.radius ) {
			if (character.y + character.radius < friendly.y + friendly.radius) {
				angle = Math.PI/2 - angle;

			}else if (character.y + character.radius > friendly.y + friendly.radius) {
				angle = (Math.PI/2 + angle)*-1;

			}
		} else if (character.x + character.radius > friendly.x + friendly.radius) {
			if (character.y + character.radius < friendly.y + friendly.radius) {
				angle = Math.PI/2 + (angle*-1);
			} else if (character.y + character.radius > friendly.y + friendly.radius) {
				angle = (Math.PI/2 + angle)*-1;
			}
		}
		direction = angle;

	}

	public void decreaseHealth(){
		health--;
	}

	public Circle getfriendly() {
		return friendly;
	}
	public void setfriendly(Circle friendly) {
		this.friendly = friendly;
	}
	public double getSpeedX() {
		return speedX;
	}
	public void setSpeedX(double speedX) {
		this.speedX = speedX;
	}
	public double getSpeedY() {
		return speedY;
	}
	public void setSpeedY(double speedY) {
		this.speedY = speedY;
	}



	@Override
	public boolean isColliding(Collidable otherObject, double bound) {
		// TODO Auto-generated method stub
		Circle other = otherObject.getControl();

		double xDif = 0.0;
		double yDif = 0.0;
		double c = 0.0;

		try {
			if (otherObject instanceof Bullet) {
		
				return friendly.contains(other.x, other.y);				
			
			} else if (otherObject instanceof Character || otherObject instanceof Friendly) {
				if (friendly.x + friendly.radius > other.x + other.radius) {
					xDif = friendly.x + friendly.radius - other.x + other.radius;
				} else if (friendly.x + friendly.radius < other.x + other.radius) {
					xDif = other.x + other.radius - friendly.x + friendly.radius;
				}

				if (friendly.y + friendly.radius > other.y + other.radius) {
					yDif = friendly.y + friendly.radius - other.y + other.radius;
				} else if (friendly.y + friendly.radius < other.y + other.radius) {
					yDif = other.y + other.radius - friendly.y + friendly.radius;
				}
				c = Math.sqrt(Math.pow(yDif, 2) + Math.pow(xDif, 2));
				c = c - 180;
			}

		} catch (Exception e) {
			return false;
		}

		return c <= bound;

	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}

	public static Texture getFriendImg() {
		return friendImg;
	}

	public static void setFriendImg(Texture eneImg) {
		Friendly.friendImg = eneImg;
	}



	public Collidable getAttachedTo() {
		return attachedTo;
	}



	public void attachTo(Collidable attachedTo) {
		this.attachedTo = attachedTo;
	}



	public int getAttachedAt() {
		return attachedAt;
	}



	public void setAttachedAt(int attachedAt) {
		this.attachedAt = attachedAt;
	}



}
