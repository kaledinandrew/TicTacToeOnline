package org.TicTacToeCLI;

public class GameField {
	private Character[][] table;

	public GameField(int height, int width) {
		table = new Character[height * 2 + 1][width * 4 + 1];
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				if (i % 2 == 0 && j % 4 == 0) {
					table[i][j] = '+';
				} else if (i % 2 == 1 && j % 4 == 0) {
					table[i][j] = '|';
				} else if (i % 2 == 0) {
					table[i][j] = '-';
				} else {
					table[i][j] = ' ';
				}

			}
		}
	}

	public void putCharacter(Character c, int height, int width) {
		table[height * 2 + 1][width * 4 + 2] = c;
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		for (Character[] line : table) {
			for (Character c : line) {
				res.append(c);
			}
			res.append('\n');
		}
		return res.toString();
	}
}
