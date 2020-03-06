package rebellion;

import behavior.Behavior;
import core.CoreListener;
import core.DeckList;
import core.DieParser;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class RebellionBehaviors {
	private Rebellion currentRebellion;

	public RebellionBehaviors(Rebellion currentRebellion) {
		this.currentRebellion = currentRebellion;
	}

	public void setCurrentRebellion(Rebellion currentRebellion) {
		this.currentRebellion = currentRebellion;
	}


	@NotNull
	public Behavior getSubSupportersBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addSupporters(-new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Supporters " + currentRebellion.getRebellionSupporters()).queue();
			}
		};
	}

	@NotNull
	public Behavior getAddSupportersBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addSupporters(new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Supporters " + currentRebellion.getRebellionSupporters()).queue();
			}
		};
	}

	@NotNull
	public Behavior getSetSupportersBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setSupporters(new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Supporters " + currentRebellion.getRebellionSupporters()).queue();
			}
		};
	}


	@NotNull
	public Behavior getSubMembersBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addMembers(-new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
			}
		};
	}

	@NotNull
	public Behavior getAddMembersBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addMembers(new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
			}
		};
	}

	@NotNull
	public Behavior getSetMembersBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setMembers(new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
			}
		};
	}

	@NotNull
	public Behavior getSubNotorietyBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addNotoriety(-new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
			}
		};
	}

	@NotNull
	public Behavior getAddNotorietyBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addNotoriety(new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
			}
		};
	}

	@NotNull
	public Behavior getSetNotorietyBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setNotoriety(new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
			}
		};
	}

	@NotNull
	public Behavior getSubPopulationBahavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addPopulation(-new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
			}
		};
	}

	@NotNull
	public Behavior getAddPopulationBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addPopulation(new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
			}
		};
	}

	@NotNull
	public Behavior getSetPopulationBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setPopulation(new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
			}
		};
	}

	@NotNull
	public Behavior getSubTreasureyBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addTreasury(-new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
			}
		};
	}

	@NotNull
	public Behavior getAddTreasureyBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addTreasury(new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
			}
		};
	}

	@NotNull
	public Behavior getSetTreasureyBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setTreasury(new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
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
			}
		};
	}

	@NotNull
	public Behavior getSetMaxRankBehavior() {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setMaxRank(new DieParser().parseDieValue(message));
				event.getChannel().sendMessage("Max Level Set: " + currentRebellion.getRebellionMaxLevel()).queue();
			}
		};
	}

}
