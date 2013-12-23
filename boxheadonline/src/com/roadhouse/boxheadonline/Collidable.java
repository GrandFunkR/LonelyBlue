package com.roadhouse.boxheadonline;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.Timer.Task;

public abstract class Collidable{


	private Circle control;
	public abstract boolean isColliding (Collidable secondObject, double bound);
	public Circle getControl() {
		return control;
	}
	public void setControl(Circle control) {
		this.control = control;
	}

}
