package com.roadhouse.boxheadonline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

public class Joypad {

	Circle pad;
	Circle knob;
	Texture padImg;
	Texture knobImg;
	boolean blueKnob;
	boolean touching;

	private double xDiff, yDiff;
	private double c = 0, multi = 0;

	public Joypad(boolean blueKnob) {

		this.blueKnob = blueKnob;

		pad = new Circle();
		pad.radius = 128;
		pad.x = 250;
		pad.y = 500;

		knob = new Circle();
		knob.radius = 64;
		knob.x = 250;
		knob.y = 500;a

		padImg = new Texture(Gdx.files.internal("sprites/fulljoy.png"));
		if (blueKnob) {
			knobImg = new Texture(Gdx.files.internal("sprites/joycentre.png"));
		} else {
			knobImg = new Texture(Gdx.files.internal("sprites/joycentreFire.png"));
		}
	}

	public void initialTouch(float x, float y) {
		pad.x = x - pad.radius;
		pad.y = y - pad.radius;
		knob.x = x - knob.radius;
		knob.y = y - knob.radius;

	}

	public void drawJoypad(SpriteBatch batch) {
		batch.draw(padImg, pad.x, pad.y);
		batch.draw(knobImg, knob.x, knob.y);
	}

	public void adjustMagnitude(float touchX, float touchY) {
		xDiff = touchX - (pad.x + pad.radius);
		yDiff = (pad.y + pad.radius) - touchY;

		if (c <= pad.radius) {

			knob.x = touchX - knob.radius;
			knob.y = touchY - knob.radius;

			if (xDiff < -1*pad.radius) {
				xDiff = -1*pad.radius;
			} else if (xDiff > pad.radius) {
				xDiff = pad.radius;
			}
			if (yDiff < -1*pad.radius) {
				yDiff = -1*pad.radius;
			} else if (yDiff > pad.radius) {
				yDiff = pad.radius;
			}

			c = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));

		} else {

			c = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
			multi = c / pad.radius;

			knob.x = (float) (pad.x + pad.radius/2 + (xDiff / multi));
			knob.y = (float) (pad.y + pad.radius/2 - (yDiff / multi));

			pad.x = (float) (pad.x + xDiff * 0.001);
			pad.y = (float) (pad.y - yDiff * 0.001);

			
			
			if (xDiff < -1*pad.radius) {
				xDiff = -1*pad.radius;
			} else if (xDiff > pad.radius) {
				xDiff = pad.radius;
			}
			if (yDiff < -1*pad.radius) {
				yDiff = -1*pad.radius;
			} else if (yDiff > pad.radius) {
				yDiff = pad.radius;
			}

		}
	}

	public double calculateDirection(boolean pretty) {
		double down, top;
		top = (pad.x + pad.radius) - (knob.x + knob.radius);
		down = (pad.y + pad.radius) - (knob.y + knob.radius);
		Double angle = 0.0;
		angle = (Math.atan(top / down));
		
		if(pretty) return angle;

		
		if (pad.x + pad.radius < knob.x + knob.radius ) {
			if (pad.y + pad.radius < knob.y + knob.radius) {
				angle = Math.PI/2 - angle;
				
			}else if (pad.y + pad.radius > knob.y + knob.radius) {
				angle = (Math.PI/2 + angle)*-1;
				
			}
		} else if (pad.x + pad.radius > knob.x + knob.radius) {
			if (pad.y + pad.radius < knob.y + knob.radius) {
				angle = Math.PI/2 + (angle*-1);
			} else if (pad.y + pad.radius > knob.y + knob.radius) {
				angle = (Math.PI/2 + angle)*-1;
			}
		}

		//Double angle2 = Math.toDegrees(angle);

		// if ()

		//Gdx.app.log("pf", angle2.toString());

		return angle;
	}

	public void resetJoypad() {
		c = 0;
		knob.x = pad.x + pad.radius - knob.radius;
		knob.y = pad.y + pad.radius - knob.radius;
	}

	public double getxDiff() {
		return xDiff;
	}

	public void setxDiff(double xDiff) {
		this.xDiff = xDiff;
	}

	public double getyDiff() {
		return yDiff;
	}

	public void setyDiff(double yDiff) {
		this.yDiff = yDiff;
	}
	
	public void dispose(){
		padImg.dispose();
		knobImg.dispose();
	}

}
