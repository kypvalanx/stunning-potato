package core;

import com.google.common.collect.Lists;
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
	private final List<String> steps = new ArrayList<>();
	private static final Random RAND = new Random();
	public static final Pattern P = Pattern.compile("\\([\\w\\s]*\\)");

	@Deprecated
	public static DieResult rollDice(DeckList<String> dieEquation) {
		return rollDice(String.join(" ", dieEquation.getDeck()));
	}

	@Deprecated
	public static DieResult rollDice(@NotNull String dieEquation) {

		return new DieParser()._rollDice(dieEquation).get(0);
	}

	public static List<DieResult> rollDiceGroups(DeckList<String> dieEquation) {
		return rollDiceGroups(String.join(" ", dieEquation.getDeck()));
	}

	public static List<DieResult> rollDiceGroups(@NotNull String dieEquation) {
		String[] dieEquations = dieEquation.split(" and ");

		return Arrays.stream(dieEquations).map(eq -> new DieParser()._rollDice(eq)).flatMap(List::stream).collect(Collectors.toList());
	}

	private List<DieResult> _rollDice(@NotNull String dieEquation) {
		if (dieEquation.startsWith("roll ")) {
			System.err.println("getValue Die Value being called with dirty payload: " + dieEquation);
			dieEquation = dieEquation.substring(5);
		}
		dieEquation = dieEquation.trim();
		if(dieEquation.startsWith("'") || dieEquation.startsWith("`")){
			return Lists.newArrayList(new DieResult(dieEquation.substring(1)));
		}

		String digest = dieEquation.replace("-", "+-");
		String[] values = digest.split("\\+");

		int sum = Arrays.stream(values)
				.filter(Objects::nonNull)
				.filter(s -> !s.isBlank())
				.map(String::trim)
				.map(value -> {
					while(P.matcher(value).find()){
						value = resolveParens(value);
					}

					while (Variables.findVariable(value) != null){
						value = resolveVars(value);
					}

					int subtract = 1;
					if (value.startsWith("-")) {
						value = value.substring(1);
						subtract = -1;
					}
					return getValue(value.trim(), subtract);
				}).reduce(0, Integer::sum);

		return Lists.newArrayList(new DieResult(sum, "{ " + String.join(" + ", " " + steps).replace(" + -", " - ") + " }"));
	}

	private int getValue(String value, int subtract) {
		if (value.matches("\\d*d\\d+")) {
			return parseDieExpression(value, subtract);
		} else if (value.matches("\\d*e\\d+")) {
			return parseExplodingDieExpression(value, subtract);
		} else if (value.matches("\\d*c\\d+")) {
			return parseConfirmableDieExpression(value,subtract);
		} else if (value.matches("\\d+")) {
			int parsed = Integer.parseInt(value) * subtract;
			steps.add(""+parsed);
			return parsed;
		}

		throw new IllegalStateException(value + " Cannot be parsed or variable does not exist.");
	}

	@NotNull
	private String resolveParens(String value) {
		Matcher matcher = P.matcher(value);
		while(matcher.find()){
			String group = matcher.group();
			String dieEquation = group.substring(1, group.length() - 1);
			String newValue = ""+rollDice(dieEquation).getSum();
			value = value.replaceFirst(Pattern.quote(group), newValue);
		}

		return value;
	}

	private String resolveVars(String var) {
		String value = Variables.findVariable(var);
		return ""+rollDice(value).getSum();
	}

	private int parseConfirmableDieExpression(String value, int subtract) {
		String[] tokens = value.split("c");
		int range = tokens[0].isBlank()? 1 : Integer.parseInt(tokens[0]);
		int size = Integer.parseInt(tokens[1]);

		int roll = (RAND.nextInt(size) + 1) * subtract;

		steps.add(""+roll);

		if(size - range<roll){
			int confirm = RAND.nextInt(size) + 1;

			steps.add("CONFIRMATION DIE ROLL: "+confirm);
		}
		return roll;
	}

	private int parseExplodingDieExpression(String value, int subtract) {
		String[] tokens = value.split("e");
		Integer times = null;

		if(!tokens[0].isBlank()){
			times = Integer.parseInt(tokens[0]);
//			if(subtract< 0){
//				throw new IllegalStateException("Exploding dice may not have a limit less than 1");
//			}
		}

		int size = Integer.parseInt(tokens[1]);
		if(size == 1){
			throw new IllegalStateException("Exploding dice may not have a die size of 1");
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

		return total;
	}

	private int parseDieExpression(String value, int subtract) {
		String[] tokens = value.split("d");
		int times = tokens[0].isBlank()? 1 : Integer.parseInt(tokens[0]);
		int size = Integer.parseInt(tokens[1]);
		return IntStream.range(0, times).map(i -> {
					int roll = (RAND.nextInt(size) + 1) * subtract;
					steps.add(""+roll);
					return roll;
				}
		).reduce(0, Integer::sum);
	}
}