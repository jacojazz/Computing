package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Game extends JPanel {
	private static final long serialVersionUID = 1L;
	static boolean running = false;
	static JFrame frame = new JFrame("Music Madness");
	static Game game;
	static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	static int width = gd.getDisplayMode().getWidth();
	static int height = gd.getDisplayMode().getHeight();
	static ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
	static ArrayList<EnemyBullet> enemyBulletList = new ArrayList<EnemyBullet>();
	static ArrayList<TripleShot> tripleList = new ArrayList<TripleShot>();
	static ArrayList<Mine> mineList = new ArrayList<Mine>();
	static ArrayList<Particle> particleList = new ArrayList<Particle>();
	static ArrayList<Rocket> rocketList = new ArrayList<Rocket>();
	static ArrayList<TrailParticle> trailPList = new ArrayList<TrailParticle>();
	static ArrayList<Target> targetList = new ArrayList<Target>();
	static ArrayList<QuestionUI> uiList = new ArrayList<QuestionUI>();
	static Rectangle close = new Rectangle(width - 20, 0, 20, 20);
	static Rectangle bounds = new Rectangle(0, 0, width, height);
	static Rectangle healthHUD = new Rectangle(30, Game.height - 50, 200, 30);
	static Ship ship = new Ship();
	static boolean drawLevel = false, closeClicked;
	static int frames = 0;
	static long fps = 0;
	static boolean pause = false;
	static boolean questionInProgress = false;
	static Clip clip;
	static int score = 0;
	static PauseScreen pauseScreen = new PauseScreen();
	static Font spaceFont;

	public Game() {
		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				ship.keyPressed(e);
			}

			public void keyReleased(KeyEvent e) {
				ship.keyReleased(e);
			}

			public void keyTyped(KeyEvent e) {

			}
		});
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Point p = new Point(e.getX(), e.getY());
				if (pause && !questionInProgress) {
					pauseScreen.mouseClicked(e);
				}
				if (close.contains(p)) {
					System.exit(0);
				}
			}
		});
		setFocusable(true);

		new AI();

		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("music/game.wav"));
			clip = AudioSystem.getClip();
			clip.open(ais);
			Runnable r = new Runnable() {
				public void run() {
					clip.loop(Clip.LOOP_CONTINUOUSLY);
					clip.start();
				}
			};
			SwingUtilities.invokeLater(r);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
			e1.printStackTrace();
		}

		try {
			spaceFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/gui/fonts/imagine_font.ttf")).deriveFont(30f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
	}

	public void update() {
		if (!pause) {
			try {
				ship.update();

				for (Bullet bullet : bulletList) {
					bullet.update();
				}

				for (int rocketIterator = 0; rocketIterator < rocketList.size(); rocketIterator++) {
					Rocket rocket = rocketList.get(rocketIterator);
					rocket.update();
					if (rocket.y <= -10) {
						rocketList.remove(rocketIterator);
					}
					for (int targetRCheck = 0; targetRCheck < targetList.size(); targetRCheck++) {
						if (rocket.rocket.intersects(targetList.get(targetRCheck).rect)) {
							targetList.remove(targetRCheck);
							rocketList.remove(rocketIterator);
							new Explosion(rocket.x, rocket.y, 50, "gui.Game", true);
						}
					}
				}

				for (int targetIterator = 0; targetIterator < targetList.size(); targetIterator++) {
					Target targetCurrent = targetList.get(targetIterator);
					targetCurrent.update();
					if (targetCurrent.health <= 0) {
						targetList.remove(targetIterator);
					}
				}

				for (int tpIterator = 0; tpIterator < trailPList.size(); tpIterator++) {
					TrailParticle tpCurrent = trailPList.get(tpIterator);
					if (tpCurrent.y <= -10) {
						trailPList.remove(tpIterator);
					}
					if (tpCurrent.age > tpCurrent.lifetime) {
						trailPList.remove(tpIterator);
					} else {
						tpCurrent.update();
					}
				}

				for (int mineIterator = 0; mineIterator < mineList.size(); mineIterator++) {
					Mine mCurrent = mineList.get(mineIterator);
					if (mCurrent.ALIVE == false) {
						mineList.remove(mineIterator);
					} else {
						mCurrent.update();
					}
				}

				for (int particleIterator = 0; particleIterator < particleList.size(); particleIterator++) {
					if (particleList.get(particleIterator).age >= particleList.get(particleIterator).lifetime) {
						particleList.remove(particleIterator);
					} else {
						particleList.get(particleIterator).update();
					}

					for (int targetPCheck = 0; targetPCheck < targetList.size(); targetPCheck++) {
						Ellipse2D e = new Ellipse2D.Double(particleList.get(particleIterator).x, particleList.get(particleIterator).y, particleList.get(particleIterator).size, particleList.get(particleIterator).size);
						if (e.intersects(targetList.get(targetPCheck).rect)) {
							targetList.get(targetPCheck).health -= particleList.get(particleIterator).size;
							particleList.remove(particleIterator);
						}
					}
				}

				ListIterator<Bullet> bulletIterator = bulletList.listIterator();
				while (bulletIterator.hasNext()) {
					Bullet bCurrent = bulletIterator.next();
					if (bCurrent.getY() < -10) {
						bulletIterator.remove();
					}
					for (int targetBCheck = 0; targetBCheck < targetList.size(); targetBCheck++) {
						if (bCurrent.rect.intersects(targetList.get(targetBCheck).rect)) {
							targetList.get(targetBCheck).health -= 2;
							bulletIterator.remove();
						}
					}
				}

				ListIterator<EnemyBullet> enemyBulletIterator = enemyBulletList.listIterator();
				while (enemyBulletIterator.hasNext()) {
					EnemyBullet eBCurrent = enemyBulletIterator.next();
					eBCurrent.update();
					if (eBCurrent.getY() > (Game.height + 10)) {
						enemyBulletIterator.remove();
					}
					Ellipse2D s = new Ellipse2D.Double(ship.getX(), ship.getY(), 20, 20);
					if (s.intersects(eBCurrent.rect)) {
						if (!ship.isShieldEnabled()) {
							askQuestion();
							enemyBulletIterator.remove();
						}
					}

				}

				if (ship.getHealth() <= 0) {
					JOptionPane.showMessageDialog(frame, "GAME OVER!");
					System.exit(0);
				}

				if (targetList.size() == 0 && AI.level > 0) {
					particleList.clear();
					bulletList.clear();
					mineList.clear();
					AI.spawnEnemies();
					ship.setHealth(30);
					healthHUD.width = 200;
				}
			} catch (Exception e) {
			}
		}

		for (int uiIterator = 0; uiIterator < uiList.size(); uiIterator++) {
			QuestionUI currentUI = uiList.get(uiIterator);
			currentUI.update();
			if (currentUI.activated) {
				if (currentUI.correct) {
					AudioInputStream correctSoundAIS;
					try {
						correctSoundAIS = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("music/correct.wav"));
						Clip correctSound = AudioSystem.getClip();
						correctSound.open(correctSoundAIS);
						correctSound.start();
					} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
						e.printStackTrace();
					}

					ship.setShieldEnabled(true);
					Timer shieldTimer = new Timer();
					shieldTimer.schedule(new TimerTask() {
						public void run() {
							ship.setShieldEnabled(false);
						}
					}, 2000);

					questionInProgress = false;
					uiList.remove(uiIterator);
					score++;
				} else {
					try {
						AudioInputStream incorrectSoundAIS = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("music/incorrect.wav"));
						Clip incorrectSound = AudioSystem.getClip();
						incorrectSound.open(incorrectSoundAIS);
						incorrectSound.start();
					} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
						e.printStackTrace();
					}

					ship.setShieldEnabled(true);
					Timer shieldTimer = new Timer();
					shieldTimer.schedule(new TimerTask() {
						public void run() {
							ship.setShieldEnabled(false);
						}
					}, 2000);
					questionInProgress = false;
					uiList.remove(uiIterator);
					ship.setHealth(ship.getHealth() - 3);
					healthHUD.width -= 20;
				}
			}
		}

		if (Game.pause && !Game.questionInProgress) {
			pauseScreen.update();
		}
	}

	public void askQuestion() {
		pause = true;
		questionInProgress = true;
		uiList.add(new QuestionUI(QuestionManager.getRandomQAO(), game));
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if (ship.isShieldEnabled()) {
			g2d.setColor(new Color(0x9CE6FF));
		} else {
			g2d.setColor(Color.RED);
		}
		g2d.fill(healthHUD);
		g2d.setColor(Color.BLACK);

		Font defaultFont = g2d.getFont();

		g2d.setFont(spaceFont);
		FontMetrics fmSF = g2d.getFontMetrics(spaceFont);
		g2d.drawString(Integer.toString(ship.getHealth()), healthHUD.x + ((healthHUD.width / 2) - (fmSF.stringWidth(Integer.toString(ship.getHealth())) / 2)), healthHUD.y + (healthHUD.height / 2) + (fmSF.getHeight() / 2));

		g2d.setFont(defaultFont);

		ship.paint(g2d);

		for (Bullet bullet : bulletList) {
			bullet.paint(g2d);
		}

		for (EnemyBullet eBullet : enemyBulletList) {
			eBullet.paint(g2d);
		}

		for (Mine mine : mineList) {
			mine.paint(g2d);
		}

		for (Rocket rocket : rocketList) {
			rocket.paint(g2d);
		}

		for (int z = 0; z < trailPList.size(); z++) {
			trailPList.get(z).paint(g2d);
		}

		for (int i = 0; i < particleList.size(); i++) {
			particleList.get(i).paint(g2d);
		}

		for (int p = 0; p < targetList.size(); p++) {
			targetList.get(p).paint(g2d);
		}

		if (drawLevel) {
			try {
				String texty = "Level Complete! Starting level " + Integer.toString(AI.level - 1) + "...";
				Font font = new Font("System", Font.PLAIN, 50);
				FontMetrics fm = g2d.getFontMetrics(font);
				int x = (Game.width - fm.stringWidth(texty)) / 2;
				int y = (fm.getAscent() + (Game.height - (fm.getAscent() + fm.getDescent())) / 2);
				g2d.setFont(font);
				g2d.drawString(texty, x, y);
			} finally {
				g2d.dispose();
			}
		}

		g2d.drawString("Bullets: " + Integer.toString(bulletList.size()), 0, 10);
		g2d.drawString("Triple Shot: " + Integer.toString(ship.getAmmoTripleShot()), 0, 20);
		g2d.drawString("Mines: " + Integer.toString(ship.getAmmoMine()), 0, 30);
		g2d.drawString("Rockets: " + Integer.toString(ship.getAmmoRocket()), 0, 40);
		g2d.drawString("Enemies: " + Integer.toString(targetList.size()), 0, 50);
		g2d.drawString("Level: " + Integer.toString(AI.level), 0, 60);
		g2d.drawString("Frames: " + Integer.toString(frames), 0, 70);
		g2d.drawString("FPS: " + Long.toString(fps), 0, 80);
		g2d.drawString("Score: " + Integer.toString(score), 0, 90);

		for (int uiIterator = 0; uiIterator < uiList.size(); uiIterator++) {
			QuestionUI currentUI = uiList.get(uiIterator);
			currentUI.paint(g2d);
		}

		if (pause && !questionInProgress) {
			pauseScreen.paint(g2d);
		}

		g2d.setColor(Color.RED);
		g2d.fill(close);
	}

	public static void runGame() throws InterruptedException {
		game = new Game();
		frame.setSize(width, height);
		frame.add(game);
		frame.setUndecorated(true);
		frame.setBackground(Color.BLACK);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		long lastLoopTime = System.nanoTime();
		final int TARGET_FPS = 120;
		final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
		long lastFpsTime = 0;
		long gameTime;

		running = true;

		while (running) {
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

}
