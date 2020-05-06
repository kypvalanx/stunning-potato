package behavior;

import core.DeckList;
import java.util.Collection;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class BehaviorHelper {
	@NotNull
	public static Behavior getAlphabetizedList(@NotNull final Collection<String> behaviorKeys, @NotNull final String listTitle, final String help) {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				if (!behaviorKeys.isEmpty()) {
					String keyList = behaviorKeys.stream().sorted()
							.map(key -> String.join(", ", key)).collect(Collectors.joining("\n"));

					channel.sendMessage(listTitle).queue();

					ChannelHelper.sendLongMessage("\n", keyList, channel);
				}
			}

			@Override
			public String getHelp(DeckList<String> s, String key) {
				return help;
			}
		};
	}
}
