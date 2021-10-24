package io.github.lmnch.sudokusolver;

public class Test {

	public static void main(String[] args) {
		Integer[][] input = new Integer[9][9];
		for (int y = 0; y < 9; y++) {
			for(int x=0; x < 9; x++) {
				final char value = args[y].charAt(x);
				if (value != '-') {
					input[y][x] = Integer.parseInt("" + value);
				}
			}
		}
		
		final Sudoku sudoku = new Sudoku(input);
		System.out.print(sudoku.toString());
	}

}
