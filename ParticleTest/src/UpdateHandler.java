public class UpdateHandler {
	public static void update() {
		updateAllParticles();
	}

	static void updateAllParticles() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				for (int particleIterator = 0; particleIterator < Game.pList.size(); particleIterator++) {
					Particle p = Game.pList.get(particleIterator);
					p.update();
				}
			}
		});
		t.run();
	}
}
