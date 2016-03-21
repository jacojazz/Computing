package engine.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import math.geom2d.Point2D;
import math.geom2d.polygon.Rectangle2D;
import engine.utils.Reference;

public class Menu {

	Point2D position = new Point2D(10, 10);
	int width = 200;
	int height = 300;

	boolean visible = true;

	Point2D mouseRelation;

	Font textFont;

	boolean dragging = false;

	Point p = new Point(0, 0);

	Rectangle2D baseRect;

	Rectangle2D minRect;
	Rectangle2D minHandler;

	Rectangle2D moveRect;

	Rectangle2D manualRect;

	Rectangle2D rainPlus;
	Rectangle2D rainRect;
	Rectangle2D rainMinus;

	Rectangle2D clearParticles;

	Rectangle2D exitRect;

	public Menu() {
		try {
			textFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/engine/res/fonts/Abel-Regular.ttf")).deriveFont(15f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (minHandler.contains(Game.mouse)) {
				visible = !visible;
			} else if (manualRect.contains(Game.mouse)) {
			} else if (rainPlus.contains(Game.mouse)) {
			} else if (rainRect.contains(Game.mouse)) {
			} else if (rainMinus.contains(Game.mouse)) {
			} else if (clearParticles.contains(Game.mouse)) {
			} else if (exitRect.contains(Game.mouse)) {
				System.exit(0);
			}
		}

	}

	public void mousePressed(MouseEvent e) {
		if (moveRect.contains(Game.mouse)) {
			dragging = true;

			mouseRelation = Game.mouse.minus(new Point2D(baseRect.getX(), baseRect.getY()));
		}
	}

	public void mouseReleased(MouseEvent e) {
		dragging = false;
	}

	public void update() {
		baseRect = new Rectangle2D(position.getX(), position.getY(), width, height);

		minRect = new Rectangle2D((position.getX() + width) - 18, position.getY() + 15, 16, 3);
		minHandler = new Rectangle2D((position.getX() + width) - 20, position.getY(), 20, 20);

		moveRect = new Rectangle2D(position.getX(), position.getY(), width, height - 280);

		manualRect = new Rectangle2D(position.getX() + 10, position.getY() + 40, width - 20, height - 280);

		rainPlus = new Rectangle2D(position.getX() + 10, position.getY() + 70, width / 10, width / 10);
		rainRect = new Rectangle2D(position.getX() + 35, position.getY() + 70, width - 70, height - 280);
		rainMinus = new Rectangle2D(position.getX() + 170, position.getY() + 70, width / 10, width / 10);

		clearParticles = new Rectangle2D(position.getX() + 10, position.getY() + 220, width - 20, height - 280);

		exitRect = new Rectangle2D(position.getX() + 10, position.getY() + 270, width - 20, height - 280);

		if (dragging == true) {
			position = Game.mouse.minus(mouseRelation);
		}
	}

	public void paint(Graphics2D g2d) {
		if (visible) {
			g2d.setColor(new Color(20, 20, 20, 220));
			baseRect.fill(g2d);
		}

		g2d.setColor(Color.GRAY);
		moveRect.fill(g2d);

		FontMetrics fmT = g2d.getFontMetrics(textFont);
		g2d.setFont(textFont);
		g2d.setColor(Color.BLACK);
		g2d.drawString(Reference.NAME + " " + Reference.VERSION + " Options", (int) moveRect.getX() + 5, (int) (moveRect.getY() + (moveRect.getHeight() / 2)) + (fmT.getHeight() / 4));

		g2d.setColor(Color.BLACK);
		minRect.fill(g2d);

		if (visible) {
			g2d.setColor(new Color(51, 51, 51, 255));
			manualRect.fill(g2d);

			g2d.setColor(Color.GRAY);
			g2d.drawString("Manual Mode", (int) (manualRect.getX() + (manualRect.getWidth() / 2) - (fmT.stringWidth("Manual Mode") / 2)), (int) (manualRect.getY() + (manualRect.getHeight() / 2)) + (fmT.getHeight() / 4));

			g2d.setColor(new Color(51, 51, 51, 255));
			rainPlus.fill(g2d);
			rainRect.fill(g2d);
			rainMinus.fill(g2d);

			g2d.setColor(Color.GRAY);
			g2d.drawString("+", (int) (rainPlus.getX() + (rainPlus.getWidth() / 2) - (fmT.stringWidth("+") / 2)), (int) (rainPlus.getY() + (rainPlus.getHeight() / 2)) + (fmT.getHeight() / 4));
			g2d.drawString("Rain Toggle", (int) (rainRect.getX() + (rainRect.getWidth() / 2) - (fmT.stringWidth("Rain Toggle") / 2)), (int) (rainRect.getY() + (rainRect.getHeight() / 2)) + (fmT.getHeight() / 4));
			g2d.drawString("-", (int) (rainMinus.getX() + (rainMinus.getWidth() / 2) - (fmT.stringWidth("-") / 2)), (int) (rainMinus.getY() + (rainMinus.getHeight() / 2)) + (fmT.getHeight() / 4));

			g2d.setColor(new Color(51, 51, 51, 255));
			clearParticles.fill(g2d);

			g2d.setColor(Color.GRAY);
			g2d.drawString("Clear Particles", (int) (clearParticles.getX() + (clearParticles.getWidth() / 2) - (fmT.stringWidth("Clear Particles") / 2)), (int) (clearParticles.getY() + (clearParticles.getHeight() / 2)) + (fmT.getHeight() / 4));

			g2d.setColor(new Color(0x521616));
			exitRect.fill(g2d);

			g2d.setColor(Color.WHITE);
			g2d.drawString("Exit", (int) (exitRect.getX() + (exitRect.getWidth() / 2) - (fmT.stringWidth("Exit") / 2)), (int) (exitRect.getY() + (exitRect.getHeight() / 2)) + (fmT.getHeight() / 4));
		}
	}
}
