package engine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;

public class InputHandler {
	Point2D mouse = new Point2D(0, 0);
	Point2D initial = mouse;
	boolean dragging = false;

	MouseListener ml = new MouseListener() {
		public void mouseClicked(MouseEvent e) {

		}

		public void mouseEntered(MouseEvent e) {

		}

		public void mouseExited(MouseEvent e) {

		}

		public void mousePressed(MouseEvent e) {
			initial = mouse;
			dragging = true;
		}

		public void mouseReleased(MouseEvent e) {
			dragging = false;
			Vector2D distance = new Vector2D(initial.minus(mouse));
			Game.pList.add(new Particle(initial, 40, distance.times(0.125)));
		}
	};

	MouseMotionListener mml = new MouseMotionListener() {
		public void mouseDragged(MouseEvent e) {
			mouse = new Point2D(e.getPoint());
		}

		public void mouseMoved(MouseEvent e) {
			mouse = new Point2D(e.getPoint());
		}
	};

	KeyListener kl = new KeyListener() {
		public void keyPressed(KeyEvent e) {

		}

		public void keyReleased(KeyEvent e) {

		}

		public void keyTyped(KeyEvent e) {

		}
	};
}
