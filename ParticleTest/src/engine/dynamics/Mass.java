package engine.dynamics;

import java.text.DecimalFormat;

import engine.resources.Messages;
import math.geom2d.conic.Circle2D;

public class Mass {
	private double mass;

	public Mass(Circle2D c, double density) {
		this(Math.PI * ((c.radius() / 100) * (c.radius() / 100)), density);
	}

	Mass(double area, double density) {
		if (density <= 0.0) {
			throw new IllegalArgumentException(Messages.getString("dynamics.mass.invaliddensity"));
		}

		DecimalFormat oneDigit = new DecimalFormat("#,##0.0");
		double temp = area * density;
		double formatted = Double.valueOf(oneDigit.format(temp));
		this.mass = formatted;
	}

	public double getMass() {
		return mass;
	}
}
