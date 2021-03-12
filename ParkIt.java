//Rana Al-Khulaidi
//ICS4U-03
//ParkIt game
//June 5, 2020
//This game allows the user to pick a difficulty and attempt parking a car in a given spot while not exceeding the time and gas limit

//imports
import java.awt.geom.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.*;
import java.awt.Rectangle.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javazoom.jl.player.Player;

//this is the main class which loads all the levels depending on difficulty and allows the user to play them in order
public class ParkIt extends JFrame implements ActionListener{
	
	private  javax.swing.Timer myTimer;
	private  GamePanel game1,game2,game3,game;
	//arrayLists
	private  ArrayList<Image> pics=new ArrayList<Image>();
	private  ArrayList<HousesBlock> block=new ArrayList<HousesBlock>();
	private  ArrayList<Boulevard> boulevard=new ArrayList<Boulevard>();
	private  ArrayList<StreetRect> rects=new ArrayList<StreetRect>();
	private  ArrayList<Car> cars=new ArrayList<Car>();
	private  ArrayList<TrafficBarrier> barrier=new ArrayList<TrafficBarrier>();
	
	private  Image road,heartPic,gameOver,winImage;
	private  UserCar userCarLevel;
	private  ParkingSpot parking;
	private  String level;
	private String difficulty,lev1,lev2,lev3;
	private MP3 pop;
	
	//this method loads and adds all the basic images and loads level 1
    public ParkIt() {
    	
    	super("PARKIT :)");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(900,650);
    
    	myTimer = new javax.swing.Timer(40, this);
    	//sound from Zapsplat.com
    	pop=new MP3("pop.mp3");
    	
    	//allows user to pick difficulty of levels at start of game
    	pickDifficulty();
    		
    	try {
    		road = ImageIO.read(new File("road.png"));
    		heartPic=ImageIO.read(new File("heart.png"));
    		gameOver=ImageIO.read(new File("gameOver.png"));
    		winImage=ImageIO.read(new File("winBg.png"));
		}
		catch (IOException e) {
		}

		road=road.getScaledInstance(900,650, Image.SCALE_DEFAULT);

		pics.add(road);
		pics.add(heartPic);
		pics.add(gameOver);
		pics.add(winImage);
		
		load(lev1);
		add(game);
		
		setResizable(false);
		setVisible(true);     
    }
    
    //this method loads the level file in the parameter and adds all the level components to the proper arrayList
    public  void load(String fileName){  
    	
    	//this clears out all the arrayLists and variables
		if(game!=null){
			remove(game);
		}
		if(cars.size()>0){
			cars.clear();
		}
		if(boulevard.size()>0){
			boulevard.clear();
		}
		if(rects.size()>0){
			rects.clear();
		}
		if(block.size()>0){
			block.clear();
		}
		if(barrier.size()>0){
			barrier.clear();
		}
		
		//this scans the file and adds the components to the proper arrayList 
		try{
			Scanner inFile = new Scanner(new BufferedReader(new FileReader(fileName)));
			
			//gets level number
			level=inFile.nextLine();
			
			//uses the integer given in the file before the objects in order to properly add the correct amount of objects
			int boulevNum=Integer.parseInt(inFile.nextLine());
			for(int n=0;n<boulevNum;n++){
				boulevard.add(new Boulevard(inFile.nextLine()));
			}
			
			int rectNum=Integer.parseInt(inFile.nextLine());
			for(int n=0;n<rectNum;n++){	
				rects.add(new StreetRect(inFile.nextLine()));
			}
			
			int carNum=Integer.parseInt(inFile.nextLine());
			for(int n=0;n<carNum;n++){	
				cars.add(new Car(inFile.nextLine()));
			}
			
			int barrierNum=Integer.parseInt(inFile.nextLine());
			for(int n=0;n<barrierNum;n++){	
				barrier.add(new TrafficBarrier(inFile.nextLine()));
			}
			
			int blockNum=Integer.parseInt(inFile.nextLine());
			for(int n=0;n<blockNum;n++){
				block.add(new HousesBlock(inFile.nextLine()));
			}
			
			int parkNum=Integer.parseInt(inFile.nextLine());
			for(int n=0;n<parkNum;n++){
				parking=new ParkingSpot(inFile.nextLine());
			}
			
			int userNum=Integer.parseInt(inFile.nextLine());
			for(int n=0;n<userNum;n++){
				userCarLevel=new UserCar(inFile.nextLine());
			}
			inFile.close();
		}
		catch(IOException ex){
			System.out.println("ex");
		}
		
		//sets the gamePanel variable as the level that was loaded using the arrayLists that were filled
		game = new GamePanel(this,pics,block,boulevard,rects,cars,userCarLevel,barrier,parking,level);
		
		//adds the panel to the JFrame
		add(game);
		validate();
    }
    
    //this method creates an Option Pane that allows the user to pick the difficulty of the levels 
    public void pickDifficulty(){
    	
    	String options[] = {"Easy","Medium","Hard"};
    	
    	int answer = JOptionPane.showOptionDialog(null,"How Difficult?", "Difficulty?", JOptionPane.YES_NO_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        
        //sets level variables to name of file based on choice of difficulty
    	if (answer == JOptionPane.YES_OPTION){
    		pop.play();
    		difficulty="easy";
    		lev1="level1E.txt";
    		lev2="level2E.txt";
    		lev3="level3E.txt";
		}
		else if (answer == JOptionPane.NO_OPTION){
			pop.play();
    		difficulty="medium";
    		lev1="level1M.txt";
    		lev2="level2M.txt";
    		lev3="level3M.txt";
		}
    	else{
    		pop.play();
    		difficulty="hard";
    		lev1="level1H.txt";
    		lev2="level2H.txt";
    		lev3="level3H.txt";
    	}
    }
    
    //this method runs the game
    public void actionPerformed(ActionEvent evt){
    	
		if(game!= null && game.ready==true ){
			game.move();
			game.repaint();
			
			//restarts game from level 1 if all lives are lost
			if(game.getRestart()){	
				load(lev1);	
			}
			
			//proceeds to next level if user parks successfully
			else if(game.getNextLev()&&game.getLev().equals("1")){
				load(lev2);
				game.setNextLev(false);
			}
			
			else if(game.getNextLev()&&game.getLev().equals("2")){
				load(lev3);
				game.setNextLev(false);
			}
			
			else if(game.getNextLev()){
				game.setWin(true);
			}	
		}
	}
	
	public void start(){
		myTimer.start();
	}
	
    public static void main(String[] arguments) {
		Menu frame = new Menu();
    }
    
}

//this class was copied from the provided program on Edsby
class MP3 {
    private String filename;
    private Player player; 

    // constructor that takes the name of an MP3 file
    public MP3(String filename) {
        this.filename = filename;
    }

    public void close() { if (player != null) player.close(); }

    // play the MP3 file to the sound card
    public void play() {
        try {
            FileInputStream fis     = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        }
        catch (Exception e) {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }

        // run in new thread to play in background
        new Thread() {
            public void run() {
                try { player.play(); }
                catch (Exception e) { System.out.println(e); }
            }
        }.start();
    }
}

//this class creates a start screen for the game and allows the user to start the game by pressing on a button
class Menu extends JFrame{
	private JLayeredPane layeredPane=new JLayeredPane();
	private MP3 pop;
	
	//this method loads all the files and sets the variables
    public Menu() {
		super("ParkIT");
		setSize(900,650);
		ImageIcon backPic = new ImageIcon("backgr.png");
		ImageIcon startPic = new ImageIcon("startPic.png");	
		//sound from Zapsplat.com
		pop=new MP3("pop.mp3");
		
		JLabel back = new JLabel(backPic);
	
		back.setBounds(0, 0,backPic.getIconWidth(),backPic.getIconHeight());

		layeredPane.add(back,1);
		
		//creates a button to start
		JButton startBtn = new JButton(startPic);	
		startBtn.addActionListener(new ClickStart());
		startBtn.setBounds(300,350,startPic.getIconWidth(),startPic.getIconHeight());
		
		layeredPane.add(startBtn,2);

		setContentPane(layeredPane);        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setResizable(false);
		setVisible(true);
    }
    
    //this method displays the instruction screen when the user clicks start
    class ClickStart implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent evt){
    		//plays sound when button is clicked
			pop.play();
    		setVisible(false);
    		Instructions iFrame=new Instructions();
    		
    	}
    }
}

//this class creates an instruction screen to show user how to play
class Instructions extends JFrame{
	
	private JLayeredPane layeredPane=new JLayeredPane();
	//sound from Zapsplat.com
	private MP3 pop;

	//this method loods the images and creates a button to start the game
    public Instructions() {
    	
		super("ParkIT");
		setSize(900,650);
		ImageIcon backPic = new ImageIcon("instructions.png");
		ImageIcon startPic = new ImageIcon("startPic1.png");	
		pop=new MP3("pop.mp3");
		
		JLabel back = new JLabel(backPic);
	
		back.setBounds(0, 0,backPic.getIconWidth(),backPic.getIconHeight());

		layeredPane.add(back,1);

		//creates button and adds it to panel 
		JButton startBtn = new JButton(startPic);	
		startBtn.addActionListener(new ClickStart());
		startBtn.setBounds(360,520,startPic.getIconWidth(),startPic.getIconHeight());
		layeredPane.add(startBtn,2);

		setContentPane(layeredPane);        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setResizable(false);
		setVisible(true);
    }
    
    //starts actual game when the button is pressed
    class ClickStart implements ActionListener{
    	@Override
    	public void actionPerformed(ActionEvent evt){
			pop.play();
    		setVisible(false);
    		ParkIt gFrame=new ParkIt();
    		
    	}
    }
}

//this class sets up the levels
class GamePanel extends JPanel {
	
	//initializes variables
	private boolean []keys;
	public boolean ready=false;
	private ParkIt mainFrame;
	private UserCar userCar;
	private Image road,heart,gameOverPic,winPic,instrucBg;
	private int lives,carX,carY;
	private String level;
	private boolean gameOverScreen,gameOver,restart,startTime,startGas,nextLev,winScreen,win,instrucScreen;
	private HousesBlock leftHouseBlock;
	private ParkingSpot parking;
	private ArrayList<TrafficBarrier> trafficBarriers;
	private ArrayList<Boulevard> boulevardList;
	private ArrayList<StreetRect> streetLines;
	private ArrayList<Car> cars;
	private ArrayList<Image> images;
	private ArrayList<HousesBlock> blocks;
	private Font fontSys=null;
	//starting amounts
	private double time=21;
	private double gas=100;
	//amounts lost per each frame
	private double loseGas=0.5;
	//base time and gas
	private final double timeBasic=21;
	private final double gasBasic=100;
	//sounds from Zapsplat.com
	private MP3 bell,crash,pop;
	
	//this method sets all the variables 
	public GamePanel(ParkIt pGame,ArrayList<Image> pics,ArrayList<HousesBlock> block,ArrayList<Boulevard>boulevards
		,ArrayList<StreetRect>roadLines,ArrayList<Car>nonUserCars,UserCar playerCar,ArrayList<TrafficBarrier>barriers,
		ParkingSpot park,String levelNum){
		
		fontSys = new Font("Arial",Font.PLAIN,23);
		keys = new boolean[KeyEvent.KEY_LAST+1];
		
		addKeyListener(new moveListener());
		
		bell=new MP3("bell.mp3");
		crash=new MP3("crash.mp3");
		pop=new MP3("pop.mp3");
		images=pics;
		mainFrame = pGame;
		//arrayLists
		userCar=playerCar;
		trafficBarriers=barriers;
		boulevardList=boulevards;
		streetLines=roadLines;
		cars=nonUserCars;
		parking=park;
		blocks=block;
		blocks=block;
		
		road=pics.get(0);
		heart=pics.get(1);
		gameOverPic=pics.get(2);
		winPic=pics.get(3);
		
		lives=3;
		carX=userCar.getPX();
		carY=userCar.getPY();
		gameOverScreen=false;
		gameOver=false;
		restart=false;
		level=levelNum;
		startTime=true;
		startGas=false;
		nextLev=false;
		//changes based on whether you win the screen was displayed
		winScreen=false;
		win=false;
	}
	
	//this methods delays the game for a given length
	public static void delay (long len){
		try	{
		    Thread.sleep (len);
		}
		catch (InterruptedException ex)	{
		}
    }
	
	//this method allows game to begin
	public void addNotify() {
        super.addNotify();
        requestFocus();
        ready = true;
        mainFrame.start(); 
    }

	//this method displays all the components of the level
	public void paintComponent(Graphics g){
		
		g.drawImage(road,0,0,this);
		
		//goes through each arrayList and displays the images
		if(leftHouseBlock!=null){
			leftHouseBlock.draw(g);
		}
    	for(HousesBlock b:blocks){
    		b.draw(g);
    	}
    	for(Boulevard b:boulevardList){
    		b.draw(g);
    	}
  		//sets colour for street lines
    	g.setColor(new Color(255,255,255));
    	for(StreetRect r:streetLines){
    		g.drawRect((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());
    		g.fillRect((int)r.getX(),(int)r.getY(),(int)r.getWidth(),(int)r.getHeight());
    	}
    	parking.draw(g);
    	userCar.draw(g);
    	for(Car c:cars){
    		c.draw(g);
    	}
    	for(TrafficBarrier t:trafficBarriers){
    		t.draw(g);
    	}
    	
    	//displays information about level,gas, and time
    	g.setColor(new Color(209, 59,46));
    	g.fillRoundRect(325,10,120,40,30,20);
    	g.fillRoundRect(455,10,120,40,30,20);
    	g.fillRoundRect(585,10,120,40,30,20);
    	g.setFont(fontSys);
		g.setColor(new Color(255, 255, 255));
		g.drawString("Level",477,39);
		g.drawString(level,540,39);
		g.drawString("TIME",340,39);
		g.drawString(intTime(),402,39);
		g.drawString("GAS",598,39);
		g.drawString(intGas(),652,39);
    
    	//displays hearts for the amount of lives
    	for(int x=0;x<lives;x++){
    		g.drawImage(heart,60+x*55,14,null);
    	}
   		
   		//displays game over screen when the user loses all lives
    	if(gameOverScreen==false && gameOver==true){
    		g.drawImage(gameOverPic,0,0,null);
    		//asks user about playing again when this boolean is true
    		gameOverScreen=true;
    	}
    	
    	//displays you win screen when user completes all levels
    	if(winScreen==false && win==true){
    		g.drawImage(winPic,0,0,null);
    		//asks user about playing again when this boolean is true
    		winScreen=true;
    	}
    }
    
    //this method resets the time when a life is lost
    public void resetTime(){
    	time=timeBasic;
    }
    
    //this method resets gas when a life is lost
    public void resetGas(){
    	gas=gasBasic;
    }
    
    //this method turns time into a string 
    public String intTime(){
    	return Integer.toString((int)(time));
    }
    
    //this method turns gas into a string 
    public String intGas(){
    	return Integer.toString((int)(gas));
    }
    
    //this method allows user to move and does game logic
	public void move(){
		
		if(keys[KeyEvent.VK_UP] ){
			
			userCar.moveFront();
			
			//starts reducing gas when player moves
			startGas=true;
			
			//allows user to change directions only when moving
			if(keys[KeyEvent.VK_RIGHT]){
				userCar.changeAngle(5);
			}
			if(keys[KeyEvent.VK_LEFT] ){
				userCar.changeAngle(-5);
			}
		}
			
		else if(keys[KeyEvent.VK_DOWN] ){

			startGas=true;
			
			userCar.moveBack();
			
			if(keys[KeyEvent.VK_RIGHT]){
				userCar.changeAngle(5);
			}
			if(keys[KeyEvent.VK_LEFT] ){
				userCar.changeAngle(-5);
			}
		}
		
		//pauses game when the user presses the space button
		else if(keys[KeyEvent.VK_SPACE]) {
		
			//stops reducing time and gas
			startTime=false;
			startGas=false;
			
			//allows user to pick between continuing or exiting
	 	 	String options[] = {"Play","Exit"};
	 	 	
    		int answer = JOptionPane.showOptionDialog(null,"Continue playing?", "Play?", JOptionPane.DEFAULT_OPTION,
        	JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
  
        	keys[KeyEvent.VK_SPACE] = false;
        	//continues game if user chooses yes
    		if (answer == JOptionPane.YES_OPTION){
    			pop.play();
    			return;
			}
    		else{
    			pop.play();
    			System.exit(0);
    		}
		}
		
		else{
			//ensures gas isn't getting reduced without movement
			startGas=false;
		}
		
		//increases speed and causes gas to reduce faster
		if(keys[KeyEvent.VK_W]&&userCar.getSpeed()!=10) {
			startTime=false;
			startGas=false;
			loseGas+=0.25;
	 	 	userCar.setSpeed(userCar.getSpeed()+1);
		}
		//decreases speed and causes gas to reduce lower
		else if(keys[KeyEvent.VK_S]&&userCar.getSpeed()!=2) {
			startTime=false;
			startGas=false;
			loseGas-=0.25;
	 	 	userCar.setSpeed(userCar.getSpeed()-1);
		}
		
		//starts reducing time
		if(startTime){
			timerCount();
		}
		
		//starts reducing gas
		if(startGas){
			gasCount();
		}
		
		//moves all the non-user cars
		for(Car c:cars){
    		c.move();
    	}
    	
    	//user loses life if they run out of time or gas
    	if(gas<=0 || time<=0){
    		loseLife();
    	}
    	//ensures user doesnt leave boundaries 
    	if(userCar.getMidPosX()<0 || userCar.getMidPosX()>900){
    		loseLife();
    	}
    	if(userCar.getMidPosY()<0 || userCar.getMidPosY()>600){
    		loseLife();
    	}
		
		//checks for collisions between user car and all other objects 
    	for(TrafficBarrier t:trafficBarriers){
    		if(userCar.checkCollision(t.getRect())){
    			crash.play();
    			//user loses life if they crash
    			loseLife();
    		}
    	}
    	//checks for collisions between user and objects
    	for(Car c:cars){
    		if(userCar.checkCollision(c.getRect())){
    			crash.play();
    			loseLife();
    			
    		}
    	}
    	
    	for(Boulevard b:boulevardList){
    		if(userCar.checkCollision(b.getRect())){
    			crash.play();
    			loseLife();
    		}
    	}

    	for(HousesBlock b:blocks){	
    		if(userCar.checkCollision(b.getRect())){
    			crash.play();
    			loseLife();
    		}
    	}
    	
   		//checks if user successfully parked
		if(checkParking()){
			bell.play();
			nextLev=true;
			//starts next level
		}
		
		//checks if game over screen was displayed then displays an option pane for the user 
		if(gameOver==true && gameOverScreen==true){
			//stops game
			ready=false;
			//allows the user to pick between restarting and exiting
	 	 	String options[] = {"Play", "Exit"};
	 	 	
    		int answer = JOptionPane.showOptionDialog(null,"Do you want to play again?", "Play again?", JOptionPane.DEFAULT_OPTION,
        	JOptionPane.WARNING_MESSAGE, null, options, options[1]);
        	
    		if (answer == JOptionPane.YES_OPTION){
    			pop.play();
    			//restarts game 
    			restart=true;
    			ready=false;
    		}
    		else{
    			pop.play();
    			System.exit(0);	
    		}
		}
		
		//checks if you win screen was displayed then displays an option pane for the user 
		if(win==true && winScreen==true){
			ready=false;
	 	 	String options[] = {"Play", "Exit"};
    		int answer = JOptionPane.showOptionDialog(null,"Do you want to play again?", "Play again?", JOptionPane.DEFAULT_OPTION,
        	JOptionPane.WARNING_MESSAGE, null, options, options[1]);
        	
    		if (answer == JOptionPane.YES_OPTION){
    			pop.play();
    			//allows user to repick difficulty and play again
    			mainFrame.pickDifficulty();
    			restart=true;
    			ready=false;
    		}
    		
    		else{
    			pop.play();
    			System.exit(0);	
    		}
		}
	}
	
	//this method reduces the time by 0.04 each time the timer restarts
	public void timerCount(){
		time=time-0.04;
	}
	
	public String getLev(){
		return level;
	}
	
	//reduces gas by set value
	public void gasCount(){
		gas=gas-loseGas;
	}

	public boolean getNextLev(){
		return nextLev;
	}
	
	public boolean getRestart(){
		return restart;
	}
	
	public void setNextLev(boolean bool){
		nextLev=bool;
	}
	
	public void setWin(boolean bool){
		win=bool;
	}

	//this method checks if the user car is in the parking spot
	public boolean checkParking(){
		
		//current 4 corner points of car
		double[] points=userCar.getPoints();
		
		if(parking.checkContains(points)){
			return true;
		}
		else{
			return false;
		}
	}
	
	//this method reduces lives and checks if user has lost all lives
	public void loseLife(){
		resetTime();
		resetGas();
		if(lives>1){
			lives--;
		}
		else{
			gameOver();
		}
		resetPos();
	}
	
	//this method restarts the position of the car when the user loses a life
	public void resetPos(){
		//returns car points to original points
		userCar=new UserCar(Integer.toString(carX)+","+Integer.toString(carY));
		delay(70);
	}
	
	public void gameOver(){
		gameOver=true;
	}
	
    class moveListener implements KeyListener{
	    public void keyTyped(KeyEvent e) {}

	    public void keyPressed(KeyEvent e) {
	        keys[e.getKeyCode()] = true;
	    }
	    public void keyReleased(KeyEvent e) {
	        keys[e.getKeyCode()] = false;
	    }
    }
}

//this class creates Boulevard objects 
class Boulevard{
	
	private Image boulevardPic,boulevardPicSized,verticalBoulevard,horizontalBoulevard;
	private Rectangle2D boulevardRect;
	private int posX,posY,width,height;
	private String data;
	
	//this method takes in a string and gets all the information
	public Boulevard(String data){
		
		try {
    		verticalBoulevard=ImageIO.read(new File("verticalBoulevard.png"));
    		horizontalBoulevard=ImageIO.read(new File("horizontalBoulevard.png"));
		}
		catch (IOException e) {
		}
		
		String [] stats = data.split(",");
		
		//gets image type from file
		if(stats[0].equals("verticalBoulevard")){
			boulevardPic=verticalBoulevard;
		}
		else{
			boulevardPic=horizontalBoulevard;
		}
		
		//gets position
		posX=Integer.parseInt(stats[1]);
		posY=Integer.parseInt(stats[2]);
		
		//rescales image based on orientation
		if(stats[3].equals("true")){
			boulevardPicSized=boulevardPic.getScaledInstance(70,129, Image.SCALE_DEFAULT);
		}
		else{
			boulevardPicSized=boulevardPic.getScaledInstance(129,70, Image.SCALE_DEFAULT);
		}
		
		//gets dimensions
		height=boulevardPicSized.getHeight(null);
		width=boulevardPicSized.getWidth(null);
		
		//creates rect object to track collisions
		boulevardRect=new Rectangle (posX,posY,width,height);
	}
	public Rectangle2D getRect(){
		return boulevardRect;
	}
	public void draw(Graphics g){
        g.drawImage(boulevardPicSized,posX,posY,null);
	}
	public int getHeight(){
		return height;
	}
	public int getWidth(){
		return width;
	}
	public int getPX(){
		return posX;
	}
	public int getPY(){
		return posY;
	}
	
}

//this class creates a traffic barrier object
class TrafficBarrier{
	
	private Image barrierPic,barrierPicSized,verticalBarrier,horizontalBarrier;
	private Rectangle2D barrierRect;
	private String data;
	private boolean horizontal;
	private int posX,posY,width,height;
	public TrafficBarrier(String data){
		
		try {
    		verticalBarrier = ImageIO.read(new File("verticalBarrier.png"));
    		horizontalBarrier = ImageIO.read(new File("horizontalBarrier.png"));
		}
		
		catch (IOException e) {
		}
		
		String [] stats = data.split(",");
		
		//this sets type of object
		if(stats[0].equals("verticalBarrier")){
			barrierPic=verticalBarrier;
		}
		else{
			barrierPic=horizontalBarrier;
		}
		
		//this sets position
		posX=Integer.parseInt(stats[1]);
		posY=Integer.parseInt(stats[2]);
		
		//resizes based on orientation
		if(stats[3].equals("true")){
			barrierPicSized=barrierPic.getScaledInstance(50,14, Image.SCALE_DEFAULT);
		}
		else{
			barrierPicSized=barrierPic.getScaledInstance(14,50, Image.SCALE_DEFAULT);
		}
		
		height=barrierPicSized.getHeight(null);
		width=barrierPicSized.getWidth(null);
		
		barrierRect=new Rectangle (posX,posY,width,height);
	}
	public Rectangle2D getRect(){
		return barrierRect;
	}
	public void draw(Graphics g){
        g.drawImage(barrierPicSized,posX,posY,null);
	}
	public int getHeight(){
		return height;
	}
	public int getWidth(){
		return width;
	}
	public int getPX(){
		return posX;
	}
	public int getPY(){
		return posY;
	}
}

//this class creates a houses block object
class HousesBlock{
	
	private Image housesBlockPic,housesPicSized,leftBlockPic;
	private Rectangle2D housesBlockRect;
	private int posX,posY,width,height;
	private String data;

	public HousesBlock(String data){
		
		try {
    		leftBlockPic=ImageIO.read(new File("leftBlock.png"));
		}
		catch (IOException e) {
		}
			
		String [] stats = data.split(",");
		
		//sets image
		if(stats[0].equals("leftBlock")){
			housesBlockPic=leftBlockPic;
		}
		
		//resizes
		housesPicSized=housesBlockPic.getScaledInstance(78,640, Image.SCALE_DEFAULT);
		
		height=housesPicSized.getHeight(null);
		width=housesPicSized.getWidth(null);
		
		//sets position
		posX=Integer.parseInt(stats[1]);
		posY=Integer.parseInt(stats[2]);
		
		//rect object to track collisions
		housesBlockRect=new Rectangle (posX,posY,width,height);
	}
	
	public Rectangle2D getRect(){
		return housesBlockRect;
	}
	
	public void draw(Graphics g){
        g.drawImage(housesPicSized,posX,posY,null);
	}
	public int getHeight(){
		return height;
	}
	public int getWidth(){
		return width;
	}
	public int getPX(){
		return posX;
	}
	public int getPY(){
		return posY;
	}
}
	
//this class creates a car object for the non-user cars
class Car{
	
	private BufferedImage car1,car2,car3,car4,car5,car6,car7,car8,car9,car10,car,car11;
	private Image carPic,carPicSized;
	private Rectangle2D carRect;
	private final int SPEED=4;
	private String data;
	private boolean dirX,dirY,horizontal,pointUp;
	private int posX,posY,height,width;
	
	//this method sets all the variables
	public Car(String data){
	
		try {
    		car = ImageIO.read(new File("car.png"));
    		car1 = ImageIO.read(new File("car1.png"));
    		car2 = ImageIO.read(new File("car2.png"));
    		car3 = ImageIO.read(new File("car3.png"));
    		car4 = ImageIO.read(new File("car4.png"));
    		car5 = ImageIO.read(new File("car5.png"));
    		car6 = ImageIO.read(new File("car6.png"));
    		car7 = ImageIO.read(new File("car7.png"));
    		car8 = ImageIO.read(new File("car8.png"));
    		car9 = ImageIO.read(new File("car9.png"));
    		car10 = ImageIO.read(new File("car10.png"));
    		car11 = ImageIO.read(new File("car11.png"));
		}
		catch (IOException e) {
		}
		
		String [] stats = data.split(",");
		
		//sets image of car based on file
		if(stats[0].equals("car1")){
			carPic=car1;
		}
		else if(stats[0].equals("car2")){
			carPic=car2;
		}
		else if(stats[0].equals("car3")){
			carPic=car3;
		}
		else if(stats[0].equals("car4")){
			carPic=car4;
		}
		else if(stats[0].equals("car5")){
			carPic=car5;
		}
		else if(stats[0].equals("car6")){
			carPic=car6;
		}
		else if(stats[0].equals("car7")){
			carPic=car7;
		}
		else if(stats[0].equals("car8")){
			carPic=car8;
		}
		else if(stats[0].equals("car9")){
			carPic=car9;
		}
		else if(stats[0].equals("car10")){
			carPic=car10;
		}
		else if(stats[0].equals("car")){
			carPic=car;
		}
		else if(stats[0].equals("car11")){
			carPic=car11;
		}
		
		//sets position
		posX=Integer.parseInt(stats[1]);
		posY=Integer.parseInt(stats[2]);
		
		//checks horizontal movement based on file
		if(stats[3].equals("true")){
			dirX=true;
		}
		else{
			dirX=false;
		}
		//checks vertical movement based on file
		if(stats[4].equals("true")){
			dirY=true;
		}
		else{
			dirY=false;
		}
		
		//resizes based on orientation
		if(stats[5].equals("true")){
			carPicSized=carPic.getScaledInstance(80,45, Image.SCALE_DEFAULT);
		}
		else{
			carPicSized=carPic.getScaledInstance(45,80, Image.SCALE_DEFAULT);
		}
		
		//sees where car is pointing to move car in right direction
		if(stats[6].equals("true")){
			pointUp=true;
		}
		else{
			pointUp=false;
		}

		width=carPicSized.getWidth(null);
		height=carPicSized.getHeight(null);
	
		//creates rect object to track collisions
		carRect=new Rectangle(posX,posY,width,height);	
	}
	
	//this method moves the car
	public void move(){
		
			//moves car based on direction where the car points and direction of movement
			if(dirX){
				if (pointUp){
					posX=posX-SPEED;
				}
				else{
					posX=posX+SPEED;
				}
			}
			else if(dirY){
				if (pointUp){
					posY=posY-SPEED;
				}
				else{
					posY=posY+SPEED;
				}
			}
			//creates new rectangle when car moves
			carRect=new Rectangle(posX,posY,width,height);
	}
	
	public int getHeight(){
		return height;
	}
	public int getWidth(){
		return width;
	}
	public int getPX(){
		return posX;
	}
	public int getPY(){
		return posY;
	}
	public Rectangle2D getRect(){
		return carRect;
	}
	public void draw(Graphics g){	
        g.drawImage(carPicSized,posX,posY,null);
        
	}
}

//this method creates a user car object
class UserCar{
	
	private boolean []keys;
	private int[] xPts=new int[4];
	private int[] yPts= new int[4];
	double xComp,yComp;
	double [] p1Comps,p2Comps,p3Comps,p4Comps;
	private int posX,posY,midPosX,midPosY;
	private int width,height;
	private BufferedImage carPic;
	private int speed;
	private double angle,picAngle;
	private Polygon carRect;
	private String data;
	
	//this method sets the variables
	public UserCar(String data){
	
		try{
    		carPic = ImageIO.read(new File("car.png"));
		}
		catch (IOException e) {
		}
		
		String [] stats = data.split(",");
		
		angle=90;
		
		width=carPic.getWidth(null);
		height=carPic.getHeight(null);
		
		posX=Integer.parseInt(stats[0]);
		posY=Integer.parseInt(stats[1]);
		
		//sets middle point variables
		midPosX=posX+width/2;
		midPosY=posY+height/2;
	
		//sets the x and y components from the mid point to the corners
		xComp=width/2;
		yComp=height/2;
		
		//sets difference between midpoint and 4 corners
		p1Comps=new double [] {xComp,-yComp};
		p2Comps=new double [] {xComp,yComp};
		p3Comps=new double [] {-xComp,yComp};
		p4Comps=new double [] {-xComp,-yComp};
		
		//sets the x and y coordinates
		xPts[0]=(int)(midPosX+p1Comps[0]);
		xPts[1]=(int)(midPosX+p2Comps[0]);
		xPts[2]=(int)(midPosX+p3Comps[0]);
		xPts[3]=(int)(midPosX+p4Comps[0]);
		
		yPts[0]=(int)(midPosY+p1Comps[1]);
		yPts[1]=(int)(midPosY+p2Comps[1]);
		yPts[2]=(int)(midPosY+p3Comps[1]);
		yPts[3]=(int)(midPosY+p4Comps[1]);
		
		//sets polygon to track collisions
		carRect=new Polygon(xPts,yPts,4);
		
		speed=8;
	}
	
	//this method changes the polygon and coordinates whenever the car moves
	public void changePoly(){
		//adds the x,y vectors to the points by first turning the movement 
		//into a vector then breaking it into its components 
		xPts[0]=(int)(midPosX+getComponents(xyToVec(p1Comps))[0]);
		xPts[1]=(int)(midPosX+getComponents(xyToVec(p2Comps))[0]);
		xPts[2]=(int)(midPosX+getComponents(xyToVec(p3Comps))[0]);
		xPts[3]=(int)(midPosX+getComponents(xyToVec(p4Comps))[0]);
		
		yPts[0]=(int)(midPosY+getComponents(xyToVec(p1Comps))[1]);
		yPts[1]=(int)(midPosY+getComponents(xyToVec(p2Comps))[1]);
		yPts[2]=(int)(midPosY+getComponents(xyToVec(p3Comps))[1]);
		yPts[3]=(int)(midPosY+getComponents(xyToVec(p4Comps))[1]);
		//updates polygon
		carRect=new Polygon(xPts,yPts,4);	
	}
	
	public double[] getPoints(){
		double[] points=new double[]{xPts[0],yPts[0],xPts[1],yPts[1],xPts[2],yPts[2],xPts[3],yPts[3]};
		return points;
	}
	
	//this method changes the x,y coordinates into a vector where the car moved
	public double[] xyToVec(double[]xy){
		double[] ans=new double[2];
		ans[0]=Math.sqrt(Math.pow(xy[0],2)+Math.pow(xy[1],2));
		ans[1]=Math.atan2(xy[1],xy[0])+toRadians(picAngle);
		return ans;
	}
	
	//this method gets the x and y components of the vector where the car moved
	public double[] getComponents(double[]data){
		double[] ans=new double[2];
		ans[0]= Math.cos(data[1])*data[0];
		ans[1]= Math.sin(data[1])*data[0];
		return ans;
	}
	
	//this method changes the midpoint and the corner point based on where the car is moving
	public void moveFront(){
		//adds x,y components to the 2 points
		midPosY=(int)(midPosY-(Math.sin(toRadians(angle)))*speed);
		midPosX=(int)(midPosX-(Math.cos(toRadians(angle)))*speed);
		posX=(int)(posX-(Math.cos(toRadians(angle)))*speed);
		posY=(int)(posY-(Math.sin(toRadians(angle)))*speed);
		//updates polygon
		changePoly();
		
	}
	public void moveBack(){
		midPosY=(int)(midPosY+(Math.sin(toRadians(angle)))*speed);
		midPosX=(int)(midPosX+(Math.cos(toRadians(angle)))*speed);
		posX=(int)(posX+(Math.cos(toRadians(angle)))*speed);
		posY=(int)(posY+(Math.sin(toRadians(angle)))*speed);
		changePoly();
	}
	
	//this method draws the user's car
	public void draw(Graphics g){
		//this rotates the picture based on the angle
		AffineTransform rot = new AffineTransform();
		rot.rotate(toRadians(picAngle),width/2,height/2); 	
		
		AffineTransformOp rotOp = new AffineTransformOp(rot, AffineTransformOp.TYPE_BILINEAR); 
		Graphics2D g2D = (Graphics2D)g; 
														
		g2D.drawImage(carPic,rotOp,posX,posY);
	}
	
	//this method checks if the given object is colliding with the user's car
	public boolean checkCollision(Rectangle2D objRect){
	 	if(carRect.intersects(objRect)){
	 		return true;
	 	}
	 	else{
	 		return false;
	 	}
	 }	
	 
	 //changes angle of car based on user movement
	 public void changeAngle(double angleDeg){
	 	//restarts and changes angles 
	 	if(angle+angleDeg>=360){
	 		angle=angle+angleDeg-360;
	 	}
	 	else if(angle+angleDeg<=0){
	 		angle=360+(angle+angleDeg);
	 	}
	 	else{
	 		angle=angle+angleDeg;
	 	}
	 	if(picAngle+angleDeg>=360){
	 		picAngle=picAngle+angleDeg-360;
	 	}
	 	else if(angle+angleDeg<=0){
	 		picAngle=360+(picAngle+angleDeg);
	 	}
	 	else{
	 		picAngle=picAngle+angleDeg;
	 	}
	 }
	 
	 public double toRadians(double angleDeg){
	 	 return Math.toRadians(angleDeg);
	 }
	 
	 public int getPX(){
	 	return posX;
	 }
	 public int getPY(){
	 	return posY;
	 }
	 public int getMidPosX(){
	 	return midPosX;
	 }
	 public int getMidPosY(){
	 	return midPosY;
	 }
	 public void setSpeed(int num){
	 	speed=num;
	 }
	 public int getSpeed(){
	 	return speed;
	 }
}

//this class creates a parking spot object
class ParkingSpot{
	
	private int posX,posY,width,height;
	private Rectangle2D parking;
	private Image parkingPic,parkPicDown,parkPicUp;
	private boolean horizontal;
	public ParkingSpot(String data){
		
		try {	
    		parkPicDown=ImageIO.read(new File("parkingdown.png"));
    		parkPicUp=ImageIO.read(new File("parkingup.png"));
		}
		catch (IOException e) {
		}
		
		String [] stats = data.split(",");
		
		//sets image based on file
		if(stats[0].equals("parkUp")){
			parkingPic=parkPicUp;
		}
		else if(stats[0].equals("parkDown")){
			parkingPic=parkPicDown;
		}
		
		//sets position
		posX=Integer.parseInt(stats[1]);
		posY=Integer.parseInt(stats[2]);
		
		//resizes based on orientation
		if(stats[3].equals("false")){
			parkingPic=parkingPic.getScaledInstance(54,96, Image.SCALE_DEFAULT);
			width=54;
			height=90;
		}
		else{
			parkingPic=parkingPic.getScaledInstance(96,54, Image.SCALE_DEFAULT);
			width=90;
			height=54;
		}
		
		//creates rect object to check whether user parked successful
		Rectangle parking=new Rectangle(posX-2,posY-2,width,height);	
	}
	public void draw(Graphics g){
		g.drawImage(parkingPic,posX,posY,null);
	}
	//this method checks if the points given are inside the parking rectangle
	public boolean checkContains(double [] points){
		//increased size to make it easier to park
		Rectangle parking=new Rectangle(posX-5,posY-5,width+5,height+5);		
		if (parking.contains(points[0],points[1]) && parking.contains(points[2],points[3])
		&& parking.contains(points[4],points[5])&& parking.contains(points[6],points[7])){
				return true;
			}
		else{
			return false;
		}
	
	}
}

//this class creates a street line object
class StreetRect{
	
	private String data;
	private int pX,pY,width,height;
	private Rectangle streetRec;
	
	public StreetRect(String data){
		String [] stats = data.split(",");
		pX=Integer.parseInt(stats[0]);
		pY=Integer.parseInt(stats[1]);
		width=Integer.parseInt(stats[2]);
		height=Integer.parseInt(stats[3]);
		streetRec=new Rectangle(pX,pY,width,height);
	}
	public Rectangle getRect(){
		return streetRec;
	}
	public int getX(){
		return pX;
	}
	public int getY(){
		return pY;
	}
	public int getHeight(){
		return height;
	}
	public int getWidth(){
		return width;
	}
}




