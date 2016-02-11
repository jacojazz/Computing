package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;

public class Game extends JPanel {
	private static final long serialVersionUID = 1L;
	static final int TARGET_FPS = 120;
	static JFrame frame = new JFrame("Circle Collision");
	static int frames = 0;
	static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	static double width = gd.getDisplayMode().getWidth();
	static double height = gd.getDisplayMode().getHeight();

	static ArrayList<Particle> particleList = new ArrayList<Particle>();

	static Platform floor = new Platform(0, height, width, height);

	Game() {
		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				particleList.add(new Particle(new Point2D(e.getX(), e.getY()), 10, new Vector2D(0, 0), Materials.Rock));
			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}
		});

		setFocusable(true);

		particleList.add(new Particle(new Point2D(width / 2, height / 2), 50, new Vector2D(4, 0), Materials.Rock));
	}

	public void update() {
		for (Particle a : particleList) {
			for (Particle b : particleList) {
				if (checkCollision(a, b)) {
					//resolveCollision(a, b);
				}
			}
			a.update();
		}
	}

	boolean checkCollision(Particle a, Particle b) {
		double r = a.radius() + b.radius();
		if (a.distance(b.center()) <= r) {
			return true;
		}
		return false;
	}

	public static void resolveCollision(Particle a, Particle b) {
		Vector2D delta = (a.position.minus(b.position));
		double d = delta.norm();
		Vector2D mtd = delta.times(((a.radius() + b.radius()) - d) / d);

		double im1 = a.getInverseMass();
		double im2 = b.getInverseMass();

		a.position = a.position.plus(mtd.times(im1 / (im1 + im2)));
		b.position = b.position.minus(mtd.times(im2 / (im1 + im2)));

		Vector2D v = (a.velocity.minus(b.velocity));
		double vn = v.dot(mtd.normalize());

		if (vn > 0.0) {
			return;
		}

		double i = (-(1.0f + 0.3) * vn) / (im1 + im2);
		Vector2D impulse = mtd.times(i);

		a.velocity = a.velocity.plus(impulse.times(im1));
		b.velocity = b.velocity.minus(impulse.times(im2));
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g2d.setColor(Color.BLACK);
		for (Particle p : particleList) {
			p.draw(g2d);
		}
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
