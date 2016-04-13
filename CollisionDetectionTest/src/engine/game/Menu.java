package engine.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.utils.Reference;
import math.geom2d.Point2D;
import math.geom2d.polygon.Rectangle2D;

public class Menu {
	Point2D position = new Point2D(10, 10);
	int width = 200;
	int height = 300;
	boolean visible = true;
	Point2D mouseRelation;
	Font textFont;
	boolean dragging = false;
	boolean toolsPopOut = false;

	Rectangle2D baseRect = new Rectangle2D(position.getX(), position.getY(), width, height);
	Rectangle2D minRect = new Rectangle2D((position.getX() + width) - 18, position.getY() + 15, 16, 3);
	Rectangle2D minHandler = new Rectangle2D((position.getX() + width) - 20, position.getY(), 20, 20);
	Rectangle2D moveRect = new Rectangle2D(position.getX(), position.getY(), width, height - 280);
	Rectangle2D sizeMinus = new Rectangle2D(position.getX() + 10, position.getY() + 40, 20, 20);
	Rectangle2D sizeRect = new Rectangle2D(position.getX() + 35, position.getY() + 40, width - 70, height - 280);
	Rectangle2D sizePlus = new Rectangle2D(position.getX() + 170, position.getY() + 40, 20, 20);
	Rectangle2D floodRect = new Rectangle2D(position.getX() + 10, position.getY() + 70, width - 20, height - 280);
	Rectangle2D gravityRect = new Rectangle2D(position.getX() + 10, position.getY() + 100, width - 20, height - 280);
	Rectangle2D toolsRect = new Rectangle2D(position.getX() + 10, position.getY() + 130, width - 20, height - 280);
	Rectangle2D toolsBaseRect = new Rectangle2D(position.getX() + width + 10, position.getY() + 120, 145, height - 260);
	Rectangle2D toolsParticle = new Rectangle2D(position.getX() + width + 15, position.getY() + 125, 30, 30);
	Rectangle2D toolsLine = new Rectangle2D(position.getX() + width + 50, position.getY() + 125, 30, 30);
	Rectangle2D toolsGravity = new Rectangle2D(position.getX() + width + 85, position.getY() + 125, 30, 30);
	Rectangle2D toolsRepulsor = new Rectangle2D(position.getX() + width + 120, position.getY() + 125, 30, 30);
	Rectangle2D debugRect = new Rectangle2D(position.getX() + 10, position.getY() + 160, width - 20, height - 280);
	Rectangle2D clearLines = new Rectangle2D(position.getX() + 10, position.getY() + 190, width - 20, height - 280);
	Rectangle2D clearParticles = new Rectangle2D(position.getX() + 10, position.getY() + 220, width - 20, height - 280);
	Rectangle2D exitRect = new Rectangle2D(position.getX() + 10, position.getY() + 270, width - 20, height - 280);

	Image particleImage, lineImage, gravityImage, repulsorImage;

	public Menu() {
		try {
			textFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/engine/res/fonts/Abel-Regular.ttf")).deriveFont(15f);
			particleImage = ImageIO.read(new File("src/engine/res/images/particle.png"));
			lineImage = ImageIO.read(new File("src/engine/res/images/line.png"));
			gravityImage = ImageIO.read(new File("src/engine/res/images/inwards.png"));
			repulsorImage = ImageIO.read(new File("src/engine/res/images/outwards.png"));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (minHandler.contains(Game.mouse)) {
				visible = !visible;
			} else if (sizePlus.contains(Game.mouse)) {
				Game.manualSize += 2;
			} else if (sizeRect.contains(Game.mouse)) {
				Game.manualSize = 40;
			} else if (sizeMinus.contains(Game.mouse)) {
				if (Game.manualSize >= 12) {
					Game.manualSize -= 2;
				}
			} else if (floodRect.contains(Game.mouse)) {
				Game.flood = !Game.flood;
			} else if (gravityRect.contains(Game.mouse)) {
				if (Game.gravityType < 2) {
					Game.gravityType++;
				} else if (Game.gravityType == 2) {
					Game.gravityType = 1;
				}

				switch (Game.gravityType) {
				case 1:
					Game.lList.clear();
					Game.lList.add(Game.floor);
					Game.lList.add(Game.leftWall);
					Game.lList.add(Game.rightWall);
					Game.lList.add(Game.roof);
					break;
				case 2:
					Game.lList.clear();
					break;
				default:
					System.out.println("Unknown case in Menu/MouseClicked switch statement!");
					break;
				}
			} else if (toolsRect.contains(Game.mouse)) {
				toolsPopOut = !toolsPopOut;
			} else if (debugRect.contains(Game.mouse)) {
				Game.debug = !Game.debug;
			} else if (clearParticles.contains(Game.mouse)) {
				Game.pList.clear();
			} else if (clearLines.contains(Game.mouse)) {
				Game.lList.clear();
				Game.lList.add(Game.floor);
				Game.lList.add(Game.leftWall);
				Game.lList.add(Game.rightWall);
				Game.lList.add(Game.roof);
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
		sizeMinus = new Rectangle2D(position.getX() + 10, position.getY() + 40, 20, 20);
		sizeRect = new Rectangle2D(position.getX() + 35, position.getY() + 40, width - 70, height - 280);
		sizePlus = new Rectangle2D(position.getX() + 170, position.getY() + 40, 20, 20);
		floodRect = new Rectangle2D(position.getX() + 10, position.getY() + 70, width - 20, height - 280);
		gravityRect = new Rectangle2D(position.getX() + 10, position.getY() + 100, width - 20, height - 280);
		toolsRect = new Rectangle2D(position.getX() + 10, position.getY() + 130, width - 20, height - 280);
		toolsBaseRect = new Rectangle2D(position.getX() + width + 10, position.getY() + 120, 145, height - 260);
		toolsParticle = new Rectangle2D(position.getX() + width + 15, position.getY() + 125, 30, 30);
		toolsLine = new Rectangle2D(position.getX() + width + 50, position.getY() + 125, 30, 30);
		toolsGravity = new Rectangle2D(position.getX() + width + 85, position.getY() + 125, 30, 30);
		toolsRepulsor = new Rectangle2D(position.getX() + width + 120, position.getY() + 125, 30, 30);
		debugRect = new Rectangle2D(position.getX() + 10, position.getY() + 160, width - 20, height - 280);
		clearLines = new Rectangle2D(position.getX() + 10, position.getY() + 190, width - 20, height - 280);
		clearParticles = new Rectangle2D(position.getX() + 10, position.getY() + 220, width - 20, height - 280);
		exitRect = new Rectangle2D(position.getX() + 10, position.getY() + 270, width - 20, height - 280);

		if (dragging == true) {
			position = Game.mouse.minus(mouseRelation);
		}
	}

	public void paint(Graphics2D g2d) {
		if (visible) {
			g2d.setColor(new Color(20, 20, 20, 250));
			baseRect.fill(g2d);

			if (toolsPopOut) {
				toolsBaseRect.fill(g2d);
			}
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
			g2d.setColor(new Color(51, 51, 51));
			sizePlus.fill(g2d);
			sizeRect.fill(g2d);
			sizeMinus.fill(g2d);

			g2d.setColor(Color.GRAY);
			g2d.drawString("+", (int) (sizePlus.getX() + (sizePlus.getWidth() / 2) - (fmT.stringWidth("+") / 2)), (int) (sizePlus.getY() + (sizePlus.getHeight() / 2)) + (fmT.getHeight() / 4));
			g2d.drawString("Change Size (" + Game.manualSize + ")", (int) (sizeRect.getX() + (sizeRect.getWidth() / 2) - (fmT.stringWidth("Change Size (" + Game.manualSize + ")") / 2)), (int) (sizeRect.getY() + (sizeRect.getHeight() / 2)) + (fmT.getHeight() / 4));
			g2d.drawString("-", (int) (sizeMinus.getX() + (sizeMinus.getWidth() / 2) - (fmT.stringWidth("-") / 2)), (int) (sizeMinus.getY() + (sizeMinus.getHeight() / 2)) + (fmT.getHeight() / 4));

			g2d.setColor(new Color(51, 51, 51));
			floodRect.fill(g2d);

			g2d.setColor(Color.GRAY);
			g2d.drawString("Flood", (int) (floodRect.getX() + (floodRect.getWidth() / 2) - (fmT.stringWidth("Flood") / 2)), (int) (floodRect.getY() + (floodRect.getHeight() / 2)) + (fmT.getHeight() / 4));

			g2d.setColor(new Color(51, 51, 51));
			gravityRect.fill(g2d);

			g2d.setColor(Color.GRAY);
			g2d.drawString("Gravity Type (" + Game.getGravityString() + ")", (int) (gravityRect.getX() + (gravityRect.getWidth() / 2) - (fmT.stringWidth("Gravity Type (" + Game.getGravityString() + ")") / 2)), (int) (gravityRect.getY() + (gravityRect.getHeight() / 2)) + (fmT.getHeight() / 4));

			g2d.setColor(new Color(51, 51, 51));
			toolsRect.fill(g2d);

			g2d.setColor(Color.GRAY);
			g2d.drawString("Tools", (int) (toolsRect.getX() + (toolsRect.getWidth() / 2) - (fmT.stringWidth("Tools") / 2)), (int) (toolsRect.getY() + (toolsRect.getHeight() / 2)) + (fmT.getHeight() / 4));

			if (toolsPopOut) {
				g2d.setColor(new Color(51, 51, 51));
				toolsParticle.fill(g2d);
				toolsLine.fill(g2d);
				toolsGravity.fill(g2d);
				toolsRepulsor.fill(g2d);

				g2d.drawImage(particleImage, (int) toolsParticle.getX() + 2, (int) toolsParticle.getY() + 2, null);
				g2d.drawImage(lineImage, (int) toolsLine.getX() + 2, (int) toolsLine.getY() + 2, null);
				g2d.drawImage(gravityImage, (int) toolsGravity.getX() + 2, (int) toolsGravity.getY() + 2, null);
				g2d.drawImage(repulsorImage, (int) toolsRepulsor.getX() + 2, (int) toolsRepulsor.getY() + 2, null);
			}

			g2d.setColor(new Color(51, 51, 51));
			debugRect.fill(g2d);

			g2d.setColor(Color.GRAY);
			g2d.drawString("Debug", (int) (debugRect.getX() + (debugRect.getWidth() / 2) - (fmT.stringWidth("Debug") / 2)), (int) (debugRect.getY() + (debugRect.getHeight() / 2)) + (fmT.getHeight() / 4));

			g2d.setColor(new Color(51, 51, 51));
			clearLines.fill(g2d);

			g2d.setColor(Color.GRAY);
			g2d.drawString("Clear Lines (" + (Game.lList.size() - 4) + ")", (int) (clearLines.getX() + (clearLines.getWidth() / 2) - (fmT.stringWidth("Clear Lines (" + (Game.lList.size() - 3) + ")") / 2)), (int) (clearLines.getY() + (clearLines.getHeight() / 2)) + (fmT.getHeight() / 4));

			g2d.setColor(new Color(51, 51, 51));
			clearParticles.fill(g2d);

			g2d.setColor(Color.GRAY);
			g2d.drawString("Clear Particles (" + Game.pList.size() + ")", (int) (clearParticles.getX() + (clearParticles.getWidth() / 2) - (fmT.stringWidth("Clear Particles (" + Game.pList.size() + ")") / 2)), (int) (clearParticles.getY() + (clearParticles.getHeight() / 2)) + (fmT.getHeight() / 4));

			g2d.setColor(new Color(0x521616));
			exitRect.fill(g2d);

			g2d.setColor(Color.WHITE);
			g2d.drawString("Exit", (int) (exitRect.getX() + (exitRect.getWidth() / 2) - (fmT.stringWidth("Exit") / 2)), (int) (exitRect.getY() + (exitRect.getHeight() / 2)) + (fmT.getHeight() / 4));
		}
	}
}
