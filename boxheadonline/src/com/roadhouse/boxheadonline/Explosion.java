package com.roadhouse.boxheadonline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion {
	
	private final static String EXPLODE_SHEET_DIR = "sprites/explode.png";
	
	private float x,y,stateTime;
	private TextureRegion currentFrame;
	
	private Animation[] explodeAnimation;
	private TextureRegion [][]sheet;
	private Texture explodeSheet;
	
	public Explosion(float x,float y){
		this.x = x;
		this.y = y;
		this.stateTime = 0f;
		
		this.explodeAnimation = new Animation [5];
		this.explodeSheet = new Texture(Gdx.files.internal(EXPLODE_SHEET_DIR)); 
        sheet = TextureRegion.split(explodeSheet, explodeSheet.getWidth() / 5, explodeSheet.getHeight() / 1); 
        for (int i = 0; i < 1; i++) {
        	for (int j = 0; j < 5; j++) {
        		sheet[i][j].flip(false, true);   
        	}
        	this.explodeAnimation[i] = new Animation(0.065f, sheet[i]);
        }
        explodeAnimation[0].setPlayMode(Animation.NORMAL);
        stateTime = 0f;   
	}

	public void draw (SpriteBatch batch){
		stateTime += Gdx.graphics.getDeltaTime(); // #15
        currentFrame = explodeAnimation[0].getKeyFrame(stateTime, false); 
        batch.draw(currentFrame, x-60, y-60); 
	}
	
	public boolean isDone (){
		if (explodeAnimation[0].isAnimationFinished(stateTime)){
			return true;
		}
			
		return false;
	}

}
