package io.github.lmnch.sudokusolver;

import java.util.ArrayList;
import java.util.List;

public class Sudoku {

	private Cell[][] cells = new Cell[][] {
			new Cell[] { new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell()
			},
			new Cell[] { new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell()
			},
			new Cell[] { new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell()
			},
			new Cell[] { new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell()
			},
			new Cell[] { new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell()
			},
			new Cell[] { new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell()
			},
			new Cell[] { new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell()
			},
			new Cell[] { new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell()
			},
			new Cell[] { new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell(),
					new Cell(), new Cell(), new Cell()
			},
	};

	public Sudoku(Integer[][] inputValues) {
		for (int x = 0; x < 9; x++) {
			for (int y = 0; y < 9; y++) {
				if (inputValues[y][x] != null) {
					this.cells[y][x].placeSolution(inputValues[y][x]);
					this.updatedCell(x, y);
				}
			}
		}
		this.solve();
	}

	protected void solve() {
		boolean sthChanged = true;
		while (sthChanged) {
			sthChanged = false;
			for (int y = 0; y < 9; y++) {
				boolean rowUpdated = this.checkRow(y);
				sthChanged = sthChanged || rowUpdated;
			}
			for (int x = 0; x < 9; x++) {
				boolean colUpdated = this.checkCol(x);
				sthChanged = sthChanged || colUpdated;
			}

			for (int sqX = 0; sqX < 3; sqX++) {
				for (int sqY = 0; sqY < 3; sqY++) {
					boolean squareUpdated = this.checkSquare(sqX, sqY);
					sthChanged = sthChanged || squareUpdated;
				}
			}
		}
	}

	protected boolean checkRow(int y) {
		for (int x = 0; x < 9; x++) {
			Cell cell = this.cells[y][x];
			if (!cell.valueKnown()) {
				for (Integer value : cell.getPotentialValues()) {
					boolean noneContainsValue = true;
					// check if none other cell in the row contains the value
					for (int x2 = 0; x2 < 9; x2++) {
						if (x != x2) {
							noneContainsValue =
									noneContainsValue && !this.cells[y][x2].getPotentialValues().contains(value);
						}
					}
					if (noneContainsValue) {
						cell.placeSolution(value);
						this.updatedCell(x, y);
						return true;
					}
				}
			}
		}
		return false;
	}

	protected boolean checkCol(final int x) {
		for (int y = 0; y < 9; y++) {
			Cell cell = this.cells[y][x];
			if (!cell.valueKnown()) {
				for (Integer value : cell.getPotentialValues()) {
					boolean noneContainsValue = true;
					// check if none other cell in the row contains the value
					for (int y2 = 0; y2 < 9; y2++) {
						if (y != y2) {
							noneContainsValue =
									noneContainsValue && !this.cells[y2][x].getPotentialValues().contains(value);
						}
					}
					if (noneContainsValue) {
						cell.placeSolution(value);
						this.updatedCell(x, y);
						return true;
					}
				}
			}
		}
		return false;
	}

	protected boolean checkSquare(final int sqX, final int sqY) {
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				Cell cell = this.cells[sqY * 3 + y][sqX * 3 + x];
				if (!cell.valueKnown()) {
					for (Integer value : cell.getPotentialValues()) {
						boolean noneContainsValue = true;

						for (int x2 = 0; x2 < 3; x2++) {
							for (int y2 = 0; y2 < 3; y2++) {
								if (x != x2 && y != y2) {
									noneContainsValue =
											noneContainsValue
													&& !this.cells[sqY * 3 + y2][sqX * 3 + x2].getPotentialValues()
															.contains(value);
								}
							}
						}

						if (noneContainsValue) {
							cell.placeSolution(value);
							this.updatedCell(sqX * 3 + x, sqY * 3 + y);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	protected void updatedCell(int x, int y) {
		List<int[]> updatedCells = new ArrayList<>();
		if (this.cells[y][x].valueKnown()) {
			int value = this.cells[y][x].determinedValue();
			for (int updateX = 0; updateX < 9; updateX++) {
				if (!this.cells[y][updateX].valueKnown()) {
					this.cells[y][updateX].removePotentialSolution(value);
					updatedCells.add(new int[] { updateX, y });
				}
			}
			for (int updateY = 0; updateY < 9; updateY++) {
				if (!this.cells[updateY][x].valueKnown()) {
					this.cells[updateY][x].removePotentialSolution(value);
					updatedCells.add(new int[] { x, updateY });
				}
			}

			// Update group
			int squareX = x / 3;
			int squareY = y / 3;
			for (int updateXIndex = 0; updateXIndex < 3; updateXIndex++) {
				int updateX = 3 * squareX + updateXIndex;
				for (int updateYIndex = 0; updateYIndex < 3; updateYIndex++) {
					int updateY = 3 * squareY + updateYIndex;
					if (!this.cells[updateY][updateX].valueKnown()) {
						this.cells[updateY][updateX].removePotentialSolution(value);
						updatedCells.add(new int[] { updateX, updateY });
					}
				}
			}
		}
		for (int[] updated : updatedCells) {
			this.updatedCell(updated[0], updated[1]);
		}
	}

	public String toString() {
		int y = 0;
		int x = 0;

		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < 13; j++) {
			if (j % 4 == 0) {
				for (int i = 0; i < 25; i++) {
					sb.append("_");
				}
				y--;
			} else {
				for (int i = 0; i < 26; i++) {
					if (i % 8 == 0) {
						sb.append("|");
					} else if (i % 2 == 1) {
						sb.append(" ");
					} else {
						Cell cell = this.cells[y][x];
						if (cell.valueKnown()) {
							sb.append(cell.determinedValue());
						} else {
							sb.append(" ");
						}
						x++;
					}
				}
			}
			x = 0;
			y++;
			sb.append("\n");
		}
		return sb.toString();
	}

	public Cell getCell(int x, int y) {
		return this.cells[y][x];
	}
}
