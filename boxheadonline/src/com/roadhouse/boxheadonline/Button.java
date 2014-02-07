package com.roadhouse.boxheadonline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class Button {
	
	final static String BLUE = "01579aa9";
	final static String RED = "01e76e66";
	final static String PURPLE = "01695b8e";
	final static String GREEN = "011b7e5a";
	final static String ORANGE = "01de8650";
	final static String NAVY_BLUE = "0128545b";
	final static String LIGHT_PURPLE = "018c5d79";
	final static String LIGHT_GREY = "012e2e2e";
	
	public static float BUTTON_WIDTH = 480;
	public static float BUTTON_HEIGHT = 92;
	
	private boolean pressed;
	private boolean hasBeenSet;
	private Rectangle touch;
	private String text;
	private String color;
	private float fx, fy;
	
	static Texture touchImg = new Texture(Gdx.files.internal("testing/buttonTouch.png"));
	FreeTypeFontGenerator _generator = new FreeTypeFontGenerator(Gdx.files.internal("testing/Montserrat-Regular.ttf"));	
	BitmapFont font = _generator.generateFont(27, Boxhead.FONT_CHARACTERS, true);
	
	public Button (float x, float y, String text, String color){
		this.touch = new Rectangle();
		this.touch.x = x;
		this.touch.y = y;
		this.touch.width = BUTTON_WIDTH;
		this.touch.height = BUTTON_HEIGHT;
		
		this.text = text;
		this.color = color;
		
		this.fx = this.touch.x + this.touch.width/2 - font.getBounds(this.text).width/2;
	    this.fy = this.touch.y + this.touch.height/2 + font.getBounds(this.text).height/2;
	}
	
	
	public void setPressed (float x, float y){
		pressed = this.touch.contains(x, y);
		hasBeenSet = true;
	}
	
	public boolean isPressed(){
		return pressed;
	}
	
	public boolean isReleased(){
		return this.isHasBeenSet() && this.isPressed();
	}
	
	public void drawButton (SpriteBatch batch, ShapeRenderer sr){
		 
		if (this.isPressed()){
			batch.draw(touchImg, touch.x, touch.y, BUTTON_WIDTH, BUTTON_HEIGHT);	
		}
		
		sr.begin(ShapeType.FilledRectangle);
		sr.setColor(colorFromHexString(this.color));
		sr.rect(this.touch.x, this.touch.y, this.touch.width, this.touch.height);
		sr.end();
		
		font.draw(batch, this.text, this.fx, this.fy);
		
	}


	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}



	public boolean isHasBeenSet() {
		return hasBeenSet;
	}


	public void setHasBeenSet(boolean hasBeenSet) {
		this.hasBeenSet = hasBeenSet;
	}
	
	private Color colorFromHexString(String s)
    {               
            if(s.startsWith("0x"))
                    s = s.substring(2);
            
            if(s.length() != 8) // AARRGGBB
                    throw new IllegalArgumentException("String must have the form AARRGGBB");
                    
            return colorFromHex(Long.parseLong(s, 16));
    }
	
	private Color colorFromHex(long hex)
    {
            float a = (hex & 0xFF000000L) >> 24;
            float r = (hex & 0xFF0000L) >> 16;
            float g = (hex & 0xFF00L) >> 8;
            float b = (hex & 0xFFL);
                            
            return new Color(r/255f, g/255f, b/255f, a/255f);
    }


}
