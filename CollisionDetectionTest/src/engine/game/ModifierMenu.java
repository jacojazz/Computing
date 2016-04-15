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
	Rectangle2D baseRect, moveRect, intensityMinus, intensityRect, intensityPlus, toggleRect, removeRect, closeRect;
	Rectangle2D sizeMinus, sizeRect, sizePlus, massMinus, massRect, massPlus;
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
			if (closeRect.contains(Game.mouse)) {
				Game.mList.remove(this);
			}

			if (selectedObject instanceof GravityNode) {
				if (intensityMinus.contains(Game.mouse)) {
					if (((GravityNode) selectedObject).intensity > 0) {
						((GravityNode) selectedObject).intensity -= 10;
					}
				} else if (intensityPlus.contains(Game.mouse)) {
					((GravityNode) selectedObject).intensity += 10;
				} else if (toggleRect.contains(Game.mouse)) {
					if (((GravityNode) selectedObject).intensity != 0) {
						((GravityNode) selectedObject).intensity = 0;
					} else {
						((GravityNode) selectedObject).intensity = 100;
					}
				} else if (removeRect.contains(Game.mouse)) {
					Game.gList.remove(selectedObject);
				}
			} else if (selectedObject instanceof Particle) {
				if (sizeMinus.contains(Game.mouse)) {
					if (((Particle) selectedObject).radius() > 10) {
						((Particle) selectedObject).setRadius(((Particle) selectedObject).radius() - 2);
					}
				} else if (sizePlus.contains(Game.mouse)) {
					((Particle) selectedObject).setRadius(((Particle) selectedObject).radius() + 2);
				} else if (massMinus.contains(Game.mouse)) {
					if (((Particle) selectedObject).getMass() > 0.5) {
						((Particle) selectedObject).setMass(((Particle) selectedObject).getMass() - 0.5);
					}
				} else if (massPlus.contains(Game.mouse)) {
					((Particle) selectedObject).setMass(((Particle) selectedObject).getMass() + 0.5);
				} else if (removeRect.contains(Game.mouse)) {
					Game.pList.remove(selectedObject);
				}
				((Particle) selectedObject).update();
			}
		}
	}

	void mouseReleased(MouseEvent e) {
		dragging = false;
	}

	void mousePressed(MouseEvent e) {
		if (moveRect.contains(Game.mouse)) {
			dragging = true;
			mouseRelation = Game.mouse.minus(new Point2D(baseRect.getX(), baseRect.getY()));
		}
	}

	void setRectangleBounds() {
		if (selectedObject instanceof Particle) {
			baseRect = new Rectangle2D(((Particle) selectedObject).center().getX(), ((Particle) selectedObject).center().getY() - 120, 200, 120);
			sizeMinus = new Rectangle2D(baseRect.getX() + 10, baseRect.getY() + 30, 20, 20);
			sizeRect = new Rectangle2D(baseRect.getX() + 35, baseRect.getY() + 30, baseRect.getWidth() - 70, 20);
			sizePlus = new Rectangle2D(baseRect.getX() + 170, baseRect.getY() + 30, 20, 20);
			massMinus = new Rectangle2D(baseRect.getX() + 10, baseRect.getY() + 60, 20, 20);
			massRect = new Rectangle2D(baseRect.getX() + 35, baseRect.getY() + 60, baseRect.getWidth() - 70, 20);
			massPlus = new Rectangle2D(baseRect.getX() + 170, baseRect.getY() + 60, 20, 20);
		} else if (selectedObject instanceof GravityNode) {
			baseRect = new Rectangle2D(((GravityNode) selectedObject).getX(), ((GravityNode) selectedObject).getY() - 120, 200, 120);
			intensityMinus = new Rectangle2D(baseRect.getX() + 10, baseRect.getY() + 30, 20, 20);
			intensityRect = new Rectangle2D(baseRect.getX() + 35, baseRect.getY() + 30, baseRect.getWidth() - 70, 20);
			intensityPlus = new Rectangle2D(baseRect.getX() + 170, baseRect.getY() + 30, 20, 20);
			toggleRect = new Rectangle2D(baseRect.getX() + 10, baseRect.getY() + 60, 180, 20);
		}

		moveRect = new Rectangle2D(baseRect.getX(), baseRect.getY(), 200, 20);
		closeRect = new Rectangle2D(baseRect.getX() + 180, baseRect.getY(), 20, 20);
		removeRect = new Rectangle2D(baseRect.getX() + 10, baseRect.getY() + baseRect.getHeight() - 30, 180, 20);
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
		g2d.setColor(Color.RED);
		closeRect.fill(g2d);

		FontMetrics fmT = g2d.getFontMetrics(textFont);
		g2d.setFont(textFont);
		g2d.setColor(Color.BLACK);

		if (selectedObject instanceof Particle) {
			g2d.drawString("Particle " + Game.pList.indexOf(selectedObject), (int) moveRect.getX() + 5, (int) (moveRect.getY() + (moveRect.getHeight() / 2)) + (fmT.getHeight() / 4));
			g2d.setColor(new Color(51, 51, 51));
			sizeMinus.fill(g2d);
			sizeRect.fill(g2d);
			sizePlus.fill(g2d);

			g2d.setColor(new Color(51, 51, 51));
			massMinus.fill(g2d);
			massRect.fill(g2d);
			massPlus.fill(g2d);

			g2d.setColor(Color.GRAY);
			g2d.drawString("-", (int) (sizeMinus.getX() + (sizeMinus.getWidth() / 2) - (fmT.stringWidth("-") / 2)), (int) (sizeMinus.getY() + (sizeMinus.getHeight() / 2)) + (fmT.getHeight() / 4));
			g2d.drawString("Size (" + ((Particle) selectedObject).radius() + ")", (int) (sizeRect.getX() + (sizeRect.getWidth() / 2) - (fmT.stringWidth("Size (" + ((Particle) selectedObject).radius() + ")") / 2)), (int) (sizeRect.getY() + (sizeRect.getHeight() / 2)) + (fmT.getHeight() / 4));
			g2d.drawString("+", (int) (sizePlus.getX() + (sizePlus.getWidth() / 2) - (fmT.stringWidth("+") / 2)), (int) (sizePlus.getY() + (sizePlus.getHeight() / 2)) + (fmT.getHeight() / 4));

			g2d.setColor(Color.GRAY);
			g2d.drawString("-", (int) (massMinus.getX() + (massMinus.getWidth() / 2) - (fmT.stringWidth("-") / 2)), (int) (massMinus.getY() + (massMinus.getHeight() / 2)) + (fmT.getHeight() / 4));
			g2d.drawString("Mass (" + ((Particle) selectedObject).getMass() + ")", (int) (massRect.getX() + (massRect.getWidth() / 2) - (fmT.stringWidth("Mass (" + ((Particle) selectedObject).getMass() + ")") / 2)), (int) (massRect.getY() + (massRect.getHeight() / 2)) + (fmT.getHeight() / 4));
			g2d.drawString("+", (int) (massPlus.getX() + (massPlus.getWidth() / 2) - (fmT.stringWidth("+") / 2)), (int) (massPlus.getY() + (massPlus.getHeight() / 2)) + (fmT.getHeight() / 4));
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

			toggleRect.fill(g2d);
			g2d.setColor(Color.WHITE);

			if (((GravityNode) selectedObject).intensity != 0) {
				g2d.drawString("ON", (int) (toggleRect.getX() + (toggleRect.getWidth() / 2) - (fmT.stringWidth("ON") / 2)), (int) (toggleRect.getY() + (toggleRect.getHeight() / 2)) + (fmT.getHeight() / 4));
			} else {
				g2d.drawString("OFF", (int) (toggleRect.getX() + (toggleRect.getWidth() / 2) - (fmT.stringWidth("OFF") / 2)), (int) (toggleRect.getY() + (toggleRect.getHeight() / 2)) + (fmT.getHeight() / 4));
			}
		}

		g2d.setColor(Color.RED);
		removeRect.fill(g2d);

		g2d.setColor(Color.WHITE);
		g2d.drawString("Delete", (int) (removeRect.getX() + (removeRect.getWidth() / 2) - (fmT.stringWidth("Delete") / 2)), (int) (removeRect.getY() + (removeRect.getHeight() / 2)) + (fmT.getHeight() / 4));
	}
}
