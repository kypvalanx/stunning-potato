package org.gary.behavior;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

public class GroupBehavior extends Behavior {

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

    private Map<String, GroupBehavior> behaviors = new HashMap<>();
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
        if(behavior != null) {
            for (String path : paths) {
                addBehaviorToPath(behavior, path);
            }
        }
        return this;
    }

    private void addBehaviorToPath(@NotNull Behavior behavior, String path) {
        String[] tokens = path.split(" ");

        GroupBehavior existing = behaviors.get(tokens[0]);

        if(tokens.length == 1){
            if(existing == null){
                if(behavior instanceof GroupBehavior) {
                    behaviors.put(path, (GroupBehavior) behavior);
                } else {
                    behaviors.put(path, new GroupBehavior(behavior));
                }
            } else {
                behaviors.put(path, (GroupBehavior) existing.merge(behavior));
            }
        } else {
            if(existing == null){
                existing = new GroupBehavior();
                behaviors.put(tokens[0], existing);
            }
            existing.addBehaviorToPath(behavior, path.substring(tokens[0].length()+1));
        }
    }

    private Behavior getDefault() {
        return defaultBehavior;
    }

    @Override
    public void run(DeckList<String> message, MessageChannel channel)
    {
        if(!message.canDraw()){
            if(defaultBehavior != null) {
                defaultBehavior.run(message, channel);
            }
            return;
        }

        String token = message.peek();

        Behavior behavior = behaviors.get(token.toLowerCase());

        if (behavior != null)
        {
            message.draw();
            behavior.run(message, channel);
        } else if (defaultBehavior != null)
        {
            defaultBehavior.run(message, channel);
        }
    }

    public GroupBehavior setDefault(Behavior behavior) {
        defaultBehavior = behavior;
        return this;
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
            if (getDefault() == null) {
                setDefault(castThat.getDefault());
            } else if (castThat.getDefault() != null) {
                setDefault(getDefault().merge(castThat.getDefault()));
            }

            for (Map.Entry<String, GroupBehavior> behaviorEntry : castThat.behaviors.entrySet()) {
                GroupBehavior behavior = behaviors.get(behaviorEntry.getKey());
                behaviors.put(behaviorEntry.getKey(), (GroupBehavior) behaviorEntry.getValue().merge(behavior));
            }
        }else if(that instanceof KeyedBehavior){
            setDefault(that.merge(getDefault()));
        }
        return this;
    }

    @Override
    public List<String> getFormattedHelp(DeckList<String> s, String key) {
        List<String> helpMessages = Lists.newArrayList();
        if(s.canDraw()){
            String token = s.draw();
            Behavior behavior = behaviors.get(token);
            if(behavior != null){
                helpMessages.addAll(behavior.getFormattedHelp(s, key + " " + token));
            }else{
                helpMessages.addAll(super.getFormattedHelp(s, key));
            }
        }else {
            if(defaultBehavior != null) {
                helpMessages.addAll(defaultBehavior.getFormattedHelp(s, key));
            }
            ListMultimap<Behavior, String> inverse = Multimaps.invertFrom(Multimaps.forMap(behaviors),
                    ArrayListMultimap.create());
            for(Behavior behavior : inverse.keySet()){
                List<String> keys = inverse.get(behavior);
                helpMessages.addAll(behavior.getFormattedHelp(s, key + " " + String.join("/", keys)));
            }
        }
        return helpMessages;
    }
}
