package engine.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
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
	static final double EARTH_GRAVITY = 0.5;
	static JFrame frame = new JFrame("Collision");
	static int frames = 0;
	static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	static int width = gd.getDisplayMode().getWidth();
	static int height = gd.getDisplayMode().getHeight();
	Menu menu = new Menu();
	static Box2D bounds = new Box2D(0, width, 0, height);
	static Point2D mouse, initial;
	static Vector2D distance;
	static boolean dragging = false, debug = false, flood = false;
	static int gravityType = 1;
	static Line2D floor = new Line2D(width, height, 0, height);
	static Line2D leftWall = new Line2D(0, height, 0, 0);
	static Line2D rightWall = new Line2D(width, 0, width, height);
	static Line2D roof = new Line2D(0, 0, width, 0);
	static ArrayList<Particle> pList = new ArrayList<Particle>();
	static ArrayList<Line2D> lList = new ArrayList<Line2D>();
	static ArrayList<GravityNode> gList = new ArrayList<GravityNode>();
	static ArrayList<ModifierMenu> mList = new ArrayList<ModifierMenu>();
	static double manualSize = 40;

	Game() {
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				menu.mouseClicked(e);

				for (int modifierIterator = 0; modifierIterator < mList.size(); modifierIterator++) {
					mList.get(modifierIterator).mouseClicked(e);
				}

				if (e.getButton() == MouseEvent.BUTTON3) {
					for (Particle p : pList) {
						if (p.center().almostEquals(mouse, p.radius())) {
							mList.add(new ModifierMenu(p));
						}
					}

					for (GravityNode gn : gList) {
						if (gn.almostEquals(mouse, 3)) {
							mList.add(new ModifierMenu(gn));
						}
					}
				}
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
				menu.mousePressed(e);

				for (ModifierMenu m : mList) {
					m.mousePressed(e);
				}

				if (!menu.baseRect.contains(mouse) && !menu.toolsBaseRect.contains(mouse) && !ModifierMenu.checkBounds(mouse)) {
					dragging = true;
					initial = new Point2D(e.getPoint());
				}
			}

			public void mouseReleased(MouseEvent e) {
				menu.mouseReleased(e);

				for (ModifierMenu m : mList) {
					m.mouseReleased(e);
				}

				if (!menu.baseRect.contains(mouse) && !menu.toolsBaseRect.contains(mouse) && !ModifierMenu.checkBounds(mouse)) {
					dragging = false;
					distance = new Vector2D(initial.minus(mouse));

					if (e.getButton() == MouseEvent.BUTTON1) {
						switch (menu.toolType) {
						case 0:
							pList.add(new Particle(initial, manualSize, manualSize / 20, distance.times(0.125)));
							break;
						case 1:
							if (!initial.equals(mouse)) {
								if (initial.getX() < mouse.getX()) {
									lList.add(new Line2D(mouse, initial));
								} else {
									lList.add(new Line2D(initial, mouse));
								}
							}
							break;
						case 2:
							if (gravityType == 2) {
								gList.add(new GravityNode(mouse, 100, false));
							}
							break;
						case 3:
							if (gravityType == 2) {
								gList.add(new GravityNode(mouse, 100, true));
							}
							break;
						default:
							break;
						}
					}
				}
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

		setFocusable(true);

		lList.add(floor);
		lList.add(leftWall);
		lList.add(rightWall);
		lList.add(roof);
	}

	static Vector2D getGravity() {
		switch (gravityType) {
		case 1:
			return new Vector2D(0, 0.5);
		case 2:
			return new Vector2D(0, 0);
		default:
			return new Vector2D(0, 0);
		}
	}

	static String getGravityString() {
		switch (gravityType) {
		case 1:
			return "Normal";
		case 2:
			return "Node-Based";
		default:
			return "Not Found";
		}
	}

	void update() {
		menu.update();

		if (pList.size() > 150) {
			pList.remove(0);
		}

		for (int particleIterator = 0; particleIterator < pList.size(); particleIterator++) {
			Particle p = pList.get(particleIterator);

			if (Game.gravityType == 2 && !Game.gList.isEmpty()) {
				p.setActive(true);
			}

			if (p.isActive()) {
				p.update();
			}

			if (p.center().getY() > height + p.radius() || p.center().getX() > width + p.radius() || p.center().getX() < 0 - p.radius()) {
				pList.remove(particleIterator);
			}
		}

		if (flood) {
			Random rand = new Random();
			pList.add(new Particle(new Point2D(rand.nextInt(width), rand.nextInt(height)), manualSize, manualSize / 20, new Vector2D(0, 0)));
		}

		for (ModifierMenu m : mList) {
			if (m.selectedObject instanceof Particle) {
				if (!pList.contains(m.selectedObject)) {
					mList.remove(m);
					break;
				} else {
					m.update();
				}
			} else if (m.selectedObject instanceof GravityNode) {
				if (!gList.contains(m.selectedObject)) {
					mList.remove(m);
					break;
				} else {
					m.update();
				}
			}
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		applyQualityRenderingHints(g2d);

		g2d.setColor(Color.BLACK);

		if (gravityType == 2) {
			for (int nodeIterator = 0; nodeIterator < gList.size(); nodeIterator++) {
				GravityNode gn = gList.get(nodeIterator);
				gn.draw(g2d, 3);
			}
		}

		for (int particleIterator = 0; particleIterator < pList.size(); particleIterator++) {
			Particle p = pList.get(particleIterator);

			if (debug) {
				if (p.isActive()) {
					p.draw(g2d);
				} else {
					p.fill(g2d);
				}
			} else {
				p.draw(g2d);
			}

			if (debug) {
				g2d.setColor(Color.BLUE);
				p.boundingBox().draw(g2d);
				g2d.setColor(Color.BLACK);

				for (int particle2Iterator = 0; particle2Iterator < pList.size(); particle2Iterator++) {
					Particle p2 = pList.get(particle2Iterator);
					if (p.inParticleCollisionRange(p2) && !p.equals(p2)) {
						g2d.setColor(Color.RED);
						new Line2D(p.center(), p2.center()).draw(g2d);
						g2d.setColor(Color.BLACK);
					}
				}

				for (int line2Iterator = 0; line2Iterator < lList.size(); line2Iterator++) {
					Line2D l2 = lList.get(line2Iterator);
					if (p.inLineCollisionRange(l2)) {
						g2d.setColor(Color.GREEN);
						new Line2D(p.center(), l2.point(l2.project(p.center()))).draw(g2d);
						g2d.setColor(Color.BLACK);
					}
				}

				if (gravityType == 2) {
					for (int nodeIterator = 0; nodeIterator < gList.size(); nodeIterator++) {
						GravityNode gn = gList.get(nodeIterator);
						gn.draw(g2d, 3);
						g2d.setColor(Color.GREEN);
						new Line2D(p.center(), gn).draw(g2d);
						g2d.setColor(Color.BLACK);
					}
				}
			}
		}

		for (int lineIterator = 0; lineIterator < lList.size(); lineIterator++) {
			Line2D l = lList.get(lineIterator);
			l.draw(g2d);
		}

		if (dragging) {
			new Line2D(initial, mouse).draw(g2d);
		}

		menu.paint(g2d);

		for (int modifierIterator = 0; modifierIterator < mList.size(); modifierIterator++) {
			mList.get(modifierIterator).paint(g2d);
		}

		g2d.setColor(Color.BLACK);
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
