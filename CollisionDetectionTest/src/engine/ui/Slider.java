package engine.ui;

import java.awt.event.MouseEvent;

import engine.game.Game;
import math.geom2d.Point2D;
import math.geom2d.polygon.Rectangle2D;

public class Slider extends Rectangle2D {
	Rectangle2D inner, mover;
	double sliderValue;
	boolean dragging = false;

	Slider(Point2D position, double width, double height) {
		super(position, new Point2D(position.getX() + width, position.getY() + height));
		sliderValue = 0;
		setRectangleBounds();
	}

	private void setRectangleBounds() {
		inner = new Rectangle2D(getX() + 2, getY() + 2, getWidth() - 4, getHeight() - 4);
		double moverX = (inner.getWidth() - inner.getHeight()) * sliderValue;
		mover = new Rectangle2D(moverX, inner.getY(), inner.getHeight(), inner.getHeight());
	}

	boolean checkBounds(Point2D p) {
		if (this.contains(Game.mouse)) {
			return true;
		} else {
			return false;
		}
	}

	void mousePressed(MouseEvent e) {
		if (mover.contains(Game.mouse)) {
			dragging = true;
		}
	}

	void mouseReleased(MouseEvent e) {
		dragging = false;
	}
}
