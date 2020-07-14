package core;

import behavior.Behavior;
import behavior.ChannelHelper;
import behavior.GroupBehavior;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

public class Variables {

	public static final String PERSONAL_DELIMITER = "###";
	public static final String VAR_SET = "var_set";
	public static final String DEFAULT = "default";
	private static File varFile = new File("resources/generated/vars.yaml");
	private volatile Map<String, String> personalVariables = new HashMap<>();
	private volatile Map<String, String> variables = new HashMap<>();

	private volatile static Variables variablesO;


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
				vars.forEach(varPair -> {
					if(varPair.getKey().startsWith("<@") && varPair.getKey().contains(PERSONAL_DELIMITER)){
						personalVariables.put(varPair.getKey(), varPair.getValue());
					}else {
						variables.put(varPair.getKey(), varPair.getValue());
					}
				});
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
		getVariables().variables.put(key, value);
		getVariables().updateFile();
	}
	public static void putP(String key, String value){
		getVariables().personalVariables.put(getPersonalKey(key), value);
		getVariables().updateFile();
	}

	private static Variables getVariables() {
		if(variablesO == null){
			variablesO = createVariables();
		}
		return variablesO;
	}

	@NotNull
	private static Variables createVariables() {
		return new Variables(varFile);
	}

	public static String get(String key){
		String var = getVariables().personalVariables.get(getPersonalKey(key));
		if(var!= null){
			return var;
		}
		var = getVariables().personalVariables.get(getPersonalDefaultKey(key));
		if(var!= null){
			return var;
		}
		return getVariables().variables.get(key);
	}

	private static String getPersonalDefaultKey(String key) {
		if(VAR_SET.equals(key)){
			return Context.getCaller().getAsMention() + Variables.PERSONAL_DELIMITER + key;
		}

		return Context.getCaller().getAsMention() + Variables.PERSONAL_DELIMITER + DEFAULT + Variables.PERSONAL_DELIMITER + key;
	}

	@NotNull
	private static String getPersonalKey(String key) {
		if(VAR_SET.equals(key)){
			return Context.getCaller().getAsMention() + Variables.PERSONAL_DELIMITER + key;
		}

		return Context.getCaller().getAsMention() + Variables.PERSONAL_DELIMITER + getVarSet() + Variables.PERSONAL_DELIMITER + key;
	}

	private static String getVarSet() {
		String s = getVariables().personalVariables.get(getPersonalKey(VAR_SET));
		if(s == null){
			return DEFAULT;
		}
		return s;
	}

	public static boolean containsKey(String key) {
		return getVariables().variables.containsKey(key);
	}
	public static boolean containsKeyP(String key) {
		return getVariables().personalVariables.containsKey(getPersonalKey(key));
	}

	public static String remove(String key) {
		String remove = getVariables().variables.remove(key);
		getVariables().updateFile();
		return remove;
	}
	public static String removeP(String key) {
		String remove = getVariables().personalVariables.remove(getPersonalKey(key));
		getVariables().updateFile();
		return remove;
	}

	private void updateFile() {
		varFile.delete();
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
		try {
			varFile.createNewFile();

			ArrayList<Map.Entry<String, String>> combined = Lists.newArrayList(variables.entrySet());

			combined.addAll(personalVariables.entrySet());

			List<VarPair> vars = combined.stream()
					.map(stringStringEntry -> new VarPair(stringStringEntry.getKey(), stringStringEntry.getValue()))
					.collect(Collectors.toList());

			mapper.writeValue(varFile, vars);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Set<Map.Entry<String, String>> entrySet() {
		return getVariables().variables.entrySet();
	}
	public static Set<Map.Entry<String, String>> entrySetP() {
		return getVariables().personalVariables.entrySet();
	}

	public static Behavior getVarBehavior() {
		return new GroupBehavior()
				.add(new String[]{"add", "set"}, new Behavior() {
					@Override
					public void run(DeckList<String> message, MessageChannel channel) {
						String key = message.draw().replace("_", " ");
						String value = String.join(" ", message.getDeck());
						if (value.isBlank()) {
							channel.sendMessage("variable " + key + " requires value").queue();
						} else {
							put(key, value);
							channel.sendMessage(key + " => " + value + " saved").queue();
						}
					}

					@Override
					public String getHelp(DeckList<String> s, String key) {
						return "Adds a new variable that can be run as a command or used as a payload for the roll command.";
					}
				})
				.add(new String[]{"remove", "r"}, new Behavior() {
					@Override
					public void run(DeckList<String> message, MessageChannel channel) {
						String key = message.draw();
						if (containsKey(key)) {
							remove(key);
							channel.sendMessage("variable '" + key + "' removed").queue();
						} else {
							channel.sendMessage("variable '" + key + "' doesn't exist").queue();
						}
					}

					@Override
					public String getHelp(DeckList<String> s, String key) {
						return "Removes a variable.";
					}
				})
				.add(new String[]{"list"}, new Behavior() {
					@Override
					public void run(DeckList<String> message, MessageChannel channel) {
						StringBuilder builder = new StringBuilder();
						for (Map.Entry<String, String> entry : entrySet()) {
							builder.append(entry.getKey()).append(" => ").append(entry.getValue()).append("\n");
						}
						String text = builder.toString();
						if(text.length() > 0) {
							channel.sendMessage(text).queue();
						}
					}

					@Override
					public String getHelp(DeckList<String> s, String key) {
						return "Lists available variables.";
					}
				});
	}


	public static Behavior getPVarBehavior() {
		return new GroupBehavior(new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				if(message.canDraw()){
					String join = String.join(" ", message.getDeck());
					putP(VAR_SET, join);
					channel.sendMessage("now using variable set: " + join).queue();
				} else {
					List<String> varSets = entrySetP().stream().map(e -> {
						String[] tok = e.getKey().split(PERSONAL_DELIMITER);
						if(tok.length<3){
							return null;
						}

						if (tok[0].equals(Context.getCaller().getAsMention())) {
							return tok[1];
						}
						return null;
					}).filter(Objects::nonNull).distinct().collect(Collectors.toList());

					channel.sendMessage("Available variable sets:\n"+String.join("\n", varSets)).queue();
				}
			}
			@Override
			public String getHelp(DeckList<String> s, String key) {
				return "sets or lists available variable sets for a player";
			}
		})
				.add(new String[]{"add", "set"}, new Behavior() {
					@Override
					public void run(DeckList<String> message, MessageChannel channel) {
						String key = message.draw().replace("_", " ");
						String value = String.join(" ", message.getDeck());
						if (value.isBlank()) {
							channel.sendMessage("variable " + key + " requires value").queue();
						} else {
							putP(key, value);
							channel.sendMessage(key + " => " + value + " saved").queue();
						}
					}

					@Override
					public String getHelp(DeckList<String> s, String key) {
						return "Adds a new variable that can be run as a command or used as a payload for the roll command.";
					}
				})
				.add(new String[]{"remove", "r"}, new Behavior() {
					@Override
					public void run(DeckList<String> message, MessageChannel channel) {
						String key = message.draw();
						if (containsKeyP(key)) {
							removeP(key);
							channel.sendMessage("variable '" + key + "' removed").queue();
						} else {
							channel.sendMessage("variable '" + key + "' doesn't exist").queue();
						}
					}

					@Override
					public String getHelp(DeckList<String> s, String key) {
						return "Removes a variable.";
					}
				})
				.add(new String[]{"remove set", "r set"}, new Behavior() {
					@Override
					public void run(DeckList<String> message, MessageChannel channel) {
						String key = String.join(" ", message.getDeck());

						putP(VAR_SET, key);

						Set<Map.Entry<String, String>> entries = Set.copyOf(entrySetP());
						String messages = entries.stream().filter(e -> {
							String[] tok = e.getKey().split(PERSONAL_DELIMITER);
							return tok[0].equals(Context.getCaller().getAsMention()) && (tok[1].equals(key));
						}).map(entry -> {
							String[] tok = entry.getKey().split(PERSONAL_DELIMITER);
							if (containsKeyP(tok[2])) {
								removeP(tok[2]);
								return "variable '" + tok[2] + "' removed";
							} else {
								return "variable '" + tok[2] + "' doesn't exist";
							}
						})
								.collect(Collectors.joining("\n"));
						ChannelHelper.sendLongMessage("\n",messages, channel);
					}

					@Override
					public String getHelp(DeckList<String> s, String key) {
						return "Removes a variable set.";
					}
				})
				.add(new String[]{"list"}, new Behavior() {
					@Override
					public void run(DeckList<String> message, MessageChannel channel) {
						StringBuilder builder = new StringBuilder();
						for (Map.Entry<String, String> entry : entrySetP().stream().filter(e -> {
							String[] tok = e.getKey().split(PERSONAL_DELIMITER);
							return tok[0].equals(Context.getCaller().getAsMention()) && (tok[1].equals(getVarSet()) || tok[1].equals(DEFAULT));
						}).sorted(Comparator.comparing(Map.Entry::getKey)).collect(Collectors.toList())) {
							String[] tokens = entry.getKey().split(PERSONAL_DELIMITER);
							builder.append("*").append(tokens[1]).append("* ").append(tokens[2]).append(" => ").append(entry.getValue()).append("\n");
						}
						String text = builder.toString();
						if(text.length() > 0) {
							ChannelHelper.sendLongMessage("\n", getVarSet()+" variables:\n"+text, channel);
						}
					}

					@Override
					public String getHelp(DeckList<String> s, String key) {
						return "Lists available variables.";
					}
				})				.add(new String[]{"list all"}, new Behavior() {
					@Override
					public void run(DeckList<String> message, MessageChannel channel) {
						StringBuilder builder = new StringBuilder();
						for (Map.Entry<String, String> entry : entrySetP()) {
							String[] tokens = entry.getKey().split(PERSONAL_DELIMITER);
							builder.append(String.join("  ", tokens)).append(" => ").append(entry.getValue()).append("\n");
						}
						String text = builder.toString();
						if(text.length() > 0) {
							channel.sendMessage(text).queue();
						}
					}

					@Override
					public String getHelp(DeckList<String> s, String key) {
						return "Lists available variables.";
					}
				});
	}

}
