package io.github.lmnch.sudokusolver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cell {

	private List<Integer> potentialValues = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));

	public void placeSolution(int i) {
		if (!this.potentialValues.contains(i)) {
			throw new IllegalArgumentException("The number '" + i + "' cannot be placed here!");
		}

		this.potentialValues.removeIf(value -> value != i);
	}

	public void removePotentialSolution(int i) {
		this.potentialValues.removeIf(value -> value == i);
	}

	public boolean valueKnown() {
		return this.potentialValues.size() == 1;
	}

	public List<Integer> getPotentialValues() {
		return Collections.unmodifiableList(this.potentialValues);
	}

	public int determinedValue() {
		if (!this.valueKnown()) {
			throw new IllegalStateException("The value of this cell is not known yet.");
		}
		return this.potentialValues.get(0);
	}

	@Override
	public String toString() {
		if (this.valueKnown()) {
			return String.valueOf(this.determinedValue());
		}
		return "{" + String.join(",", this.potentialValues.stream().map(i -> i.toString()).toList()) + "}";
	}
}
