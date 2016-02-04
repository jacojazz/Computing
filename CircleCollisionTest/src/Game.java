import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.Random;

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
	static int TARGET_FPS = 240;

	static Circle staticCircle, movingCircle;

	Game() {
		staticCircle = new Circle((width / 2) - (200 / 2), (height / 2) - (200 / 2), 200, 0, 0, 10);
		movingCircle = new Circle(width - 20, height * 0.8, 20, 280, 0.5, 1);
	}

	void update() {
		double xDif = movingCircle.circle.getCenterX() - staticCircle.circle.getCenterX();
		double yDif = movingCircle.circle.getCenterY() - staticCircle.circle.getCenterY();
		double distanceSquared = Math.pow(xDif, 2) + Math.pow(yDif, 2);
		
		if (distanceSquared <= Math.pow(movingCircle.radius + staticCircle.radius, 2)) {
			movingCircle.speed = 0;
		}

		movingCircle.update();
		staticCircle.update();
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		staticCircle.paint(g2d);
		movingCircle.paint(g2d);
	}

	public static void main(String[] args) {
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
