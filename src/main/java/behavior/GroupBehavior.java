package behavior;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupBehavior implements Behavior {

    private Map<String, Behavior> behaviors = new HashMap<>();
    private Behavior defaultBehavior;

    public GroupBehavior(){

    }
    public GroupBehavior(Behavior behavior){
        defaultBehavior = behavior;
    }

    public GroupBehavior add(Behavior behavior, String... paths)
    {
        for (String path : paths)
        {
            String[] tokens = path.split(" ");

            if(tokens.length == 1)
            {
                Behavior existing = behaviors.get(path);
                if(existing == null){
                    behaviors.put(path.toLowerCase(), new GroupBehavior(behavior));
                    continue;
                }
                if(existing instanceof GroupBehavior){
                    GroupBehavior existingGroupBehavior = ((GroupBehavior) existing);
                    if(!existingGroupBehavior.hasDefault()){
                        existingGroupBehavior.setDefault(behavior);
                        continue;
                    }
                    throw new IllegalStateException("default behavior already exists");
                }
                behaviors.put(path.toLowerCase(), new GroupBehavior(behavior));
            } else {
                Behavior existing = behaviors.get(tokens[0]);
                if(existing == null){
                    existing = new GroupBehavior();
                    behaviors.put(tokens[0].toLowerCase(), existing);
                }
                if(existing instanceof GroupBehavior){
                    ((GroupBehavior)existing).add(new GroupBehavior(behavior), path.substring(tokens[0].length()+1));
                }
            }
        }
        return this;
    }

    @Override
    public void run(MessageReceivedEvent event, ArrayList<String> message)
    {
        if(message.isEmpty()){
            if(defaultBehavior != null) {
                defaultBehavior.run(event, message);
            }
            return;
        }

        String token = message.get(0);

        if (token == null)
        {
            if(defaultBehavior != null) {
                defaultBehavior.run(event, message);
            }
            return;
        }

        Behavior behavior = behaviors.get(token.toLowerCase());

        if (behavior != null)
        {
            message.remove(0);
            behavior.run(event, message);
        } else if (defaultBehavior != null)
        {
            defaultBehavior.run(event, message);
        }
    }

    public void setDefault(Behavior behavior) {
        defaultBehavior = behavior;
    }

    public boolean hasDefault() {
        return defaultBehavior != null;
    }

    @Override
    public String getHelp(MessageReceivedEvent event, String s) {
        String help = defaultBehavior.getHelp(event, s);
        for(Behavior behavior : behaviors.values()){
            help = help.concat(behavior.getHelp(event, s));
        }
        return help;
    }
}
