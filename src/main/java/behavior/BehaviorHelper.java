package behavior;

import core.DeckList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class BehaviorHelper {
	@NotNull
	public static Behavior getAlphabetizedList(@NotNull final List<KeyedBehavior> behaviors, @NotNull final String listTitle) {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				if (!behaviors.isEmpty()) {
					String rulesList = behaviors.stream()
							.sorted(Comparator.comparing(o -> o.getKeys()[0]))
							.map(keyedBehavior -> String.join(", ", keyedBehavior.getKeys())).collect(Collectors.joining("\n"));

					event.getChannel().sendMessage(listTitle).queue();

					ChannelHelper.sendLongMessage(event, "\n", rulesList);
				}
			}
		};
	}
}
