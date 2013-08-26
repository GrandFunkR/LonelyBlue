//In the name of Allah, the Most Merciful, The Most Merciful


package com.roadhouse.boxheadonline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;



import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.Timer;
import com.gamooga.client.ConnectCallback;
import com.gamooga.client.GamoogaClient;
import com.gamooga.client.MessageCallback;
import com.roadhouse.ui.InputHandler;


public class Boxhead implements ApplicationListener {

	OrthographicCamera camera;
	SpriteBatch batch;

	BitmapFont font ;
	BitmapFont huge ;
	FreeTypeFontGenerator generator;

	Character character;
	Joypad mover;
	Joypad shooter;
	Texture randimg;
	Texture patternFill;


	ArrayList<Bullet> bullets;
	public static ArrayList<Enemy> enemies;
	ArrayList <ScoreUp> scoreUps;
	
	public static ArrayList<Friendly> roamingFriendlies;
	public static ArrayList<Friendly> attachedFriendlies;

	ArrayList<Circle> pattern;
	
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
	boolean hostWaitingRoom = false;
	boolean createOrJoinRoom = false;

	boolean network = false;
	boolean host = false;

	Button replay;
	Button quitToMain;

	Button solo;
	Button online;
	Button options;

	Button createRoom;
	Button enterRoom;

	double speedX, speedY;
	int level;
	int score;
	int kills;
	int deathLimit;
	
	private GamoogaClient gc;

	int currentScore;
	float cex, cey;
	String healthString;

	int myID;
	public static enum platformCode {DESKTOP, ANDROID, HTML5};

	public Boxhead(platformCode pC)
	{
		super();

	}


	@Override
	public void create() {
		
		
		camera = new OrthographicCamera();
		camera.setToOrtho(true, SCREEN_WIDTH, SCREEN_HEIGHT);
		batch = new SpriteBatch();

		level = 1;
		character = new Character();

		mover = new Joypad(true);
		shooter = new Joypad(false);
		randimg = new Texture(Gdx.files.internal("testing/char.png"));
		patternFill = new Texture(Gdx.files.internal("testing/fill.png"));
		bullets = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();
		scoreUps = new ArrayList<ScoreUp>();
		
		float startX = 1280/2;
		float startY = 720/2;
		//pattern poc
		// o   o
		//   o 
		// o   o
		pattern = new ArrayList<Circle>();
		pattern.add(new Circle(startX+64, startY+64, 64));
		pattern.add(new Circle(startX+64, startY-64, 64));
		pattern.add(new Circle(startX-64, startY+64, 64));
		pattern.add(new Circle(startX-64, startY-64, 64));
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("testing/pixel_maz.ttf"));
		font = generator.generateFont(96, FONT_CHARACTERS, true);
		huge = generator.generateFont(300, FONT_CHARACTERS, true);
		generator.dispose();

		healthString = "";

		for (int i = 0 ; i < character.getHealth() /16; i++){
			healthString +="|";
		}

		replay = new Button (700, 300, "REPLAY");
		quitToMain = new Button (700, 500, "MENU");

		solo = new Button (700, 100, "SOLO");
		online = new Button (700, 300, "ONLINE");
		options = new Button (700, 500, "OPTIONS");

		createRoom = new Button (700, 100, "CREATE ROOM");
		enterRoom = new Button (700, 300, "JOIN ROOM");

		deathLimit = 20;
		
	
		
	}

	@Override
	public void dispose() {
		randimg.dispose();
		Bullet.dispose();
		shooter.dispose();
		mover.dispose();
	}

	@Override
	public void render() {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		// //////
		// Start drawing objects to batch<SpriteBatch>

		if (game){
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


			if (network){
				// Draw character on screen
				batch.draw(randimg, 
						networkCharacter.getControl().x - networkCharacter.getControl().radius,
						networkCharacter.getControl().y - networkCharacter.getControl().radius);

			}

			for(int i = 0; i < pattern.size(); i++){
				Circle c = pattern.get(i);
				batch.draw(patternFill, 
						c.x - c.radius,
						c.y - c.radius);
			}
			
			for (int i = 0; i < enemies.size(); i++) {
				Enemy current = enemies.get(i);
				batch.draw(Enemy.getEneImg(), current.getEnemy().x - current.getEnemy().radius,
						current.getEnemy().y - current.getEnemy().radius);
			}

			for (int i = 0; i < roamingFriendlies.size(); i++) {
				Friendly current = roamingFriendlies.get(i);
				batch.draw(Friendly.getFriendImg(), current.getfriendly().x - current.getfriendly().radius,
						current.getfriendly().y - current.getfriendly().radius);
			}	
			
			for (int i = 0; i < attachedFriendlies.size(); i++) {
				Friendly current = attachedFriendlies.get(i);
				batch.draw(Friendly.getFriendImg(), current.getfriendly().x - current.getfriendly().radius,
						current.getfriendly().y - current.getfriendly().radius);
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


			font.draw(batch, "Level: " + level, 30, 30);
			font.draw(batch, "Score: " + score, 1000, 30);
			font.draw(batch, healthString, 250, 30);
		}
		else {

			if (mainMenu){
				huge.draw(batch, "Lonely", 40, 40);

				huge.setColor(0, 0, 1, 1);
				huge.draw(batch, "Blue.", 40, 170);
				huge.setColor(1, 1, 1, 1);

				solo.drawButton(batch, 140);
				online.drawButton(batch, 90);
				options.drawButton(batch, 60);

			}
			else if (gameOver){
				huge.draw(batch, "GAME OVER", 40, 40);
				replay.drawButton(batch, 90);
				quitToMain.drawButton(batch, 130);

			}
			else if (onlineMenu){

				font.draw(batch, "Welcome to Roadhouse.", 40, 40);
				createRoom.drawButton(batch, 0);
				enterRoom.drawButton(batch, 0);

			}
			else if (hostWaitingRoom){
			}
		}
		// End drawing to batch<SpriteBatch>
		batch.end();

		////////////////////////////////////////////////////************************////////////////////////////

		int left = 4;
		int right = 4;
		if (game){
			if (!pause){ 

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

					if (network){
					
					}

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

					Double direction = shooter.calculateDirection();
					speedX = 30 * Math.cos(direction);
					speedY = 30 * Math.sin(direction);

					
					
					bullets.add(new Bullet(character.getControl().x - 8,
							character.getControl().y - 8, speedX, speedY));
					
				
				}
				else {
					shooter.resetJoypad();
					currentTouchTwo = false;
				}
				
				for (int i = 0 ; i < abs.size() ; i++){
					AugmentedBullet current = abs.get(i);
					bullets.add(new Bullet(current.getX(),current.getY(),current.getSpeedx(),current.getSpeedy()));
					abs.remove(i);
					
				}
				
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
					Timer.schedule(new SpawnEnemies(level), 3, 1, level * 20);
					kills = 0;
					deathLimit = level *20;
					printf ("newlevel");
					isScheduled = true;

				} else if (enemies.size() > 0 && isScheduled) {
					isScheduled = false;
				}

				for (int i = 0 ; i < roamingFriendlies.size() ; i++){
					Friendly r = roamingFriendlies.get(i);
					r.move(character.getControl());		
					for (int j = 0 ; j < attachedFriendlies.size() ; j++){
						Friendly a = attachedFriendlies.get(j);	
						if (a.isColliding(r, 120)){
							r.attachTo(a);
//							//needs to be based on closest angle
//							r.setAttachedAt(Friendly.NORTH);
							if(r.getControl().x > a.getControl().x){
								if(r.getControl().y > a.getControl().y){
									r.setAttachedAt(Friendly.SOUTH_EAST);
								}
								else {
									r.setAttachedAt(Friendly.NORTH_EAST);
								}
							}
							else {
								if(r.getControl().y > a.getControl().y){
									r.setAttachedAt(Friendly.SOUTH_WEST);
								}
								else {
									r.setAttachedAt(Friendly.NORTH_WEST);
								}
							}
							attachedFriendlies.add(r);
							roamingFriendlies.remove(i);
							break;
						}
						a = null;
					}
					
					if (r.isColliding(character, 120)){
						r.attachTo(character);
						if(r.getControl().x > character.getControl().x){
							if(r.getControl().y > character.getControl().y){
								r.setAttachedAt(Friendly.SOUTH_EAST);
							}
							else {
								r.setAttachedAt(Friendly.NORTH_EAST);
							}
						}
						else {
							if(r.getControl().y > character.getControl().y){
								r.setAttachedAt(Friendly.SOUTH_WEST);
							}
							else {
								r.setAttachedAt(Friendly.NORTH_WEST);
							}
						}
						attachedFriendlies.add(r);
						//TODO: size check before remove
						roamingFriendlies.remove(i);
					}
					
					r = null;
				}

				int matchBound  = 25;
				for (int i = 0; i < attachedFriendlies.size() ; i++){
					Friendly a = attachedFriendlies.get(i);
					if (a.getAttachedAt() == Friendly.NORTH_EAST){
						a.getControl().x = a.getAttachedTo().getControl().x + 64;
						a.getControl().y = a.getAttachedTo().getControl().y - 64;
					}
					else if(a.getAttachedAt() == Friendly.NORTH_WEST){
						a.getControl().x = a.getAttachedTo().getControl().x - 64;
						a.getControl().y = a.getAttachedTo().getControl().y - 64;	
					}
					else if (a.getAttachedAt() == Friendly.SOUTH_EAST){
						a.getControl().x = a.getAttachedTo().getControl().x + 64;
						a.getControl().y = a.getAttachedTo().getControl().y + 64;
					}
					else if (a.getAttachedAt() == Friendly.SOUTH_WEST){
						a.getControl().x = a.getAttachedTo().getControl().x - 64;
						a.getControl().y = a.getAttachedTo().getControl().y + 64;
					}
					int matchCount = 0;
					
					for (int j = 0; j < pattern.size(); j++){
						Circle c = pattern.get(j);
//						System.out.println(matchCount);
						if (matchCount == pattern.size()-1){
							System.out.println("full match");
						}
						if (a.getControl().x >=  c.x - matchBound && a.getControl().x <= c.x + matchBound){
							if (a.getControl().y >=  c.y - matchBound && a.getControl().y <= c.y + matchBound){
//								System.out.println("match");
								matchCount++;
								System.out.println(matchCount);
							}	
						}
					}
					
					
				}
				
				try {
					for (int i = 0; i < enemies.size(); i++) {

						if (enemies.get(i).isColliding(character, 120)){

							//Timer.schedule(new DecreaseHealth (character), 0, 1, 1);
							//remove
//							character.decreaseHealth();
//							healthString = "";
//							for (int k = 0; k < character.getHealth() /16; k++){
//								healthString+="|";
//							}
						}
						else {
							enemies.get(i).move(character.getControl());
						}

						for (int j = 0; j < bullets.size(); j++) {
							if (enemies.get(i).isColliding(bullets.get(j), 60)) {
								enemies.get(i).decreaseHealth();
								bullets.remove(j);
							}
						}
						if (enemies.get(i).getHealth() < 0){

							Random rand = new Random();

							currentScore = 1 + rand.nextInt(5);
							score += currentScore;
							rand = null;
							cex = enemies.get(i).getEnemy().x;
							cey = enemies.get(i).getEnemy().y;
							scoreUps.add(new ScoreUp(currentScore,cex,cey));

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
					solo.setPressed(touchPos.x, touchPos.y);
					online.setPressed(touchPos.x, touchPos.y);
					options.setPressed(touchPos.x, touchPos.y);


				}
				else if (gameOver){
					replay.setPressed(touchPos.x, touchPos.y);
					quitToMain.setPressed(touchPos.x, touchPos.y);

				}
				else if (onlineMenu){
					createRoom.setPressed(touchPos.x, touchPos.y);
					enterRoom.setPressed(touchPos.x, touchPos.y);

				}
			}else {
				if (mainMenu){
					if (solo.isReleased()){
						solo.setPressed(false);
						mainMenu = false;
						game = true;

						resetGame();
					}
					else if (online.isReleased() ){
						online.setPressed(false);
						//if (clientMSG.isConnected()){
							//onlineMenu = true;
							//mainMenu = false;
						//}else {
						//}

					}
					else if (options.isReleased()){
						options.setPressed(false);
					}

				}
				else if (gameOver){

					if (replay.isReleased()){
						replay.setPressed(false);
						gameOver = false;
						game = true;
						resetGame();
					}
					else if (quitToMain.isReleased()){
						quitToMain.setPressed(false);
						gameOver = false;
						mainMenu = true;
					}

				}
				else if (onlineMenu){


					if (createRoom.isReleased()){
						createRoom.setPressed(false);
						onlineMenu = false;
						createOrJoinRoom = true;
						
						host = true;

						createPassword.displayDialog("Create a password.", "");


					}
					else if (enterRoom.isReleased()){
						enterRoom.setPressed(false);

						onlineMenu = false;
						createOrJoinRoom = true;

						host = false;

						createPassword.displayDialog("Create a password.", "");
						roomNumber.displayDialog("Enter a room number.", "");


					}
				}
				else if (createOrJoinRoom){

					if (host){
						if (createPassword.isInput()){
							createOrJoinRoom = false;
							hostWaitingRoom = true;
							password = createPassword.getText();
							//clientMSG.sendMessage("CREATE_ROOM" + " " + clientMSG.getId() + " " + password);
						}
					}
					else{
						if (createPassword.isInput() && roomNumber.isInput()){
							createOrJoinRoom = false;
							
							printf(createPassword.getText() + " " + roomNumber.getText());
							//clientMSG.sendMessage("JOIN_ROOM" 
							//+ " " + roomNumber.getText() 
							//+ " " + createPassword.getText() 
							//+ " " + clientMSG.getId());
							
						}
					}
				}
				else if (hostWaitingRoom){


				}

			}
		}


	}

	int other_id = -1;
	InputHandler createPassword = new InputHandler();
	InputHandler roomNumber = new InputHandler();
	String password = "";






	public void resetGame(){
		level = 1;
		score = 0;
		bullets = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();
		

		roamingFriendlies = new ArrayList<Friendly>();
		attachedFriendlies = new ArrayList<Friendly>();

		
		character.resetCharacter();
		healthString = "";

		Timer t = Timer.instance;
		t.clear();
		t = null;
		for (int i = 0 ; i < character.getHealth() /16; i++){
			healthString +="|";
		}

		Timer.schedule(new SpawnFriendlies(level), 0, 2, 20);

		Timer.schedule(new SpawnEnemies(level), 0, 2, 20);
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


	////Networking Properties

	Character networkCharacter = new Character();
	ArrayList<AugmentedBullet> abs = new ArrayList<AugmentedBullet>();
	
	int network_id = -1;
	public void setNetworkCharacter (float x, float y)
	{
		networkCharacter.setPosition(x, y);
	}
	
	public void createNetworkBullets (float x, float y, double speedX, double speedY)
	{
		abs.add(new AugmentedBullet (x,y,speedX,speedY));
	}
	public void startGame (int id){
		network_id = id;
		hostWaitingRoom = false;
		game = true;
		resetGame();
		network = true;
	}


}
