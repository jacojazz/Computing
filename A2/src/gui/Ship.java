package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Ship {

	private int x, y, size;
	private int ammoTripleShot = 999, ammoMine = 999, ammoRocket = 999;
	private boolean wDown = false;
	private boolean sDown = false;
	private boolean aDown = false;
	private boolean dDown = false;
	private int xVelocity = 0;
	private int yVelocity = 0;
	private int health = 30;
	private boolean spawnBullet = false;
	private boolean spawnTriple = false;
	private boolean spawnMine = false;
	private boolean spawnRocket = false;
	private boolean paused = false;
	private boolean shieldEnabled = false;

	public Ship() {
		x = Game.width / 2;
		y = (Game.height / 2) + (Game.height / 4);
		size = 20;
	}

	public void update() {
		if (wDown) {
			yVelocity = -3;
		} else if (sDown) {
			yVelocity = 3;
		} else {
			yVelocity = 0;
		}
		if ((y + yVelocity) < 0) {
			y = 0;
		} else if (((y + yVelocity) + 20) > Game.height) {
			y = Game.height - 20;
		} else {
			y += yVelocity;
		}

		if (aDown) {
			xVelocity = -3;
		} else if (dDown) {
			xVelocity = 3;
		} else {
			xVelocity = 0;
		}
		if ((x + xVelocity) < 0) {
			x = 0;
		} else if (((x + xVelocity) + 20) > Game.width) {
			x = Game.width - 20;
		} else {
			x += xVelocity;
		}
	}

	public void paint(Graphics2D g2d) {

		if (shieldEnabled) {
			g2d.setColor(new Color(0x9CE6FF));
			g2d.fillOval(x - 5, y - 5, 30, 30);
		}

		g2d.setColor(Color.BLACK);
		g2d.fillOval(x, y, size, size);
		g2d.setColor(Color.RED);
		g2d.fillRect(x - 5, y - 7, health, 2);
		g2d.setColor(Color.BLACK);
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			wDown = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			sDown = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			aDown = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			dDown = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			bulletSpawn();
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			tripleSpawn();
		}
		if (e.getKeyCode() == KeyEvent.VK_M) {
			mineSpawn();
		}
		if (e.getKeyCode() == KeyEvent.VK_R) {
			rocketSpawn();
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if(!Game.questionInProgress) {
				Game.pause = !Game.pause;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			wDown = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			sDown = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			aDown = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			dDown = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			spawnBullet = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			spawnTriple = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_M) {
			spawnMine = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_R) {
			spawnRocket = false;
		}
	}

	public void bulletSpawn() {
		if (!spawnBullet) {
			spawnBullet = true;
			Game.bulletList.add(new Bullet(x, y, 0, -4, true));
		}
	}

	public void rocketSpawn() {
		if (!spawnRocket && ammoRocket > 0) {
			spawnRocket = true;
			Game.rocketList.add(new Rocket(x + 8, y, 8));
			ammoRocket--;
		}
	}

	public void tripleSpawn() {
		if (!spawnTriple && ammoTripleShot > 0) {
			spawnTriple = true;
			Game.tripleList.add(new TripleShot(x, y));
			ammoTripleShot--;
		}
	}

	public void mineSpawn() {
		if (!spawnMine && ammoMine > 0) {
			spawnMine = true;
			Game.mineList.add(new Mine(x, y));
			ammoMine--;
		}
	}

	public void gamePause(boolean p) {
		Game.pause = p;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getAmmoTripleShot() {
		return ammoTripleShot;
	}

	public void setAmmoTripleShot(int ammoTripleShot) {
		this.ammoTripleShot = ammoTripleShot;
	}

	public int getAmmoMine() {
		return ammoMine;
	}

	public void setAmmoMine(int ammoMine) {
		this.ammoMine = ammoMine;
	}

	public int getAmmoRocket() {
		return ammoRocket;
	}

	public void setAmmoRocket(int ammoRocket) {
		this.ammoRocket = ammoRocket;
	}

	public boolean iswDown() {
		return wDown;
	}

	public void setwDown(boolean wDown) {
		this.wDown = wDown;
	}

	public boolean issDown() {
		return sDown;
	}

	public void setsDown(boolean sDown) {
		this.sDown = sDown;
	}

	public boolean isaDown() {
		return aDown;
	}

	public void setaDown(boolean aDown) {
		this.aDown = aDown;
	}

	public boolean isdDown() {
		return dDown;
	}

	public void setdDown(boolean dDown) {
		this.dDown = dDown;
	}

	public int getxVelocity() {
		return xVelocity;
	}

	public void setxVelocity(int xVelocity) {
		this.xVelocity = xVelocity;
	}

	public int getyVelocity() {
		return yVelocity;
	}

	public void setyVelocity(int yVelocity) {
		this.yVelocity = yVelocity;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public boolean isSpawnBullet() {
		return spawnBullet;
	}

	public void setSpawnBullet(boolean spawnBullet) {
		this.spawnBullet = spawnBullet;
	}

	public boolean isSpawnTriple() {
		return spawnTriple;
	}

	public void setSpawnTriple(boolean spawnTriple) {
		this.spawnTriple = spawnTriple;
	}

	public boolean isSpawnMine() {
		return spawnMine;
	}

	public void setSpawnMine(boolean spawnMine) {
		this.spawnMine = spawnMine;
	}

	public boolean isSpawnRocket() {
		return spawnRocket;
	}

	public void setSpawnRocket(boolean spawnRocket) {
		this.spawnRocket = spawnRocket;
	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean isShieldEnabled() {
		return shieldEnabled;
	}

	public void setShieldEnabled(boolean shieldEnabled) {
		this.shieldEnabled = shieldEnabled;
	}
}
