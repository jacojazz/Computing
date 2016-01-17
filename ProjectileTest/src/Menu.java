import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class Menu {

	int x = 120;
	int y = 120;
	int width = 200;
	int height = 300;

	int mouseX;
	int mouseY;
	int mouseXChange;
	int mouseYChange;

	boolean dragging = false;

	Point p = new Point(0, 0);

	Rectangle baseRect;
	Rectangle moveRect;

	Rectangle manualRect;
	Rectangle rainRect;
	Rectangle snowRect;

	Rectangle exitRect;

	public Menu() {

	}

	public void mouseClicked(MouseEvent e) {
		p = new Point(e.getX(), e.getY());

		if (moveRect.contains(p)) {
			dragging = true;

		} else if (manualRect.contains(p)) {
			Game.MANUAL = !Game.MANUAL;
		} else if (rainRect.contains(p)) {
			Game.rainX = Game.randInt(-5, 5);
			Game.RAIN = !Game.RAIN;
		} else if (snowRect.contains(p)) {
			Game.snowX = Game.randInt(-3, 3);
			Game.SNOW = !Game.SNOW;
		} else if (exitRect.contains(p)) {
			System.exit(0);
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
		rainRect = new Rectangle(x + 10, y + 140, width - 20, height - 280);
		snowRect = new Rectangle(x + 10, y + 180, width - 20, height - 280);

		exitRect = new Rectangle(x + 10, y + 270, width - 20, height - 280);

		if (dragging == true) {
			x = mouseX;
			y = mouseY;
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
		g2d.fill(rainRect);

		g2d.setColor(new Color(51, 51, 51, 220));
		g2d.fill(snowRect);

		g2d.setColor(new Color(51, 51, 51, 220));
		g2d.fill(exitRect);

	}

}
