package behavior;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public class BehaviorHelper {
	@NotNull
	public static Behavior getAlphabetizedList(@NotNull final List<KeyedBehavior> behaviors, @NotNull final String listTitle) {
		return (event, message) -> {
			if(!behaviors.isEmpty()) {
				String rulesList = behaviors.stream()
						.sorted(Comparator.comparing(o -> o.getKeys()[0]))
						.map(keyedBehavior -> String.join(", ", keyedBehavior.getKeys())).collect(Collectors.joining("\n"));

				event.getChannel().sendMessage(listTitle).queue();

				ChannelHelper.sendLongMessage(event, "\n", rulesList);
			}
		};
	}
}
