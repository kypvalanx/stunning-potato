package rebellion;

import behavior.Behavior;
import behavior.NachoHelpBehavior;
import core.CheckDC;
import core.DeckList;
import static core.DieParser.rollDice;
import core.DieResult;
import java.util.List;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class RebellionBehaviors {
	private final Behavior followUpBehavior;
	private Rebellion currentRebellion;

	public RebellionBehaviors(Rebellion currentRebellion, Behavior followUpBehavior) {
		this.currentRebellion = currentRebellion;
		this.followUpBehavior = followUpBehavior;
	}

	public void setCurrentRebellion(Rebellion currentRebellion) {
		this.currentRebellion = currentRebellion;
	}


	@NotNull
	public Behavior getSubSupportersBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addSupporters(-rollDice(message).getSum());

				event.getChannel().sendMessage("Current Supporters " + currentRebellion.getSupporters()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"subtracts from supporters, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getAddSupportersBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addSupporters(rollDice(message).getSum());

				event.getChannel().sendMessage("Current Supporters " + currentRebellion.getSupporters()).queue();
				followUpBehavior.run(event, message);
			}
			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"adds to supporters, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getSetSupportersBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setSupporters(rollDice(message).getSum());

				event.getChannel().sendMessage("Current Supporters " + currentRebellion.getSupporters()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"sets supporters, accepts a die roll equation"));
			}
		};
	}


	@NotNull
	public Behavior getSubMembersBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addMembers(-rollDice(message).getSum());

				event.getChannel().sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"subtracts members, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getAddMembersBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addMembers(rollDice(message).getSum());

				event.getChannel().sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"adds members, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getSubNotorietyBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addNotoriety(-rollDice(message).getSum());

				event.getChannel().sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"subtracts Notoriety, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getAddNotorietyBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addNotoriety(rollDice(message).getSum());

				event.getChannel().sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"adds members, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getSubPopulationBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addPopulation(-rollDice(message).getSum());

				event.getChannel().sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"subtracts population, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getAddPopulationBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addPopulation(rollDice(message).getSum());

				event.getChannel().sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"adds population, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getSubTreasuryBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addTreasury(-rollDice(message).getSum());

				event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"subtracts from treasury, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getAddTreasuryBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addTreasury(rollDice(message).getSum());

				event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"adds to treasury, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getSetStrategistBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setStrategist(message.getDeck());
				event.getChannel().sendMessage("Strategist Available: " + currentRebellion.isHasStrategist()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"adds a strategist, or removes the strategist if nothing is provided."));
			}
		};
	}

	@NotNull
	public Behavior getSetSpymasterBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setSpyMaster(message.getDeck());
				event.getChannel().sendMessage("Spymaster Dex/Int Set: " + currentRebellion.getSpymasterDexOrIntBonus()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the Spymaster's Dex/Int stat, accepts the bonus you want active, both appropriate bonuses, or a 6 bonus array"));
			}
		};
	}

	@NotNull
	public Behavior getSetSentinelBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setSentinal(message.getDeck());
				event.getChannel().sendMessage("Sentinel Con/Cha, Str/Wis, Dex/Int Set: " + currentRebellion.getSentinelConOrChaBonus() + ", " + currentRebellion.getSentinelStrOrWisBonus() + ", " + currentRebellion.getSentinelDexOrIntBonus()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the Sentinel stats. Accepts three bonuses [Con/Cha, Str/Wis, Dex/Int], or a 6 bonus array"));
			}
		};
	}

	@NotNull
	public Behavior getSetRecruiterBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setRecruiter(Integer.parseInt(message.getDeck().get(0)));
				event.getChannel().sendMessage("Recruiter Level Set: " + currentRebellion.getRecruiterLvlBonus()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the Recruiters Character Level. Accepts a single number"));
			}
		};
	}

	@NotNull
	public Behavior getSetPartisanBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setPartisan(message.getDeck());
				event.getChannel().sendMessage("Partisan Str/Wis Set: " + currentRebellion.getPartisanStrOrWisBonus()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the Partisan's Str/Wis stat, accepts the bonus you want active, both appropriate bonuses, or a 6 bonus array"));
			}
		};
	}

	@NotNull
	public Behavior getSetDemagogueBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setDemagogue(message.getDeck());
				event.getChannel().sendMessage("Demagogue Con/Cha Set: " + currentRebellion.getDemagogueConOrChaBonus()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the Demagogue's Con/Cha stat, accepts the bonus you want active, both appropriate bonuses, or a 6 bonus array"));
			}
		};
	}

	@NotNull
	public Behavior getSetSkillFocusBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				Focus focus = Focus.valueOf(String.join(" ", message.getDeck()).toUpperCase());
				currentRebellion.setFocus(focus);
				event.getChannel().sendMessage("Focus Set: " + currentRebellion.getFocus()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the rebellion Skill Focus: none, loyalty, secrecy, security."));
			}
		};
	}

	@NotNull
	public Behavior getSetMaxRankBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setMaxRank(rollDice(message).getSum());
				event.getChannel().sendMessage("Max Level Set: " + currentRebellion.getRebellionMaxLevel()).queue();
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the maximum rank the rebellion can reach"));
			}
		};
	}

	public Behavior getRollLoyaltyBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				rollCheck(event, message, currentRebellion.getLoyaltyBonus());
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Rolls using your rebellion's loyalty bonus."));
			}
		};
	}

	public Behavior getRollSecrecyBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				rollCheck(event, message, currentRebellion.getSecrecyBonus());
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Rolls using your rebellion's secrecy bonus."));
			}
		};
	}

	public Behavior getRollSecurityBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				rollCheck(event, message, currentRebellion.getSecurityBonus());
				followUpBehavior.run(event, message);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Rolls using your rebellion's security bonus."));
			}
		};
	}




	private void rollCheck(MessageReceivedEvent event, DeckList<String> message, int bonus) {
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
}
