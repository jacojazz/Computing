import java.awt.Color;
import java.awt.Graphics2D;

import math.geom2d.Point2D;
import math.geom2d.Vector2D;

public class Projectile extends Point2D {
	Vector2D velocity;

	Projectile(Point2D c, Vector2D velocity) {
		super(c.getX(), c.getY());
		this.velocity = velocity;
	}

	void update() {
		velocity = velocity.plus(Game.gravity);
	}

	void paint(Graphics2D g2d) {
		g2d.setColor(Color.YELLOW);
		this.draw(g2d, 2);
	}
}
