import math.geom2d.conic.Circle2D;

public class Mass {
	private double mass;

	Mass(Circle2D c, double density) {
		this(Math.PI * ((c.radius() / 100) * (c.radius() / 100)), density);
	}

	Mass(double area, double density) {
		this.mass = area * density;
	}

	public double getMass() {
		return mass;
	}
}
