package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jetbrains.annotations.NotNull;

public class DieParser {
	private final List<Integer> steps = new ArrayList<>();
	private static final Random RAND = new Random();

	public static DieResult rollDice(DeckList<String> dieEquation) {
		return rollDice(String.join(" ", dieEquation.getDeck()));
	}

	public static DieResult rollDice(@NotNull String dieEquation) {

		return new DieParser()._rollDice(dieEquation);
	}

	public static List<DieResult> rollDiceGroups(DeckList<String> dieEquation) {
		return rollDiceGroups(String.join(" ", dieEquation.getDeck()));
	}

	public static List<DieResult> rollDiceGroups(@NotNull String dieEquation) {
		String[] dieEquations = dieEquation.split(" and ");

		return Arrays.stream(dieEquations).map(eq -> new DieParser()._rollDice(eq)).collect(Collectors.toList());
	}

	@NotNull
	private DieResult _rollDice(@NotNull String dieEquation) {
		if (dieEquation.startsWith("roll ")) {
			System.err.println("getValue Die Value being called with dirty payload: " + dieEquation);
			dieEquation = dieEquation.substring(5);
		}
		dieEquation = dieEquation.trim();
		if(dieEquation.startsWith("'") || dieEquation.startsWith("`")){
			return new DieResult(dieEquation.substring(1));
		}

		String digest = dieEquation.replace("-", "+-");
		String[] values = digest.split("\\+");

		int sum = Arrays.stream(values)
				.filter(Objects::nonNull)
				.filter(s -> !s.isBlank())
				.map(String::trim)
				.map(value -> {
					int subtract = 1;
					if (value.startsWith("-")) {
						value = value.substring(1);
						subtract = -1;
					}
					return getValue(value.trim(), subtract);
				}).reduce(0, Integer::sum);

		return new DieResult(sum, "{ " + String.join(" + ", " " + steps).replace(" + -", " - ") + " }");
	}

	private int getValue(String value, int subtract) {
		if (value.matches("\\d*d\\d+")) {
			return parseDieExpression(value) * subtract;
		} else if (value.matches("\\d*e\\d+")) {
			return parseExplodingDieExpression(value) * subtract;
		} else if (value.matches("\\d+")) {
			int parsed = Integer.parseInt(value) * subtract;
			steps.add(parsed);
			return parsed;
		}
		return _rollDice(Variables.findVariable(value)).getSum() * subtract;
	}

	private int parseExplodingDieExpression(String value) {
		String[] tokens = value.split("e");
		int times = tokens[0].isBlank()? -1 : Integer.parseInt(tokens[0]);
		int size = Integer.parseInt(tokens[1]);
		if(size == 1){
			return -1;
		}
		int total = 0;
		while (times != 0){
			int roll = RAND.nextInt(size) + 1;
			steps.add(roll);
			total += roll;

			if(roll != size){
				break;
			}else {
				times--;
			}
		}

		return total;
	}

	private int parseDieExpression(String value) {
		String[] tokens = value.split("d");
		int times = tokens[0].isBlank()? 1 : Integer.parseInt(tokens[0]);
		int size = Integer.parseInt(tokens[1]);
		return IntStream.range(0, times).map(i -> {
					int roll = RAND.nextInt(size) + 1;
					steps.add(roll);
					return roll;
				}
		).reduce(0, Integer::sum);
	}
}