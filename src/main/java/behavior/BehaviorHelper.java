package behavior;

import core.DeckList;
import java.util.Collection;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class BehaviorHelper {
	@NotNull
	public static Behavior getAlphabetizedList(@NotNull final Collection<String> behaviorKeys, @NotNull final String listTitle) {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				if (!behaviorKeys.isEmpty()) {
					String keyList = behaviorKeys.stream().sorted()
							.map(key -> String.join(", ", key)).collect(Collectors.joining("\n"));

					event.getChannel().sendMessage(listTitle).queue();

					ChannelHelper.sendLongMessage(event, "\n", keyList);
				}
			}
		};
	}
}
