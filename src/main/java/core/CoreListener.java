package core;

import behavior.Behavior;
import behavior.ChannelHelper;
import behavior.GroupBehavior;
import behavior.NachoHelpBehavior;
import items.ItemBehavior;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import rebellion.Focus;
import rebellion.Rebellion;
import rebellion.RebellionBehaviors;
import rebellion.events.RebellionEvent;
import rules.RulesLookupBehavior;

public class CoreListener extends ListenerAdapter {
    private final GroupBehavior primaryContext;
    private final GroupBehavior defaultContext;
    private Map<String, Rebellion> rebellions;
    private Rebellion currentRebellion;
    private Context currentContext = Context.DEFAULT;

    public CoreListener() {
        rebellions = new HashMap<>();
        currentRebellion = new Rebellion();

        Behavior dc = new GroupBehavior()
                .setDefault(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        if (!CheckDC.hasDC()) {
                            event.getChannel().sendMessage("No current set DC.").queue();
                        } else {
                            event.getChannel().sendMessage("The Next die roll will be checked against DC " + CheckDC.peek()).queue();
                        }
                    }
                })
                .add("set", new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        CheckDC.setDC(Integer.parseInt(message.draw()));
                        if (message.canDraw()) {
                            CheckDC.setFailureMessage(String.join(" ", message.getDeck()));
                        }
                        event.getChannel().sendMessage("The Next die roll will be checked against DC " + CheckDC.peek()).queue();
                    }
                });

        primaryContext = new GroupBehavior()
                .add(new String[]{"rebellion", "r"}, getRebellionBehavior())
                .add(getRollBehavior(), "roll")
                .add(getVarBehavior(), "var")
                .add("dc", dc)
                .add(new String[]{"rule", "rules"}, new RulesLookupBehavior())
                .add(new String[]{"item","!i"}, new ItemBehavior());

        Behavior help = new NachoHelpBehavior(primaryContext);
        primaryContext.add(help, "help");


        defaultContext = new GroupBehavior().add((new Behavior() {
            @Override
            public void run(MessageReceivedEvent event, DeckList<String> message) {
                currentContext = Context.DEFAULT;
            }
        }), "quit", "exit");

    }



    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if("PathfinderBot".equals(event.getAuthor().getName())){
            return;
        }
        try {
            final String contentRaw = event.getMessage().getContentRaw();
            final String key = contentRaw.toLowerCase().trim();

            //ArrayList<String> tokens = new ArrayList<>(Arrays.asList(key.split(" ")));
            DeckList<String> message = new DeckList<>(Arrays.asList(key.split(" ")));

            if (currentContext == Context.DEFAULT) {
                primaryContext.run(event, message);
            }
            defaultContext.run(event, message);
        } catch (Exception e) {
            event.getChannel().sendMessage(">>>>>>>>>>>>>>ERROR\n" + e.getMessage()).queue();
            throw e;
        }
    }


	private GroupBehavior getRebellionBehavior() {
        return new GroupBehavior()
                .setDefault(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        handleRebellionSheet(event, message);
                    }
                })

                .add(RebellionBehaviors.getSetSupportersBehavior(currentRebellion), "set supporters")
                .add(RebellionBehaviors.getAddSupportersBehavior(currentRebellion), "add supporters")
                .add(RebellionBehaviors.getSubSupportersBehavior(currentRebellion), "subtract supporters")


                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.setTreasury(new DieParser().parseDieValue(message));

                        event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
                    }
                }, "set treasury")
                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.addTreasury(new DieParser().parseDieValue(message));

                        event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
                    }
                }, "add treasury")
                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.addTreasury(-new DieParser().parseDieValue(message));

                        event.getChannel().sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
                    }
                }, "subtract treasury")


                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.setPopulation(new DieParser().parseDieValue(message));

                        event.getChannel().sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
                    }
                }, "set population")
                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.addPopulation(new DieParser().parseDieValue(message));

                        event.getChannel().sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
                    }
                }, "add population")
                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.addPopulation(-new DieParser().parseDieValue(message));

                        event.getChannel().sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
                    }
                }, "subtract population")


                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.setNotoriety(new DieParser().parseDieValue(message));

                        event.getChannel().sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
                    }
                }, "set notoriety")
                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.addNotoriety(new DieParser().parseDieValue(message));

                        event.getChannel().sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
                    }
                }, "add notoriety")
                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.addNotoriety(-new DieParser().parseDieValue(message));

                        event.getChannel().sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
                    }
                }, "subtract notoriety")


                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.setMembers(new DieParser().parseDieValue(message));

                        event.getChannel().sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
                    }
                }, "set members")
                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.addMembers(new DieParser().parseDieValue(message));

                        event.getChannel().sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
                    }
                }, "add members")
                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.addMembers(-new DieParser().parseDieValue(message));

                        event.getChannel().sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
                    }
                }, "subtract members")


                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.setMaxRank(new DieParser().parseDieValue(message));
                        event.getChannel().sendMessage("Max Level Set: " + currentRebellion.getRebellionMaxLevel()).queue();
                    }
                }, "set max rank", "set max level")


                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        Focus focus = Focus.valueOf(String.join(" ", message.getDeck()).toUpperCase());
                        currentRebellion.setFocus(focus);
                        event.getChannel().sendMessage("Focus Set: " + currentRebellion.getFocus()).queue();
                    }
                }, "set focus")


                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.setDemagogue(message.getDeck());
                        event.getChannel().sendMessage("Demagogue Con/Cha Set: " + currentRebellion.getDemagogueConOrChaBonus()).queue();
                    }
                }, "set demagogue")


                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.setPartisan(message.getDeck());
                        event.getChannel().sendMessage("Partisan Str/Wis Set: " + currentRebellion.getPartisanStrOrWisBonus()).queue();
                    }
                }, "set partisan")


                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.setRecruiter(Integer.parseInt(message.getDeck().get(0)));
                        event.getChannel().sendMessage("Recruiter Level Set: " + currentRebellion.getRecruiterLvlBonus()).queue();
                    }
                }, "set recruiter")


                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.setSentinal(message.getDeck());
                        event.getChannel().sendMessage("Sentinel Con/Cha, Str/Wis, Dex/Int Set: " + currentRebellion.getSentinelConOrChaBonus() + ", " + currentRebellion.getSentinelStrOrWisBonus() + ", " + currentRebellion.getSentinelDexOrIntBonus()).queue();
                    }
                }, "set sentinel")


                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.setSpyMaster(message.getDeck());
                        event.getChannel().sendMessage("Spymaster Dex/Int Set: " + currentRebellion.getSpymasterDexOrIntBonus()).queue();
                    }
                }, "set spymaster")


                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        currentRebellion.setStrategist(message.getDeck());
                        event.getChannel().sendMessage("Strategist Available: " + currentRebellion.isHasStrategist()).queue();
                    }
                }, "set strategist")


                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        final DieParser dieParser = new DieParser();
                        if (!message.canDraw()) {
                            event.getChannel().sendMessage(" " + dieParser.parseDieValue("1d20+" + currentRebellion.getLoyaltyBonus())).queue();
                        } else {
                            event.getChannel().sendMessage(" " + dieParser.parseDieValue(String.join(" ", message.getDeck()) + "+" + currentRebellion.getLoyaltyBonus())).queue();
                        }
                        event.getChannel().sendMessage(" " + dieParser.getSteps()).queue();
                        CoreListener.this.attemptCheck(event, dieParser.getRoll());
                    }
                }, "roll loyalty")
                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        final DieParser dieParser = new DieParser();
                        if (!message.canDraw()) {
                            event.getChannel().sendMessage(" " + dieParser.parseDieValue("1d20+" + currentRebellion.getSecrecyBonus())).queue();
                        } else {
                            event.getChannel().sendMessage(" " + dieParser.parseDieValue(String.join(" ", message.getDeck()) + "+" + currentRebellion.getSecrecyBonus())).queue();
                        }
                        event.getChannel().sendMessage(" " + dieParser.getSteps()).queue();
                        CoreListener.this.attemptCheck(event, dieParser.getRoll());
                    }
                }, "roll secrecy")
                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        final DieParser dieParser = new DieParser();
                        if (!message.canDraw()) {
                            event.getChannel().sendMessage(" " + dieParser.parseDieValue("1d20+" + currentRebellion.getSecurityBonus())).queue();
                        } else {
                            event.getChannel().sendMessage(" " + dieParser.parseDieValue(String.join(" ", message.getDeck()) + "+" + currentRebellion.getSecurityBonus())).queue();
                        }
                        event.getChannel().sendMessage(" " + dieParser.getSteps()).queue();
                        CoreListener.this.attemptCheck(event, dieParser.getRoll());
                    }
                }, "roll security")

                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        int eventNumber;
                        if (!message.canDraw()) {
                            final DieParser dieParser = new DieParser();
                            eventNumber = dieParser.parseDieValue("1d100" + "+" + currentRebellion.getDangerRating());
                        } else {
                            eventNumber = Integer.parseInt(String.join(" ", message.getDeck()));
                        }
                        RebellionEvent rebellionEvent = RebellionEvent.getEvent(eventNumber);
                        event.getChannel().sendMessage("{" + eventNumber + "}").queue();
                        event.getChannel().sendMessage(rebellionEvent.getDescription()).queue();
                    }
                }, "event")

                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        int eventNumber;
                        if (!message.canDraw()) {
                            final DieParser dieParser = new DieParser();
                            eventNumber = dieParser.parseDieValue("1d100" + "+" + currentRebellion.getDangerRating());
                        } else {
                            eventNumber = Integer.parseInt(String.join(" ", message.getDeck()));
                        }
                        RebellionEvent rebellionEvent = RebellionEvent.getEvent(eventNumber);
                        event.getChannel().sendMessage("{" + eventNumber + "}").queue();
                        event.getChannel().sendMessage(rebellionEvent.getDescription()).queue();
                        String text = rebellionEvent.doEvent();
                        if (!text.isBlank()) {
                            event.getChannel().sendMessage(text).queue();
                        }
                    }
                }, "event do", "do event");
    }


    @NotNull
    private Behavior getRollBehavior() {
        return new Behavior() {
            @Override
            public void run(MessageReceivedEvent event, DeckList<String> message) {
                final DieParser dieParser = new DieParser();
                final int roll = dieParser.parseDieValue(message);
                event.getChannel().sendMessage(" " + roll).queue();
                ChannelHelper.sendLongMessage(event, " ", dieParser.getSteps());
                CoreListener.this.attemptCheck(event, roll);
            }
        };
    }

    private void attemptCheck(MessageReceivedEvent event, int roll) {
        if (CheckDC.hasDC()) {
            if (roll < CheckDC.getDC()) {
                event.getChannel().sendMessage("Check Failed! " + CheckDC.getFailureMessage()).queue();
            } else {
                event.getChannel().sendMessage("Check Passed!").queue();
            }
        }
    }

    private Behavior getVarBehavior() {
        return new GroupBehavior()
                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        String key = message.draw();
                        String value = String.join(" ", message.getDeck());
                            if (value.isBlank()) {
                            event.getChannel().sendMessage("variable " + key + " requires value").queue();
                        } else {
                            Variables.put(key, value);
                            event.getChannel().sendMessage(key + " => " + value + " saved").queue();
                        }
                    }
                }, "add")
                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        String key = message.draw();
                        if (Variables.containsKey(key)) {
                            Variables.remove(key);
                            event.getChannel().sendMessage("variable '" + key + "' removed").queue();
                        } else {
                            event.getChannel().sendMessage("variable '" + key + "' doesn't exist").queue();
                        }
                    }
                }, "remove")
                .add(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        StringBuilder builder = new StringBuilder();
                        for (Map.Entry<String, String> entry : Variables.entrySet()) {
                            builder.append(entry.getKey()).append(" => ").append(entry.getValue()).append("\n");
                        }
                        event.getChannel().sendMessage(builder.toString()).queue();
                    }
                }, "list");
    }


    private void handleRebellionSheet(@NotNull MessageReceivedEvent event, DeckList<String> message) {
        if (!message.canDraw()) {

            Rebellion rebellion = getCurrentRebellion();
            event.getChannel().sendMessage(rebellion.getSheet()).queue();
        } else {
            String key = String.join(" ", message.getDeck());
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
