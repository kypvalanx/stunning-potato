package rebellion;

import behavior.Behavior;
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

}
