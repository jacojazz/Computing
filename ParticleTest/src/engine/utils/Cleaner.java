package engine.utils;

import java.util.Iterator;

import engine.Game;
import engine.Particle;

public class Cleaner implements Runnable {
	public void update() {
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		for (Iterator<Particle> pIterator = Game.pList.iterator(); pIterator.hasNext();) {
			Particle p = pIterator.next();
			if (!Game.bounds.asAwtRectangle2D().intersects(p.boundingBox().asAwtRectangle2D())) {
				Game.pList.remove(p);
			}
		}
	}
}
