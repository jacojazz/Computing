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
	private static BufferedImage bulgedSubImage;
	private static int i = 0;
	private static boolean customDelay = false;
	private static boolean bulge = false;
	private float speedFloat = 1f;
	private int delayCounter = 0;

	CircleImage(BufferedImage original, boolean bulge) {
		CircleImage.original = original;
		CircleImage.bulge = bulge;
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
			subImage = original.getSubimage(i, 0, original.getHeight(), original.getHeight());
			subImage = makeRoundedCorner(subImage, original.getHeight());
		} else {
			if (excess != 0 && (original.getWidth() - i) != 0) {
				Image img1 = original.getSubimage(i, 0, original.getWidth() - i, original.getHeight());
				Image img2 = original.getSubimage(0, 0, excess, original.getHeight());
				subImage = toBufferedImage(joinImage(img1, img2));
				subImage = makeRoundedCorner(subImage, original.getHeight());
			}
		}

		return subImage;
	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		return bimage;
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

	private static BufferedImage computeBulgeImage(BufferedImage input, int cx, int cy, double bulgeStrength, double bulgeRadius) {
		BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
		int w = input.getWidth();
		int h = input.getHeight();
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int dx = x - cx;
				int dy = y - cy;
				double distanceSquared = dx * dx + dy * dy;

				int sx = x;
				int sy = y;
				if (distanceSquared < bulgeRadius * bulgeRadius) {
					double distance = Math.sqrt(distanceSquared);
					boolean otherMethod = false;
					otherMethod = true;
					if (otherMethod) {
						double r = distance / bulgeRadius;
						double a = Math.atan2(dy, dx);
						double rn = Math.pow(r, bulgeStrength) * distance;
						double newX = rn * Math.cos(a) + cx;
						double newY = rn * Math.sin(a) + cy;
						sx += (newX - x);
						sy += (newY - y);
					} else {
						double dirX = dx / distance;
						double dirY = dy / distance;
						double alpha = distance / bulgeRadius;
						double distortionFactor = distance * Math.pow(1 - alpha, 1.0 / bulgeStrength);
						sx -= distortionFactor * dirX;
						sy -= distortionFactor * dirY;
					}
				}
				if (sx >= 0 && sx < w && sy >= 0 && sy < h) {
					int rgb = input.getRGB(sx, sy);
					output.setRGB(x, y, rgb);
				}
			}
		}
		return output;
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
			g2d.drawImage(original.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, width, height, null);
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
