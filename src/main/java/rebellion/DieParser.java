package rebellion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DieParser {
    private final Map<String, String> variables;
    private final List<String> steps = new ArrayList<>();
    private int sum;

    public DieParser(Map<String, String> variables) {
        this.variables = variables;
    }

    public int parseDieValue(String payload) {
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
                parsed= Integer.parseInt(value) * subtract;
                steps.add(""+parsed);
            } else {
                parsed= findVariable(value) * subtract;
            }
            sum += parsed;
        }

        return sum;
    }

    private int findVariable(String value) {
        return parseDieValue(variables.computeIfAbsent(value, s -> {
            throw new IllegalStateException("couldn't find " + s);
        }));
    }

    private int parseDie(String value) {
        String[] tokens = value.split("d");
        if(tokens[0].isBlank()){
            tokens[0] = "1";
        }
        int times = Integer.parseInt(tokens[0]);
        int size = Integer.parseInt(tokens[1]);
        Random rand = new Random();
        int sum = 0;
        for (int i = 0; i < times; i++) {
            final int roll = rand.nextInt(size) + 1;
            sum += roll;

            steps.add(""+roll);
        }
        return sum;
    }

    public String getSteps(){
        return "{ "+String.join(" + ", steps).replace(" + -", " - ")+" }";

    }

    public int getRoll(){
        return sum;
    }
}