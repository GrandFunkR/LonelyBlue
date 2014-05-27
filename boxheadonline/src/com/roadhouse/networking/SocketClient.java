package com.roadhouse.networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class SocketClient {
	
	private InputStream is;
	private OutputStream os;
	private String networkPlayerId;
	private boolean isConnected;
	
	public SocketClient (InputStream is, OutputStream os, String npi){
		this.is = is;
		this.os = os;
		this.networkPlayerId = npi;
	}
	
	public void send (String message){
		try {
			os.write(message.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String receive(){
		byte[] returnable = null;
		//sit till new data
		try {
			is.read(returnable);
			return new String(returnable);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	

}
