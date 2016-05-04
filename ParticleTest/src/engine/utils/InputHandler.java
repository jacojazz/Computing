package engine.utils;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.line.Line2D;
import engine.Game;
import engine.Particle;

public class InputHandler {
	Point2D mouse = new Point2D(0, 0);
	Point2D initial = mouse;
	boolean dragging = false;

	public MouseListener ml = new MouseListener() {
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

			if (e.getButton() == MouseEvent.BUTTON1) {
				Game.pList.add(new Particle(initial, 40, distance.times(0.125)));
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				Game.lList.add(new Line2D(initial, mouse));
			}
		}
	};

	public MouseMotionListener mml = new MouseMotionListener() {
		public void mouseDragged(MouseEvent e) {
			mouse = new Point2D(e.getPoint());
		}

		public void mouseMoved(MouseEvent e) {
			mouse = new Point2D(e.getPoint());
		}
	};

	public KeyListener kl = new KeyListener() {
		public void keyPressed(KeyEvent e) {

		}

		public void keyReleased(KeyEvent e) {

		}

		public void keyTyped(KeyEvent e) {

		}
	};

	void paint(Graphics2D g2d) {
		if (dragging) {
			new Line2D(initial, mouse).draw(g2d);
		}
	}
}
