package org.TicTacToeCLI;

import java.util.ArrayList;
import java.util.HashMap;

public class GameField {

	public static String toString_(ArrayList<ArrayList<Integer>> old) {
		int height = old.size();
		int width = old.get(0).size();
		Character[][] table = new Character[height * 2 + 1][width * 4 + 1];
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

		HashMap<Integer, Character> m = new HashMap<>();

		m.put(111, 'o');
		m.put(120, 'x');
		m.put(0, ' ');

		height = 0;
		for (ArrayList<Integer> list : old) {
			width = 0;
			for (Integer i :list) {
				table[height * 2 + 1][width * 4 + 2] = m.get(i);
				width++;
			}
			height++;
		}

		StringBuilder res = new StringBuilder();
		for (Character[] line : table) {
			for (Character c : line) {
				res.append(c);
			}
			res.append('\n');
		}
		return res.toString();
	}

//	public void putCharacter(Character c, int height, int width) {
//
//	}

//	@Override
//	public String toString() {
//
//	}
}
