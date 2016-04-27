package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionUI {
	int x, y, mouseX, mouseY;
	String questionString, answerString, option1String, option2String, option3String, box1String, box2String, box3String, box4String;
	boolean correct = false, mouseClicked = false, activated = false;
	static Rectangle outline = new Rectangle((Game.width / 2) - (610 / 2), (Game.height / 2) - (410 / 2), 610, 410);
	static Rectangle UI = new Rectangle((Game.width / 2) - (600 / 2), (Game.height / 2) - (400 / 2), 600, 400);
	static Rectangle answerPanel = new Rectangle(UI.x, UI.y + (UI.height / 2), 600, 200);
	static Rectangle box1 = new Rectangle(answerPanel.x, answerPanel.y, answerPanel.width / 2, answerPanel.height / 2);
	static Rectangle box2 = new Rectangle(answerPanel.x + (answerPanel.width / 2), answerPanel.y, answerPanel.width / 2, answerPanel.height / 2);
	static Rectangle box3 = new Rectangle(answerPanel.x, answerPanel.y + (answerPanel.height / 2), answerPanel.width / 2, answerPanel.height / 2);
	static Rectangle box4 = new Rectangle(answerPanel.x + (answerPanel.width / 2), answerPanel.y + (answerPanel.height / 2), answerPanel.width / 2, answerPanel.height / 2);
	Font spaceSmallFont, spaceAnswerFont;
	List<String> stringList;

	QuestionUI(List<String> qaoArray, Game game) {
		questionString = qaoArray.get(0);
		answerString = qaoArray.get(1);
		option1String = qaoArray.get(2);
		option2String = qaoArray.get(3);
		option3String = qaoArray.get(4);

		game.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				mouseClicked = true;
				mouseX = e.getX();
				mouseY = e.getY();
			}
		});

		try {
			spaceSmallFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/gui/fonts/imagine_font.ttf")).deriveFont(20f);
			spaceAnswerFont = Font.createFont(Font.TRUETYPE_FONT, new File("src/gui/fonts/imagine_font.ttf")).deriveFont(10f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}

		// Selection Randomisation
		stringList = new ArrayList<String>();
		stringList.add(answerString);
		stringList.add(option1String);
		stringList.add(option2String);
		stringList.add(option3String);
		Collections.shuffle(stringList);

		box1String = stringList.get(0);
		box2String = stringList.get(1);
		box3String = stringList.get(2);
		box4String = stringList.get(3);
	}

	public void update() {
		if (mouseClicked) {
			mouseClicked = false;
			Point p = new Point(mouseX, mouseY);
			if (box1.contains(p)) {
				if (box1String.equals(answerString)) {
					correct();
				} else {
					incorrect();
				}
			} else if (box2.contains(p)) {
				if (box2String.equals(answerString)) {
					correct();
				} else {
					incorrect();
				}
			} else if (box3.contains(p)) {
				if (box3String.equals(answerString)) {
					correct();
				} else {
					incorrect();
				}
			} else if (box4.contains(p)) {
				if (box4String.equals(answerString)) {
					correct();
				} else {
					incorrect();
				}
			}
		}
	}

	public void correct() {
		activated = true;
		correct = true;
		Game.pause = false;
	}

	public void incorrect() {
		activated = true;
		correct = false;
		Game.pause = false;
	}

	public void paint(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.fill(outline);
		g2d.setColor(new Color(0xE3E3E3));
		g2d.fill(UI);
		g2d.setColor(Color.GRAY);
		g2d.fill(answerPanel);
		g2d.setColor(Color.BLACK);

		g2d.draw(box1);
		g2d.draw(box2);
		g2d.draw(box3);
		g2d.draw(box4);

		g2d.setColor(Color.BLACK);

		FontMetrics fmS = g2d.getFontMetrics(spaceSmallFont);
		List<String> strings = StringUtilities.wrap(questionString, fmS, 375);
		g2d.setFont(spaceSmallFont);
		
		int yLeader = 0;
		
		for (int fontWrap = 0; fontWrap < strings.size(); fontWrap++) {
			String line = strings.get(fontWrap);
			g2d.drawString(line, (UI.x + (UI.width / 2)) - (fmS.stringWidth(line) / 2), ((UI.y + (UI.height / 4)) + (fmS.getHeight() / 2)) + yLeader);
			
			yLeader += (fmS.getHeight() + 10);
		}

		// g2d.drawString(questionString, (UI.x + (UI.width / 2)) -
		// (fmS.stringWidth(questionString) / 2), (UI.y + (UI.height / 4)) +
		// fmS.getHeight() / 2);

		int box1StringX = box1.x + ((box1.width / 2) - (fmS.stringWidth(stringList.get(0)) / 2));
		int box1StringY = box1.y + ((box1.height / 2) + (fmS.getHeight() / 2));
		g2d.drawString(stringList.get(0), box1StringX, box1StringY);

		int box2StringX = box2.x + ((box2.width / 2) - (fmS.stringWidth(stringList.get(1)) / 2));
		int box2StringY = box2.y + ((box2.height / 2) + (fmS.getHeight() / 2));
		g2d.drawString(stringList.get(1), box2StringX, box2StringY);

		int box3StringX = box3.x + ((box3.width / 2) - (fmS.stringWidth(stringList.get(2)) / 2));
		int box3StringY = box3.y + ((box3.height / 2) + (fmS.getHeight() / 2));
		g2d.drawString(stringList.get(2), box3StringX, box3StringY);

		int box4StringX = box4.x + ((box4.width / 2) - (fmS.stringWidth(stringList.get(3)) / 2));
		int box4StringY = box4.y + ((box4.height / 2) + (fmS.getHeight() / 2));
		g2d.drawString(stringList.get(3), box4StringX, box4StringY);

	}
}
