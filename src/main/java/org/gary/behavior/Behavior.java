package org.gary.behavior;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

public abstract class Behavior {

    private GroupBehavior parent;

    @NotNull
    public static Behavior getAlphabetizedList(@NotNull final Collection<String> behaviorKeys, @NotNull final String listTitle, final String help) {
        return new Behavior() {
            @Override
            public void run(DeckList<String> message, MessageChannel channel) {
                if (!behaviorKeys.isEmpty()) {
                    String keyList = behaviorKeys.stream().sorted()
                            .map(key -> String.join(", ", key)).collect(Collectors.joining("\n"));

                    channel.sendMessage(listTitle).queue();

                    ChannelHelper.sendLongMessage("\n", keyList, channel);
                }
            }

            @Override
            public String getHelp(DeckList<String> s, String key) {
                return help;
            }
        };
    }

    public abstract void run(DeckList<String> message, MessageChannel channel);


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
