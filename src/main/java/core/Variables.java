package core;

import behavior.Behavior;
import behavior.GroupBehavior;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Variables {

	private Variables(){

	}

	private static Map<String, String> variables = new HashMap<>();


	public static void put(String key, String value){
		variables.put(key, value);
	}

	public static String get(String key){
		return variables.get(key);
	}

	public static boolean containsKey(String key) {
		return variables.containsKey(key);
	}

	public static String remove(String key) {
		return variables.remove(key);
	}

	public static Set<Map.Entry<String, String>> entrySet() {
		return variables.entrySet();
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
						event.getChannel().sendMessage(builder.toString()).queue();
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
