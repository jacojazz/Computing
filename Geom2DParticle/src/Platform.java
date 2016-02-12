import java.awt.Graphics2D;

import math.geom2d.line.Line2D;

public class Platform extends Line2D {

	Material material;

	public Platform(double x1, double y1, double x2, double y2, Material material) {
		super(x1, y1, x2, y2);
		this.material = material;
	}

	public void paint(Graphics2D g2d) {
		draw(g2d);
	}
}
