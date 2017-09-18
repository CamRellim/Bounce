package main;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import game_objects.Ball;

public class Board extends JPanel{

	private final int DELAY = 15;
	private final int RADIUS = 100;

	// Display
	private Rectangle bounds;

	// Animation
	private Timer updateBoard;
	private Timer force;
	private Timer hitDetection;

	// Ball
	private Ball ball;
	private int speed = 10;
	private int angle = 40;

	public Board(Rectangle bounds) {
		// init variables
		this.bounds = bounds;
		updateBoard = new Timer(DELAY, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		force = new Timer(DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// Move Ball
				moveBall(ball, speed, angle);
			}
		});
		hitDetection = new Timer(DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// handle possible collision
				hitsBounds(ball);
			}
		});
		
		//load image
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("images/egg.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Image tmp = img.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
		BufferedImage buffered = new BufferedImage(200,200,BufferedImage.TYPE_INT_ARGB);
		buffered.getGraphics().drawImage(tmp, 0, 0, null);
		
		ball = new Ball(RADIUS, buffered);

		// start animation
		ball.setPosition((int) bounds.getCenterX() - ball.getRadius(), (int) bounds.getCenterY() - ball.getRadius());
		repaint();
		updateBoard.start();
		force.start();
		hitDetection.start();
		
		setVisible(true);
	}

	private void moveBall(Ball ball, int speed, double directionAngle) {
		
		Point p = ball.getPosition();
		int x = (int)Math.round(p.x + speed * Math.cos(Math.toRadians(directionAngle)));
		int y = (int)Math.round(p.y + speed * Math.sin(Math.toRadians(directionAngle)));
		
		ball.setPosition(x, y);
	}
	
	private boolean hitsBounds(Ball ball) {

		boolean hit = false;
		Rectangle hitbox = ball.getHitbox();
		if (hitbox.x <= bounds.getX() || hitbox.x + hitbox.width >= bounds.getX() + bounds.getWidth()){
			angle = 180 - angle;
			hit = true;
		}
		if (hitbox.y <= bounds.getY() || hitbox.y + hitbox.height >= bounds.getY() + bounds.getHeight()){
			angle = 360 - angle;
			hit = true;
		}
		
		return hit;
	}

	public void draw(Ball ball, Graphics g, JPanel panel) {
		
		Rectangle hitbox = ball.getHitbox();
		int radius = ball.getRadius();
		BufferedImage img = ball.getImage();
		
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
}
