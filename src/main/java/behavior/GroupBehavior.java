package behavior;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimaps;
import core.DeckList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GroupBehavior extends Behavior {
    private String helpString;

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
                String first = tokens[0];
                Behavior existing = behaviors.get(first);
                if(existing == null){
                    existing = new GroupBehavior();
                    behaviors.put(first.toLowerCase(), existing);
                }
                if(existing instanceof GroupBehavior){
                    ((GroupBehavior)existing).add(new GroupBehavior(behavior), path.substring(first.length()+1));
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
    public void run(MessageReceivedEvent event, DeckList<String> message)
    {
        if(!message.canDraw()){
            if(defaultBehavior != null) {
                defaultBehavior.run(event, message);
            }
            return;
        }

        String token = message.peek();

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
            message.draw();
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
    public void getHelp(MessageReceivedEvent event, DeckList<String> message, String context) {
        if(helpString != null){
            event.getChannel().sendMessage(context + " " + helpString).queue();
            return;
        }

        if(defaultBehavior != null) {
            defaultBehavior.getHelp(event, message, context);
        }

        ArrayListMultimap<Behavior, String> keysByBehavior = Multimaps.invertFrom(Multimaps.forMap(behaviors), ArrayListMultimap.create());

        for(Behavior behavior : keysByBehavior.keySet()){
            List<String> keys = keysByBehavior.get(behavior);
            behavior.getHelp(event, message, context.concat(" ").concat(String.join("/", keys)));
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

    public void setHelpString(String helpString) {
        this.helpString = helpString;
    }

    @Override
    public void getDetailedHelp(MessageReceivedEvent event, DeckList<String> s, String key) {
        if(s.canDraw()){
            String token = s.draw();
            Behavior behavior = behaviors.get(token);
            if(behavior != null){
                behavior.getDetailedHelp(event, s, key + " " + token);
            }else{
                super.getDetailedHelp(event, s, key);
            }
        }else {
            if(defaultBehavior != null) {
                defaultBehavior.getDetailedHelp(event, s, key);
            }
            for(Map.Entry<String, Behavior> behaviorEntry : behaviors.entrySet()){
                behaviorEntry.getValue().getDetailedHelp(event, s, key + " " + behaviorEntry.getKey());
            }
        }
    }
}
