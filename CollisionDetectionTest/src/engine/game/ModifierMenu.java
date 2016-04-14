package engine.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import math.geom2d.polygon.Rectangle2D;

public class ModifierMenu {
	Object selectedObject;
	Rectangle2D baseRect, moveRect;
	Font textFont;

	ModifierMenu(Object selectedObject) {
		this.selectedObject = selectedObject;

		setRectangleBounds();

		try {
			textFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/engine/res/fonts/Abel-Regular.ttf")).deriveFont(15f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	void setRectangleBounds() {
		if (selectedObject instanceof Particle) {
			baseRect = new Rectangle2D(((Particle) selectedObject).center().getX(), ((Particle) selectedObject).center().getY() - 50, 200, 50);
		} else if (selectedObject instanceof GravityNode) {
			baseRect = new Rectangle2D(((GravityNode) selectedObject).getX(), ((GravityNode) selectedObject).getY() - 50, 200, 50);
		}

		moveRect = new Rectangle2D(baseRect.getX(), baseRect.getY(), 200, 20);
	}

	void update() {
		setRectangleBounds();
	}

	void paint(Graphics2D g2d) {
		g2d.setColor(new Color(20, 20, 20, 250));
		baseRect.fill(g2d);
		g2d.setColor(Color.GRAY);
		moveRect.fill(g2d);

		FontMetrics fmT = g2d.getFontMetrics(textFont);
		g2d.setFont(textFont);

		if (selectedObject instanceof Particle) {

		} else if (selectedObject instanceof GravityNode) {

		}
	}
}
