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

import javax.swing.JFrame;
import javax.swing.JPanel;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.Line2D;

public class Game extends JPanel {
	private static final long serialVersionUID = 1L;
	static final int TARGET_FPS = 240;
	static JFrame frame = new JFrame("Circle Collision");
	static int frames = 0;
	static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	static int width = gd.getDisplayMode().getWidth();
	static int height = gd.getDisplayMode().getHeight();

	static double mouseX, mouseY, initialX, initialY;
	static boolean dragging = false;

	static boolean HOPPER = true;

	static ArrayList<Particle> particleList = new ArrayList<Particle>();
	static ArrayList<Platform> platformList = new ArrayList<Platform>();

	Game() {
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();

				if (e.getButton() == MouseEvent.BUTTON1) {
					dragging = true;
					initialX = e.getX();
					initialY = e.getY();
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					particleList.add(new Particle(new Point2D(mouseX, mouseY), new Vector2D(0, 0), 6, Material.Ball));
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					dragging = false;
					platformList.add(new Platform(initialX, initialY, mouseX, mouseY, Material.Static));
				}
			}
		});

		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});

		setFocusable(true);

		if (HOPPER) {
			platformList.add(new Platform(0, 0, (width / 2) - 100, height, Material.Static));
			platformList.add(new Platform(width, 0, (width / 2) + 100, height, Material.Static));
		}
	}

	void update() {
		for (int particleIterator = 0; particleIterator < particleList.size(); particleIterator++) {
			Particle currentParticle = particleList.get(particleIterator);
			currentParticle.update();
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		for (Platform plat : platformList) {
			plat.paint(g2d);
		}

		for (Particle part : particleList) {
			part.paint(g2d);
		}

		if (dragging) {
			new Line2D(initialX, initialY, mouseX, mouseY).draw(g2d);
		}
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
				game.repaint();
				delta--;
			}
		}
	}
}
