package rules;

import behavior.Behavior;
import behavior.GroupBehavior;
import java.util.ArrayList;
import java.util.Arrays;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import rebellion.RulesLookupBehavior;

public class RulesListener extends ListenerAdapter {
	private final Behavior core;

	public RulesListener(){

		Behavior rules = new RulesLookupBehavior().getRulesBehavior();
		core =  new GroupBehavior().add(new String[]{"how", "how do i", "how does", "how do", "what is", "what"}, rules);
	}

	@Override
	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
		final String contentRaw = event.getMessage().getContentRaw();
		final String key = contentRaw.toLowerCase().trim();

		ArrayList<String> tokens = new ArrayList<>(Arrays.asList(key.split(" ")));
		core.run(event, tokens);
	}
}
