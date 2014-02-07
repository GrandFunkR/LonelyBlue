package com.roadhouse.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;

public class InputHandler {

	private K k;
	
	
	public InputHandler (){
		k = new K ();
		
	}
	
	public void displayDialog (String title, String text){
		Gdx.input.getTextInput(k, title, text);	
	}
	
	public String getText (){
		return k.text();	
	}
	
	public boolean isCancelled (){
		return k.c;
	}
	
	public boolean isInput (){
		return k.i;
	}
	
	private class K implements TextInputListener {


		boolean i = false;
		boolean c = false;
		String text;
		@Override
		public void input(String text) {
			// TODO Auto-generated method stub
			this.text = text;
			i = true;

		}

		@Override
		public void canceled() {
			// TODO Auto-generated method stub
			c = true;
		}

		public String text (){
			return text;
		}

	}
}
