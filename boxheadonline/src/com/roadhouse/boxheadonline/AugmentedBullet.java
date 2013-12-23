package com.roadhouse.boxheadonline;

public class AugmentedBullet {

	private float x;
	private float y;
	private double speedx;
	private double speedy;
	
	public AugmentedBullet (float x, float y, double speedx, double speedy){
		this.x = x;
		this.y = y;
		this.speedx = speedx;
		this.speedy = speedy;
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public double getSpeedx() {
		return speedx;
	}
	public void setSpeedx(double speedx) {
		this.speedx = speedx;
	}
	public double getSpeedy() {
		return speedy;
	}
	public void setSpeedy(double speedy) {
		this.speedy = speedy;
	}
	
}
