package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel {
	private static final long serialVersionUID = 1L;
	static JFrame frame = new JFrame("Circle Collision");
	static int frames = 0;
	static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	static double width = gd.getDisplayMode().getWidth();
	static double height = gd.getDisplayMode().getHeight();
	static Rectangle2D screenBounds = new Rectangle2D.Double(0, 0, width, height);
	static int TARGET_FPS = 120;

	BufferedImage original;
	int imageCounter = 0;
	double scale = 1;
	
	CircleImage cImage;

	Game() throws IOException {
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				scale -= (double) e.getWheelRotation() / 10;
			}
		});
		
		setFocusable(true);

		original = ImageIO.read(new File("banner2.png"));
		cImage = new CircleImage(original);
		cImage.scale(4f);
	}

	void update() {

	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.scale(scale, scale);
		g2d.drawImage(cImage.getNextImage(), 0, 0, null);
	}

	public static void main(String[] args) throws IOException {
		Game game = new Game();

		frame.setSize((int) width, (int) height);
		frame.add(game);
		frame.setUndecorated(true);
		frame.setBackground(Color.BLACK);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		long lastLoopTime = System.nanoTime();

		long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
		long lastFpsTime = 0;
		long gameTime;
		while (true) {
			OPTIMAL_TIME = 1000000000 / TARGET_FPS;
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;
			lastFpsTime += updateLength;
			if (lastFpsTime >= 1000000000) {
				lastFpsTime = 0;
			}
			game.update();
			game.repaint();

			try {
				gameTime = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
				Thread.sleep(gameTime);
			} catch (Exception e) {
			}
		}
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

	public static double randDouble(double min, double max) {
		Random rand = new Random();
		return min + (max - min) * rand.nextDouble();
	}
}
