package gui;

import java.util.Random;

public class AI {
	static int level;

	public AI() {
		level = 1;
		spawnEnemies();
	}

	public static void spawnEnemies() {
		for (int i = 0; i < (level * level); i++) {
			int targetX = randInt(30, Game.width - 30);
			int targetY = randInt(30, Game.height / 2);
			int targetVelocity = randInt(1, 4);
			int targetSize = 0;
			float alpha = 1.0f;

			switch (targetVelocity) {
			case 1:
				targetSize = 20;
				alpha = 0.4f;
				break;
			case 2:
				targetSize = 25;
				alpha = 0.6f;
				break;
			case 3:
				targetSize = 30;
				alpha = 0.8f;
				break;
			case 4:
				targetSize = 35;
				alpha = 1.0f;
				break;
			}

			Game.targetList.add(new Target(targetX, targetY, targetVelocity, targetSize, alpha));
		}
		level++;
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}
}
