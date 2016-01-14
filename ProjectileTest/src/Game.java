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
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel {
	private static final long serialVersionUID = 636997359079194521L;
	static int frames = 0;
	static JFrame frame = new JFrame("Projectile Test");
	static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	static int width = gd.getDisplayMode().getWidth();
	static int height = gd.getDisplayMode().getHeight();
	static ArrayList<Particle> particleList = new ArrayList<Particle>();
	static int angle = 0;
	static int TARGET_FPS = 120;
	static int initialX, initialY, distanceX, distanceY;
	static boolean HOSE = true;
	static boolean dragging = false;
	static int mouseX, mouseY;

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
				initialX = e.getX();
				initialY = e.getY();
			}

			public void mouseReleased(MouseEvent e) {
				dragging = false;
				distanceX = initialX - e.getX();
				distanceY = initialY - e.getY();
				particleList.add(new Particle(initialX, initialY, distanceX / 20, distanceY / 20, 5));

			}
		});

		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}

			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});

		setFocusable(true);

		if (HOSE) {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					particleList.add(new Particle(0, height / 2, 4, randDouble(0, -10), 5));
				}
			}, 0, 1);
		}
	}

	public void update() {
		for (int i = 0; i < particleList.size(); i++) {
			try {
				Particle currentParticle = particleList.get(i);
				if (currentParticle.getX() > width || currentParticle.getX() < 0 || currentParticle.age > currentParticle.lifetime) {
					particleList.remove(i);
				} else {
					currentParticle.update();
				}
			} catch (Exception e) {
			}
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for (int i = 0; i < particleList.size(); i++) {
			try {
				particleList.get(i).paint(g2d);
			} catch (Exception e) {
			}
		}

		g2d.setColor(Color.BLACK);

		if (dragging) {
			g2d.drawLine(initialX, initialY, mouseX, mouseY);
		}

		g2d.drawString(Integer.toString(particleList.size()), 0, 10);
	}

	public static void main(String[] args) {
		Game game = new Game();

		frame.setSize(width, height);
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
			frames++;

			try {
				gameTime = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
				Thread.sleep(gameTime);
			} catch (Exception e) {
			}
		}
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public static double randDouble(double rangeMin, double rangeMax) {
		Random r = new Random();
		return rangeMin + (rangeMax - rangeMin) * r.nextDouble();
	}
}
