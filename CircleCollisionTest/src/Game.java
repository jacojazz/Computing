import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
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
	static double mouseX, mouseY, initialX, initialY, distanceX, distanceY;
	static boolean dragging = false;
	static ArrayList<Circle> circleList = new ArrayList<Circle>();

	static Circle staticCircle, movingCircle;
	static Point2D p;

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
				dragging = true;
				initialX = e.getX();
				initialY = e.getY();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				dragging = false;
				distanceX = initialX - e.getX();
				distanceY = initialY - e.getY();

				double angle = Math.atan2(distanceY, distanceX);
				circleList.add(new Circle(initialX, initialY, 20d, angle, 1, 1));
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

		staticCircle = new Circle((width / 2) - (200 / 2), (height / 2) - (200 / 2), 200, Math.PI, 0, 100);
		movingCircle = new Circle(600, 0, 20, 65 * (Math.PI / 180), 1, 1);
	}

	void update() {
		for (int i = 0; i < circleList.size(); i++) {
			Circle currentCircle = circleList.get(i);
			double xDif = currentCircle.circle.getCenterX() - staticCircle.circle.getCenterX();
			double yDif = currentCircle.circle.getCenterY() - staticCircle.circle.getCenterY();
			double distanceSquared = Math.pow(xDif, 2) + Math.pow(yDif, 2);

			double collisionPointX = ((staticCircle.circle.getCenterX() * currentCircle.radius) + (currentCircle.circle.getCenterX() * staticCircle.radius)) / (staticCircle.radius + currentCircle.radius);
			double collisionPointY = ((staticCircle.circle.getCenterY() * currentCircle.radius) + (currentCircle.circle.getCenterY() * staticCircle.radius)) / (staticCircle.radius + currentCircle.radius);

			// distanceSquared <= Math.pow(movingCircle.radius +
			// staticCircle.radius, 2)

			if (distanceSquared <= Math.pow(currentCircle.radius + staticCircle.radius, 2)) {
				double newVelX1 = (currentCircle.xVelocity * (currentCircle.mass - staticCircle.mass) + (2 * staticCircle.mass * staticCircle.xVelocity)) / (currentCircle.mass + staticCircle.mass);
				double newVelY1 = (currentCircle.yVelocity * (currentCircle.mass - staticCircle.mass) + (2 * staticCircle.mass * staticCircle.yVelocity)) / (currentCircle.mass + staticCircle.mass);

				currentCircle.angle = Math.atan2(newVelY1, newVelX1);
			}

			currentCircle.update();
		}
		staticCircle.update();
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		staticCircle.paint(g2d);

		for (Circle c : circleList) {
			c.paint(g2d);
		}

		if (dragging) {
			g2d.drawLine((int) initialX, (int) initialY, (int) mouseX, (int) mouseY);
			g2d.setColor(Color.WHITE);
			g2d.drawLine((int) initialX + 1, (int) initialY + 1, (int) mouseX, (int) mouseY);
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
