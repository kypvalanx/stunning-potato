package behavior;


import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

/**
 * Created by al029298 on 2/10/17.
 */
public class NachoHelpBehavior implements Behavior
{

    private final GroupBehavior groupBehavior;

    public NachoHelpBehavior(GroupBehavior groupBehavior)
    {
        this.groupBehavior = groupBehavior;
    }


    @Override
    public void run(MessageReceivedEvent event, ArrayList<String> message) {

        if(message.isEmpty()){
            event.getChannel().sendMessage(groupBehavior.getHelp(event,  message.get(0))).queue();
        }

        if(message.size()>1){
            event.getChannel().sendMessage(groupBehavior.getDetailedHelp(event, message)).queue();
        }else
        {
            event.getChannel().sendMessage(groupBehavior.getHelp(event,  message.get(0))).queue();
        }
    }


}
