package main;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import game_objects.Ball;

public class Board extends JPanel {

	private final int DELAY = 15;
	private final int RADIUS = 100;

	// Display
	private Rectangle bounds;

	// Animation
	private Timer force;
	private Timer hitDetection;

	// Ball
	private Ball ball;
	private int velocityX = 5;
	private int velocityY = 7;
	
	// Air resistance (not yet implemented)
	private final double CW = 0.9;
	
	// Gravity
	private final int GRAVITY = 1;

	public Board(Rectangle bounds, boolean isGravity) {
		// init variables
		this.bounds = bounds;
		force = new Timer(DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// Move Ball
				moveBall(ball, velocityX, velocityY);
				
				//optional gravity 
				if(isGravity)
					gravity();
			}
		});
		hitDetection = new Timer(DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// handle possible collision
				hitsBounds(ball);
			}
		});
		ImageIcon img = new ImageIcon("images/egg.png");
		ball = new Ball(RADIUS, img.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT));

		// start animation
		ball.setPosition((int) bounds.getCenterX() - ball.getRadius(), (int) bounds.getCenterY() - ball.getRadius());
		repaint();
		force.start();
		hitDetection.start();

		setVisible(true);
	}

	private void moveBall(Ball ball, int velocityX, int velocityY) {
		
		Point p = ball.getPosition();
		int x = p.x + velocityX;
		int y = p.y + velocityY;

		ball.setPosition(x, y);

		repaint();
	}
	
	private void gravity(){
		velocityY += GRAVITY;
	}

	private boolean hitsBounds(Ball ball) {

		boolean hit = false;
		Rectangle hitbox = ball.getHitbox();
		if (hitbox.x <= bounds.getX() || hitbox.x + hitbox.width >= bounds.getX() + bounds.getWidth()){
			velocityX *= -1;
			hit = true;
		}
		else if (hitbox.y <= bounds.getY() || hitbox.y + hitbox.height >= bounds.getY() + bounds.getHeight()){
			velocityY *= -1;
			hit = true;
		}
		
		return hit;
	}

	public void draw(Ball ball, Graphics g, JPanel panel) {
		
		Rectangle hitbox = ball.getHitbox();
		int radius = ball.getRadius();
		Image img = ball.getImage();
		
		//draw hitbox (only for debugging)
//		g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
		
		if (img != null)
			g.drawImage(img, hitbox.x, hitbox.y, panel);
		else {
			g.fillOval(hitbox.x, hitbox.y, 2 * radius, 2 * radius);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		draw(ball, g, this);
	}

	// TEST FUNCTION
	private void push(Ball ball) {

		Point currentPos = ball.getPosition();
		double a = currentPos.y + velocityY;
		double b = currentPos.x + velocityX;

		int angleIn = 90;
		if (b != 0 && a != 0) {
			double tan = Math.tan(a / b);
			angleIn = (int) Math.round(Math.toDegrees(Math.atan(tan)));
		}
		int angleOut = 180 - angleIn;

	}
}
