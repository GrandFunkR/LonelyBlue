//In the name of Allah, the Most Merciful, The Ever Merciful

package com.roadhouse.boxheadonline;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.roadhouse.networking.NetworkThread;
import com.roadhouse.networking.Packet;
import com.roadhouse.ui.InputHandler;


public class Boxhead implements ApplicationListener {

	public final static int DOWN_LEFT = 0;
	public final static int DOWN_RIGHT = 1;
	public final static int DOWN = 2;
	public final static int LEFT = 3;
	public final static int UP_LEFT = 4;
	public final static int UP_RIGHT = 5;
	public final static int UP = 6;
	public final static int RIGHT = 7;

	public final static int ENEMIES_PER_LEVEL = 100;
	public final static float ENEMY_SPAWN_DELAY = 0.35f;

	public static Preferences prefs;
	public static boolean played;

	OrthographicCamera camera;
	SpriteBatch batch;
	ShapeRenderer sr;

	BitmapFont font ;
	BitmapFont huge ;
	BitmapFont _font;
	FreeTypeFontGenerator generator;

	Character character;
	Character peer;
	Joypad mover;
	Joypad shooter;

	Texture randimg;

	Texture mainMenuImg_signedIn;
	Texture mainMenuImg_signedOut;
	Texture gameOverImg;
	Texture multiPlayerImg;

	ArrayList<Bullet> bullets;
	public static ArrayList<Enemy> enemies;
	ArrayList <ScoreUp> scoreUps;
	ArrayList <Explosion> explosions;

	public final static int SCREEN_WIDTH = 1280;
	public final static int SCREEN_HEIGHT = 720;
	//public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.:;,{}\"�`'<>";
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|/?-+=()*&.:;,{}";

	boolean currentTouchOne = false;
	boolean currentTouchTwo = false;
	boolean isScheduled = true;
	boolean collideScheduled = false;

	boolean game = false;
	boolean gameOver = false;
	boolean pause = false;
	boolean mainMenu = true;
	boolean onlineMenu = false;

	boolean signedIn = false;
	boolean isServer = false;
	boolean multiplayerGame = false;

	Button replayBtn;
	Button quitToMainBtn;

	Button singlePlayerBtn;
	Button multiplayerBtn;
	Button instructionsBtn;
	Button signIn_OutBtn;

	Button invitePlayersBtn;
	Button showInvitationsBtn;
	Button backBtn;

	Music music;

	double speedX, speedY;
	int level;
	int score;
	int kills;
	int deathLimit;

	int currentScore;
	float cex, cey;
	String healthString;
	AndroidAccessLayer android;

	boolean tapToStart = false;

	int myID;
	public static enum platformCode {DESKTOP, ANDROID, HTML5};

	public Boxhead(platformCode pC)
	{
		super();
	}

	public Boxhead(platformCode pC, AndroidAccessLayer aal){
		super();
		android = aal;
		if (pC == platformCode.DESKTOP)signedIn = false;
		else if (pC == platformCode.ANDROID)signedIn = android.isSignedIn();

	}

	@Override
	public void create() {		
		printf("Lonely Blue. v0.1");

		prefs = Gdx.app.getPreferences("myprefs");
		played = !prefs.get().isEmpty();

		if (!played){
			LBInputListener listener = new LBInputListener(prefs);
			Gdx.input.setOnscreenKeyboardVisible(true);
			Gdx.input.getTextInput(listener, "Display Name:", "");

			prefs.putInteger("highscore", 0);
			prefs.flush();
		}

		music = Gdx.audio.newMusic(Gdx.files.internal("testing/track1.mp3"));
		music.setVolume(0.5f);
		music.setLooping(true);
		//music.play(); 

		Texture.setEnforcePotImages(false);

		camera = new OrthographicCamera();
		camera.setToOrtho(true, SCREEN_WIDTH, SCREEN_HEIGHT);
		batch = new SpriteBatch();
		sr = new ShapeRenderer();

		level = 1;
		character = new Character();

		mover = new Joypad(true);
		shooter = new Joypad(false);
		randimg = new Texture(Gdx.files.internal("testing/char.png"));
		mainMenuImg_signedIn = new Texture(Gdx.files.internal("testing/mainMenuImg_signedIn.png"));
		mainMenuImg_signedOut = new Texture(Gdx.files.internal("testing/mainMenuImg_signedOut.png"));
		multiPlayerImg = new Texture(Gdx.files.internal("testing/multiplayerImg.png"));

		bullets = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();
		scoreUps = new ArrayList<ScoreUp>();
		explosions = new ArrayList<Explosion>();

		FreeTypeFontGenerator _generator = new FreeTypeFontGenerator(Gdx.files.internal("testing/Montserrat-Regular.ttf"));	
		font = _generator.generateFont(27, FONT_CHARACTERS, true);
		huge = _generator.generateFont(300, FONT_CHARACTERS, true);
		_font = _generator.generateFont(60, FONT_CHARACTERS, true);
		_generator.dispose();

		healthString = "";

		for (int i = 0 ; i < character.getHealth() /13; i++){
			healthString +="|";
		}

		replayBtn = new Button (0, 300, "Replay", Button.BLUE);
		quitToMainBtn = new Button (0, 500, "Quit", Button.BLUE);

		singlePlayerBtn = new Button (800, 54, "SINGLE PLAYER", Button.BLUE);

		multiplayerBtn = new Button (800, 149, "MULTIPLAYER", Button.RED);
		//options = new Button (700, 500, "OPTIONS (COMING SOON)");
		signIn_OutBtn = new Button (0,535, "SIGN IN", Button.LIGHT_GREY);

		invitePlayersBtn = new Button (800, 54, "INVITE PLAYERS", Button.ORANGE);
		showInvitationsBtn = new Button (800, 149, "SHOW INVITATIONS", Button.NAVY_BLUE);
		backBtn = new Button (800,244, "BACK", Button.LIGHT_PURPLE);

		deathLimit = 20;
	}

	@Override
	public void dispose() {
		randimg.dispose();
		Bullet.dispose();
		shooter.dispose();
		mover.dispose();
		mainMenuImg_signedIn.dispose();
		mainMenuImg_signedOut.dispose();
		multiPlayerImg.dispose();

	}
	boolean update = false;

	@Override
	public void render() {
		try {
			Thread.sleep(10);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		sr.setProjectionMatrix(camera.combined);
		
		
		//batch.draw(bg, 0,0);
		if (game){

			if (!tapToStart){
				//draw initial game play stuff
				tapToStart = currentTouchOne || currentTouchTwo;
				resetGame();
				
			}

			// If currently touching screen, then draw respective Joypad
			if (currentTouchOne) {
				mover.drawJoypad(batch);
			}
			if (currentTouchTwo) {
				shooter.drawJoypad(batch);
			}

			// Draw character on screen

			batch.draw(randimg, 
					character.getControl().x - character.getControl().radius,
					character.getControl().y - character.getControl().radius);

			if (multiplayerGame){
				batch.draw(randimg, 
						peer.getControl().x - peer.getControl().radius,
						peer.getControl().y - peer.getControl().radius);

			}


			for (int i = 0; i < enemies.size(); i++) {
				Enemy current = enemies.get(i);
				batch.draw(Enemy.getEneImg(), current.getEnemy().x - current.getEnemy().radius,
						current.getEnemy().y - current.getEnemy().radius);
			}

			// draw possible bullets that are shot
			for (int i = 0; i < bullets.size(); i++) {
				Bullet current = bullets.get(i);
				batch.draw(Bullet.getImage(), current.getControl().x,
						current.getControl().y);
			}

			for (int i = 0 ; i < scoreUps.size() ; i++){
				ScoreUp current = scoreUps.get(i);
				if (current.incrementOrRemove()){
					scoreUps.remove(i);
				}
				current.y--;	  

				font.draw(batch, "+" + current.currentScore, current.x, current.y);
			}

			for (int i = 0; i < explosions.size() ; i++){
				Explosion current = explosions.get(i);
				if(!current.isDone())current.draw(batch);
				else explosions.remove(i);
			}

			font.draw(batch, "Level: " + level, 30, 30);
			font.draw(batch, "Score: " + score, 1000, 30);
			font.draw(batch, healthString, 250, 30);
		}
		else {

			if (mainMenu){
				if (signedIn)batch.draw(mainMenuImg_signedIn, 0, 0, 1280, 720, 0, 0, 1280, 720, false, true);
				else batch.draw(mainMenuImg_signedOut, 0, 0, 1280, 720, 0, 0, 1280, 720, false, true);
				font.draw(batch, prefs.getString("name"), 130, 475);

				_font.draw(batch, Integer.toString(prefs.getInteger("highscore")), 862, 490);

				//singlePlayerBtn.drawButton(batch);
				//multiplayerBtn.drawButton(batch);
				//options.drawButton(batch, 0);
				//signIn_OutBtn.drawButton(batch);

			}
			else if (gameOver){
				huge.draw(batch, "GAME OVER", 40, 40);
				//replayBtn.drawButton(batch);
				//quitToMainBtn.drawButton(batch);

			}
			else if (onlineMenu){
				batch.draw(multiPlayerImg, 0, 0, 1280, 720, 0, 0, 1280, 720, false, true);
				//invitePlayersBtn.drawButton(batch);
				//showInvitationsBtn.drawButton(batch);
				//backBtn.drawButton(batch);

			}
		}
		// End drawing to batch<SpriteBatch>
		batch.end();

		////////////////////////////////////////////////////************************////////////////////////////

		int left = 4;
		int right = 4;
		if (game){
			if (!pause){ 

				if (multiplayerGame){
					send = null;
					send = new Packet();
					send.setCharacter(character.toString());
					send.setHost(isServer);
					nt.sendPacket(send.getBytes());
					recieve = Packet.createFromBytes(nt.getNewPacket());
					peer.interpret(recieve.getCharacter());
				}

				if (Gdx.input.isTouched(0)) {

					// Touch initialization
					Vector3 t1 = new Vector3();
					t1.set(Gdx.input.getX(0), Gdx.input.getY(0), 0);
					camera.unproject(t1);
					if (t1.x < SCREEN_WIDTH/2){
						left = 0;
					}
					else if (t1.x > SCREEN_WIDTH/2){
						right = 0;
					}

				} 

				if (Gdx.input.isTouched(1)) {

					// Touch initialization
					Vector3 t1 = new Vector3();
					t1.set(Gdx.input.getX(1), Gdx.input.getY(1), 0);
					camera.unproject(t1);
					if (t1.x < SCREEN_WIDTH/2 && left > 1){
						left = 1;
					}
					else if (t1.x > SCREEN_WIDTH/2 && right > 1){
						right = 1;
					}

				} 

				if (Gdx.input.isTouched(left)){

					Vector3 touchLeft = new Vector3();
					touchLeft.set(Gdx.input.getX(left), Gdx.input.getY(left), 0);
					camera.unproject(touchLeft);

					if (!currentTouchOne) {
						mover.initialTouch(touchLeft.x, touchLeft.y);
						currentTouchOne = true;
					}

					// adjust magnitude of knob with pad in mover<Joypad>
					mover.adjustMagnitude(touchLeft.x, touchLeft.y);
					// move character according to mover<Joypad> x/y differences
					character.moveCharacter(mover.getxDiff(), mover.getyDiff());

				}
				else {
					mover.resetJoypad();
					currentTouchOne = false;
				}

				if (Gdx.input.isTouched(right)){

					Vector3 touchRight = new Vector3();
					touchRight.set(Gdx.input.getX(right), Gdx.input.getY(right), 0);
					camera.unproject(touchRight);

					if (!currentTouchTwo) {
						shooter.initialTouch(touchRight.x, touchRight.y);
						currentTouchTwo = true;
					}

					shooter.adjustMagnitude(touchRight.x, touchRight.y);

					Double direction = shooter.calculateDirection(false);
					speedX = 30 * Math.cos(direction);
					speedY = 30 * Math.sin(direction);



					bullets.add(new Bullet(character.getControl().x - 8,
							character.getControl().y - 8, speedX, speedY));


				}
				else {
					shooter.resetJoypad();
					currentTouchTwo = false;
				}
				if (tapToStart){
					//gameplay
					// remove bullets that are outside of the screen, and move them
					for (int i = 0; i < bullets.size(); i++) {

						Bullet current = bullets.get(i);
						current.moveBullet();
						if (current.getControl().x > SCREEN_WIDTH
								|| current.getControl().x < 0
								|| current.getControl().y > SCREEN_HEIGHT
								|| current.getControl().y < 0) {
							bullets.remove(i);
							current = null;

						}
					}


					//printf(enemies.size());
					//	printf ("kills:"+kills);
					//	printf ("deathLimit:"+deathLimit);
					//	printf (isScheduled);
					//	printf(enemies.size());


					if (enemies.size() == 0 && !isScheduled && kills >= deathLimit) {
						level++;
						Timer t = Timer.instance;
						t.clear();
						Timer.schedule(new SpawnEnemies(level), 1, ENEMY_SPAWN_DELAY, level * ENEMIES_PER_LEVEL);
						kills = 0;
						deathLimit = level *ENEMIES_PER_LEVEL;
						printf ("newlevel");
						isScheduled = true;

					} else if (enemies.size() > 0 && isScheduled) {
						isScheduled = false;
					}

					try {
						for (int i = 0; i < enemies.size(); i++) {

							if (enemies.get(i).isColliding(character,-18)){

								//character.decreaseHealth();
								character.setHealth(0);
								//healthString = "";
								//for (int k = 0; k < character.getHealth() /13; k++){
								//	healthString+="|";
								//}
							}
							else {
								enemies.get(i).move(character.getControl());
							}

							for (int j = 0; j < bullets.size(); j++) {
								if (enemies.get(i).isColliding(bullets.get(j), 30)) {
									//enemies.get(i).decreaseHealth();
									enemies.get(i).setHealth(-1);
									bullets.remove(j);
								}
							}
							if (enemies.get(i).getHealth() < 0){

								Random rand = new Random();

								float distance = character.getDistance(enemies.get(i));
								if (distance > 550) currentScore = 10;
								else if (distance > 400) currentScore = 5;
								else if (distance > 300) currentScore = 4;
								else if (distance > 200) currentScore = 3;
								else if (distance > 100) currentScore = 2;
								else currentScore = 1;

								score += currentScore;
								rand = null;
								cex = enemies.get(i).getEnemy().x;
								cey = enemies.get(i).getEnemy().y;
								scoreUps.add(new ScoreUp(currentScore,cex,cey));
								explosions.add(new Explosion(cex,cey));

								enemies.remove(i);
								kills++;

							}
						}

					} catch (Exception e) {
						printf(e.getMessage());
					}

					if (character.getHealth() <= 0){
						game = false;
						gameOver = true;
					}
				}

			}
			//pause
			else {


			}
		}
		//!game
		else {


			if (Gdx.input.isTouched()) {

				// Touch initialization
				Vector3 touchPos = new Vector3();
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);

				if (mainMenu){
					singlePlayerBtn.setPressed(touchPos.x, touchPos.y);
					multiplayerBtn.setPressed(touchPos.x, touchPos.y);
					//options.setPressed(touchPos.x, touchPos.y);
					signIn_OutBtn.setPressed(touchPos.x, touchPos.y);


				}
				else if (gameOver){
					replayBtn.setPressed(touchPos.x, touchPos.y);
					quitToMainBtn.setPressed(touchPos.x, touchPos.y);

				}
				else if (onlineMenu){
					invitePlayersBtn.setPressed(touchPos.x, touchPos.y);
					showInvitationsBtn.setPressed(touchPos.x, touchPos.y);
					backBtn.setPressed(touchPos.x,touchPos.y);

				}
			}else {
				if (mainMenu){
					if (singlePlayerBtn.isReleased()){
						singlePlayerBtn.setPressed(false);
						mainMenu = false;
						game = true;

						
					}
					else if (multiplayerBtn.isReleased() ){
						multiplayerBtn.setPressed(false);
						if (signedIn){
							onlineMenu = true;
							mainMenu = false;	
						}

					}
					//					else if (options.isReleased()){
					//						options.setPressed(false);
					//					}
					else if (signIn_OutBtn.isReleased()){
						signIn_OutBtn.setPressed(false);
						if (!signedIn)android.initiateSignIn();

					}

				}
				else if (gameOver){
					if (prefs.getInteger("highscore") < score){
						prefs.putInteger("highscore", score);
						prefs.flush();
					}
					if (replayBtn.isReleased()){
						replayBtn.setPressed(false);
						gameOver = false;
						game = true;
					}
					else if (quitToMainBtn.isReleased()){
						quitToMainBtn.setPressed(false);
						gameOver = false;
						mainMenu = true;
					}

				}
				else if (onlineMenu){
					if (invitePlayersBtn.isReleased()){
						invitePlayersBtn.setPressed(false);
						//onlineMenu = false;
						//createOrJoinRoom = true;
						isServer = true;
						android.initiateRoomCreation();
					}
					else if (showInvitationsBtn.isReleased()){
						showInvitationsBtn.setPressed(false);
						//onlineMenu = false;
						//createOrJoinRoom = true;
						isServer = false;
						android.initiateInviteInbox();

					}
					else if (backBtn.isReleased()){
						backBtn.setPressed(false);
						onlineMenu = false;
						mainMenu = true;
					}
				}


			}
		}


	}

	NetworkThread nt = null;
	Packet send = null, recieve = null;
	public void startMultiplayerGame(InputStream is, OutputStream os, String npId){
		nt = new NetworkThread(is, os, npId);
		nt.start();
		multiplayerGame = true;
		onlineMenu = false;
		game = true;
		if (isServer)resetGame();
	}


	public void resetGame(){
		level = 1;
		score = 0;
		bullets = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();

		character.resetCharacter();
		healthString = "";

		Timer t = Timer.instance;
		t.clear();
		t = null;
		for (int i = 0 ; i < character.getHealth() /13; i++){
			healthString +="|";
		}

		Timer.schedule(new SpawnEnemies(level), 0, ENEMY_SPAWN_DELAY, ENEMIES_PER_LEVEL);
	}


	public static void printf(Object o) {
		Gdx.app.log("pf", o.toString());

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	public void onSignedIn (boolean connected){
		signedIn = connected;
	}

	public void onDisconnected (){

	}




}
