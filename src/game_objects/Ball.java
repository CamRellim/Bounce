package game_objects;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class Ball{

	private int radius;
	private Rectangle hitbox;
	private BufferedImage img;

	public Ball(int radius) {
		this(radius, null);
	}

	public Ball(int radius, BufferedImage img) {

		this.radius = radius;
		hitbox = new Rectangle(2 * radius, 2 * radius);
		this.img = img;
	}

	public int getRadius() {
		return radius;
	}
	
	public Rectangle getHitbox(){
		return hitbox;
	}
	
	public BufferedImage getImage(){
		return img;
	}

	public void setPosition(int x, int y) {
		hitbox.setLocation(x, y);
	}

	public Point getPosition() {
		return hitbox.getLocation();
	}
}
