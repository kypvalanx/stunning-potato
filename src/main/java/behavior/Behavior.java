package behavior;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public interface Behavior {

    void run(MessageReceivedEvent event, ArrayList<String> message);

    default void getHelp(MessageReceivedEvent event, ArrayList<String> message)
    {
        event.getChannel().sendMessage("No help available for ".concat(String.join(" ", message)).concat(".")).queue();
    }

    default void getDetailedHelp(MessageReceivedEvent event, ArrayList<String> s)
    {
        event.getChannel().sendMessage("No detailed help available for ".concat(String.join(" ", s)).concat(".")).queue();
    }

    default Behavior merge(Behavior that){
        System.err.println("Unsupported Behavior Merge: " + this + "\n                            " + that);
        return null;
    }
}
