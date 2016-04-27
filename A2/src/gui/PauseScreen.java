package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class PauseScreen {
	private Rectangle UI = new Rectangle((Game.width / 2) - (400 / 2), (Game.height / 2) - (350 / 2), 400, 200);
	private Rectangle resume = new Rectangle(UI.x + ((UI.width / 2) - (200 / 2)), UI.y + 30, 200, 50);
	private Rectangle exit = new Rectangle(UI.x + ((UI.width / 2) - (200 / 2)), UI.y + 110, 200, 50);
	private Font spaceFont;

	PauseScreen() {
		try {
			spaceFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/gui/fonts/imagine_font.ttf")).deriveFont(25f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	void update() {

	}

	void mouseClicked(MouseEvent e) {
		Point p = new Point(e.getX(), e.getY());
		if (resume.contains(p)) {
			Game.pause = false;
		} else if (exit.contains(p)) {
			System.exit(0);
		}
	}

	void paint(Graphics2D g2d) {
		g2d.setColor(new Color(0xD6D6D6));
		g2d.fill(UI);

		FontMetrics fM = g2d.getFontMetrics(spaceFont);
		g2d.setFont(spaceFont);

		g2d.setColor(new Color(0x858585));
		g2d.drawString("Paused", UI.x, UI.y);

		g2d.setColor(Color.BLACK);
		g2d.fill(resume);
		g2d.fill(exit);

		g2d.setColor(Color.WHITE);

		int resumeStringX = resume.x + ((resume.width / 2) - (fM.stringWidth("Resume") / 2));
		int resumeStringY = resume.y + ((resume.height / 2) + (fM.getHeight() / 2));
		g2d.drawString("Resume", resumeStringX, resumeStringY);

		int exitStringX = exit.x + ((exit.width / 2) - (fM.stringWidth("Exit") / 2));
		int exitStringY = exit.y + ((exit.height / 2) + (fM.getHeight() / 2));
		g2d.drawString("Exit", exitStringX, exitStringY);

		g2d.setColor(Color.BLACK);
	}
}
