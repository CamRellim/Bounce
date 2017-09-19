package main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import game_objects.Ball;

public class Board extends JPanel implements MouseListener {

	private final int DELAY = 17;
	private final int COUNTDOWN = 1000;
	private final int RADIUS = 100;
	private final int MAX_SPEED = 100;
	private final int INITAL_SPEED = 10;
	private final int INITAL_SCORE = 0;
	private final int INITIAL_COUNTDOWN = 30;

	// Score
	private int score;
	private int multiplicator;

	// Display
	private Rectangle bounds;
	private BufferedImage bg;
	private BufferedImage play;
	private JLabel display_score;

	// Animation
	private Timer animation;
	private Timer countdown;

	// Game
	private Ball ball;
	private int speed;
	private int angle;
	private int gameTime;

	public Board(Rectangle bounds) {
		// init variables
		this.bounds = bounds;
		initValues();
		
		 // JLabel for score
		 display_score = new JLabel();
		 display_score.setBounds(350, 100, 100, 100);
		 Font font = new Font("Arial", Font.BOLD + Font.ITALIC, 30); // Schriftgröße und -stil werden geändert
		 display_score.setFont(font);
		 display_score.setHorizontalAlignment(JLabel.LEFT);
		 add(display_score, BorderLayout.NORTH);

		// load images
		bg = getScaledBufferedImage("images/bg.png", bounds.width, bounds.height);
		play = getScaledBufferedImage("images/play.png", 100, 100);
		BufferedImage ballImg = getScaledBufferedImage("images/egg.png", 2*RADIUS, 2*RADIUS);

		// add ball
		ball = new Ball(RADIUS, ballImg);

		// add timers
		animation = new Timer(DELAY, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				// 1. Move Ball
				moveBall(ball, speed, angle);
				// 2. Handle possible collision
				hitsBounds(ball);
				// 3. Update panel
				repaint();
			}
		});
		countdown = new Timer(COUNTDOWN, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				gameTime--;
				if (gameTime == 0)
					gameOver();
			}
		});

		// add mouselistener and keybindings
		setupKeyBindings();
		addMouseListener(this);

		// start game
		ball.setPosition((int) bounds.getCenterX() - ball.getRadius(), (int) bounds.getCenterY() - ball.getRadius());
		repaint();
		animation.start();
		countdown.start();
		
		// init play/pause button
		 JLabel pause = new JLabel(new ImageIcon(play));
		 pause.setBounds(100, 100, 100, 100);
		 add(pause);
		 pause.addMouseListener(new MouseAdapter(){

			public void mouseClicked(MouseEvent arg0) {
					pauseGame();
		            int reply = JOptionPane.showConfirmDialog(null, "Weiterspielen?", "Punktestand; " + score, JOptionPane.YES_NO_OPTION);
		              if(reply == JOptionPane.YES_OPTION)
		              {
		              	continueGame();
		              }
		              else {
		            	  System.exit(0);
		              }
				}
		});
	}

	public void initValues() {
		speed = INITAL_SPEED;
		angle = new Random().nextInt(361);
		score = INITAL_SCORE;
		gameTime = INITIAL_COUNTDOWN;
		multiplicator = 0;
	}

	private BufferedImage getScaledBufferedImage(String filename, int width, int height) {

		BufferedImage img;
		try {
			img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		Image tmp = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		BufferedImage buffered = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		buffered.getGraphics().drawImage(tmp, 0, 0, null);

		return buffered;
	}

	private void setupKeyBindings() {
		// Add space key
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_SPACE), "pause");
		getActionMap().put("pause", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pauseGame();
			}
		});
		// Add escape key
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ESCAPE), "escape");
		getActionMap().put("escape", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	private void moveBall(Ball ball, int speed, double directionAngle) {

		Point p = ball.getPosition();
		int x = (int) Math.round(p.x + speed * Math.cos(Math.toRadians(directionAngle)));
		int y = (int) Math.round(p.y + speed * Math.sin(Math.toRadians(directionAngle)));

		ball.setPosition(x, y);
	}

	private boolean hitsBounds(Ball ball) {

		boolean hit = false;
		Rectangle hitbox = ball.getHitbox();
		if (hitbox.x <= bounds.getX() || hitbox.x + hitbox.width >= bounds.getX() + bounds.getWidth()) {
			angle = 180 - angle;
			hit = true;
		}
		if (hitbox.y <= bounds.getY() || hitbox.y + hitbox.height >= bounds.getY() + bounds.getHeight()) {
			angle = 360 - angle;
			hit = true;
		}

		return hit;
	}

	public void draw(Ball ball, Graphics g, JPanel panel) {

		Rectangle hitbox = ball.getHitbox();
		int radius = ball.getRadius();
		BufferedImage img = ball.getImage();

		// draw hitbox (only for debugging)
		// g.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);

		if (img != null)
			g.drawImage(img, hitbox.x, hitbox.y, panel);
		else {
			g.fillOval(hitbox.x, hitbox.y, 2 * radius, 2 * radius);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point mouseLocation = e.getLocationOnScreen();
		if (ball.getHitbox().contains(mouseLocation))
			ballClicked();
		else {
			multiplicator = 0;
			speed = INITAL_SPEED;
		}
	}

	private void pauseGame() {
		animation.stop();
		countdown.stop();
	}
	
	private void continueGame(){
		animation.start();
		countdown.start();
	}

	// options if game is over
	private void gameOver() {
		pauseGame();

		int reply = JOptionPane.showConfirmDialog(null, "Nochmal?", "Verloren", JOptionPane.YES_NO_OPTION);
		if (reply == JOptionPane.YES_OPTION) {
			initValues();
			continueGame();
		} else {
			System.exit(0);
		}
	}

	public void ballClicked() {
		multiplicator++;

		int randNr = new Random().nextInt(361);
		angle = randNr;

		if (speed < MAX_SPEED)
			speed += 10;
		score += multiplicator;
		display_score.setText("Score: " + String.valueOf(score));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(bg, 0, 0, null);
		draw(ball, g, this);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}
