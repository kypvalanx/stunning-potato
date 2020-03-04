package behavior;

import com.google.common.base.MoreObjects;
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
                Behavior toAdd = behavior;
                Behavior existing = behaviors.get(path);
                if(existing == null){
                    behaviors.put(path.toLowerCase(), new GroupBehavior(toAdd));
                    continue;
                }
                if(existing instanceof GroupBehavior){
                    GroupBehavior existingGroupBehavior = ((GroupBehavior) existing);
                    
                    if(existingGroupBehavior.hasDefault()){
                        toAdd = mergeBehaviors(toAdd, existingGroupBehavior.getDefault());
                    }
                    
              //      if(!existingGroupBehavior.hasDefault()){
                        existingGroupBehavior.setDefault(toAdd);
                        continue;
            //        }
//                    Behavior existingDefault = existingGroupBehavior.getDefault();
//                    if(!existingDefault.equals(toAdd)) {
//
//
//                        System.out.println("default behavior already exists: "+ path);
//                        System.out.println(existingDefault);
//                        System.out.println(behavior);
////                            throw new IllegalStateException("default behavior already exists: "+ path);
//                    }
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

    private Behavior mergeBehaviors(Behavior one, Behavior two) {
        if(one.getClass() == two.getClass()){
            return one.merge(two);
        }

        if(one instanceof GroupBehavior){
            ((GroupBehavior) one).setDefault(mergeBehaviors(((GroupBehavior) one).getDefault(), two));
            return one;
        }
        if(two instanceof GroupBehavior){
            ((GroupBehavior) two).setDefault(mergeBehaviors(((GroupBehavior) two).getDefault(), one));
            return two;
        }
        System.out.println(one.toString() + two.toString());
        return null;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("behaviors", behaviors)
                .add("defaultBehavior", defaultBehavior)
                .toString();
    }

    @Override
    public Behavior merge(Behavior that){
        if(that == null){
            return this;
        }

        if(that instanceof GroupBehavior) {
            GroupBehavior castThat = (GroupBehavior) that;
            if (this.getDefault() == null) {
                this.setDefault(castThat.getDefault());
            } else if (castThat.getDefault() != null) {
                this.setDefault(this.getDefault().merge(castThat.getDefault()));
            }

            for (Map.Entry<String, Behavior> behaviorEntry : castThat.behaviors.entrySet()) {
                Behavior behavior = behaviors.get(behaviorEntry.getKey());
                if (behavior != null) {
                    behaviors.put(behaviorEntry.getKey(), behavior.merge(behaviorEntry.getValue()));
                }
            }
        }else if(that instanceof KeyedBehavior){
            defaultBehavior = that.merge(defaultBehavior);
        }
        return this;
    }
}
