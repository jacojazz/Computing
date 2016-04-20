package engine.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import math.geom2d.Point2D;
import math.geom2d.polygon.Rectangle2D;
import engine.game.Game;

public class Slider extends Rectangle2D {
	Rectangle2D inner, mover;
	double sliderValue;
	static boolean dragging = false;
	double moverX, mouseRelation;

	Slider(Point2D position, double width, double height) {
		super(position, new Point2D(position.getX() + width, position.getY() + height));
		sliderValue = 0;
		setRectangleBounds();
	}

	void mousePressed(MouseEvent e) {
		if (mover.contains(Game.mouse)) {
			mouseRelation = Game.mouse.getX();
			dragging = true;
		}
	}

	void mouseReleased(MouseEvent e) {
		dragging = false;
	}

	private void setRectangleBounds() {
		inner = new Rectangle2D(getX() + 5, getY() + 5, getWidth() - 10, getHeight() - 10);
		if (moverX != 0) {
			mover = new Rectangle2D(moverX, inner.getY(), inner.getHeight(), inner.getHeight());
		} else {
			mover = new Rectangle2D(inner.getX(), inner.getY(), inner.getHeight(), inner.getHeight());
		}
	}

	void update() {
		if (dragging) {
			moverX = Game.mouse.getX() - mouseRelation;
		}

		setRectangleBounds();
	}

	boolean checkBounds(Point2D p) {
		if (this.contains(Game.mouse)) {
			return true;
		} else {
			return false;
		}
	}

	void paint(Graphics2D g2d) {
		FontMetrics fm = g2d.getFontMetrics();
		g2d.setColor(new Color(20, 20, 20, 250));
		this.fill(g2d);
		g2d.setColor(new Color(20, 20, 20, 255));
		inner.fill(g2d);
		g2d.setColor(new Color(51, 51, 51));
		mover.fill(g2d);
		g2d.setColor(Color.GRAY);
		g2d.drawString(String.valueOf(Game.manualSize), (int) (mover.getX() + (mover.getWidth() / 2) - (fm.stringWidth(String.valueOf(Game.manualSize)) / 2)), (int) (mover.getY() + (mover.getHeight() / 2)) + (fm.getHeight() / 4));
	}
}
