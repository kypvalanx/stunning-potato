package core;

import armor.ArmorBehavior;
import behavior.Behavior;
import behavior.ChannelHelper;
import behavior.GroupBehavior;
import behavior.NachoHelpBehavior;
import static core.DieParser.rollDice;
import items.ItemBehavior;
import java.util.Arrays;
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
				.add(new String[]{"armor", "!a"}, new ArmorBehavior());
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
		if ("PathfinderBot".equals(event.getAuthor().getName())) {
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
	public Behavior getRollCheckBehavior(int bonus) {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				DieResult dieResult;
				if (!message.canDraw()) {
					dieResult = rollDice("1d20+" + bonus);
					event.getChannel().sendMessage(" " + dieResult.getSum()).queue();
				} else {
					dieResult = rollDice(String.join(" ", message.getDeck()) + "+" + bonus);
					event.getChannel().sendMessage(" " + dieResult.getSum()).queue();
				}
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
				DieResult dieResult = rollDice(message);
				event.getChannel().sendMessage(event.getAuthor().getName() + " rolls " + dieResult.getSum()).queue();
				ChannelHelper.sendLongMessage(event, " ", dieResult.getSteps());
				CheckDC.attemptCheck(event, dieResult.getSum());
			}
		};
	}

}
