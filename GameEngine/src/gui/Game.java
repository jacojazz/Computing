package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel {
	private static final long serialVersionUID = 1L;
	static final int TARGET_FPS = 120;
	static JFrame frame = new JFrame("Circle Collision");
	static int frames = 0;
	static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	static double width = gd.getDisplayMode().getWidth();
	static double height = gd.getDisplayMode().getHeight();

	ArrayList<Circle> circleList = new ArrayList<Circle>();

	Game() {
	}

	public void update() {
		for (Circle a : circleList) {
			for (Circle b : circleList) {
				if (collisionDistance(a, b)) {
					System.out.println(true);
				}
			}
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g2d.setColor(Color.BLACK);

		for (Circle c : circleList) {
			g2d.fill(c.getGeneralPath());
		}
	}

	public boolean collisionDistance(Circle a, Circle b) {
		double r = a.radius + b.radius;
		r *= r;
		return r < Math.pow(a.center().x() + b.center().x(), 2) + Math.pow(a.center().y() + b.center().y(), 2);
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

		boolean running = true;

		long lastTime = System.nanoTime();
		final double ns = 1000000000 / TARGET_FPS;
		double delta = 0;
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				frames++;
				game.update();
				game.repaint();
				delta--;
			}
		}
	}
}
