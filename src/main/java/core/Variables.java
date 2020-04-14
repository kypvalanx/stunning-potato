package core;

import behavior.Behavior;
import behavior.GroupBehavior;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class Variables {

	private static File varFile = new File("resources/generated/vars.yaml");

	private Map<String, String> variables = new HashMap<>();

	private static Variables variablesO;


	private Variables(File varFile){
		if (varFile.canRead()) {
			if(varFile.length() == 0){
				return;
			}
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

			TypeFactory typeFactory = mapper.getTypeFactory();

			CollectionType mapType = typeFactory.constructCollectionType(ArrayList.class, VarPair.class);

			try {
				List<VarPair> vars =  mapper.readValue(varFile, mapType);
				vars.forEach(varPair -> variables.put(varPair.getKey(), varPair.getValue()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				varFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void put(String key, String value){
		getVariables().put(key, value);
		variablesO.updateFile();
	}

	private static Map<String, String> getVariables() {
		if(variablesO == null){
			variablesO = createVariables();
		}
		return variablesO.variables;
	}

	@NotNull
	private static Variables createVariables() {
		return new Variables(varFile);
	}

	public static String get(String key){
		return getVariables().get(key);
	}

	public static boolean containsKey(String key) {
		return getVariables().containsKey(key);
	}

	public static String remove(String key) {
		String remove = getVariables().remove(key);
		variablesO.updateFile();
		return remove;
	}

	private void updateFile() {
		varFile.delete();
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
		try {
			varFile.createNewFile();

			List<VarPair> vars = variables.entrySet().stream()
					.map(stringStringEntry -> new VarPair(stringStringEntry.getKey(), stringStringEntry.getValue()))
					.collect(Collectors.toList());

			mapper.writeValue(varFile, vars);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Set<Map.Entry<String, String>> entrySet() {
		return getVariables().entrySet();
	}

	public static Behavior getVarBehavior() {
		return new GroupBehavior()
				.add(new String[]{"add"}, new Behavior() {
					@Override
					public void run(MessageReceivedEvent event, DeckList<String> message) {
						String key = message.draw();
						String value = String.join(" ", message.getDeck());
						if (value.isBlank()) {
							event.getChannel().sendMessage("variable " + key + " requires value").queue();
						} else {
							put(key, value);
							event.getChannel().sendMessage(key + " => " + value + " saved").queue();
						}
					}
				})
				.add(new String[]{"remove"}, new Behavior() {
					@Override
					public void run(MessageReceivedEvent event, DeckList<String> message) {
						String key = message.draw();
						if (containsKey(key)) {
							remove(key);
							event.getChannel().sendMessage("variable '" + key + "' removed").queue();
						} else {
							event.getChannel().sendMessage("variable '" + key + "' doesn't exist").queue();
						}
					}
				})
				.add(new String[]{"list"}, new Behavior() {
					@Override
					public void run(MessageReceivedEvent event, DeckList<String> message) {
						StringBuilder builder = new StringBuilder();
						for (Map.Entry<String, String> entry : entrySet()) {
							builder.append(entry.getKey()).append(" => ").append(entry.getValue()).append("\n");
						}
						String text = builder.toString();
						if(text.length() > 0) {
							event.getChannel().sendMessage(text).queue();
						}
					}
				});
	}

	public static String findVariable(String key) {
		String value = get(key);

		if (value == null) {
			throw new IllegalStateException("couldn't find " + key);
		}
		return value;
	}
}
