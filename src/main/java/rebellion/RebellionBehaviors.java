package rebellion;

import behavior.Behavior;
import behavior.NachoHelpBehavior;
import core.DeckList;
import static core.DieParser.rollDice;
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
			public void getDetailedHelp(MessageReceivedEvent event, DeckList<String> s, String key) {
				event.getChannel().sendMessage(NachoHelpBehavior.formatHelp(key,"subtracts from supporters, accepts a die roll equation")).queue();
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
			public void getDetailedHelp(MessageReceivedEvent event, DeckList<String> s, String key) {
				event.getChannel().sendMessage(NachoHelpBehavior.formatHelp(key,"adds to supporters, accepts a die roll equation")).queue();
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
			public void getDetailedHelp(MessageReceivedEvent event, DeckList<String> s, String key) {
				event.getChannel().sendMessage(NachoHelpBehavior.formatHelp(key,"sets supporters, accepts a die roll equation")).queue();
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
		};
	}

	@NotNull
	public Behavior getSetMembersBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setMembers(rollDice(message).getSum());

				event.getChannel().sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
				followUpBehavior.run(event, message);
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
		};
	}

	@NotNull
	public Behavior getSetNotorietyBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setNotoriety(rollDice(message).getSum());

				event.getChannel().sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
				followUpBehavior.run(event, message);
			}
		};
	}

	@NotNull
	public Behavior getSubPopulationBahavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addPopulation(-rollDice(message).getSum());

				event.getChannel().sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
				followUpBehavior.run(event, message);
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
		};
	}

	@NotNull
	public Behavior getSetPopulationBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setPopulation(rollDice(message).getSum());

				event.getChannel().sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
				followUpBehavior.run(event, message);
			}
		};
	}

	@NotNull
	public Behavior getSubTreasureyBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addTreasury(-rollDice(message).getSum());

				event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
				followUpBehavior.run(event, message);
			}
		};
	}

	@NotNull
	public Behavior getAddTreasureyBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addTreasury(rollDice(message).getSum());

				event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
				followUpBehavior.run(event, message);
			}
		};
	}

	@NotNull
	public Behavior getSetTreasureyBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setTreasury(rollDice(message).getSum());

				event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
				followUpBehavior.run(event, message);
			}
		};
	}


	@NotNull
	public Behavior getSetStrategistBahavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setStrategist(message.getDeck());
				event.getChannel().sendMessage("Strategist Available: " + currentRebellion.isHasStrategist()).queue();
				followUpBehavior.run(event, message);
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
		};
	}

}
