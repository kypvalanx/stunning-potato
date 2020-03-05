package rebellion;

import behavior.Behavior;
import core.DeckList;
import core.DieParser;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class RebellionBehaviors {
	@NotNull
	public static Behavior getSubSupportersBehavior(final Rebellion currentRebellion) {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addSupporters(-new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Supporters " + currentRebellion.getRebellionSupporters()).queue();
			}
		};
	}

	@NotNull
	public static Behavior getAddSupportersBehavior(final Rebellion currentRebellion) {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.addSupporters(new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Supporters " + currentRebellion.getRebellionSupporters()).queue();
			}
		};
	}

	@NotNull
	public static Behavior getSetSupportersBehavior(final Rebellion currentRebellion) {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				currentRebellion.setSupporters(new DieParser().parseDieValue(message));

				event.getChannel().sendMessage("Current Supporters " + currentRebellion.getRebellionSupporters()).queue();
			}
		};
	}
}
