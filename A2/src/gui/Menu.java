package gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Menu extends JPanel {

	private static final long serialVersionUID = 1L;
	static JFrame frame = new JFrame("Music Madness");
	Rectangle start = new Rectangle((width / 2) - 100, 150, 200, 50);
	Rectangle questionManager = new Rectangle((width / 2) - 90, 230, 180, 50);
	Rectangle exit = new Rectangle((width / 2) - 90, 310, 180, 50);
	static int width = 640;
	static int height = 480;
	boolean mouseClicked = false;
	static int x, y;
	static Font spaceTitleFont;
	static Font spaceFont, spaceSmallFont;
	static int frames = 0;
	static int frameLastExplosion = 0;
	final Font defaultFont = new JLabel().getFont();
	static ArrayList<Particle> particleList = new ArrayList<Particle>();
	static QuestionManager qM = new QuestionManager();
	static Clip clip, beep, beep2, beep3;
	static ArrayList<String> questionArray = (ArrayList<String>) QuestionManager.getQuestions();
	static boolean startClicked = false, stopExplosions = false;

	public Menu() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					mouseClicked = true;
					x = e.getX();
					y = e.getY();
					new Explosion(x, y, 50, "gui.Menu", false);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					mouseClicked = false;
				}
			}
		});

		try {
			spaceTitleFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/gui/fonts/cubic.ttf")).deriveFont(50f);
			spaceFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/gui/fonts/imagine_font.ttf")).deriveFont(30f);
			spaceSmallFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/gui/fonts/imagine_font.ttf")).deriveFont(20f);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}

		try {
			AudioInputStream musicAIS = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("music/menu.wav"));
			clip = AudioSystem.getClip();
			clip.open(musicAIS);
			Runnable r = new Runnable() {
				public void run() {
					clip.loop(Clip.LOOP_CONTINUOUSLY);
					clip.start();
				}
			};
			SwingUtilities.invokeLater(r);

			AudioInputStream beepAIS = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("music/minebeep.wav"));
			beep = AudioSystem.getClip();
			beep.open(beepAIS);

			AudioInputStream beep2AIS = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("music/minebeep.wav"));
			beep2 = AudioSystem.getClip();
			beep2.open(beep2AIS);

			AudioInputStream beep3AIS = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("music/minebeep.wav"));
			beep3 = AudioSystem.getClip();
			beep3.open(beep3AIS);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e1) {
			e1.printStackTrace();
		}
	}

	public static void runMenu() throws InterruptedException {
		Menu menu = new Menu();

		frame.setSize(width, height);
		frame.add(menu);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		while (true) {
			menu.repaint();
			menu.update();
			Thread.sleep(16);
		}
	}

	public void update() {
		frames++;
		if (mouseClicked) {
			Point p = new Point(x, y);
			if (start.contains(p)) {
				stopExplosions = true;
				mouseClicked = false;
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(-10.0f); // Reduce volume by 10 decibels.

				do {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} while (!(gainControl.getValue() == -10.0f));
				Timer t = new Timer();
				beep.start();
				t.schedule(new TimerTask() {
					public void run() {
						beep.stop();
						beep2.start();
					}
				}, 1000);
				t.schedule(new TimerTask() {
					public void run() {
						beep2.stop();
						beep3.start();
					}
				}, 2000);
				t.schedule(new TimerTask() {
					public void run() {
						beep3.stop();
						startClicked = true;
						new Explosion(width / 2, height / 2, 50, "gui.Menu", true);
						for (int eX = 0; eX < 50; eX++) {
							Random rand = new Random();
							new Explosion(rand.nextInt(width), rand.nextInt(height), 50, "gui.Menu", false);
						}
						clip.stop();
					}
				}, 3000);
				t.schedule(new TimerTask() {
					public void run() {
						frame.dispose();
						try {
							Game.runGame();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}, 4000);
			} else if (questionManager.contains(p)) {
				QuestionEditor.runStringEditor(questionArray);
				mouseClicked = false;
			} else if (exit.contains(p)) {
				System.exit(0);
				mouseClicked = false;
			}
		}

		Random rand = new Random();
		if (!stopExplosions) {
			if (frames > (frameLastExplosion + rand.nextInt(10))) {
				frameLastExplosion = frames;
				new Explosion(rand.nextInt(width), rand.nextInt(height), 50, getClass().getName(), false);
			}
		}

		for (int i = 0; i < particleList.size(); i++) {
			if (particleList.get(i).age >= particleList.get(i).lifetime) {
				particleList.remove(i);
			} else {
				particleList.get(i).update();
			}
		}
	}

	private AlphaComposite makeComposite(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(Color.BLACK);

		if (!startClicked) {
			Composite originalComposite = g2d.getComposite();
			g2d.setComposite(makeComposite(0.3f));
			for (int i = 0; i < particleList.size(); i++) {
				particleList.get(i).paint(g2d);
			}
			g2d.setComposite(originalComposite);
		}

		g2d.setColor(Color.BLACK);

		g2d.setFont(spaceTitleFont);
		FontMetrics fmT = g2d.getFontMetrics(spaceTitleFont);

		String titleText = "Music Madness";
		g2d.drawString(titleText, (width / 2) - (fmT.stringWidth(titleText) / 2), 80);

		g2d.setColor(Color.BLACK);
		g2d.fill(start);
		g2d.fill(questionManager);
		g2d.fill(exit);

		FontMetrics fm = g2d.getFontMetrics(spaceFont);
		FontMetrics fmS = g2d.getFontMetrics(spaceSmallFont);

		g2d.setFont(spaceFont);

		String startText = "Start";
		g2d.setColor(Color.WHITE);
		g2d.drawString(startText, start.x + (start.width / 2) - (fm.stringWidth(startText) / 2), (start.y + (start.height / 2)) + (fm.getHeight() / 2));

		g2d.setFont(spaceSmallFont);

		String questionText = "Questions";
		g2d.setColor(Color.WHITE);
		g2d.drawString(questionText, questionManager.x + (questionManager.width / 2) - (fmS.stringWidth(questionText) / 2), (questionManager.y + (questionManager.height / 2)) + (fmS.getHeight() / 2));

		g2d.setFont(spaceSmallFont);

		String exitText = "Exit";
		g2d.setColor(Color.WHITE);
		g2d.drawString(exitText, exit.x + (exit.width / 2) - (fmS.stringWidth(exitText) / 2), (exit.y + (exit.height / 2)) + (fmS.getHeight() / 2));

		if (startClicked) {
			Composite originalComposite = g2d.getComposite();
			g2d.setComposite(makeComposite(1f));
			for (int i = 0; i < particleList.size(); i++) {
				particleList.get(i).paint(g2d);
			}
			g2d.setComposite(originalComposite);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		runMenu();
	}
}
