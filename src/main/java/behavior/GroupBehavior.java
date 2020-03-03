package behavior;

import java.util.Objects;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupBehavior implements Behavior {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupBehavior that = (GroupBehavior) o;
        return Objects.equals(behaviors, that.behaviors) &&
                Objects.equals(defaultBehavior, that.defaultBehavior);
    }

    @Override
    public int hashCode() {
        return Objects.hash(behaviors, defaultBehavior);
    }

    private Map<String, Behavior> behaviors = new HashMap<>();
    private Behavior defaultBehavior;

    public GroupBehavior(){

    }
    public GroupBehavior(Behavior behavior){
        defaultBehavior = behavior;
    }

    public GroupBehavior add(List<KeyedBehavior> keyedBehaviors){
        for (KeyedBehavior keyedBehavior :
                keyedBehaviors) {
            add(keyedBehavior.getKeys(), keyedBehavior);
        }
        return this;
    }

    public GroupBehavior add(String path, Behavior behavior){
        return add(behavior, path);
    }
    public GroupBehavior add(String[] path, Behavior behavior){
        return add(behavior, path);
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
                    Behavior existingDefault = existingGroupBehavior.getDefault();
                    if(!existingDefault.equals(behavior)) {
System.out.println("default behavior already exists: "+ path);
//                            throw new IllegalStateException("default behavior already exists: "+ path);
                        }
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

    private Behavior getDefault() {
        return defaultBehavior;
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

    public GroupBehavior setDefault(Behavior behavior) {
        defaultBehavior = behavior;
        return this;
    }

    public boolean hasDefault() {
        return defaultBehavior != null;
    }

    @Override
    public void getHelp(MessageReceivedEvent event, ArrayList<String> s) {

        if(defaultBehavior != null) {
            defaultBehavior.getHelp(event, s);
        }
        for(Behavior behavior : behaviors.values()){
            behavior.getHelp(event, s);
        }
    }
}
