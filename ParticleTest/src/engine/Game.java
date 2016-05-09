package engine;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.Rectangle2D;
import engine.collision.CollisionHandler;
import engine.geometry.Particle;
import engine.handler.InputHandler;
import engine.handler.PaintHandler;
import engine.handler.ToolHandler;
import engine.handler.UpdateHandler;
import engine.utils.Cleaner;

public class Game extends JPanel {
	private static final long serialVersionUID = 1L;
	static final int TARGET_FPS = 60;
	static JFrame frame = new JFrame("Particle Test");
	static int frames = 0;
	static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	public static int width = gd.getDisplayMode().getWidth();
	public static int height = gd.getDisplayMode().getHeight();
	public static Rectangle2D bounds = new Rectangle2D(new Point2D(0, 0), new Point2D(width, height));
	public static boolean debug = false;
	CollisionHandler ch = new CollisionHandler();
	UpdateHandler uh = new UpdateHandler();
	Cleaner cl = new Cleaner();
	PaintHandler ph = new PaintHandler();
	public static CopyOnWriteArrayList<Particle> pList = new CopyOnWriteArrayList<Particle>();
	public static CopyOnWriteArrayList<LineSegment2D> lList = new CopyOnWriteArrayList<LineSegment2D>();

	Game() {
		addMouseListener(InputHandler.ml);
		addMouseMotionListener(InputHandler.mml);
		addKeyListener(InputHandler.kl);
		setFocusable(true);
		for (Iterator<LineSegment2D> lIterator = bounds.edges().iterator(); lIterator.hasNext();) {
			lList.add(lIterator.next());
		}
	}

	void update() {
		ch.update();
		uh.update();
		cl.update();
		ToolHandler.update();
	}

	public void paint(Graphics g) {
		super.paint(g);
		final Graphics2D g2d = (Graphics2D) g;
		applyQualityRenderingHints(g2d);

		ph.paint(g2d);
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
