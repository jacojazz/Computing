package gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class CircleImage {
	private static BufferedImage original;
	private static BufferedImage currentImage;
	private static BufferedImage subImage;
	private static int i = 0;
	private static boolean customDelay = false;
	private float speedFloat = 1f;
	private int delayCounter = 0;

	CircleImage(BufferedImage original) {
		CircleImage.original = original;
	}

	public BufferedImage getNextImage() {
		currentImage = subImage;
		if (!customDelay) {
			if (i < original.getWidth()) {
				i++;
			} else {
				i = 0;
			}
		} else {
			i = getDelay();
		}

		int excess = original.getHeight() - (original.getWidth() - i);

		if (!((i + original.getHeight()) > original.getWidth())) {
			subImage = makeRoundedCorner(original.getSubimage(i, 0, original.getHeight(), original.getHeight()), original.getHeight());
		} else {
			if (excess != 0 && (original.getWidth() - i) != 0) {
				Image img1 = original.getSubimage(i, 0, original.getWidth() - i, original.getHeight());
				Image img2 = original.getSubimage(0, 0, excess, original.getHeight());
				subImage = makeRoundedCorner(joinImage(img1, img2), original.getHeight());
			}
		}
		return subImage;
	}

	public BufferedImage getCurrentImage() {
		return currentImage;
	}

	public static Image joinImage(Image img1, Image img2) {
		int width = img1.getWidth(null) + img2.getWidth(null);
		int height = Math.max(img1.getHeight(null), img2.getHeight(null));

		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = newImage.createGraphics();
		Color oldColor = g2d.getColor();

		g2d.setPaint(Color.WHITE);
		g2d.fillRect(0, 0, width, height);
		g2d.setColor(oldColor);
		g2d.drawImage(img1, 0, 0, null);
		g2d.drawImage(img2, img1.getWidth(null), 0, null);
		g2d.dispose();

		return newImage;
	}

	public static BufferedImage makeRoundedCorner(Image image, int cornerRadius) {
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = output.createGraphics();

		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

		g2.setComposite(AlphaComposite.SrcAtop);
		g2.drawImage(image, 0, 0, null);

		g2.dispose();

		return output;
	}

	void scale(float scale) {
		if (scale != 1f) {
			int width = (int) (original.getWidth() * scale);
			int height = (int) (original.getHeight() * scale);
			BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = temp.createGraphics();
			g2d.drawImage(original.getScaledInstance(width, height, Image.SCALE_DEFAULT), 0, 0, width, height, null);
			g2d.dispose();

			original = temp;
		}
	}

	int getDelay() {
		if (speedFloat >= 1f) {
			if ((i + speedFloat) < original.getWidth()) {
				return (int) (i + speedFloat);
			} else {
				return 0;
			}
		} else {
			delayCounter++;
			if (delayCounter % (1 / speedFloat) == 0) {
				if (i < original.getWidth()) {
					return i + 1;
				} else {
					return 0;
				}
			} else {
				return i;
			}
		}
	}

	void setSpeed(float speed) {
		customDelay = true;
		speedFloat = speed;
	}

	void resize(int width, int height) {
		if (width > height) {
			BufferedImage temp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = temp.createGraphics();
			g2d.drawImage(original.getScaledInstance(width, height, Image.SCALE_DEFAULT), 0, 0, width, height, null);
			g2d.dispose();

			original = temp;
		} else {
			try {
				throw new Exception("Width needs to be greater than Height.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
