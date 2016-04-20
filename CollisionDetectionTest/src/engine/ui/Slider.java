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
	int sliderValue;
	static boolean dragging = false;
	static double moverXDifference;
	static Point2D mouseRelation;

	Slider(Point2D position, double width, double height) {
		super(position, new Point2D(position.getX() + width, position.getY() + height));
		sliderValue = 0;
		setRectangleBounds();
		moverXDifference = 0;
	}

	void mousePressed(MouseEvent e) {
		if (mover.contains(Game.mouse)) {
			dragging = true;
			mouseRelation = Game.mouse.minus(new Point2D(mover.getX(), mover.getY()));
		}
	}

	void mouseReleased(MouseEvent e) {
		dragging = false;
	}

	private void setRectangleBounds() {
		inner = new Rectangle2D(getX() + 5, getY() + 5, getWidth() - 10, getHeight() - 10);
		mover = new Rectangle2D(inner.getX() + moverXDifference, inner.getY(), inner.getHeight(), inner.getHeight());
	}

	void update() {
		if (dragging) {
			double calc = Game.mouse.minus(mouseRelation).getX() - inner.getX();
			double max = inner.getWidth() - mover.getWidth();
			double min = 0;

			if (calc <= max && calc >= min) {
				moverXDifference = calc;
			} else if (calc > max) {
				moverXDifference = max;
			} else if (calc < min) {
				moverXDifference = min;
			}
		}

		sliderValue = (int) ((mover.getX() - inner.getX()) / (inner.getWidth() - mover.getWidth()) * 100);
		setRectangleBounds();
	}

	int getValue() {
		return sliderValue;
	}

	void setPosition(Point2D position) {
		x0 = position.getX();
		y0 = position.getY();
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
		g2d.setColor(Color.BLACK);
		inner.fill(g2d);
		g2d.setColor(new Color(51, 51, 51));
		mover.fill(g2d);
		g2d.setColor(Color.GRAY);
		g2d.drawString(String.valueOf(sliderValue), (int) (mover.getX() + (mover.getWidth() / 2) - (fm.stringWidth(String.valueOf(sliderValue)) / 2)), (int) (mover.getY() + (mover.getHeight() / 2)) + (fm.getHeight() / 4));
	}
}
