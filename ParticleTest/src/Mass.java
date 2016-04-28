import math.geom2d.conic.Circle2D;

public class Mass {
	private double mass;

	Mass(Circle2D c, double density) {
		new Mass(Math.PI * (c.radius() * c.radius()), density);
	}

	Mass(double area, double density) {
		this.mass = area * density;
	}

	public double getMass() {
		return mass;
	}
}
