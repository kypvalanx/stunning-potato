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
        RebellionBehaviors rebellionBehaviors = new RebellionBehaviors(currentRebellion);
        return new GroupBehavior()
                .setDefault(new Behavior() {
                    @Override
                    public void run(MessageReceivedEvent event, DeckList<String> message) {
                        handleRebellionSheet(event, message);
                    }
                })


                .add(rebellionBehaviors.getSetSupportersBehavior(), "set supporters")
                .add(rebellionBehaviors.getAddSupportersBehavior(), "add supporters")
                .add(rebellionBehaviors.getSubSupportersBehavior(), "subtract supporters")


                .add(rebellionBehaviors.getSetTreasureyBehavior(), "set treasury")
                .add(rebellionBehaviors.getAddTreasureyBehavior(), "add treasury")
                .add(rebellionBehaviors.getSubTreasureyBehavior(), "subtract treasury")


                .add(rebellionBehaviors.getSetPopulationBehavior(), "set population")
                .add(rebellionBehaviors.getAddPopulationBehavior(), "add population")
                .add(rebellionBehaviors.getSubPopulationBahavior(), "subtract population")


                .add(rebellionBehaviors.getSetNotorietyBehavior(), "set notoriety")
                .add(rebellionBehaviors.getAddNotorietyBehavior(), "add notoriety")
                .add(rebellionBehaviors.getSubNotorietyBehavior(), "subtract notoriety")


                .add(rebellionBehaviors.getSetMembersBehavior(), "set members")
                .add(rebellionBehaviors.getAddMembersBehavior(), "add members")
                .add(rebellionBehaviors.getSubMembersBehavior(), "subtract members")


                .add(rebellionBehaviors.getSetMaxRankBehavior(), "set max rank", "set max level")


                .add(rebellionBehaviors.getSetSkillFocusBehavior(), "set focus")


                .add(rebellionBehaviors.getSetDemagogueBehavior(), "set demagogue")


                .add(rebellionBehaviors.getSetPartisanBehavior(), "set partisan")


                .add(rebellionBehaviors.getSetRecruiterBehavior(), "set recruiter")


                .add(rebellionBehaviors.getSetSentinelBehavior(), "set sentinel")


                .add(rebellionBehaviors.getSetSpymasterBehavior(), "set spymaster")


                .add(rebellionBehaviors.getSetStrategistBahavior(), "set strategist")


                .add(getRollCheckBehavior(currentRebellion.getLoyaltyBonus()), "roll loyalty")
                .add(getRollCheckBehavior(currentRebellion.getSecrecyBonus()), "roll secrecy")
                .add(getRollCheckBehavior(currentRebellion.getSecurityBonus()), "roll security")

                .add(RebellionEvent.getEventBehavior(currentRebellion), "event")

                .add(RebellionEvent.getEventDoBehavior(currentRebellion), "event do", "do event");
    }



    @NotNull
    public Behavior getRollCheckBehavior(int loyaltyBonus) {
        return new Behavior() {
            @Override
            public void run(MessageReceivedEvent event, DeckList<String> message) {
                final DieParser dieParser = new DieParser();
                if (!message.canDraw()) {
                    event.getChannel().sendMessage(" " + dieParser.parseDieValue("1d20+" + loyaltyBonus)).queue();
                } else {
                    event.getChannel().sendMessage(" " + dieParser.parseDieValue(String.join(" ", message.getDeck()) + "+" + loyaltyBonus)).queue();
                }
                event.getChannel().sendMessage(" " + dieParser.getSteps()).queue();
                CoreListener.this.attemptCheck(event, dieParser.getRoll());
            }
        };
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
        } else if(false){
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
