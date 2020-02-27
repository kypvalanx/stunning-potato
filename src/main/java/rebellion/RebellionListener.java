package rebellion;

import behavior.Behavior;
import behavior.GroupBehavior;
import behavior.NachoHelpBehavior;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RebellionListener extends ListenerAdapter {
    private final GroupBehavior defaultContext;
    private Map<String, Rebellion> rebellions;
    private Rebellion currentRebellion;
    private Map<String, String> variables;
    private Context currentContext = Context.DEFAULT;

    public RebellionListener() {
        rebellions = new HashMap<>();
        currentRebellion = new Rebellion();
        variables = new HashMap<>();

        final GroupBehavior rebellion = new GroupBehavior()
                .add(this::handleRebellionSheet, "sheet")


                .add((event, message) -> {
                    currentRebellion.setSupporters(parseMessage(String.join(" ",message)));

                    event.getChannel().sendMessage("Current Supporters " + currentRebellion.getRebellionSupporters()).queue();
                }, "set supporters")
                .add((event, message) -> {
                    currentRebellion.addSupporters(parseMessage(String.join(" ",message)));

                    event.getChannel().sendMessage("Current Supporters " + currentRebellion.getRebellionSupporters()).queue();
                }, "add supporters")
                .add((event, message) -> {
                    currentRebellion.addSupporters(-parseMessage(String.join(" ",message)));

                    event.getChannel().sendMessage("Current Supporters " + currentRebellion.getRebellionSupporters()).queue();
                }, "subtract supporters")


                .add((event, message) -> {
                    currentRebellion.setTreasury(parseMessage(String.join(" ",message)));

                    event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
                }, "set treasury")
                .add((event, message) -> {
                    currentRebellion.addTreasury(parseMessage(String.join(" ",message)));

                    event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
                }, "add treasury")
                .add((event, message) -> {
                    currentRebellion.addTreasury(-parseMessage(String.join(" ",message)));

                    event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
                }, "subtract treasury")


                .add((event, message) -> {
                    currentRebellion.setPopulation(parseMessage(String.join(" ",message)));

                    event.getChannel().sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
                }, "set population")
                .add((event, message) -> {
                    currentRebellion.addPopulation(parseMessage(String.join(" ",message)));

                    event.getChannel().sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
                }, "add population")
                .add((event, message) -> {
                    currentRebellion.addPopulation(-parseMessage(String.join(" ",message)));

                    event.getChannel().sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
                }, "subtract population")


                .add((event, message) -> {
                    currentRebellion.setNotoriety(parseMessage(String.join(" ",message)));

                    event.getChannel().sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
                }, "set notoriety")
                .add((event, message) -> {
                    currentRebellion.addNotoriety(parseMessage(String.join(" ",message)));

                    event.getChannel().sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
                }, "add notoriety")
                .add((event, message) -> {
                    currentRebellion.addNotoriety(-parseMessage(String.join(" ",message)));

                    event.getChannel().sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
                }, "subtract notoriety")


                .add((event, message) -> {
                    currentRebellion.setMembers(parseMessage(String.join(" ",message)));

                    event.getChannel().sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
                }, "set members")
                .add((event, message) -> {
                    currentRebellion.addMembers(parseMessage(String.join(" ",message)));

                    event.getChannel().sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
                }, "add members")
                .add((event, message) -> {
                    currentRebellion.addMembers(-parseMessage(String.join(" ",message)));

                    event.getChannel().sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
                }, "subtract members")


                .add((event, message) -> {
                    currentRebellion.setMaxRank(parseMessage(String.join(" ", message)));
                    event.getChannel().sendMessage("Max Level Set: " + currentRebellion.getRebellionMaxLevel()).queue();
                }, "set max rank", "set max level");

        Behavior roll = getRollBehavior();

        Behavior var = getVarBehavior();

        defaultContext = new GroupBehavior()
                .add(rebellion, "rebellion")
                .add(roll, "roll")
                .add(var, "var");

        Behavior help = new NachoHelpBehavior(defaultContext);
        defaultContext.add(help, "help");

    }

    private int parseMessage(String join) {
        return new DieParser(variables).parseDieValue(join);
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        final String contentRaw = event.getMessage().getContentRaw();
        final String key = contentRaw.toLowerCase().trim();

        ArrayList<String> tokens = new ArrayList<>(Arrays.asList(key.split(" ")));

        if(currentContext == Context.DEFAULT){
            defaultContext.run(event, tokens);
        }
    }

    @NotNull
    private Behavior getRollBehavior() {
        return (event, message) -> {
            final DieParser dieParser = new DieParser(variables);
            event.getChannel().sendMessage(" " + dieParser.parseDieValue(String.join(" ", message))).queue();
            event.getChannel().sendMessage(" " + dieParser.getSteps()).queue();
        };
    }

    private Behavior getVarBehavior() {
        return new GroupBehavior()
                .add((event, message) -> {
                    String key = message.remove(0);
                    String value = String.join(" ", message);
                    if (value.isBlank()) {
                        event.getChannel().sendMessage("variable " + key + " requires value").queue();
                    } else {
                        variables.put(key, value);
                        event.getChannel().sendMessage(key + " => " + value + " saved").queue();
                    }
                }, "add")
                .add((event, message) -> {
                    String key = message.remove(0);
                    if (variables.containsKey(key)) {
                        variables.remove(key);
                        event.getChannel().sendMessage("variable '" + key + "' removed").queue();
                    } else {
                        event.getChannel().sendMessage("variable '" + key + "' doesn't exist").queue();
                    }
                }, "remove")
                .add((event, message) -> {
                    StringBuilder builder = new StringBuilder();
                    for (Map.Entry<String, String> entry : variables.entrySet()) {
                        builder.append(entry.getKey()).append(" => ").append(entry.getValue()).append("\n");
                    }
                    event.getChannel().sendMessage(builder.toString()).queue();
                }, "list");
    }

    private void runOnMatch(String key, @NotNull MessageReceivedEvent event, String rebellion_set_supporters, Consumer<Integer> consumer) {
        if (key.startsWith(rebellion_set_supporters)) {
            String payload = key.substring(rebellion_set_supporters.length()).trim();

            if (key.isBlank()) {
                event.getChannel().sendMessage("no value to set").queue();
                return;
            }
            consumer.accept(parseMessage(payload));
        }
    }


    private void handleRebellionSheet(@NotNull MessageReceivedEvent event, ArrayList<String> message) {
        if (message.isEmpty()) {

            Rebellion rebellion = getCurrentRebellion();
            event.getChannel().sendMessage(rebellion.getSheet()).queue();
        } else {
            String key = String.join(" ", message);
            Rebellion rebellion = getRebellion(key);
            if (rebellion == null) {
                event.getChannel().sendMessage("I can't find the " + key + " rebellion").queue();
            } else {
                event.getChannel().sendMessage(rebellion.getSheet()).queue();
            }
        }
    }

    private Rebellion getRebellion(String key) {
        return rebellions.get(key);
    }

    private Rebellion getCurrentRebellion() {
        return currentRebellion;
    }
}
