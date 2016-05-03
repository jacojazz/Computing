import java.util.Iterator;

public class UpdateHandler {
	public void update() {
		for (Iterator<Particle> pIterator = Game.pList.iterator(); pIterator.hasNext();) {
			Particle p = pIterator.next();
			p.update();
		}
	}
}
