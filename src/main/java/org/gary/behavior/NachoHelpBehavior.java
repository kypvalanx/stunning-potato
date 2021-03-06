package org.gary.behavior;


import java.util.List;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

/**
 * Created by al029298 on 2/10/17.
 */
public class NachoHelpBehavior extends Behavior
{
    private final GroupBehavior groupBehavior;

    public NachoHelpBehavior(GroupBehavior groupBehavior)
    {
        this.groupBehavior = groupBehavior;
    }

    @NotNull
    public static String formatHelp(String key, final String message) {
        return key + "   =>   " + message;
    }

    @Override
    public void run(DeckList<String> message, MessageChannel channel) {
        List<String> messages = groupBehavior.getFormattedHelp(message, "");

        ChannelHelper.sendLongMessage("\n", String.join("\n", messages), channel);
    }

    @Override
    public String getHelp(DeckList<String> s, String key) {
        return "This is a help Function, but you seem to have figured that out";
    }
}
