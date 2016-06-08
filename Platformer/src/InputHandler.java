import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler {
	static boolean left = false;
	static boolean right = false;
	static MouseListener ml = new MouseListener() {
		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		@Override
		public void mouseExited(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {

		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}
	};

	static MouseMotionListener mml = new MouseMotionListener() {
		@Override
		public void mouseDragged(MouseEvent e) {

		}

		@Override
		public void mouseMoved(MouseEvent e) {

		}
	};

	static KeyListener kl = new KeyListener() {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_W) {
				Game.player.jump();
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {

			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				left = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				right = true;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_W) {

			}
			if (e.getKeyCode() == KeyEvent.VK_S) {

			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				left = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				right = false;
			}
		}

		@Override
		public void keyTyped(KeyEvent e) {

		}
	};
}
