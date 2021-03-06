import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Area;

import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Vector2D;
import math.geom2d.conic.Circle2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.Rectangle2D;

public class Tank {
	Point2D position = new Point2D(50, 50);
	double turretAngle = 0;
	double currentAngle = 0;
	double power = 50;
	Box2D boundingBox;
	Rectangle2D tracks;
	Rectangle2D body;
	Circle2D turret;
	Point2D turretAxis, barrelEnd;
	LineSegment2D barrel;
	Color playerColor;

	static double barrelRotationSpeed = 1;
	boolean onTerrain = false;

	AffineTransform2D af = new AffineTransform2D();

	Vector2D velocity = new Vector2D(0, 0);
	Vector2D force = new Vector2D(0, 0.05);
	boolean dragging = false;

	Tank(Point2D p, Color pColor) {
		this.position = p;
		this.playerColor = pColor;
		setBounds();
	}

	Tank(double x, double y, Color pColor) {
		this(new Point2D(x, y), pColor);
	}

	void setBounds() {
		tracks = new Rectangle2D(position.getX() - 10, position.getY() - 3, 20, 3);
		body = new Rectangle2D(position.getX() - 11, position.getY() - 8, 22, 5);
		turret = new Circle2D(position.getX(), position.getY() - 8, 7);
		turretAxis = new Point2D(position.getX(), position.getY() - 10);

		Circle2D barrelTurn = new Circle2D(turretAxis, 15);
		barrelEnd = barrelTurn.point(Math.toRadians(currentAngle) - (Math.PI / 2));
		barrel = new LineSegment2D(turretAxis, barrelEnd);

		boundingBox = new Box2D(new Point2D(position.getX() - 11, position.getY() - 15), new Point2D(position.getX() + 11, position.getY()));
	}

	void update() {
		if (dragging) {
			Circle2D referenceCircle = new Circle2D(Game.initial, 100);
			if (referenceCircle.isInside(Game.mouse)) {
				position = Game.mouse.plus(Game.mouseRelation);
			}
		}

		if (currentAngle < turretAngle) {
			currentAngle += barrelRotationSpeed;
		} else if (currentAngle > turretAngle) {
			currentAngle -= barrelRotationSpeed;
		} else {
			currentAngle = turretAngle;
		}

		if (!onTerrain) {
			velocity = velocity.plus(force);
		}
		position = position.plus(velocity);
		setBounds();
	}

	private Area calculateRectOutside(Rectangle2D r) {
		Area outside = new Area(new Rectangle2D(0, 0, Game.width, Game.height).boundingBox().asAwtRectangle2D());
		outside.subtract(new Area(r.boundingBox().asAwtRectangle2D()));
		return outside;
	}

	void paint(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		barrel.buffer(1).fill(g2d);
		g2d.setColor(playerColor);
		Shape c = g2d.getClip();
		g2d.setClip(calculateRectOutside(new Rectangle2D(body.getX(), body.getY(), body.getWidth(), body.getHeight() * 2)));
		turret.fill(g2d);
		g2d.setClip(c);
		g2d.setColor(Color.BLACK);
		tracks.fill(g2d);
		g2d.setColor(playerColor);
		body.fill(g2d);

	}
}
