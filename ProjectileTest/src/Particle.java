import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class Particle {
	double mass, x, y, xVelocity, yVelocity, lifetime, age = 0;
	Ellipse2D rect;
	Color color;
	boolean touchFloor = false;

	final double GRAVITY = 0.05;
	final double FRICTION_COEFFICIENT = 0.7;
	final double BOUNCE_COEFFICIENT = 0.62;

	Particle(double x, double y, double xVelocity, double yVelocity, double lifetime) {
		this.x = x;
		this.y = y;
		this.xVelocity = xVelocity;
		this.yVelocity = yVelocity;
		this.mass = 1;
		this.lifetime = lifetime * Game.TARGET_FPS;

		rect = new Ellipse2D.Double((int) x, (int) y, 8, 8);
		color = new Color(64, 164, 223);
	}

	public void update() {
		age++;

		// setY(Game.height - rect.getHeight());
		// yVelocity = -(yVelocity * BOUNCE_VALUE);

		if (isTouchingFloor() == false) {
			yVelocity += GRAVITY;
		} else if (isTouchingFloor() == true && yVelocity > 0) {
			yVelocity = bounce();
			xVelocity *= FRICTION_COEFFICIENT;
		} else {
			setY(Game.height - rect.getHeight());
			yVelocity = 0;
		}

		if (xVelocity != 0 && isTouchingFloor() == true) {
			xVelocity *= FRICTION_COEFFICIENT;
		}
		
		x += xVelocity;
		y += yVelocity;

		rect = new Ellipse2D.Double((int) x, (int) y, rect.getWidth(), rect.getHeight());
	}

	public boolean isTouchingFloor() {
		if (getY() >= (Game.height - rect.getHeight())) {
			return true;
		} else {
			return false;
		}
	}

	public double bounce() {
		if (yVelocity < 0.2 && yVelocity > -0.2) {
			setY(Game.height - rect.getHeight());
			return 0;
		} else {
			setY(Game.height - rect.getHeight());
			return -(yVelocity * BOUNCE_COEFFICIENT);
		}
	}

	public void paint(Graphics2D g2d) {
		g2d.setColor(color);
		g2d.fill(rect);
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getxVelocity() {
		return xVelocity;
	}

	public void setxVelocity(double xVelocity) {
		this.xVelocity = xVelocity;
	}

	public double getyVelocity() {
		return yVelocity;
	}

	public void setyVelocity(double yVelocity) {
		this.yVelocity = yVelocity;
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public static double randDouble(double min, double max) {
		Random r = new Random();
		double result = (r.nextInt(101) - 10) / 10.0;
		return result;
	}
}
