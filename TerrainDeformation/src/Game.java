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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.point.PointArray2D;
import math.geom2d.polygon.Polygon2D;
import math.geom2d.polygon.convhull.JarvisMarch2D;

public class Game extends JPanel {
	private static final long serialVersionUID = 1L;
	static final int TARGET_FPS = 60;
	static JFrame frame = new JFrame("Clicker");
	static int frames = 0;
	static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	static int width = gd.getDisplayMode().getWidth();
	static int height = gd.getDisplayMode().getHeight();
	static Box2D clipBox = new Box2D(new Point2D(0, 0), width, height);
	Random r = new Random();
	Point2D mouse = new Point2D(0, 0);
	boolean mousePressed = false;
	boolean ctrlPressed = false;
	int pAmount = 1000;
	double mouseRadius = 50;
	Circle2D mouseCircle = new Circle2D(mouse, mouseRadius);

	PointArray2D p = new PointArray2D();
	JarvisMarch2D jm = new JarvisMarch2D();
	Polygon2D ch = clipBox.asRectangle();

	Game() {
		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F5) {
					algorithm();
				}
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
					ctrlPressed = true;
				}
			}

			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
					ctrlPressed = false;
				}
			}

			public void keyTyped(KeyEvent e) {

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
				mousePressed = true;
			}

			public void mouseReleased(MouseEvent e) {
				mousePressed = false;
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
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (!ctrlPressed) {
					int scale = 10;
					if ((pAmount - scale) > 0) {
						pAmount -= e.getPreciseWheelRotation() * scale;
					} else if (e.getPreciseWheelRotation() < 0) {
						pAmount -= e.getPreciseWheelRotation() * scale;
					}
				} else {
					if ((mouseRadius - 1) > 0) {
						mouseRadius -= e.getPreciseWheelRotation();
					} else if (e.getPreciseWheelRotation() < 0) {
						mouseRadius -= e.getPreciseWheelRotation();
					}
				}
			}
		});
		setFocusable(true);
		algorithm();
	}

	void algorithm() {
		p.clear();
		for (int x = 0; x < pAmount; x++) {
			p.add(new Point2D(200 + r.nextInt(width - 400), 200 + r.nextInt(height - 400)));
		}
	}

	void convexHull() {
		if (p.points().size() >= 2) {
			CopyOnWriteArrayList<Point2D> c = new CopyOnWriteArrayList<Point2D>(p.points());
			ch = jm.convexHull(c);
		}
	}

	void update() {
		convexHull();
		mouseCircle = new Circle2D(mouse, mouseRadius);
		if (mousePressed) {
			CopyOnWriteArrayList<Point2D> c = new CopyOnWriteArrayList<Point2D>(p.points());
			for (Iterator<Point2D> pIterator = c.iterator(); pIterator.hasNext();) {
				Point2D currentPoint = pIterator.next();
				if (currentPoint.distance(mouse) < mouseCircle.radius()) {
					p.remove(currentPoint);
				}
			}
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		applyQualityRenderingHints(g2d);

		CopyOnWriteArrayList<Point2D> c = new CopyOnWriteArrayList<Point2D>(p.points());
		for (Iterator<Point2D> pIterator = c.iterator(); pIterator.hasNext();) {
			Point2D currentPoint = pIterator.next();
			currentPoint.draw(g2d);
		}

		ch.draw(g2d);
		if (mousePressed || ctrlPressed) {
			mouseCircle.draw(g2d);
		}

		g2d.drawString(Integer.toString(pAmount), 0, 10);
		g2d.drawString(Double.toString(mouseCircle.radius()), 0, 20);
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
		frame.setBackground(Color.BLACK);
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
