package com.roadhouse.boxheadonline;

import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.Preferences;

public class LBInputListener implements TextInputListener{

	private Preferences prefs;
	
	public LBInputListener (Preferences prefs){
		this.prefs = prefs;	
	}
	
	@Override
	public void input(String text) {
		// TODO Auto-generated method stub
		prefs.putString("name",text);
        prefs.flush();	
	}

	@Override
	public void canceled() {
		// TODO Auto-generated method stub
		
	}

}
