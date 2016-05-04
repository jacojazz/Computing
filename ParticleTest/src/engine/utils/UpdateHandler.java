package engine.utils;

import java.util.Iterator;

import engine.Game;
import engine.Particle;

public class UpdateHandler {
	public void update() {
		for (Iterator<Particle> particleIterator = Game.pList.iterator(); particleIterator.hasNext();) {
			Particle p = particleIterator.next();
			p.update();
		}
	}
}
