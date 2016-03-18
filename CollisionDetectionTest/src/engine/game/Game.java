package engine.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.Line2D;

public class Game extends JPanel {
	private static final long serialVersionUID = 1L;
	static final int TARGET_FPS = 60;
	static final double GRAVITY = (double) TARGET_FPS / (2 * (double) TARGET_FPS);
	static JFrame frame = new JFrame("Clicker");
	static int frames = 0;
	static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	static int width = gd.getDisplayMode().getWidth();
	static int height = gd.getDisplayMode().getHeight();
	static Box2D bounds = new Box2D(0, width, 0, height);
	static Point2D mouse, initial;
	static Vector2D distance;
	static boolean dragging = false, debug = false, flood = false;
	static Line2D floor = new Line2D(0, height, width, height);
	static ArrayList<Particle> pList = new ArrayList<Particle>();
	static ArrayList<Line2D> lList = new ArrayList<Line2D>();

	Game() {
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
				dragging = true;
				initial = new Point2D(e.getPoint());
			}

			public void mouseReleased(MouseEvent e) {
				dragging = false;
				distance = new Vector2D(initial.minus(mouse));
				pList.add(new Particle(initial, 20, 1, distance.times(0.125)));
			}
		});

		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				mouse = new Point2D(e.getPoint());
			}

			public void mouseMoved(MouseEvent e) {
				mouse = new Point2D(e.getPoint());
			}
		});

		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_P) {
					debug = !debug;
				}
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});

		setFocusable(true);

		lList.add(new Line2D(0, height, width, height));
	}

	void update() {
		for (int particleIterator = 0; particleIterator < pList.size(); particleIterator++) {
			Particle p = pList.get(particleIterator);
			p.update();
			if (p.center().getY() > height || p.center().getX() > width || p.center().getX() < 0) {
				pList.remove(particleIterator);
			}
		}

		if (pList.size() > 30) {
			// pList.clear();
		}

		if (flood) {
			Random rand = new Random();
			pList.add(new Particle(new Point2D(rand.nextInt(width), -25), 5, 1, new Vector2D(0, 0)));
		}

		if (frames % TARGET_FPS == 0) {
			pList.add(new Particle(new Point2D(0, height - 20), 20, 1, new Vector2D(20, -20)));
			pList.add(new Particle(new Point2D(width, height - 20), 20, 1, new Vector2D(-19, -20)));
		}

	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		applyQualityRenderingHints(g2d);

		for (int particleIterator = 0; particleIterator < pList.size(); particleIterator++) {
			Particle p = Game.pList.get(particleIterator);
			p.draw(g2d);

			if (debug) {
				g2d.setColor(Color.BLUE);
				p.boundingBox().draw(g2d);
				g2d.setColor(Color.BLACK);

				for (int particle2Iterator = 0; particle2Iterator < Game.pList.size(); particle2Iterator++) {
					Particle p2 = Game.pList.get(particle2Iterator);
					if (p.inParticleCollisionRange(p2)) {
						g2d.setColor(Color.RED);
						new Line2D(p.center(), p2.center()).draw(g2d);
						g2d.setColor(Color.BLACK);
					}
				}
			}
		}

		if (dragging) {
			new Line2D(initial, mouse).draw(g2d);
		}

		g2d.drawString(Integer.toString(pList.size()), 0, 10);
	}

	public static void applyQualityRenderingHints(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	}

	public static void main(String[] args) {
		Game game = new Game();

		frame.add(game);
		frame.setSize(width, height);
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
				delta--;
			}
			game.repaint();
		}
	}
}
