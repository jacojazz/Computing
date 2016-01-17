import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
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
	static int TARGET_FPS = 240;
	static int initialX, initialY, distanceX, distanceY;
	static boolean RAIN = false;
	static boolean SNOW = false;
	static boolean MANUAL = false;
	static int rainX = 0, rainY = 10;
	static int snowX = 0, snowY = 5;
	static int rainDelay = 2;
	static int snowDelay = 10;
	static boolean dragging = false;
	static int mouseX, mouseY;
	static Timer snowTimer, rainTimer;
	static Rectangle clearScreen = new Rectangle(width - 20, height - 20, 20, 20);
	static Rectangle manualToggle = new Rectangle(width - 60, 0, 20, 20);
	static Rectangle rainToggle = new Rectangle(width - 40, 0, 20, 20);
	static Rectangle snowToggle = new Rectangle(width - 20, 0, 20, 20);

	final static double GRAVITY = 0.10;

	Game() {
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				Point p = new Point(e.getX(), e.getY());
				if (clearScreen.contains(p)) {
					particleList.clear();
				}

				if (rainToggle.contains(p)) {
					rainX = randInt(-5, 5);
					RAIN = !RAIN;
				}

				if (snowToggle.contains(p)) {
					snowX = randInt(-3, 3);
					SNOW = !SNOW;
				}

				if (manualToggle.contains(p)) {
					MANUAL = !MANUAL;
				}
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
				if (MANUAL) {
					dragging = true;
					initialX = e.getX();
					initialY = e.getY();
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (MANUAL) {
					dragging = false;
					distanceX = initialX - e.getX();
					distanceY = initialY - e.getY();
					if (!manualToggle.contains(new Point(e.getX(), e.getY()))) {
						particleList.add(new Particle(initialX, initialY, distanceX / 20, distanceY / 20, 5, 8, 1f, 0));
					}
				}
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

		rainTimer = new Timer();
		snowTimer = new Timer();
	}

	public double offScreenCalculationRain() {

		double rainXVelocity = Math.abs(rainX);
		double rainYVelocity = Math.abs(rainY);

		double yV = Math.sqrt(Math.pow(rainYVelocity, 2) + (2 * GRAVITY * height));
		double t = (yV - rainYVelocity) / GRAVITY;
		double xS = ((rainXVelocity + rainXVelocity) / 2) * t;

		return xS; //Test

		/*
		 * } else { double rainXVelocity1 = Math.abs(rainX); double
		 * rainYVelocity1 = Math.abs(rainY);
		 * 
		 * double yS1 = (0 - Math.pow(rainYVelocity1, 2)) / (2 * GRAVITY);
		 * double yT1 = (2 * yS1) / (rainYVelocity1 + 0);
		 * 
		 * double xS1 = ((rainXVelocity1 + rainXVelocity1) / 2) * (2 * yT1);
		 * 
		 * double yV = Math.sqrt(Math.pow(rainYVelocity1, 2) + (2 * GRAVITY *
		 * height)); double t2 = (yV - rainYVelocity1) / GRAVITY; double xS2 =
		 * ((rainXVelocity1 + rainXVelocity1) / 2) * t2;
		 * 
		 * double xS = xS2 + (2 * xS1);
		 * 
		 * return xS; }
		 */

	}

	public double offScreenCalculationSnow() {
		double snowXVelocity = Math.abs(snowX);
		double snowYVelocity = Math.abs(snowY);

		double yV = Math.sqrt(Math.pow(snowYVelocity, 2) + (2 * GRAVITY * height));
		double t = (yV - snowYVelocity) / GRAVITY;
		double xS = ((snowXVelocity + snowXVelocity) / 2) * t;

		return xS;
	}

	public double xSpawnPosition(int type) {
		if (type == 1) {
			if (snowX > 0) {
				return randDouble(-offScreenCalculationSnow(), width);
			} else if (snowX < 0) {
				return randDouble(0, width + offScreenCalculationSnow());
			} else if (snowX == 0) {
				return randDouble(0, width);
			}
		} else if (type == 2) {
			if (rainX > 0) {
				return randDouble(-offScreenCalculationRain(), width);
			} else if (rainX < 0) {
				return randDouble(0, width + offScreenCalculationRain());
			} else if (rainX == 0) {
				return randDouble(0, width);
			}
		}
		return 0;
	}

	public void update() {
		frames++;
		for (int i = 0; i < particleList.size(); i++) {
			try {
				Particle currentParticle = particleList.get(i);
				if (currentParticle.TYPE == 0) {
					if (currentParticle.getX() > width || currentParticle.getX() < 0 || currentParticle.age > currentParticle.lifetime) {
						particleList.remove(i);
					} else {
						currentParticle.update();
					}
				} else if (currentParticle.TYPE == 1) {
					if (currentParticle.getX() > (width + offScreenCalculationSnow()) || currentParticle.getX() < -offScreenCalculationSnow() || currentParticle.age > currentParticle.lifetime) {
						particleList.remove(i);
					} else {
						currentParticle.update();
					}
				} else if (currentParticle.TYPE == 2) {
					if (currentParticle.getX() > (width + offScreenCalculationRain()) || currentParticle.getX() < 0 - offScreenCalculationRain() || currentParticle.getY() > height) {
						particleList.remove(i);
					} else {
						currentParticle.update();
					}

					if (currentParticle.getY() > height - 15 && currentParticle.getY() < height) {
						for (int x = 0; x <= 2; x++) {
							double calculatedX = 0;
							if (randInt(0, 2) == 1) {
								calculatedX = -((currentParticle.getxVelocity() / 2) + randDouble(1, -1));
							} else {
								calculatedX = ((currentParticle.getxVelocity() / 2) + randDouble(1, -1));
							}
							particleList.add(new Particle(currentParticle.getX(), currentParticle.getY(), calculatedX, randDouble(0, -3.0), 1, 2, currentParticle.alpha, 3));
						}
					}
				} else if (currentParticle.TYPE == 3) {
					if (currentParticle.getX() > width || currentParticle.getX() < 0 || currentParticle.age > currentParticle.lifetime) {
						particleList.remove(i);
					} else {
						currentParticle.update();
					}
				}
			} catch (Exception e) {

			}
		}

		if (frames % TARGET_FPS == 0) {
			snowTimer.cancel();
			snowTimer.purge();
			rainTimer.cancel();
			rainTimer.purge();

			snowTimer = new Timer();
			rainTimer = new Timer();

			snowTimer.schedule(new TimerTask() {
				public void run() {
					if (SNOW) {
						particleList.add(new Particle(xSpawnPosition(1), -2, snowX, snowY, 5, randInt(2, 5), (float) randDouble(0.01, 1), 1));
					}
				}
			}, 0, snowDelay);

			rainTimer.schedule(new TimerTask() {
				public void run() {
					if (RAIN) {
						particleList.add(new Particle(xSpawnPosition(2), -2, rainX, rainY, 5, randInt(2, 3), (float) randDouble(0.01, 0.8), 2));
					}
				}
			}, 0, rainDelay);
		}
	}

	public double getXFromAngle(double initialX, int angle, double velocity) {
		return initialX + (velocity * Math.cos(angle));
	}

	public double getYFromAngle(double initialY, int angle, double velocity) {
		return initialY + (velocity * Math.cos(angle));
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setPaint(new GradientPaint(width / 2, height, new Color(0x212121), width / 2, 0, new Color(0x00263B)));
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(Color.black);

		for (int i = 0; i < particleList.size(); i++) {
			try {
				particleList.get(i).paint(g2d);
			} catch (Exception e) {
			}
		}

		g2d.setColor(Color.BLACK);

		if (dragging) {
			g2d.drawLine(initialX, initialY, mouseX, mouseY);
			g2d.setColor(Color.WHITE);
			g2d.drawLine(initialX + 1, initialY + 1, mouseX, mouseY);
		}

		g2d.setColor(Color.green);
		g2d.fill(manualToggle);

		g2d.setColor(Color.blue);
		g2d.fill(rainToggle);

		g2d.setColor(Color.white);
		g2d.fill(snowToggle);

		g2d.setColor(Color.red);
		g2d.fill(clearScreen);

		g2d.setColor(Color.BLACK);

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

//dicks?