package behavior;


import core.DeckList;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
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
        return key + " => " + message;
    }


    @Override
    public void run(MessageReceivedEvent event, DeckList<String> message) {
        if(message.canDraw()){
            groupBehavior.getDetailedHelp(event, message, "");
        }else
        {
            groupBehavior.getHelp(event,  message, "");
        }
    }

    @Override
    public void getHelp(MessageReceivedEvent event, DeckList<String> message, String context) {
        //NOOP
    }


}
