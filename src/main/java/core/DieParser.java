package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class DieParser {
	private final List<String> steps = new ArrayList<>();
	private int sum;

	public DieParser() {
	}

	public int parseDieValue(DeckList<String> message) {

		return parseDieValue(String.join(" ", message.getDeck()));
	}

	public int parseDieValue(String payload) {
		if (payload.startsWith("roll ")) {
			throw new RuntimeException("parse Die Value being called with dirty payload: " + payload);
		}
		String digest = payload.replace("-", "+-");
		String[] values = digest.split("\\+");

		sum = 0;

		for (String value : values) {
			int subtract = 1;
			if (value.startsWith("-")) {
				value = value.substring(1);
				subtract = -1;
			}
			value = value.trim();
			int parsed;
			if (value.matches("\\d*d\\d+")) {
				parsed = parseDie(value) * subtract;
			} else if (value.matches("\\d+")) {
				parsed = Integer.parseInt(value) * subtract;
				steps.add("" + parsed);
			} else {
				parsed = findVariable(value) * subtract;
			}
			sum += parsed;
		}

		return sum;
	}

	private int findVariable(String key) {
		String value = Variables.get(key);

		if (value == null) {
			throw new IllegalStateException("couldn't find " + key);
		}
		return parseDieValue(value);
	}

	private int parseDie(String value) {
		String[] tokens = value.split("d");
		if (tokens[0].isBlank()) {
			tokens[0] = "1";
		}
		int times = Integer.parseInt(tokens[0]);
		int size = Integer.parseInt(tokens[1]);
		Random rand = new Random();
		return IntStream.range(0, times).parallel().map(i -> {
					int roll = rand.nextInt(size) + 1;
					steps.add("" + roll);
					return roll;
				}
		).reduce(0, (left, right) -> left + right);
	}

	public String getSteps() {
		return "{ " + String.join(" + ", steps).replace(" + -", " - ") + " }";

	}

	public int getRoll() {
		return sum;
	}
}