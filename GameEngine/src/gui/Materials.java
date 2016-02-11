package gui;

public enum Materials {
	Rock(0.6, 0.1, 0.0), Wood(0.3, 0.2, 0.0), Metal(1.2, 0.05, 0.0), BouncyBall(0.3, 0.8, 0.0), SuperBall(0.3, 0.95, 0.0), Pillow(0.1, 0.2, 0.0), Static(0.0, 0.4, 0.95);

	private final double density;
	private final double restitution;
	private final double friction;

	Materials(double density, double restitution, double friction) {
		this.density = density;
		this.restitution = restitution;
		this.friction = friction;
	}

	public double density() {
		return density;
	}

	public double restitution() {
		return restitution;
	}

	public double friction() {
		return friction;
	}
}
