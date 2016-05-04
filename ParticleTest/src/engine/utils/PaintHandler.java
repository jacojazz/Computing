package engine.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Iterator;

import math.geom2d.line.Line2D;
import engine.Game;
import engine.Particle;

public class PaintHandler {
	public void paint(Graphics2D g2d) {
		InputHandler.paint(g2d);

		particles(g2d);
		lines(g2d);
	}

	private void particles(Graphics2D g2d) {
		for (Iterator<Particle> particleIterator = Game.pList.iterator(); particleIterator.hasNext();) {
			Particle p = particleIterator.next();
			p.draw(g2d);

			if (Game.debug) {
				g2d.setColor(Color.BLUE);
				p.boundingBox().draw(g2d);
				g2d.setColor(Color.BLACK);
			}
		}
	}

	private void lines(Graphics2D g2d) {
		for (Iterator<Line2D> lineIterator = Game.lList.iterator(); lineIterator.hasNext();) {
			Line2D l = lineIterator.next();
			l.draw(g2d);
		}
	}
}
