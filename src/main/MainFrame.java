package main;

import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	private Rectangle bounds;

	public MainFrame() {
		bounds = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		//smaller board
//		bounds = new Rectangle(600, 600);

		// init JFrame
		setTitle("Bounce");
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setUndecorated(true);
		setBounds(bounds);
		add(new Board(bounds));
		setVisible(true);
	}

	public static void main(String[] args) {
		new MainFrame();
	}
}
