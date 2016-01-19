package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class Menu {

	int x = 10;
	int y = 10;
	int width = 200;
	int height = 300;

	boolean visible = true;

	int mouseX;
	int mouseY;
	int mouseXChange;
	int mouseYChange;
	int mouseRelationX;
	int mouseRelationY;

	Font titleFont, textFont;

	boolean dragging = false;

	Point p = new Point(0, 0);

	Rectangle baseRect;

	Rectangle minRect;
	Rectangle minHandler;

	Rectangle moveRect;

	Rectangle manualRect;

	Rectangle rainPlus;
	Rectangle rainRect;
	Rectangle rainMinus;

	Rectangle snowPlus;
	Rectangle snowRect;
	Rectangle snowMinus;

	Rectangle changeAngle;

	Rectangle dayNightToggle;
	
	Rectangle gravityToggle;

	Rectangle exitRect;

	public Menu() {
		try {
			titleFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/gui/fonts/Abel-Regular.ttf")).deriveFont(15f);
			textFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/gui/fonts/Abel-Regular.ttf")).deriveFont(15f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	public void mouseClicked(MouseEvent e) {
		p = new Point(e.getX(), e.getY());

		if (minHandler.contains(p)) {
			visible = !visible;
		} else if (manualRect.contains(p)) {
			Game.MANUAL = !Game.MANUAL;
		} else if (rainPlus.contains(p)) {
			if (Game.rainDelay > 1) {
				Game.rainDelay--;
			}
		} else if (rainRect.contains(p)) {
			Game.RAIN = !Game.RAIN;
		} else if (rainMinus.contains(p)) {
			Game.rainDelay++;
		} else if (snowPlus.contains(p)) {
			if (Game.snowDelay > 1) {
				Game.rainDelay--;
			}
		} else if (snowRect.contains(p)) {
			Game.SNOW = !Game.SNOW;
		} else if (snowMinus.contains(p)) {
			Game.snowDelay++;
		} else if (changeAngle.contains(p)) {
			int xVariation = Game.randInt(-5, 5);
			Game.rainX = xVariation;
			Game.snowX = xVariation;
		} else if (dayNightToggle.contains(p)) {
			Game.NIGHT = !Game.NIGHT;
		} else if (gravityToggle.contains(p)) {
			if(Game.GRAVITY != 0) {
				Game.GRAVITY = 0;
			} else {
				Game.GRAVITY = 0.10;
			}
		} else if (exitRect.contains(p)) {
			System.exit(0);
		}

	}

	public void mousePressed(MouseEvent e) {
		p = new Point(e.getX(), e.getY());

		if (moveRect.contains(p)) {
			dragging = true;

			mouseRelationX = e.getX() - baseRect.x;
			mouseRelationY = e.getY() - baseRect.y;
		}
	}

	public void mouseReleased(MouseEvent e) {
		dragging = false;
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void update() {
		baseRect = new Rectangle(x, y, width, height);

		minRect = new Rectangle((x + width) - 18, y + 15, 16, 3);
		minHandler = new Rectangle((x + width) - 20, y, 20, 20);

		moveRect = new Rectangle(x, y, width, height - 280);

		manualRect = new Rectangle(x + 10, y + 40, width - 20, height - 280);

		rainPlus = new Rectangle(x + 10, y + 70, width / 10, width / 10);
		rainRect = new Rectangle(x + 35, y + 70, width - 70, height - 280);
		rainMinus = new Rectangle(x + 170, y + 70, width / 10, width / 10);

		snowPlus = new Rectangle(x + 10, y + 100, width / 10, width / 10);
		snowRect = new Rectangle(x + 35, y + 100, width - 70, height - 280);
		snowMinus = new Rectangle(x + 170, y + 100, width / 10, width / 10);

		changeAngle = new Rectangle(x + 10, y + 130, width - 20, height - 280);

		dayNightToggle = new Rectangle(x + 10, y + 160, width - 20, height - 280);
		
		gravityToggle = new Rectangle(x + 10, y + 190, width - 20, height - 280);

		exitRect = new Rectangle(x + 10, y + 270, width - 20, height - 280);

		if (dragging == true) {
			x = mouseX - mouseRelationX;
			y = mouseY - mouseRelationY;
		}

	}

	public void paint(Graphics2D g2d) {
		if (visible) {
			g2d.setColor(new Color(20, 20, 20, 220));
			g2d.fill(baseRect);
		}

		g2d.setColor(Color.GRAY);
		g2d.fill(moveRect);

		FontMetrics fmT = g2d.getFontMetrics(textFont);
		g2d.setFont(titleFont);
		g2d.setColor(Color.BLACK);
		g2d.drawString(Reference.NAME + " " + Reference.VERSION + " Options", moveRect.x + 5, (moveRect.y + (moveRect.height / 2)) + (fmT.getHeight() / 4));

		g2d.setColor(Color.BLACK);
		g2d.fill(minRect);

		if (visible) {
			g2d.setColor(new Color(51, 51, 51, 220));
			g2d.fill(manualRect);
			
			g2d.setColor(Color.GRAY);
			g2d.drawString("Manual Mode", manualRect.x + (manualRect.width / 2) - (fmT.stringWidth("Manual Mode") / 2), (manualRect.y + (manualRect.height / 2)) + (fmT.getHeight() / 4));
			

			g2d.setColor(new Color(51, 51, 51, 220));
			g2d.fill(rainPlus);
			g2d.fill(rainRect);
			g2d.fill(rainMinus);
			
			g2d.setColor(Color.GRAY);
			g2d.drawString("+", rainPlus.x + (rainPlus.width / 2) - (fmT.stringWidth("+") / 2), (rainPlus.y + (rainPlus.height / 2)) + (fmT.getHeight() / 4));
			g2d.drawString("Rain Toggle (" + Game.rainDelay + ")", rainRect.x + (rainRect.width / 2) - (fmT.stringWidth("Rain Toggle (" + Game.rainDelay + ")") / 2), (rainRect.y + (rainRect.height / 2)) + (fmT.getHeight() / 4));
			g2d.drawString("-", rainMinus.x + (rainMinus.width / 2) - (fmT.stringWidth("-") / 2), (rainMinus.y + (rainMinus.height / 2)) + (fmT.getHeight() / 4));
			

			g2d.setColor(new Color(51, 51, 51, 220));
			g2d.fill(snowPlus);
			g2d.fill(snowRect);
			g2d.fill(snowMinus);
			
			g2d.setColor(Color.GRAY);
			g2d.drawString("+", snowPlus.x + (snowPlus.width / 2) - (fmT.stringWidth("+") / 2), (snowPlus.y + (snowPlus.height / 2)) + (fmT.getHeight() / 4));
			g2d.drawString("Snow Toggle (" + Game.snowDelay + ")", snowRect.x + (snowRect.width / 2) - (fmT.stringWidth("Snow Toggle (" + Game.snowDelay + ")") / 2), (snowRect.y + (snowRect.height / 2)) + (fmT.getHeight() / 4));
			g2d.drawString("-", snowMinus.x + (snowMinus.width / 2) - (fmT.stringWidth("-") / 2), (snowMinus.y + (snowMinus.height / 2)) + (fmT.getHeight() / 4));

			g2d.setColor(new Color(51, 51, 51, 220));
			g2d.fill(changeAngle);
			
			g2d.setColor(Color.GRAY);
			g2d.drawString("Change Angle", changeAngle.x + (changeAngle.width / 2) - (fmT.stringWidth("Change Angle") / 2), (changeAngle.y + (changeAngle.height / 2)) + (fmT.getHeight() / 4));

			g2d.setColor(new Color(51, 51, 51, 220));
			g2d.fill(dayNightToggle);
			
			g2d.setColor(Color.GRAY);
			g2d.drawString("Day/Night Toggle", dayNightToggle.x + (dayNightToggle.width / 2) - (fmT.stringWidth("Day/Night Toggle") / 2), (dayNightToggle.y + (dayNightToggle.height / 2)) + (fmT.getHeight() / 4));

			g2d.setColor(new Color(51, 51, 51, 220));
			g2d.fill(gravityToggle);
			
			g2d.setColor(Color.GRAY);
			g2d.drawString("Gravity Toggle", gravityToggle.x + (gravityToggle.width / 2) - (fmT.stringWidth("Gravity Toggle") / 2), (gravityToggle.y + (gravityToggle.height / 2)) + (fmT.getHeight() / 4));
			
			g2d.setColor(new Color(0x521616));
			g2d.fill(exitRect);
			
			g2d.setColor(Color.WHITE);
			g2d.drawString("Exit", exitRect.x + (exitRect.width / 2) - (fmT.stringWidth("Exit") / 2), (exitRect.y + (exitRect.height / 2)) + (fmT.getHeight() / 4));
		}
	}

}
