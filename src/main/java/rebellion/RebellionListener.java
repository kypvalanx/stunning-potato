package rebellion;

import behavior.Behavior;
import behavior.GroupBehavior;
import behavior.NachoHelpBehavior;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import rebellion.events.RebellionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RebellionListener extends ListenerAdapter {
	private Integer checkDC = null;
	private String failureString = null;

	private final GroupBehavior primaryContext;
	private final GroupBehavior defaultContext;
	private Map<String, Rebellion> rebellions;
	private Rebellion currentRebellion;
	private Map<String, String> variables;
	private Context currentContext = Context.DEFAULT;

	public RebellionListener() {
		rebellions = new HashMap<>();
		currentRebellion = new Rebellion();
		variables = new HashMap<>();

		final GroupBehavior rebellion = getRebellionBehavior();

		Behavior roll = getRollBehavior();

		Behavior var = getVarBehavior();

		Behavior dc = new GroupBehavior()
				.setDefault(((event, message) -> {
					if(checkDC == null){
						event.getChannel().sendMessage("No current set DC.").queue();
					}else {
						event.getChannel().sendMessage("The Next die roll will be checked against DC " + checkDC).queue();
					}
				}))
				.add("set", (event, message) -> {
					checkDC = Integer.parseInt(message.remove(0));
					if(!message.isEmpty()) {
						failureString = String.join(" ", message);
					}
					event.getChannel().sendMessage("The Next die roll will be checked against DC " + checkDC).queue();
				});

		primaryContext = new GroupBehavior()
				.add(rebellion, "rebellion", "r")
				.add(roll, "roll")
				.add(var, "var")
				.add("dc", dc);

		Behavior help = new NachoHelpBehavior(primaryContext);
		primaryContext.add(help, "help");


		defaultContext = new GroupBehavior().add(((event, message) -> currentContext = Context.DEFAULT), "quit", "exit");

	}

	private GroupBehavior getRebellionBehavior() {
		return new GroupBehavior()
				.add(this::handleRebellionSheet, "sheet")

				.add((event, message) -> {
					currentRebellion.setSupporters(parseMessage(String.join(" ", message)));

					event.getChannel().sendMessage("Current Supporters " + currentRebellion.getRebellionSupporters()).queue();
				}, "set supporters")
				.add((event, message) -> {
					currentRebellion.addSupporters(parseMessage(String.join(" ", message)));

					event.getChannel().sendMessage("Current Supporters " + currentRebellion.getRebellionSupporters()).queue();
				}, "add supporters")
				.add((event, message) -> {
					currentRebellion.addSupporters(-parseMessage(String.join(" ", message)));

					event.getChannel().sendMessage("Current Supporters " + currentRebellion.getRebellionSupporters()).queue();
				}, "subtract supporters")


				.add((event, message) -> {
					currentRebellion.setTreasury(parseMessage(String.join(" ", message)));

					event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
				}, "set treasury")
				.add((event, message) -> {
					currentRebellion.addTreasury(parseMessage(String.join(" ", message)));

					event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
				}, "add treasury")
				.add((event, message) -> {
					currentRebellion.addTreasury(-parseMessage(String.join(" ", message)));

					event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
				}, "subtract treasury")


				.add((event, message) -> {
					currentRebellion.setPopulation(parseMessage(String.join(" ", message)));

					event.getChannel().sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
				}, "set population")
				.add((event, message) -> {
					currentRebellion.addPopulation(parseMessage(String.join(" ", message)));

					event.getChannel().sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
				}, "add population")
				.add((event, message) -> {
					currentRebellion.addPopulation(-parseMessage(String.join(" ", message)));

					event.getChannel().sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
				}, "subtract population")


				.add((event, message) -> {
					currentRebellion.setNotoriety(parseMessage(String.join(" ", message)));

					event.getChannel().sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
				}, "set notoriety")
				.add((event, message) -> {
					currentRebellion.addNotoriety(parseMessage(String.join(" ", message)));

					event.getChannel().sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
				}, "add notoriety")
				.add((event, message) -> {
					currentRebellion.addNotoriety(-parseMessage(String.join(" ", message)));

					event.getChannel().sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
				}, "subtract notoriety")


				.add((event, message) -> {
					currentRebellion.setMembers(parseMessage(String.join(" ", message)));

					event.getChannel().sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
				}, "set members")
				.add((event, message) -> {
					currentRebellion.addMembers(parseMessage(String.join(" ", message)));

					event.getChannel().sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
				}, "add members")
				.add((event, message) -> {
					currentRebellion.addMembers(-parseMessage(String.join(" ", message)));

					event.getChannel().sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
				}, "subtract members")


				.add((event, message) -> {
					currentRebellion.setMaxRank(parseMessage(String.join(" ", message)));
					event.getChannel().sendMessage("Max Level Set: " + currentRebellion.getRebellionMaxLevel()).queue();
				}, "set max rank", "set max level")


				.add((event, message) -> {
					Focus focus = Focus.valueOf(String.join(" ", message).toUpperCase());
					currentRebellion.setFocus(focus);
					event.getChannel().sendMessage("Focus Set: " + currentRebellion.getFocus()).queue();
				}, "set focus")


				.add((event, message) -> {
					currentRebellion.setDemagogue(message);
					event.getChannel().sendMessage("Demagogue Con/Cha Set: " + currentRebellion.getDemagogueConOrChaBonus()).queue();
				}, "set demagogue")


				.add((event, message) -> {
					currentRebellion.setPartisan(message);
					event.getChannel().sendMessage("Partisan Str/Wis Set: " + currentRebellion.getPartisanStrOrWisBonus()).queue();
				}, "set partisan")


				.add((event, message) -> {
					currentRebellion.setRecruiter(Integer.parseInt(message.get(0)));
					event.getChannel().sendMessage("Recruiter Level Set: " + currentRebellion.getRecruiterLvlBonus()).queue();
				}, "set recruiter")


				.add((event, message) -> {
					currentRebellion.setSentinal(message);
					event.getChannel().sendMessage("Sentinel Con/Cha, Str/Wis, Dex/Int Set: " + currentRebellion.getSentinelConOrChaBonus() + ", " + currentRebellion.getSentinelStrOrWisBonus() + ", " + currentRebellion.getSentinelDexOrIntBonus()).queue();
				}, "set sentinel")


				.add((event, message) -> {
					currentRebellion.setSpyMaster(message);
					event.getChannel().sendMessage("Spymaster Dex/Int Set: " + currentRebellion.getSpymasterDexOrIntBonus()).queue();
				}, "set spymaster")


				.add((event, message) -> {
					currentRebellion.setStrategist(message);
					event.getChannel().sendMessage("Strategist Available: " + currentRebellion.isHasStrategist()).queue();
				}, "set strategist")


				.add((event, message) -> {
					final DieParser dieParser = new DieParser(variables);
					if(message.isEmpty()){
						event.getChannel().sendMessage(" " + dieParser.parseDieValue("1d20+"+currentRebellion.getLoyaltyBonus())).queue();
					}else{
						event.getChannel().sendMessage(" " + dieParser.parseDieValue(String.join(" ", message)+"+"+currentRebellion.getLoyaltyBonus())).queue();
					}
					event.getChannel().sendMessage(" " + dieParser.getSteps()).queue();
					attemptCheck(event, dieParser.getRoll());
				}, "roll loyalty")
				.add((event, message) -> {
					final DieParser dieParser = new DieParser(variables);
					if(message.isEmpty()){
						event.getChannel().sendMessage(" " + dieParser.parseDieValue("1d20+"+currentRebellion.getSecrecyBonus())).queue();
					}else{
						event.getChannel().sendMessage(" " + dieParser.parseDieValue(String.join(" ", message)+"+"+currentRebellion.getSecrecyBonus())).queue();
					}
					event.getChannel().sendMessage(" " + dieParser.getSteps()).queue();
					attemptCheck(event, dieParser.getRoll());
				}, "roll secrecy")
				.add((event, message) -> {
					final DieParser dieParser = new DieParser(variables);
					if(message.isEmpty()){
						event.getChannel().sendMessage(" " + dieParser.parseDieValue("1d20+"+currentRebellion.getSecurityBonus())).queue();
					}else{
						event.getChannel().sendMessage(" " + dieParser.parseDieValue(String.join(" ", message)+"+"+currentRebellion.getSecurityBonus())).queue();
					}
					event.getChannel().sendMessage(" " + dieParser.getSteps()).queue();
					attemptCheck(event, dieParser.getRoll());
				}, "roll security")

				.add((event, message) -> {
					int eventNumber;
					if(message.isEmpty()){
						final DieParser dieParser = new DieParser(variables);
						eventNumber = dieParser.parseDieValue("1d100" + "+"+currentRebellion.getDangerRating());
					}else{
						eventNumber = Integer.parseInt(String.join(" ", message));
					}
					RebellionEvent rebellionEvent = RebellionEvent.getEvent(eventNumber);
					event.getChannel().sendMessage("{"+eventNumber+"}").queue();
					event.getChannel().sendMessage(rebellionEvent.getDescription()).queue();
				}, "event")

				.add((event, message) -> {
					int eventNumber;
					if(message.isEmpty()){
						final DieParser dieParser = new DieParser(variables);
						eventNumber = dieParser.parseDieValue("1d100" + "+"+currentRebellion.getDangerRating());
					}else{
						eventNumber = Integer.parseInt(String.join(" ", message));
					}
					RebellionEvent rebellionEvent = RebellionEvent.getEvent(eventNumber);
					event.getChannel().sendMessage("{"+eventNumber+"}").queue();
					event.getChannel().sendMessage(rebellionEvent.getDescription()).queue();
					String text = rebellionEvent.doEvent();
					if(!text.isBlank()) {
						event.getChannel().sendMessage(text).queue();
					}
					}, "event do", "do event");
	}

	private int parseMessage(String join) {
		return new DieParser(variables).parseDieValue(join);
	}


	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		try {
			final String contentRaw = event.getMessage().getContentRaw();
			final String key = contentRaw.toLowerCase().trim();

			ArrayList<String> tokens = new ArrayList<>(Arrays.asList(key.split(" ")));

			if (currentContext == Context.DEFAULT) {
				primaryContext.run(event, tokens);
			}
			defaultContext.run(event, tokens);
		}catch(Exception e){
			event.getChannel().sendMessage(">>>>>>>>>>>>>>ERROR\n"+e.getMessage()).queue();
			throw e;
		}
	}

	@NotNull
	private Behavior getRollBehavior() {
		return (event, message) -> {
			final DieParser dieParser = new DieParser(variables);
			final int roll = dieParser.parseDieValue(String.join(" ", message));
			event.getChannel().sendMessage(" " + roll).queue();
			event.getChannel().sendMessage(" " + dieParser.getSteps()).queue();
			attemptCheck(event, roll);
		};
	}

	private void attemptCheck(MessageReceivedEvent event, int roll) {
		if(checkDC != null){
			if(roll < checkDC){
				event.getChannel().sendMessage("Check Failed! " + failureString).queue();
			}else{
				event.getChannel().sendMessage("Check Passed!").queue();
			}
			checkDC = null;
			failureString = null;
		}
	}

	private Behavior getVarBehavior() {
		return new GroupBehavior()
				.add((event, message) -> {
					String key = message.remove(0);
					String value = String.join(" ", message);
					if (value.isBlank()) {
						event.getChannel().sendMessage("variable " + key + " requires value").queue();
					} else {
						variables.put(key, value);
						event.getChannel().sendMessage(key + " => " + value + " saved").queue();
					}
				}, "add")
				.add((event, message) -> {
					String key = message.remove(0);
					if (variables.containsKey(key)) {
						variables.remove(key);
						event.getChannel().sendMessage("variable '" + key + "' removed").queue();
					} else {
						event.getChannel().sendMessage("variable '" + key + "' doesn't exist").queue();
					}
				}, "remove")
				.add((event, message) -> {
					StringBuilder builder = new StringBuilder();
					for (Map.Entry<String, String> entry : variables.entrySet()) {
						builder.append(entry.getKey()).append(" => ").append(entry.getValue()).append("\n");
					}
					event.getChannel().sendMessage(builder.toString()).queue();
				}, "list");
	}


	private void handleRebellionSheet(@NotNull MessageReceivedEvent event, ArrayList<String> message) {
		if (message.isEmpty()) {

			Rebellion rebellion = getCurrentRebellion();
			event.getChannel().sendMessage(rebellion.getSheet()).queue();
		} else {
			String key = String.join(" ", message);
			Rebellion rebellion = getRebellion(key);
			if (rebellion == null) {
				event.getChannel().sendMessage("I can't find the " + key + " rebellion").queue();
			} else {
				event.getChannel().sendMessage(rebellion.getSheet()).queue();
			}
		}
	}

	private Rebellion getRebellion(String key) {
		return rebellions.get(key);
	}

	private Rebellion getCurrentRebellion() {
		return currentRebellion;
	}
}
