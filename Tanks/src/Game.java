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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.SimplePolygon2D;

public class Game extends JPanel {
	private static final long serialVersionUID = 1L;
	static final int TARGET_FPS = 60;
	static JFrame frame = new JFrame("Tanks");
	static int frames = 0;
	static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	static int width = gd.getDisplayMode().getWidth();
	static int height = gd.getDisplayMode().getHeight();
	static Point2D mouse, initial, mouseRelation;
	static SimplePolygon2D terrain = new SimplePolygon2D();
	static Vector2D gravity = new Vector2D(0, 0.05);
	Tank p1Tank = new Tank(300, 300, Color.CYAN);
	Tank p2Tank = new Tank(width - 300, 300, Color.LIGHT_GRAY);
	Slider leftAim = new Slider(new Point2D(20, 20), 200, 50);
	Slider rightAim = new Slider(new Point2D(width - 220, 20), 200, 50);
	Slider leftPower = new Slider(new Point2D(20, 90), 200, 50);
	Slider rightPower = new Slider(new Point2D(width - 220, 90), 200, 50);
	static CopyOnWriteArrayList<Projectile> projectiles = new CopyOnWriteArrayList<Projectile>();

	Game() {
		generateTerrain(50);

		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				mouse = new Point2D(e.getPoint());
			}

			public void mouseMoved(MouseEvent e) {
				mouse = new Point2D(e.getPoint());
			}
		});

		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {

			}

			public void mouseEntered(MouseEvent e) {

			}

			public void mouseExited(MouseEvent e) {

			}

			public void mousePressed(MouseEvent e) {
				leftAim.mousePressed(e);
				rightAim.mousePressed(e);
				leftPower.mousePressed(e);
				rightPower.mousePressed(e);

				initial = new Point2D(e.getPoint());

				if (p1Tank.boundingBox.contains(mouse)) {
					p1Tank.dragging = true;
					mouseRelation = p1Tank.position.minus(Game.mouse);
				} else if (p2Tank.boundingBox.contains(mouse)) {
					p2Tank.dragging = true;
					mouseRelation = p2Tank.position.minus(Game.mouse);
				}
			}

			public void mouseReleased(MouseEvent e) {
				leftAim.mouseReleased(e);
				rightAim.mouseReleased(e);
				leftPower.mouseReleased(e);
				rightPower.mouseReleased(e);
				p1Tank.dragging = false;
				p2Tank.dragging = false;
			}
		});
		setFocusable(true);
	}

	void generateTerrain(int smoothness) {
		if (smoothness <= 0) {
			smoothness = 1;
		}
		terrain.clearVertices();
		Random r = new Random();
		Point2D previousPoint = new Point2D(0, height / 2);
		terrain.addVertex(previousPoint);
		for (int terrainWidth = smoothness; terrainWidth < width; terrainWidth += smoothness) {
			if (r.nextInt(2) == 0 && previousPoint.getY() < height - 200 && previousPoint.getY() > 200) {
				terrain.addVertex(new Point2D(terrainWidth, previousPoint.getY() + r.nextInt(smoothness)));
			} else if (r.nextInt(2) == 1 && previousPoint.getY() < height - 200 && previousPoint.getY() > 200) {
				terrain.addVertex(new Point2D(terrainWidth, previousPoint.getY() - r.nextInt(smoothness)));
			} else {
				terrain.addVertex(new Point2D(terrainWidth, previousPoint.getY()));
			}
		}
		terrain.addVertex(new Point2D(width, height / 2));
		terrain.addVertex(new Point2D(width, height));
		terrain.addVertex(new Point2D(0, height));
	}

	double randDouble(double rangeMin, double rangeMax) {
		Random r = new Random();
		return rangeMin + (rangeMax - rangeMin) * r.nextDouble();
	}

	void update() {
		leftAim.update();
		rightAim.update();
		leftPower.update();
		rightPower.update();

		for (Iterator<Projectile> pIterator = projectiles.iterator(); pIterator.hasNext();) {
			Projectile p = pIterator.next();
			if (terrain.contains(p.center())) {
				projectiles.remove(p);
			} else {
				p.update();
			}

		}

		ArrayList<Tank> tanks = new ArrayList<Tank>();
		tanks.add(p1Tank);
		tanks.add(p2Tank);
		for (Iterator<Tank> tankIterator = tanks.iterator(); tankIterator.hasNext();) {
			Tank t = tankIterator.next();
			if (tanks.indexOf(t) == 0) {
				t.turretAngle = ((leftAim.getValue() * 180) / 100) - 90;
				t.power = leftPower.getValue();
			} else {
				t.turretAngle = ((rightAim.getValue() * 180) / 100) - 90;
				t.power = rightPower.getValue();
			}
			if (terrain.contains(t.position) || terrain.distance(t.position) < 1) {
				List<Point2D> pointCollection = new ArrayList<Point2D>();
				for (Iterator<LineSegment2D> terrainIterator = terrain.edges().iterator(); terrainIterator.hasNext();) {
					LineSegment2D l = terrainIterator.next();
					pointCollection.add(l.point(l.positionOnLine(t.position)));
				}
				Collections.sort(pointCollection, new PointDistance(t.position));
				t.position = pointCollection.get(0);
				t.onTerrain = true;
				t.velocity = new Vector2D(0, 0);
				t.update();
			} else {
				t.onTerrain = false;
				t.update();
			}
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		applyQualityRenderingHints(g2d);

		g2d.setColor(Color.BLACK);

		if (p1Tank.dragging || p2Tank.dragging) {
			new Circle2D(initial, 100).draw(g2d);
		}

		g2d.setColor(new Color(0x00cc00));
		terrain.fill(g2d);

		g2d.setColor(Color.BLACK);
		for (Iterator<Projectile> pIterator = projectiles.iterator(); pIterator.hasNext();) {
			Projectile p = pIterator.next();
			p.paint(g2d);
		}
		p1Tank.paint(g2d);
		p2Tank.paint(g2d);
		leftAim.paint(g2d);
		rightAim.paint(g2d);
		leftPower.paint(g2d);
		rightPower.paint(g2d);
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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
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
