import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

public class Circle {
	double x, y, size, xVelocity, yVelocity, radius, mass, angle, speed;
	Ellipse2D circle;

	Circle(double x, double y, double size, double angle, double speed, double mass) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.angle = angle;
		this.speed = speed;
		this.mass = mass;

		circle = new Ellipse2D.Double(x, y, size, size);
		radius = size / 2;
	}

	double getAngle(double x, double y) {
		return Math.atan2(y, x);
	}

	List<Double> getVelocitiesFromAngle(double angle, double speed) {
		double velocity_X = speed * Math.cos(angle);
		double velocity_Y = speed * Math.sin(angle);
		List<Double> vector = new ArrayList<Double>();
		vector.add(velocity_X);
		vector.add(velocity_Y);
		return vector;
	}

	void update() {
		List<Double> velocities = getVelocitiesFromAngle(angle, speed);
		
		xVelocity = velocities.get(0);
		yVelocity = velocities.get(1);
		
		x += xVelocity;
		y += yVelocity;

		circle = new Ellipse2D.Double(x, y, size, size);
	}

	void paint(Graphics2D g2d) {
		g2d.draw(circle);
	}
}
