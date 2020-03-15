package core;

import armor.ArmorBehavior;
import behavior.Behavior;
import behavior.ChannelHelper;
import behavior.GroupBehavior;
import behavior.NachoHelpBehavior;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import items.ItemBehavior;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pack.PackBehavior;
import rebellion.Rebellion;
import rebellion.RebellionBehaviors;
import rebellion.events.RebellionEvent;
import rules.RulesLookupBehavior;
import weapons.WeaponsBehavior;

public class CoreListener extends ListenerAdapter {
	private final GroupBehavior primaryContext;
	private final GroupBehavior defaultContext;
	//private Map<String, Rebellion> rebellions;
	private Rebellion currentRebellion;
	private Context currentContext = Context.DEFAULT;

	public CoreListener() {
		currentRebellion = getRebellion();

		Behavior dc = new GroupBehavior()
				.setDefault(new Behavior() {
					@Override
					public void run(MessageReceivedEvent event, DeckList<String> message) {
						if (!CheckDC.hasDC()) {
							event.getChannel().sendMessage("No current set DC.").queue();
						} else {
							event.getChannel().sendMessage("The Next die roll will be checked against DC " + CheckDC.peek()).queue();
						}
					}
				})
				.add("set", new Behavior() {
					@Override
					public void run(MessageReceivedEvent event, DeckList<String> message) {
						CheckDC.setDC(Integer.parseInt(message.draw()));
						if (message.canDraw()) {
							CheckDC.setFailureMessage(String.join(" ", message.getDeck()));
						}
						event.getChannel().sendMessage("The Next die roll will be checked against DC " + CheckDC.peek()).queue();
					}
				});

		primaryContext = new GroupBehavior()
				.add(new String[]{"rebellion", "r"}, getRebellionBehavior())
				.add(getRollBehavior(), "roll")
				.add(getVarBehavior(), "var")
				.add("dc", dc)
				.add(new String[]{"rule", "rules"}, new RulesLookupBehavior())
				.add(new String[]{"item", "!i"}, new ItemBehavior())
				.add(new String[]{"weapon", "!w"}, new WeaponsBehavior())
                .add(new String[]{"pack", "!p"}, new PackBehavior())
                .add(new String[]{"armor", "!a"}, new ArmorBehavior());

		Behavior help = new NachoHelpBehavior(primaryContext);
		primaryContext.add(help, "help");


		defaultContext = new GroupBehavior().add((new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentContext = Context.DEFAULT;
			}
		}), "quit", "exit");

	}

	@NotNull
	private Rebellion getRebellion() {
		File rebellionFile = new File("resources/rebellion.yaml");
		if (rebellionFile.canRead()) {

			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

			try {
				return mapper.readValue(rebellionFile, Rebellion.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new Rebellion();
	}

	private void writeOutRebellion(){
		File rebellionFile = new File("resources/rebellion.yaml");
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
		try {
			if(!rebellionFile.createNewFile()){
				rebellionFile.delete();
				rebellionFile.createNewFile();
			}
			mapper.writeValue(rebellionFile, currentRebellion);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if ("PathfinderBot".equals(event.getAuthor().getName())) {
			return;
		}
		try {
			final String contentRaw = event.getMessage().getContentRaw();
			final String key = contentRaw.toLowerCase().trim();

			//ArrayList<String> tokens = new ArrayList<>(Arrays.asList(keys.split(" ")));
			DeckList<String> message = new DeckList<>(Arrays.asList(key.split(" ")));

			if (currentContext == Context.DEFAULT) {
				primaryContext.run(event, message);
			}
			defaultContext.run(event, message);
		} catch (Exception e) {
			event.getChannel().sendMessage(">>>>>>>>>>>>>>ERROR\n" + e.getMessage()).queue();
			throw e;
		}
	}


	private GroupBehavior getRebellionBehavior() {
		Behavior updateRebellion = new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				writeOutRebellion();
			}
		};
		RebellionBehaviors rebellionBehaviors = new RebellionBehaviors(currentRebellion, updateRebellion);


		return new GroupBehavior()
				.setDefault(new Behavior() {
					@Override
					public void run(MessageReceivedEvent event, DeckList<String> message) {
						handleRebellionSheet(event, message);
					}

					@Override
					public void getDetailedHelp(MessageReceivedEvent event, DeckList<String> s, String key) {
						event.getChannel().sendMessage(NachoHelpBehavior.formatHelp(key, "prints the rebellion sheet")).queue();
					}
				})
				.add(rebellionBehaviors.getSetSupportersBehavior(), "set supporters")
				.add(rebellionBehaviors.getAddSupportersBehavior(), "add supporters")
				.add(rebellionBehaviors.getSubSupportersBehavior(), "subtract supporters")
				.add(rebellionBehaviors.getSetTreasureyBehavior(), "set treasury")
				.add(rebellionBehaviors.getAddTreasureyBehavior(), "add treasury")
				.add(rebellionBehaviors.getSubTreasureyBehavior(), "subtract treasury")
				.add(rebellionBehaviors.getSetPopulationBehavior(), "set population")
				.add(rebellionBehaviors.getAddPopulationBehavior(), "add population")
				.add(rebellionBehaviors.getSubPopulationBahavior(), "subtract population")
				.add(rebellionBehaviors.getSetNotorietyBehavior(), "set notoriety")
				.add(rebellionBehaviors.getAddNotorietyBehavior(), "add notoriety")
				.add(rebellionBehaviors.getSubNotorietyBehavior(), "subtract notoriety")
				.add(rebellionBehaviors.getSetMembersBehavior(), "set members")
				.add(rebellionBehaviors.getAddMembersBehavior(), "add members")
				.add(rebellionBehaviors.getSubMembersBehavior(), "subtract members")
				.add(rebellionBehaviors.getSetMaxRankBehavior(), "set max rank", "set max level")
				.add(rebellionBehaviors.getSetSkillFocusBehavior(), "set focus")
				.add(rebellionBehaviors.getSetDemagogueBehavior(), "set demagogue")
				.add(rebellionBehaviors.getSetPartisanBehavior(), "set partisan")
				.add(rebellionBehaviors.getSetRecruiterBehavior(), "set recruiter")
				.add(rebellionBehaviors.getSetSentinelBehavior(), "set sentinel")
				.add(rebellionBehaviors.getSetSpymasterBehavior(), "set spymaster")
				.add(rebellionBehaviors.getSetStrategistBahavior(), "set strategist")
				.add(getRollCheckBehavior(currentRebellion.getLoyaltyBonus()), "roll loyalty")
				.add(getRollCheckBehavior(currentRebellion.getSecrecyBonus()), "roll secrecy")
				.add(getRollCheckBehavior(currentRebellion.getSecurityBonus()), "roll security")
				.add(RebellionEvent.getEventBehavior(currentRebellion), "event")
				.add(RebellionEvent.getEventDoBehavior(currentRebellion), "event do", "do event");
	}


	@NotNull
	public Behavior getRollCheckBehavior(int loyaltyBonus) {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				final DieParser dieParser = new DieParser();
				if (!message.canDraw()) {
					event.getChannel().sendMessage(" " + dieParser.parseDieValue("1d20+" + loyaltyBonus)).queue();
				} else {
					event.getChannel().sendMessage(" " + dieParser.parseDieValue(String.join(" ", message.getDeck()) + "+" + loyaltyBonus)).queue();
				}
				event.getChannel().sendMessage(" " + dieParser.getSteps()).queue();
				CoreListener.this.attemptCheck(event, dieParser.getRoll());
			}
		};
	}


	@NotNull
	private Behavior getRollBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				final DieParser dieParser = new DieParser();
				final int roll = dieParser.parseDieValue(message);
				event.getChannel().sendMessage(" " + roll).queue();
				ChannelHelper.sendLongMessage(event, " ", dieParser.getSteps());
				CoreListener.this.attemptCheck(event, roll);
			}
		};
	}

	private void attemptCheck(MessageReceivedEvent event, int roll) {
		if (CheckDC.hasDC()) {
			if (roll < CheckDC.getDC()) {
				event.getChannel().sendMessage("Check Failed! " + CheckDC.getFailureMessage()).queue();
			} else {
				event.getChannel().sendMessage("Check Passed!").queue();
			}
		}
	}

	private Behavior getVarBehavior() {
		return new GroupBehavior()
				.add(new Behavior() {
					@Override
					public void run(MessageReceivedEvent event, DeckList<String> message) {
						String key = message.draw();
						String value = String.join(" ", message.getDeck());
						if (value.isBlank()) {
							event.getChannel().sendMessage("variable " + key + " requires value").queue();
						} else {
							Variables.put(key, value);
							event.getChannel().sendMessage(key + " => " + value + " saved").queue();
						}
					}
				}, "add")
				.add(new Behavior() {
					@Override
					public void run(MessageReceivedEvent event, DeckList<String> message) {
						String key = message.draw();
						if (Variables.containsKey(key)) {
							Variables.remove(key);
							event.getChannel().sendMessage("variable '" + key + "' removed").queue();
						} else {
							event.getChannel().sendMessage("variable '" + key + "' doesn't exist").queue();
						}
					}
				}, "remove")
				.add(new Behavior() {
					@Override
					public void run(MessageReceivedEvent event, DeckList<String> message) {
						StringBuilder builder = new StringBuilder();
						for (Map.Entry<String, String> entry : Variables.entrySet()) {
							builder.append(entry.getKey()).append(" => ").append(entry.getValue()).append("\n");
						}
						event.getChannel().sendMessage(builder.toString()).queue();
					}
				}, "list");
	}


	private void handleRebellionSheet(@NotNull MessageReceivedEvent event, DeckList<String> message) {
		if (!message.canDraw()) {
			event.getChannel().sendMessage(currentRebellion.getSheet()).queue();
		}
//        else if(false){
//            String keys = String.join(" ", message.getDeck());
//            Rebellion rebellion = getRebellion(keys);
//            if (rebellion == null) {
//                event.getChannel().sendMessage("I can't find the " + keys + " rebellion").queue();
//            } else {
//                event.getChannel().sendMessage(rebellion.getSheet()).queue();
//            }
//        }
	}

//    private Rebellion getRebellion(String keys) {
//        return rebellions.get(keys);
//    }

}
