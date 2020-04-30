package core;

import armor.ArmorBehavior;
import behavior.Behavior;
import behavior.ChannelHelper;
import behavior.GroupBehavior;
import behavior.NachoHelpBehavior;
import com.google.common.collect.Lists;
import static core.DieParser.rollDice;
import static core.DieParser.rollDiceGroups;
import items.ItemBehavior;
import java.util.Arrays;
import java.util.List;
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
	private Rebellion currentRebellion;
	private Context currentContext = Context.DEFAULT;

	public CoreListener() {
		currentRebellion = Rebellion.getRebellionFromFile();

		primaryContext = new GroupBehavior()
				.setDefault(new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				String key = String.join(" ", message.getAll());
				String value = Variables.get(key);
				if(value != null) {
					primaryContext.run(event, new DeckList<>(Arrays.asList(value.split(" "))));
				}
			}
		})
				.add(new String[]{"rebellion", "!r"}, getRebellionBehavior())
				.add(new String[]{"roll", "/roll", "!roll"}, getRollBehavior())
				.add(new String[]{"var"}, Variables.getVarBehavior())
				.add(new String[]{"dc"}, CheckDC.getDCBehavior())
				.add(new String[]{"rule", "rules"}, new RulesLookupBehavior())
				.add(new String[]{"item", "!i"}, new ItemBehavior())
				.add(new String[]{"weapon", "!w"}, new WeaponsBehavior())
				.add(new String[]{"pack", "!p"}, new PackBehavior())
				.add(new String[]{"armor", "!a"}, new ArmorBehavior())
		.add(new String[]{"hey gary"}, new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				event.getChannel().sendMessage("sup fellow human").queue();
			}

			@Override
			public List<String> getDetailedHelp(DeckList<String> s, String key) {
				return Lists.newArrayList(NachoHelpBehavior.formatHelp(key, "a way to check if Gary is around."));
			}
		});
		primaryContext
				.add(new String[]{"help"}, new NachoHelpBehavior(primaryContext));
		defaultContext = new GroupBehavior()
				.add(new String[]{"quit", "exit"}, getExitContextBehavior());
	}

	@NotNull
	private Behavior getExitContextBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentContext = Context.DEFAULT;
			}
		};
	}


	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) {
			return;
		}
		try {
			final String key = event.getMessage().getContentRaw().toLowerCase().trim();

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
				Rebellion.writeOutRebellionToFile(currentRebellion);
			}
		};
		RebellionBehaviors rebellionBehaviors = new RebellionBehaviors(currentRebellion, updateRebellion);


		return new GroupBehavior()
				.setDefault(new Behavior() {
					@Override
					public void run(MessageReceivedEvent event, DeckList<String> message) {
						if (!message.canDraw()) {
							event.getChannel().sendMessage(currentRebellion.getSheet()).queue();
						}
					}

					@Override
					public List<String> getDetailedHelp(DeckList<String> s, String key) {
						return List.of(NachoHelpBehavior.formatHelp(key, "prints the rebellion sheet"));
					}
				})
				.add(rebellionBehaviors.getAddSupportersBehavior(), "supporters add")
				.add(rebellionBehaviors.getSubSupportersBehavior(), "supporters sub", "supporters subtract")
				.add(rebellionBehaviors.getAddTreasuryBehavior(), "treasury add")
				.add(rebellionBehaviors.getSubTreasuryBehavior(), "treasury sub", "treasury subtract")
				.add(rebellionBehaviors.getAddPopulationBehavior(), "population add", "pop add")
				.add(rebellionBehaviors.getSubPopulationBehavior(), "population sub", "population subtract", "pop sub", "pop subtract")
				.add(rebellionBehaviors.getAddNotorietyBehavior(), "notoriety add")
				.add(rebellionBehaviors.getSubNotorietyBehavior(), "notoriety subtract", "notoriety sub")
				.add(rebellionBehaviors.getAddMembersBehavior(), "members add")
				.add(rebellionBehaviors.getSubMembersBehavior(), "members sub", "members subtract")
				.add(rebellionBehaviors.getSetMaxRankBehavior(), "max rank set", "max level set")
				.add(rebellionBehaviors.getSetSkillFocusBehavior(), "focus set")
				.add(rebellionBehaviors.getSetDemagogueBehavior(), "demagogue set")
				.add(rebellionBehaviors.getSetPartisanBehavior(), "partisan set")
				.add(rebellionBehaviors.getSetRecruiterBehavior(), "recruiter set")
				.add(rebellionBehaviors.getSetSentinelBehavior(), "sentinel set")
				.add(rebellionBehaviors.getSetSpymasterBehavior(), "spymaster set")
				.add(rebellionBehaviors.getSetStrategistBehavior(), "strategist set")
				.add(getRollCheckBehavior(currentRebellion.getLoyaltyBonus()), "roll loyalty")
				.add(getRollCheckBehavior(currentRebellion.getSecrecyBonus()), "roll secrecy")
				.add(getRollCheckBehavior(currentRebellion.getSecurityBonus()), "roll security")
				.add(RebellionEvent.getEventBehavior(currentRebellion), "event")
				.add(RebellionEvent.getEventDoBehavior(currentRebellion), "event do", "do event");
	}


	@NotNull
	public Behavior getRollCheckBehavior(int bonus) {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				DieResult dieResult;
				if (!message.canDraw()) {
					dieResult = rollDice("1d20+" + bonus);
				} else {
					dieResult = rollDice(String.join(" ", message.getDeck()) + "+" + bonus);
				}
				event.getChannel().sendMessage(" " + dieResult.getSum()).queue();
				event.getChannel().sendMessage(" " + dieResult.getSteps()).queue();
				CheckDC.attemptCheck(event, dieResult.getSum());
			}
		};
	}


	@NotNull
	private Behavior getRollBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				List<DieResult> dieResults = rollDiceGroups(message);
				for(DieResult dieResult : dieResults) {
					event.getChannel().sendMessage(event.getAuthor().getAsMention() + " rolls " + dieResult.getSum()).queue();
					ChannelHelper.sendLongMessage(event, " ", dieResult.getSteps());
					CheckDC.attemptCheck(event, dieResult.getSum());
				}
			}
		};
	}

}
