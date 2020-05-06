package behavior;

import core.DeckList;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Behavior {

    private GroupBehavior parent;

    public abstract void run(DeckList<String> message, MessageChannel channel);

//    public void getHelp(MessageReceivedEvent event, DeckList<String> message, String context)
//    {
//        System.err.println("No help available for ".concat(context).concat("."));
//    }

    public List<String> getFormattedHelp(DeckList<String> s, String key)
    {
        String help = getHelp(s, key);
        if(help == null){
            return List.of();
        }
        return List.of(NachoHelpBehavior.formatHelp(key, help));
    }

    public String getHelp(DeckList<String> s, String key)
    {
        if(!key.isBlank())
        {System.err.println(s.getAll() + key);}
        return null;
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
