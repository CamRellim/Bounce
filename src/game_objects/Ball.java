package game_objects;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import utils.CollisionType;

public class Ball {

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

	public Rectangle getHitbox() {
		return hitbox;
	}

	public BufferedImage getImage() {
		return img;
	}

	public void setPosition(int x, int y) {
		hitbox.setLocation(x, y);
	}

	public Point getPosition() {
		return hitbox.getLocation();
	}

	public void draw(Graphics g, JPanel panel) {

		// draw hitbox (only for debugging)
		// g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

		if (img != null)
			g.drawImage(img, hitbox.x, hitbox.y, panel);
		else {
			g.fillOval(hitbox.x, hitbox.y, 2 * radius, 2 * radius);
		}
	}

	public void move(int speed, double directionAngle) {

		Point p = hitbox.getLocation();
		int x = (int) Math.round(p.x + speed * Math.cos(Math.toRadians(directionAngle)));
		int y = (int) Math.round(p.y + speed * Math.sin(Math.toRadians(directionAngle)));

		hitbox.setLocation(x, y);
	}

	public CollisionType hitsBounds(Rectangle bounds) {

		CollisionType hit = null;

		if (hitbox.x <= bounds.getX() || hitbox.x + hitbox.width >= bounds.getX() + bounds.getWidth()) {
			hit = CollisionType.HORIZONTAL;
		}
		if (hitbox.y <= bounds.getY() || hitbox.y + hitbox.height >= bounds.getY() + bounds.getHeight()) {
			if (hit == null)
				hit = CollisionType.VERTICAL;
			else
				hit = CollisionType.BOTH;
		}

		return hit;
	}
}
