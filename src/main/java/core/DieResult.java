package core;

import com.google.common.base.MoreObjects;

public class DieResult {
	private final String steps;
	private final int sum;

	public DieResult(int sum, String steps) {
		this.sum = sum;
		this.steps = steps;
	}

	public String getSteps() {
		return steps;
	}

	public int getSum() {
		return sum;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("steps", steps)
				.add("sum", sum)
				.toString();
	}
}
