package com.roadhouse.boxheadonline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;

public class Bullet extends Collidable {
	
	private Circle control;
	private static Texture image = new Texture (Gdx.files.internal("sprites/bullet.png"));
	private double speedX,speedY;
	
	public Bullet(float x, float y, double speedX, double speedY){
		super.setControl(new Circle());
		control = super.getControl();
		control.x = x;
		control.y = y;
		control.radius = 8;
		this.speedX = speedX;
		this.speedY = speedY;
	}

	public Circle getControl() { return control; }
	public static Texture getImage() {	return image; }
	
	public void setControl(Circle control) { this.control = control; }
	
	public void moveBullet (){
		
		control.x += this.speedX;
		control.y += this.speedY;
	}

	public double getSpeedY() {
		return speedY;
	}

	public void setSpeedY(double speedY) {
		this.speedY = speedY;
	}

	public double getSpeedX() {
		return speedX;
	}

	public void setSpeedX(double speedX) {
		this.speedX = speedX;
	}
	
	public static void dispose(){
		image.dispose();
	}

	@Override
	public boolean isColliding(Collidable secondObject, double bound) {
		// TODO Auto-generated method stub
		return false;
	}

}
