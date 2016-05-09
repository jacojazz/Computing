package engine.handler;

import java.util.Random;

import math.geom2d.Point2D;
import engine.Game;
import engine.geometry.Particle;

public class ToolHandler {
	static boolean flood;

	public static void update() {
		if (flood) {
			Point2D p = new Point2D(new Random().nextInt(Game.width), new Random().nextInt(Game.height));
			Game.pList.add(new Particle(p, 20));
		}
	}
}
