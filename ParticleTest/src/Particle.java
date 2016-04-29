import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;

public class Particle extends Circle2D {
	Vector2D velocity, force;
	Mass mass;

	Particle(Point2D c, double r) {
		this(c, r, new Vector2D(0, 0));
	}

	Particle(Point2D c, double r, Vector2D v) {
		this(c, r, v, new Vector2D(0, 0));
	}

	Particle(Point2D c, double r, Vector2D v, Vector2D f) {
		super(c, r);
		this.velocity = v;
		this.force = f;
		this.mass = new Mass(this, 1);
	}

	void update() {
		velocity = velocity.plus(force);
		setPosition(center().plus(velocity));
	}

	public void setPosition(Point2D c) {
		xc = c.getX();
		yc = c.getY();
	}

	public Vector2D getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2D velocity) {
		this.velocity = velocity;
	}
}
