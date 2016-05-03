import java.util.Iterator;

public class UpdateHandler implements Runnable {
	Thread t;

	UpdateHandler() {
		t = new Thread(this);
		t.start();
	}

	public void update() {
		t.run();
	}

	public void run() {
		for (Iterator<Particle> pIterator = Game.pList.iterator(); pIterator.hasNext();) {
			Particle p = pIterator.next();
			p.update();
		}
	}
}
