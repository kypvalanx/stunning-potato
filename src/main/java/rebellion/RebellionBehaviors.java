package rebellion;

import behavior.Behavior;
import behavior.ChannelHelper;
import behavior.GroupBehavior;
import behavior.NachoHelpBehavior;
import core.CheckDC;
import core.DeckList;
import core.DieParser;
import core.DieResult;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;
import rebellion.events.RebellionEvent;

public class RebellionBehaviors {
	private static final Behavior FOLLOW_UP_BEHAVIOR = new Behavior() {
		@Override
		public void run(DeckList<String> message, MessageChannel channel) {
			Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());
			RebellionDelegate.writeOutRebellionToFile(currentRebellion);
		}
	};


	public static Behavior getSupportersBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());
				if(message.canDraw()){

					currentRebellion.addSupporters(DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());
				}

				channel.sendMessage("Current Supporters " + currentRebellion.getSupporters()).queue();
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"reports the current supporters or adds the payload"));
			}
		};
	}

	@NotNull
	public static Behavior getSubMembersBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());

				currentRebellion.addMembers(-DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Members " + currentRebellion.getMembers()).queue();
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"subtracts members, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public static Behavior getAddMembersBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());

				currentRebellion.addMembers(DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Members " + currentRebellion.getMembers()).queue();
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"adds members, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public static Behavior getSubNotorietyBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());

				currentRebellion.addNotoriety(-DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"subtracts Notoriety, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public static Behavior getAddNotorietyBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());

				currentRebellion.addNotoriety(DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"adds members, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public static Behavior getSubPopulationBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());

				currentRebellion.addPopulation(-DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"subtracts population, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public static Behavior getAddPopulationBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());

				currentRebellion.addPopulation(DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
				FOLLOW_UP_BEHAVIOR.run( message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"adds population, accepts a die roll equation"));
			}
		};
	}

	public static Behavior getTreasuryBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());
				if(message.canDraw()){

					currentRebellion.addTreasury(DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());
				}

				channel.sendMessage("Treasury " + currentRebellion.getTreasury()).queue();
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"reports the treasury or adds a supplied payload"));
			}
		};
	}

	@NotNull
	public static Behavior getSetStrategistBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());
				currentRebellion.setStrategist(message.getDeck());
				channel.sendMessage("Strategist Available: " + currentRebellion.isHasStrategist()).queue();
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"adds a strategist, or removes the strategist if nothing is provided."));
			}
		};
	}

	@NotNull
	public static Behavior getSetSpymasterBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());
				currentRebellion.setSpyMaster(message.getDeck());
				channel.sendMessage("Spymaster Dex/Int Set: " + currentRebellion.getSpymasterSecrecyBonus()).queue();
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the Spymaster's Dex/Int stat, accepts the bonus you want active, both appropriate bonuses, or a 6 bonus array"));
			}
		};
	}

	@NotNull
	public static Behavior getSetSentinelBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());
				currentRebellion.setSentinal(message.getDeck());
				channel.sendMessage("Sentinel Con/Cha, Str/Wis, Dex/Int Set: " + currentRebellion.getSentinelLoyaltyBonus() + ", " + currentRebellion.getSentinelSecurityBonus() + ", " + currentRebellion.getSentinelSecrecyBonus()).queue();
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the Sentinel stats. Accepts three bonuses [Con/Cha, Str/Wis, Dex/Int], or a 6 bonus array"));
			}
		};
	}

	@NotNull
	public static Behavior getSetRecruiterBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());
				currentRebellion.setRecruiter(Integer.parseInt(message.getDeck().get(0)));
				channel.sendMessage("Recruiter Level Set: " + currentRebellion.getRecruiterLvlBonus()).queue();
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the Recruiters Character Level. Accepts a single number"));
			}
		};
	}

	@NotNull
	public static Behavior getSetPartisanBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());
				currentRebellion.setPartisan(message.getDeck());
				channel.sendMessage("Partisan Str/Wis Set: " + currentRebellion.getPartisanSecurityBonus()).queue();
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the Partisan's Str/Wis stat, accepts the bonus you want active, both appropriate bonuses, or a 6 bonus array"));
			}
		};
	}

	@NotNull
	public static Behavior getSetDemagogueBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());
				currentRebellion.setDemagogue(message.getDeck());
				channel.sendMessage("Demagogue Con/Cha Set: " + currentRebellion.getDemagogueLoyaltyBonus()).queue();
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the Demagogue's Con/Cha stat, accepts the bonus you want active, both appropriate bonuses, or a 6 bonus array"));
			}
		};
	}

	@NotNull
	public static Behavior getSetSkillFocusBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());
				Focus focus = Focus.valueOf(String.join(" ", message.getDeck()).toUpperCase());
				currentRebellion.setFocus(focus);
				channel.sendMessage("Focus Set: " + currentRebellion.getFocus()).queue();
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the rebellion Skill Focus: none, loyalty, secrecy, security."));
			}
		};
	}

	@NotNull
	public static Behavior getSetMaxRankBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());

				currentRebellion.setMaxRank(DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());
				channel.sendMessage("Max Level Set: " + currentRebellion.getMaxLevel()).queue();
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the maximum rank the rebellion can reach"));
			}
		};
	}

	public static Behavior getRollLoyaltyBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());
				rollCheck(message, currentRebellion.getLoyaltyBonus(), channel);
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Rolls using your rebellion's loyalty bonus."));
			}
		};
	}

	public static Behavior getRollSecrecyBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());
				rollCheck(message, currentRebellion.getSecrecyBonus(), channel);
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Rolls using your rebellion's secrecy bonus."));
			}
		};
	}

	public static Behavior getRollSecurityBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());
				rollCheck(message, currentRebellion.getSecurityBonus(), channel);
				FOLLOW_UP_BEHAVIOR.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Rolls using your rebellion's security bonus."));
			}
		};
	}




	private static void rollCheck(DeckList<String> message, int bonus, MessageChannel channel) {
		DieResult dieResult;
		if (!message.canDraw()) {

			dieResult = DieParser.rollDiceGroups("1d20+" + bonus).get(0);
		} else {

			dieResult = DieParser.rollDiceGroups(String.join(" ", message.getDeck()) + "+" + bonus).get(0);
		}
		channel.sendMessage(" " + dieResult.getSum()).queue();
		channel.sendMessage(" " + dieResult.getSteps()).queue();
		CheckDC.attemptCheck(dieResult.getSum(), channel);
	}

	@NotNull
	public static Behavior getPrintsTheRebellionSheetBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				if (!message.canDraw()) {
					channel.sendMessage(RebellionDelegate.getCurrentRebellion(channel.getId()).getSheet()).queue();
				}
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key, "prints the rebellion sheet"));
			}
		};
	}

	public static GroupBehavior getRebellionBehavior() {
		return new GroupBehavior()
				.setDefault(getPrintsTheRebellionSheetBehavior())
				.add(getSupportersBehavior(), "supporters")
				.add(getTreasuryBehavior(), "treasury")
				.add(getAddPopulationBehavior(), "population add", "pop add")
				.add(getSubPopulationBehavior(), "population sub", "population subtract", "pop sub", "pop subtract")
				.add(getAddNotorietyBehavior(), "notoriety add")
				.add(getSubNotorietyBehavior(), "notoriety subtract", "notoriety sub")
				.add(getAddMembersBehavior(), "members add")
				.add(getSubMembersBehavior(), "members sub", "members subtract")
				.add(getSetMaxRankBehavior(), "max rank set", "max level set")
				.add(getSetSkillFocusBehavior(), "focus")
				.add(getSetDemagogueBehavior(), "demagogue")
				.add(getSetPartisanBehavior(), "partisan")
				.add(getSetRecruiterBehavior(), "recruiter")
				.add(getSetSentinelBehavior(), "sentinel")
				.add(getSetSpymasterBehavior(), "spymaster")
				.add(getSetStrategistBehavior(), "strategist")
				.add(getRollLoyaltyBehavior(), "roll loyalty", "loyalty")
				.add(getRollSecrecyBehavior(), "roll secrecy", "secrecy")
				.add(getRollSecurityBehavior(), "roll security", "security")
				.add(RebellionEvent.getEventBehavior(), "event")
				.add(getListBehavior(), "list")
				.add(getSetBehavior(), "set");
	}

	private static Behavior getSetBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				if (message.canDraw()) {
					try {
						long id = Long.parseLong(message.draw());
						new RebellionDelegate().setMapping(channel.getId(), id);
						channel.sendMessage("This Channel now using rebellion: "+id).queue();
					}catch (NumberFormatException e){
						channel.sendMessage("rebellion ids are numbers").queue();
					}
				}
			}

			@Override
			public List<@NotNull String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key, "allows you to set what rebellion the current channel is mapped to"));
			}
		};
	}

	private static Behavior getListBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Map<String, Long> mappings = new RebellionDelegate().getMappings();
				String steps = "Current Mappings ("+channel.getId()+"):\n";
				for(Map.Entry<String, Long> mapping : mappings.entrySet()){
					steps = steps.concat(mapping.getKey() + " : " + mapping.getValue() + "\n");
				}

				ChannelHelper.sendLongMessage("\n", steps, channel);
			}

			@Override
			public List<@NotNull String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key, "Lists Channel mappings to rebellions"));
			}
		};
	}
}
