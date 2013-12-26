package com.roadhouse.boxheadonline;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;
import com.google.example.games.basegameutils.GameHelper;


public class MainActivity extends AndroidApplication implements GameHelperListener, ConnectionCallbacks, RoomUpdateListener, RealTimeMessageReceivedListener, RoomStatusUpdateListener{
	
	private GameHelper gh;
		
	public MainActivity(){
		gh = new GameHelper(this);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Log.i("pf", "running");
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        gh.setup(this);
        this.runOnUiThread(new Runnable(){
        	@Override
			public void run() {
				// TODO Auto-generated method stub
	        	gh.beginUserInitiatedSignIn();
			}
        });
        initialize(new Boxhead(Boxhead.platformCode.ANDROID), cfg);
    }

    
    
	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignInSucceeded() {
		
	}

	private RoomConfig.Builder makeBasicRoomConfigBuilder() {
	    return RoomConfig.builder(this)
	            .setMessageReceivedListener(this)
	            .setRoomStatusUpdateListener(this);
	}

	
	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		if (connectionHint != null) {
	        Invitation inv = connectionHint.getParcelable(GamesClient.EXTRA_INVITATION);

	        if (inv != null) {
	            // accept invitation
	            RoomConfig.Builder roomConfigBuilder = makeBasicRoomConfigBuilder();
	            roomConfigBuilder.setInvitationIdToAccept(inv.getInvitationId());
	            gh.getGamesClient().joinRoom(roomConfigBuilder.build());

	            // prevent screen from sleeping during handshake
	            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	            // go to game screen
	        }
	    }
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onJoinedRoom(int arg0, Room arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLeftRoom(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomConnected(int arg0, Room arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomCreated(int arg0, Room arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRealTimeMessageReceived(RealTimeMessage arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectedToRoom(Room arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnectedFromRoom(Room arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onP2PConnected(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onP2PDisconnected(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPeerDeclined(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPeerInvitedToRoom(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPeerJoined(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPeerLeft(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPeersConnected(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPeersDisconnected(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomAutoMatching(Room arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRoomConnecting(Room arg0) {
		// TODO Auto-generated method stub
		
	}
}