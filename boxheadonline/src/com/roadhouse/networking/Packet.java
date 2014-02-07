package com.roadhouse.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Packet implements Serializable {
	
	private String character;
	private ArrayList<String> bullets;
	private ArrayList<String> enemies;
	
	private boolean host;
	
	public Packet(){
		setCharacter("");
		bullets = new ArrayList<String>();
		setHost(false);
	}
	

	

	@SuppressWarnings("finally")
	public byte[] getBytes (){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		byte[] byteArray = null;
		try{
			out = new ObjectOutputStream(bos);
			out.writeObject(this);
			byteArray = bos.toByteArray();
			if(out != null)out.close();
			bos.close();
		}catch (IOException e){
			e.printStackTrace();
		}
		finally{
			return byteArray;
		}
	}
	
	@SuppressWarnings("finally")
	public static Packet createFromBytes(byte[] bytes){
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInputStream in = null;
		Packet pp = null;
		try{
			in = new ObjectInputStream(bis);
			pp = (Packet) in.readObject();
			bis.close();
			if(in != null)in.close();
		}catch (IOException e){
			e.printStackTrace();
		}
		catch(ClassNotFoundException e1){
			e1.printStackTrace();
		}finally{
			return pp;
		}
	}
	
	

	public ArrayList<String> getEnemies() {
		return enemies;
	}

	public void setEnemies(ArrayList<String> enemies) {
		this.enemies = enemies;
	}

	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public boolean isHost() {
		return host;
	}

	public void setHost(boolean host) {
		this.host = host;
	}
}
