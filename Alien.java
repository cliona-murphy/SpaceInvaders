package week4solution;
import java.awt.Image;
import java.awt.Graphics;

public class Alien extends Sprite2D{

	public boolean isAlive = true;
	
	public Alien(Image i, Image i2){
		super(i,i2);
	}
	
	public void paint(Graphics g){
		
		if(isAlive)
			super.paint(g);
	}
	
	public boolean move(){
		x += xSpeed;
	
		if(x<=0|| x>=InvadersApplication.WindowSize.width-myImage.getWidth(null))
			return true;
		else
			return false;
		
	}
	
	public void reverseDirection(){
		xSpeed = -xSpeed;
		y+=20;
	}
}
