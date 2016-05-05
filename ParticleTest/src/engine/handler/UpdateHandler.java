package engine.handler;

import java.util.Iterator;

import engine.Game;
import engine.geometry.Particle;

public class UpdateHandler {
	public void update() {
		for (Iterator<Particle> particleIterator = Game.pList.iterator(); particleIterator.hasNext();) {
			Particle p = particleIterator.next();
			p.update();
		}
	}
}
