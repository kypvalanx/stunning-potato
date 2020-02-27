package behavior;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public interface Behavior {

    void run(MessageReceivedEvent event, ArrayList<String> message);

    default String getHelp(MessageReceivedEvent event, String s)
    {
        return "No help available for ".concat(s).concat(".");
    }

    default String getDetailedHelp(MessageReceivedEvent event, ArrayList<String> s)
    {
        return "No detailed help available for ".concat(String.join(" ", s)).concat(".");
    }
}
