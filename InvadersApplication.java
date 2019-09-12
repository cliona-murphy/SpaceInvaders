package week4solution;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.util.Iterator;



public class InvadersApplication extends JFrame implements Runnable, KeyListener {
	
	public static final Dimension WindowSize = new Dimension(800,600);
	private BufferStrategy strategy;
	private Graphics offscreenBuffer;
	private static final int NUMALIENS = 30;
	private Alien[] AliensArray = new Alien[NUMALIENS];
	private Spaceship PlayerShip;
	private Image bulletImage;
	private ArrayList bulletsList = new ArrayList();
	private static boolean isInitialised = false;
	private int wave = 1; 
	private int NumLiveAlien;
	private boolean isGameInProgress = true;
	private int score;
	private int highScore =0;
	
	
	
	public InvadersApplication(){
	Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	int x = screensize.width/2 - WindowSize.width/2;
	int y = screensize.height/2 - WindowSize.height/2;
	setBounds(x, y, WindowSize.width, WindowSize.height);
	setVisible(true);
	this.setTitle("Final");

	ImageIcon icon = new ImageIcon("\\alien_ship_1.png");
	Image alienImage = icon.getImage();
	icon = new ImageIcon("\\alien_ship_2.png");
	Image alienImage2 = icon.getImage();
	icon = new ImageIcon("\\bullet.png");
	bulletImage = icon.getImage();
	
	
	
	for(int i=0; i < NUMALIENS; i++){
		AliensArray[i] = new Alien(alienImage, alienImage2);
	}
	
	
	
	icon = new ImageIcon("\\player_ship.png");
	Image shipImage = icon.getImage();
	PlayerShip = new Spaceship(shipImage, bulletImage);
	PlayerShip.setPosition(300,530);
	
	
	Thread t = new Thread(this);
	t.start();

	addKeyListener(this);


		createBufferStrategy(2);
		strategy = getBufferStrategy();
		offscreenBuffer = strategy.getDrawGraphics();
		
		isInitialised = true;
		
		startNewGame();
		
	}

public void run(){

	while (true) {
		try {
			Thread.sleep(20);

		} catch (InterruptedException e) {  }
		
			
			boolean alienDirectionReversalNeeded = false;

		for(int i =0; i<NUMALIENS; i++){
			if(AliensArray[i].isAlive){
				if(AliensArray[i].move())
					alienDirectionReversalNeeded = true;
			}
		}
	if(alienDirectionReversalNeeded){
		for(int i=0; i<NUMALIENS; i++){
			if(AliensArray[i].isAlive){
				AliensArray[i].reverseDirection();
			}
		}
	}
			PlayerShip.move();
			
		Iterator iterator = bulletsList.iterator();
		while(iterator.hasNext()){
		PlayerBullet b = (PlayerBullet) iterator.next();
		
		if(b.move()){
			iterator.remove();
		}
		else {
			double x2 = b.x, y2=b.y;
			double w1 = 50, h1 = 32;
			double w2 =6, h2 =16;
			
			for(int i=0; i<NUMALIENS; i++){
				if(AliensArray[i].isAlive ){
					double x1 = AliensArray[i].x;
					double y1 = AliensArray[i].y;
					if (
							( (x1<x2 && x1+w1>x2) ||
									(x2<x1 && x2+w2>x1) )
									&&
									( (y1<y2 && y1+h1>y2) ||
									(y2<y1 && y2+h2>y1) )
									) {
						AliensArray[i].isAlive = false;
						score+=10;
						NumLiveAlien--;
						iterator.remove();
						break;									
						}
					}
				}
			if(NumLiveAlien==0){
				startNewWave();
			}
			}
		}
		
		Rectangle shipPosition = PlayerShip.getPosition();
		double x2 = shipPosition.x;
		double y2 = shipPosition.y;
		double w2 = shipPosition.width;
		double h2 = shipPosition.height;

		
		for(int i=0; i<NUMALIENS; i++){
			if(AliensArray[i].isAlive ){
				double x1 = AliensArray[i].x;
				double y1 = AliensArray[i].y;
				
				
				if (
						( (x1<x2 && x1+w1>x2) ||
								(x2<x1 && x2+w2>x1) )
								&&
								( (y1<y2 && y1+h1>y2) ||
								(y2<y1 && y2+h2>y1) )
								) {
					isGameInProgress = false;
				}
				
		this.repaint();
		
			}
			
		}
	}
	
		
		
	}


		

public void keyPressed(KeyEvent e) {
	
	if(isGameInProgress){
	if(e.getKeyCode()==KeyEvent.VK_LEFT)
	PlayerShip.setXSpeed(-4);

	else if (e.getKeyCode()==KeyEvent.VK_RIGHT)
		PlayerShip.setXSpeed(4);

	else if(e.getKeyCode()==KeyEvent.VK_SPACE)
	 bulletsList.add(PlayerShip.shootBullet());
	}
	
	else{
		startNewGame();
	}
}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode()== KeyEvent.VK_LEFT || e.getKeyCode()== KeyEvent.VK_RIGHT)
			PlayerShip.setXSpeed(0);
		
	}


	public void KeyTyped(KeyEvent e) {}
	
		

	public void paint(Graphics g){
		
		
		
		if(!isInitialised)
			return;
			
			g = offscreenBuffer;
			
			
						
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WindowSize.width, WindowSize.height);
		
		
		if(isGameInProgress) {
			
			g.setColor(Color.WHITE);
		g.setFont(new Font("Impact", Font.PLAIN, 30));
		g.drawString(String.format("Score: %d   Best: %d", score, highScore), 200, 5);
		
		for (int i=0; i<NUMALIENS; i++)
			AliensArray[i].paint(g);
		
		
			PlayerShip.paint(g);
				
			
		Iterator iterator = bulletsList.iterator();
		while(iterator.hasNext()){
		PlayerBullet b = (PlayerBullet) iterator.next();
		b.paint(g);
			}
		
		strategy.show();
		}
		
		else {
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Impact", Font.PLAIN, 90));
			g.drawString("GAME OVER", 200, 150);
			g.setFont(new Font("Impact", Font.PLAIN, 60));
			g.drawString("Press any key to play", 160, 250);
			g.setFont(new Font("Impact", Font.PLAIN, 60));
			g.drawString("[Arrow keys to move, space to fire]", 160, 450);
		}
	}
	

	
			public static void main (String[] args) {
			InvadersApplication w = new InvadersApplication();
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			
		public void startNewWave(){
				
					
			for(int i=0; i < NUMALIENS; i++){
				
			AliensArray[i].isAlive = true;
				
				double xx = (i%5)*80 +70;
				double yy = (i/5)*40 +50;
				AliensArray[i].setPosition(xx, yy);
				AliensArray[i].setXSpeed(2*wave);
				
		}
		wave ++;
		NumLiveAlien = NUMALIENS;
		}
		
		
			
		public void startNewGame(){
			
		if(highScore > score)
			highScore = score;
		
		score=0;
		wave =1;
		
		startNewWave();
		
		isGameInProgress = true;
		
		
		}

}
