package engine.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import math.geom2d.Point2D;
import math.geom2d.polygon.Rectangle2D;

public class ModifierMenu {
	Object selectedObject;
	Rectangle2D baseRect, moveRect, intensityMinus, intensityRect, intensityPlus, toggle, remove, closeMenu;
	boolean dragging;
	Point2D mouseRelation, position;
	Font textFont;

	ModifierMenu(Object selectedObject) {
		this.selectedObject = selectedObject;

		setRectangleBounds();

		try {
			textFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/engine/res/fonts/Abel-Regular.ttf")).deriveFont(15f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (selectedObject instanceof GravityNode) {
				if (intensityMinus.contains(Game.mouse)) {
					if (((GravityNode) selectedObject).intensity > 10) {
						((GravityNode) selectedObject).intensity -= 10;
					}
				} else if (intensityPlus.contains(Game.mouse)) {
					((GravityNode) selectedObject).intensity += 10;
				} else if (toggle.contains(Game.mouse)) {
					if (((GravityNode) selectedObject).intensity != 0) {
						((GravityNode) selectedObject).intensity = 0;
					} else {
						((GravityNode) selectedObject).intensity = 100;
					}
				}
			}
		}
	}

	void mousePressed(MouseEvent e) {
		if (moveRect.contains(Game.mouse)) {
			dragging = true;
			mouseRelation = Game.mouse.minus(new Point2D(baseRect.getX(), baseRect.getY()));
		}
	}

	void mouseReleased(MouseEvent e) {
		dragging = false;
	}

	void setRectangleBounds() {
		if (selectedObject instanceof Particle) {
			baseRect = new Rectangle2D(((Particle) selectedObject).center().getX(), ((Particle) selectedObject).center().getY() - 100, 200, 100);
		} else if (selectedObject instanceof GravityNode) {
			baseRect = new Rectangle2D(((GravityNode) selectedObject).getX(), ((GravityNode) selectedObject).getY() - 100, 200, 100);
			intensityMinus = new Rectangle2D(baseRect.getX() + 10, baseRect.getY() + 30, 20, 20);
			intensityRect = new Rectangle2D(baseRect.getX() + 35, baseRect.getY() + 30, baseRect.getWidth() - 70, 20);
			intensityPlus = new Rectangle2D(baseRect.getX() + 170, baseRect.getY() + 30, 20, 20);
			toggle = new Rectangle2D(baseRect.getX() + 10, baseRect.getY() + 60, baseRect.getWidth() - 20, 20);
		}

		moveRect = new Rectangle2D(baseRect.getX(), baseRect.getY(), 200, 20);
	}

	public static boolean checkBounds(Point2D p) {
		for (ModifierMenu m : Game.mList) {
			if (m.baseRect.contains(p)) {
				return true;
			}
		}
		return false;
	}

	void update() {
		if (dragging == true) {
			position = Game.mouse.minus(mouseRelation);
		}

		setRectangleBounds();
	}

	void paint(Graphics2D g2d) {
		g2d.setColor(new Color(20, 20, 20, 250));
		baseRect.fill(g2d);
		g2d.setColor(Color.GRAY);
		moveRect.fill(g2d);

		FontMetrics fmT = g2d.getFontMetrics(textFont);
		g2d.setFont(textFont);
		g2d.setColor(Color.BLACK);

		if (selectedObject instanceof Particle) {
			g2d.drawString("Particle " + Game.pList.indexOf(selectedObject), (int) moveRect.getX() + 5, (int) (moveRect.getY() + (moveRect.getHeight() / 2)) + (fmT.getHeight() / 4));
		} else if (selectedObject instanceof GravityNode) {
			g2d.drawString("Gravity Node " + Game.gList.indexOf(selectedObject), (int) moveRect.getX() + 5, (int) (moveRect.getY() + (moveRect.getHeight() / 2)) + (fmT.getHeight() / 4));
			g2d.setColor(new Color(51, 51, 51));
			intensityMinus.fill(g2d);
			intensityRect.fill(g2d);
			intensityPlus.fill(g2d);

			g2d.setColor(Color.GRAY);
			g2d.drawString("-", (int) (intensityMinus.getX() + (intensityMinus.getWidth() / 2) - (fmT.stringWidth("-") / 2)), (int) (intensityMinus.getY() + (intensityMinus.getHeight() / 2)) + (fmT.getHeight() / 4));
			g2d.drawString("Intensity (" + ((GravityNode) selectedObject).intensity + ")", (int) (intensityRect.getX() + (intensityRect.getWidth() / 2) - (fmT.stringWidth("Intensity (" + ((GravityNode) selectedObject).intensity + ")") / 2)), (int) (intensityRect.getY() + (intensityRect.getHeight() / 2)) + (fmT.getHeight() / 4));
			g2d.drawString("+", (int) (intensityPlus.getX() + (intensityPlus.getWidth() / 2) - (fmT.stringWidth("+") / 2)), (int) (intensityPlus.getY() + (intensityPlus.getHeight() / 2)) + (fmT.getHeight() / 4));

			if (((GravityNode) selectedObject).intensity != 0) {
				g2d.setColor(Color.GREEN);
			} else {
				g2d.setColor(Color.RED);
			}
			toggle.fill(g2d);
			g2d.setColor(Color.WHITE);

			if (((GravityNode) selectedObject).intensity != 0) {
				g2d.drawString("ON", (int) (toggle.getX() + (toggle.getWidth() / 2) - (fmT.stringWidth("ON") / 2)), (int) (toggle.getY() + (toggle.getHeight() / 2)) + (fmT.getHeight() / 4));
			} else {
				g2d.drawString("OFF", (int) (toggle.getX() + (toggle.getWidth() / 2) - (fmT.stringWidth("OFF") / 2)), (int) (toggle.getY() + (toggle.getHeight() / 2)) + (fmT.getHeight() / 4));
			}
		}
	}
}
