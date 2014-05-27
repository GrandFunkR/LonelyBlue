package com.roadhouse.boxheadonline;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

public class Item extends Collidable{
	
	public static ArrayList<Item> allItems = new ArrayList<Item>();
	final static int HEALTH_MAX = 4;
	final static int MAX_SPEED = 2;
	final static int MIN_SPEED = 1;
	final static Texture[] TYPE_IMAGES = {new Texture(Gdx.files.internal("sprites/char.png"))};
	final static int DOUBLE_SHOT = 0;
   
	private Circle item;
	private Texture image;
	private long spawnTime;
	private long pickupTime;
	private int type;
	
	public Item (int type, float x, float y){
		super.setControl(new Circle());
		this.item = super.getControl();
		this.type = type;
		this.image = TYPE_IMAGES[this.type];
		this.item.x = x;
		this.item.y = y;
		this.item.radius = 16;
		this.spawnTime = System.currentTimeMillis();
		this.pickupTime = 0;
	}
	
	public static Item createNewItem(int type, float x, float y){
		Random rand = new Random();
		int a = rand.nextInt(50);
		if(a == 4){
			Item i = new Item(type, x, y);
				allItems.add(i);	
			return i;
		}
		return null;
	}
	
	public static void expireItems (){
		for (int i = 0 ; i < allItems.size() ; i++){
			if (allItems.get(i).isExpired()){
				allItems.remove(i);
			}
		}
	}
	
	public static void drawItems (SpriteBatch batch){
		for (int i = 0 ; i < allItems.size(); i++){
			allItems.get(i).draw(batch);
		}
	}
	
	public static void isItemsColliding(Character character){
		for (int i = 0 ; i < allItems.size(); i++){
			Item current = allItems.get(i);
			if (current.isColliding(character, 100)){
				current.setPickupTime();
				character.pickUp(current);
			}
		}
	}
	
	public void setPickupTime (){
		this.pickupTime = System.currentTimeMillis();
	}
	
	public long getPickupTime(){
		return this.pickupTime;
	}
	
	public boolean isExpired(){
		if (System.currentTimeMillis() - spawnTime > 5000 || pickupTime > 0){
			return true;
		}
		return false;
	}
	
	public void draw (SpriteBatch batch){
		batch.draw(this.image, 
				this.getItem().x - this.getItem().radius, 
				this.getItem().y - this.getItem().radius, 
				this.getItem().radius*2, 
				this.getItem().radius*2);
	}
	
	private int rs(){
		return MAX_SPEED + (int)(Math.random() * ((MAX_SPEED-MIN_SPEED) + 1));
	}
	
	@Override
	public boolean isColliding(Collidable otherObject, double bound) {
		Circle other = otherObject.getControl();
		double xDif = 0.0;
		double yDif = 0.0;
		double c = 0.0;
		
		if (otherObject instanceof Character) {
			if (item.x + item.radius > other.x + other.radius) {
				xDif = item.x + item.radius - other.x + other.radius;
			} else if (item.x + item.radius < other.x + other.radius) {
				xDif = other.x + other.radius - item.x + item.radius;
			}

			if (item.y + item.radius > other.y + other.radius) {
				yDif = item.y + item.radius - other.y + other.radius;
			} else if (item.y + item.radius < other.y + other.radius) {
				yDif = other.y + other.radius - item.y + item.radius;
			}
			c = Math.sqrt(Math.pow(yDif, 2) + Math.pow(xDif, 2));
			//Boxhead.printf(c);
		}

		return c <= bound;
	}
	
	public Circle getItem(){
		return item;
	}
	
	public int getType(){
		return this.type;
	}
	

}
