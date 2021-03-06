package engine.utils;

import java.util.Iterator;

import engine.Game;
import engine.geometry.Particle;

public class Cleaner implements Runnable {
	public void update() {
		Thread t = new Thread(this);
		t.start();
	}

	public void run() {
		for (Iterator<Particle> pIterator = Game.pList.iterator(); pIterator.hasNext();) {
			Particle p = pIterator.next();
			if (!Game.bounds.buffer(p.radius()).contains(p.center())) {
				Game.pList.remove(p);
			}
		}
	}
}
