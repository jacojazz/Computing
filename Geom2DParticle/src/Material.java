public enum Material {
	Static(0.78, 0.9), Ball(1, 0.95);

	private final double restitution;
	private final double friction;

	Material(double restitution, double friction) {
		this.restitution = restitution;
		this.friction = friction;
	}

	double getRestitution() {
		return restitution;
	}

	double getFriction() {
		return friction;
	}
}
