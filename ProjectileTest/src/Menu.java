import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class Menu {

	int x = 10;
	int y = 10;
	int width = 200;
	int height = 300;

	int mouseX;
	int mouseY;
	int mouseXChange;
	int mouseYChange;
	int mouseRelationX;
	int mouseRelationY;

	boolean dragging = false;

	Point p = new Point(0, 0);

	Rectangle baseRect;
	Rectangle moveRect;

	Rectangle manualRect;

	Rectangle rainPlus;
	Rectangle rainRect;
	Rectangle rainMinus;

	Rectangle snowPlus;
	Rectangle snowRect;
	Rectangle snowMinus;

	Rectangle changeAngle;

	Rectangle exitRect;

	public Menu() {

	}

	public void mouseClicked(MouseEvent e) {
		p = new Point(e.getX(), e.getY());

		if (manualRect.contains(p)) {
			Game.MANUAL = !Game.MANUAL;
		} else if (rainPlus.contains(p)) {
			if (Game.rainDelay > 1) {
				Game.rainDelay--;
			}
		} else if (rainRect.contains(p)) {
			Game.RAIN = !Game.RAIN;
		} else if (rainMinus.contains(p)) {
			Game.rainDelay++;
		} else if (snowPlus.contains(p)) {
			if (Game.snowDelay > 1) {
				Game.rainDelay--;
			}
		} else if (snowRect.contains(p)) {
			Game.SNOW = !Game.SNOW;
		} else if (snowMinus.contains(p)) {
			Game.snowDelay++;
		} else if (changeAngle.contains(p)) {
			int xVariation = Game.randInt(-5, 5);
			Game.rainX = xVariation;
			Game.snowX = xVariation;
		} else if (exitRect.contains(p)) {
			System.exit(0);
		}

	}

	public void mousePressed(MouseEvent e) {
		p = new Point(e.getX(), e.getY());

		if (moveRect.contains(p)) {
			dragging = true;

			mouseRelationX = e.getX() - baseRect.x;
			mouseRelationY = e.getY() - baseRect.y;
		}
	}

	public void mouseReleased(MouseEvent e) {
		dragging = false;
	}

	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	public void update() {
		baseRect = new Rectangle(x, y, width, height);
		moveRect = new Rectangle(x + 10, y + 10, width - 20, height - 280);

		manualRect = new Rectangle(x + 10, y + 100, width - 20, height - 280);

		rainPlus = new Rectangle(x + 10, y + 140, width / 10, width / 10);
		rainRect = new Rectangle(x + 35, y + 140, width - 70, height - 280);
		rainMinus = new Rectangle(x + 170, y + 140, width / 10, width / 10);

		snowPlus = new Rectangle(x + 10, y + 180, width / 10, width / 10);
		snowRect = new Rectangle(x + 35, y + 180, width - 70, height - 280);
		snowMinus = new Rectangle(x + 170, y + 180, width / 10, width / 10);

		changeAngle = new Rectangle(x + 10, y + 220, width - 20, height - 280);

		exitRect = new Rectangle(x + 10, y + 270, width - 20, height - 280);

		if (dragging == true) {
			x = mouseX - mouseRelationX;
			y = mouseY - mouseRelationY;
		}

	}

	public void paint(Graphics2D g2d) {
		g2d.setColor(new Color(20, 20, 20, 220));
		g2d.fill(baseRect);

		g2d.setColor(new Color(51, 51, 51, 220));
		g2d.fill(moveRect);

		g2d.setColor(new Color(51, 51, 51, 220));
		g2d.fill(manualRect);

		g2d.setColor(new Color(51, 51, 51, 220));
		g2d.fill(rainPlus);
		g2d.fill(rainRect);
		g2d.fill(rainMinus);

		g2d.setColor(new Color(51, 51, 51, 220));
		g2d.fill(snowPlus);
		g2d.fill(snowRect);
		g2d.fill(snowMinus);

		g2d.setColor(new Color(51, 51, 51, 220));
		g2d.fill(changeAngle);

		g2d.setColor(new Color(51, 51, 51, 220));
		g2d.fill(exitRect);

	}

}
