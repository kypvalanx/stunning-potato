package behavior;

import core.DeckList;
import java.util.List;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Behavior {

    private GroupBehavior parent;

    public abstract void run(MessageReceivedEvent event, DeckList<String> message);

//    public void getHelp(MessageReceivedEvent event, DeckList<String> message, String context)
//    {
//        System.err.println("No help available for ".concat(context).concat("."));
//    }

    public List<String> getDetailedHelp(DeckList<String> s, String key)
    {
        System.err.println(s.getAll() + key);
        return List.of();
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
