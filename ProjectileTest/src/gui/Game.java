package gui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel {
	private static final long serialVersionUID = 636997359079194521L;
	static int frames = 0;
	static JFrame frame = new JFrame(Reference.NAME + " " + Reference.VERSION);
	static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	static int width = gd.getDisplayMode().getWidth();
	static int height = gd.getDisplayMode().getHeight();
	static ArrayList<Particle> particleList = new ArrayList<Particle>();
	static int TARGET_FPS = 240;
	static int initialX, initialY, distanceX, distanceY;
	static boolean RAIN = false;
	static boolean SNOW = false;
	static boolean MANUAL = false;
	static boolean NIGHT = true;
	static int rainX = 0, rainY = 10;
	static int snowX = 0, snowY = 3;
	static int rainDelay = 10;
	static int snowDelay = 10;
	static boolean dragging = false;
	static int mouseX, mouseY;
	static Timer snowTimer, rainTimer;
	static Image deathStar;
	static Image deathStarResize;

	static double yGRAVITY = 0.10; // 0.10
	static double xGravity = 0.18; // 16:9 Calculated
	static int gravityMode = 0; // 0 - Normal, 1 - Anti, 2 - Orbital
	static String gravityModeString;

	static int snowCount = 0;
	static int rainCount = 0;
	static int ppsNumber = 0;

	Menu menu = new Menu();

	Game() {
		addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				menu.mouseClicked(e);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				menu.mousePressed(e);

				if (MANUAL) {
					dragging = true;
					initialX = e.getX();
					initialY = e.getY();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				menu.mouseReleased(e);

				if (MANUAL) {
					dragging = false;
					distanceX = initialX - e.getX();
					distanceY = initialY - e.getY();
					if (!menu.manualRect.contains(new Point(e.getX(), e.getY()))) {
						particleList.add(new Particle(initialX, initialY, distanceX / 20, distanceY / 20, 5, 8, 1f, 0));
					}
				}
			}
		});

		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				menu.mouseDragged(e);

				mouseX = e.getX();
				mouseY = e.getY();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				menu.mouseMoved(e);

				mouseX = e.getX();
				mouseY = e.getY();
			}
		});

		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyTyped(KeyEvent e) {
			}
		});

		setFocusable(true);

		rainTimer = new Timer();
		snowTimer = new Timer();
		
		try {
			deathStar = ImageIO.read(new File("src/gui/images/deathStar.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		deathStarResize = deathStar.getScaledInstance(128, 128, Image.SCALE_DEFAULT);
	}

	public double offScreenCalculationRain() {

		double rainXVelocity = Math.abs(rainX);
		double rainYVelocity = Math.abs(rainY);

		double yV = Math.sqrt(Math.pow(rainYVelocity, 2) + (2 * yGRAVITY * height));
		double t = (yV - rainYVelocity) / yGRAVITY;
		double xS = ((rainXVelocity + rainXVelocity) / 2) * t;

		return xS;
	}

	public double offScreenCalculationSnow() {
		double snowXVelocity = Math.abs(snowX);
		double snowYVelocity = Math.abs(snowY);

		double yV = Math.sqrt(Math.pow(snowYVelocity, 2) + (2 * yGRAVITY * height));
		double t = (yV - snowYVelocity) / yGRAVITY;
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
		switch (gravityMode) {
		case 0:
			gravityModeString = "Normal";
			break;
		case 1:
			gravityModeString = "Anti";
			break;
		case 2:
			gravityModeString = "Orbital";
			break;
		}

		if (particleList.size() > 2000) {
			for (int particleRemove = 0; particleRemove <= 500; particleRemove++) {
				particleList.remove(particleRemove);
			}
		}

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
						for (int x = 0; x <= 3; x++) {
							double calculatedX = 0;
							if (randInt(0, 2) == 1) {
								calculatedX = -((currentParticle.getxVelocity() / 2) + randDouble(1, -1));
							} else {
								calculatedX = ((currentParticle.getxVelocity() / 2) + randDouble(1, -1));
							}
							particleList.add(new Particle(currentParticle.getX(), currentParticle.getY(), calculatedX, randDouble(0, -2.0), 1, 2, currentParticle.alpha, 3));
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
				@Override
				public void run() {
					if (SNOW) {
						particleList.add(new Particle(xSpawnPosition(1), -2, snowX, snowY, 10, randInt(2, 5), (float) randDouble(0.01, 1), 1));
						snowCount++;
					}
				}
			}, 0, snowDelay);

			rainTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					if (RAIN) {
						particleList.add(new Particle(xSpawnPosition(2), -2, rainX, rainY, 5, randInt(2, 3), (float) randDouble(0.01, 0.8), 2));
					}
				}
			}, 0, rainDelay);
		}

		menu.update();
	}

	public static int getAngle() {
		return (5 * rainX);
	}

	public double getXFromAngle(double initialX, int angle, double velocity) {
		return initialX + (velocity * Math.cos(angle));
	}

	public double getYFromAngle(double initialY, int angle, double velocity) {
		return initialY + (velocity * Math.cos(angle));
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if (NIGHT == true) {
			g2d.setPaint(new GradientPaint(width / 2, height, new Color(0x212121), width / 2, 0, new Color(0x00263B)));
		} else {
			g2d.setPaint(new GradientPaint(width / 2, height, new Color(0x305e91), width / 2, 0, new Color(0xC8ACF1)));
		}
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
		
		if (gravityMode == 2) {
			g2d.drawImage(deathStarResize, (width / 2) - 64, (height / 2) - 64, 128, 128, null);
		}

		g2d.setColor(Color.WHITE);

		FontMetrics fm = g2d.getFontMetrics();
		g2d.setColor(Color.WHITE);
		g2d.drawString(Integer.toString(particleList.size()), menu.baseRect.x + (menu.baseRect.width / 2) - (fm.stringWidth(Integer.toString(particleList.size())) / 2), menu.baseRect.y + menu.moveRect.height + fm.getHeight());

		g2d.setColor(Color.BLACK);

		menu.paint(g2d);
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