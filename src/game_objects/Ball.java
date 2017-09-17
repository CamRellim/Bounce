package game_objects;

import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

public class Ball {

	private int radius;
	private Rectangle hitbox;
	private Image img;

	public Ball(int radius) {
		this(radius, null);
	}

	public Ball(int radius, Image img) {

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
	
	public Image getImage(){
		return img;
	}

	public void setPosition(int x, int y) {
		hitbox.setLocation(x, y);
	}

	public Point getPosition() {
		return hitbox.getLocation();
	}
}
