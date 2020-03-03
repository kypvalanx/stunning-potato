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
        if(message.size()>1){
            groupBehavior.getDetailedHelp(event, message);
        }else
        {
            groupBehavior.getHelp(event,  message);
        }
    }

    @Override
    public void getHelp(MessageReceivedEvent event, ArrayList<String> message) {
        //NOOP
    }


}
