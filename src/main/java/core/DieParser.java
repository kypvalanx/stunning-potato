package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.jetbrains.annotations.NotNull;

public class DieParser {
	private static final Random RAND = new Random();
	public static final Pattern P = Pattern.compile("\\([\\w\\s]*\\)");
	public static final String AND = " and ";

	public static List<DieResult> rollDiceGroups(DeckList<String> dieEquation) {
		return rollDiceGroups(String.join(" ", dieEquation.getDeck()));
	}

	public static List<DieResult> rollDiceGroups(@NotNull String dieEquation) {
		List<DieResult> dieResults = _rollDice(dieEquation);
		dieResults.forEach(DieResult::freeze);
		return dieResults;
	}

	private static List<DieResult> _rollDice(@NotNull String dieEquation) {
		if (dieEquation.startsWith("roll ")) {
			System.err.println("getValue Die Value being called with dirty payload: " + dieEquation);
			dieEquation = dieEquation.substring(5);
		}

		dieEquation = dieEquation.trim();

		String[] equations = reduceEquations(dieEquation);

		return Arrays.stream(equations)
				.filter(Objects::nonNull)
				.filter(s -> !s.isBlank())
				.map(String::trim)
				.map(eq -> {
					String[] parts = eq.split("\\+");
					return Arrays.stream(parts).map(DieParser::getValue).reduce(
							(dieResult, dieResult2) -> new DieResult(dieResult.getSum() + dieResult2.getSum(),
									join(" + ", dieResult.getSteps(), dieResult2.getSteps()),
									dieResult.getMessage() + " " + dieResult2.getMessage()));
				}).map(o -> o.orElse(null)).filter(Objects::nonNull).collect(Collectors.toList());
	}

	private static String join(String delimiter, String... steps) {
		final String[] response = {""};

		Arrays.stream(steps).filter(s -> s != null && !s.isEmpty()).forEach(s -> {
			if(!response[0].isEmpty()){
				response[0] += delimiter;
			}
			response[0] += s;
		});

		return response[0];
	}

	private static String[] reduceEquations(@NotNull String equation) {
		boolean hasChanged = true;
		String[] equationStrings = new String[0];
		while (hasChanged) {
			hasChanged = false;
			equationStrings = equation.split(AND);

			for (int i = 0; i < equationStrings.length; i++) {
				if(equationStrings[i].startsWith("`") || equationStrings[i].startsWith("'")){
					continue;
				}
				equationStrings[i] = equationStrings[i].replace("-", "+-");
				String[] values = equationStrings[i].split("\\+");

				for (int j = 0; j < values.length; j++) {
					values[j] = values[j].trim();

					while (P.matcher(values[j]).find()) {
						values[j] = resolveParens(values[j]);
						hasChanged = true;
					}

					while (Variables.get(values[j]) != null) {
						values[j] = Variables.get(values[j]);
						hasChanged = true;
					}
				}
				equationStrings[i] = String.join("+", values);
			}

			if(hasChanged) {
				equation = String.join(AND, equationStrings);
			}
		}
		return equationStrings;
	}

	private static DieResult getValue(String value) {
		if(value.startsWith("'") || value.startsWith("`")){
			return new DieResult(value.substring(1));
		}

		int subtract = 1;
		if (value.startsWith("-")) {
			value = value.substring(1).trim();
			subtract = -1;
		}


		if (value.matches("\\d*d\\d+")) {
			return parseDieExpression(value, subtract);
		} else if (value.matches("\\d*e\\d+")) {
			return parseExplodingDieExpression(value, subtract);
		} else if (value.matches("\\d*c\\d+")) {
			return parseConfirmableDieExpression(value,subtract);
		} else if (value.matches("\\d+")) {
			int parsed = Integer.parseInt(value) * subtract;
			return new DieResult(parsed, "" + parsed, "");
		}

		throw new IllegalStateException(value + " Cannot be parsed or variable does not exist.");
	}

	@NotNull
	private static String resolveParens(String value) {
		Matcher matcher = P.matcher(value);
		while(matcher.find()){
			String group = matcher.group();
			String dieEquation = group.substring(1, group.length() - 1);

			String newValue = ""+ rollDiceGroups(dieEquation).get(0).getSum();
			value = value.replaceFirst(Pattern.quote(group), newValue);
		}

		return value;
	}

	private static DieResult parseConfirmableDieExpression(String value, int subtract) {
		List<String> steps = new ArrayList<>();
		String[] tokens = value.split("c");
		int range = tokens[0].isBlank()? 1 : Integer.parseInt(tokens[0]);
		int size = Integer.parseInt(tokens[1]);

		int roll = (RAND.nextInt(size) + 1) * subtract;

		steps.add(""+roll);

		if(size - range<roll){
			int confirm = RAND.nextInt(size) + 1;

			steps.add("CONFIRMATION DIE ROLL: "+confirm);
		}
		return new DieResult(roll, join(" + ", steps.toArray(new String[0])), "");
	}

	private static DieResult parseExplodingDieExpression(String value, int subtract) {
		List<String> steps = new ArrayList<>();

		String[] tokens = value.split("e");
		Integer times = null;

		if(!tokens[0].isBlank()){
			times = Integer.parseInt(tokens[0]);
		}

		int size = Integer.parseInt(tokens[1]);
		if(size == 1){
			return new DieResult("Exploding dice may not have a die size of 1");
		}
		int total = 0;
		while (times == null || times >= 0){
			int roll = (RAND.nextInt(size) + 1) *subtract;
			steps.add(""+roll);
			total += roll;

			if(Math.abs(roll) != size){
				break;
			}else if(times != null){
				times--;
			}
		}

		return new DieResult(total, join(" + ", steps.toArray(new String[0])), "");
	}

	private static DieResult parseDieExpression(String value, int subtract) {
		List<String> steps = new ArrayList<>();
		String[] tokens = value.split("d");
		int times = tokens[0].isBlank()? 1 : Integer.parseInt(tokens[0]);
		int size = Integer.parseInt(tokens[1]);
		int sum = IntStream.range(0, times).map(i -> {
					int roll = (RAND.nextInt(size) + 1) * subtract;
					steps.add("" + roll);
					return roll;
				}
		).reduce(0, Integer::sum);

		return new DieResult(sum, join(" + ", steps.toArray(new String[0])), "");
	}
}