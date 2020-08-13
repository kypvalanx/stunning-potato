package org.gary.dice;

import com.google.common.base.MoreObjects;

public class DieResult {
	private String message;
	private String steps;
	private int sum;
	private boolean freeze = false;

	public DieResult(int sum, String steps, String message) {
		this.sum = sum;
		this.steps = steps;
		this.message = message;
	}

	public DieResult(String message) {
		this.message = message;
	}

    public String getSteps() {
		return steps;
	}

	public int getSum() {
		return sum;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("steps", steps)
				.add("sum", sum)
				.add("message", message)
				.toString();
	}

	public void freeze() {
		if(freeze){
			throw new IllegalStateException("WTF this is frozen");
		}
		if(steps != null) {
			freeze = true;
			steps = "{ " + steps + " }";
		}
	}
}
