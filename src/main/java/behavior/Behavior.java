package behavior;

import core.DeckList;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Behavior {

    private GroupBehavior parent;

    public abstract void run(MessageReceivedEvent event, DeckList<String> message);

    public void getHelp(MessageReceivedEvent event, DeckList<String> message, String context)
    {
        event.getChannel().sendMessage("No help available for ".concat(context).concat(".")).queue();
    }

    public void getDetailedHelp(MessageReceivedEvent event, DeckList<String> s)
    {
        event.getChannel().sendMessage("No detailed help available for ".concat(String.join(" ", s)).concat(".")).queue();
    }

    public Behavior merge(Behavior that){
        System.err.println("Unsupported Behavior Merge: " + this + "\n                            " + that);
        return null;
    }


    public GroupBehavior getParent() {
        return parent;
    }

    public void setParent(GroupBehavior parent) {
        this.parent = parent;
    }
}
