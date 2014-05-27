package com.roadhouse.networking;

import java.io.InputStream;
import java.io.OutputStream;

public class NetworkThread extends Thread{

	
	private boolean done;
	private SocketClient sc;
	private String recieveable;
	
	public NetworkThread(InputStream is, OutputStream os, String npId){
		this.done = false;
		sc = new SocketClient(is, os, npId);	
	}
	
	public void run (){
		while(!done){
			while(sc.isConnected()){
				recieveable = sc.receive();
			}
		}
	}
	
	public synchronized void stopThreadGracefully(){
		done = true;
	}
	
	public synchronized String getNewMessage(){
		return recieveable;
	}
	
	public synchronized void sendMessage(String message){
		sc.send(message);
	}
}
