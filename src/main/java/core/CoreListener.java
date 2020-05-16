package core;

import armor.ArmorBehavior;
import behavior.Behavior;
import behavior.ChannelHelper;
import behavior.GroupBehavior;
import behavior.NachoHelpBehavior;
import com.google.common.collect.Lists;
import static core.DieParser.rollDice;
import static core.DieParser.rollDiceGroups;
import items.ItemBehavior;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pack.PackBehavior;
import rebellion.Rebellion;
import rebellion.RebellionBehaviors;
import rebellion.events.RebellionEvent;
import rules.RulesLookupBehavior;
import table.Tables;
import weapons.WeaponsBehavior;

public class CoreListener extends ListenerAdapter {
    private static final File GYGAX_GREETING = new File("resources/images/gygax1.jpg");
    private final GroupBehavior primaryContext;
    private final GroupBehavior defaultContext;
    private Rebellion currentRebellion;
    private ListenerContext currentListenerContext = ListenerContext.DEFAULT;

    public CoreListener() {
        currentRebellion = Rebellion.getRebellionFromFile();

        primaryContext = new GroupBehavior()
                .setDefault(new Behavior() {
                    @Override
                    public void run(DeckList<String> message, MessageChannel channel) {
                        String key = String.join(" ", message.getAll());
                        String value = Variables.get(key);
                        if (value != null) {
                            primaryContext.run(new DeckList<>(Arrays.asList(value.split(" "))), channel);
                        }
                    }
                })
                .add(new String[]{"rebellion", "!r"}, getRebellionBehavior())
                .add(new String[]{"roll", "/roll", "!roll"}, getRollBehavior())
                .add(new String[]{"var"}, Variables.getVarBehavior())
                .add(new String[]{"pvar"}, Variables.getPVarBehavior())
                .add(new String[]{"dc"}, CheckDC.getDCBehavior())
                .add(new String[]{"rule", "rules"}, new RulesLookupBehavior())
                .add(new String[]{"item", "!i"}, new ItemBehavior())
                .add(new String[]{"weapon", "!w"}, new WeaponsBehavior())
                .add(new String[]{"pack", "!p"}, new PackBehavior())
                .add(new String[]{"armor", "!a"}, new ArmorBehavior())
                .add(new String[]{"table"}, Tables.getBehavior())
                .add(new String[]{"hey gary"}, new Behavior() {
                    @Override
                    public void run(DeckList<String> message, MessageChannel channel) {
                        DieResult dieResult = rollDice("d20");
                        String saying = dieResult.getSum() > 9 ? "pleased to meet you." : "so terribly, terribly disappointed in you";
                        channel.sendFile(GYGAX_GREETING, ".pleased_to_meet_you.jpg").queue();
                        channel.sendMessage("I am ... \n @GaryBot rolls " + dieResult.getSum() + "\n" + dieResult.getSteps() + "\n ... " + saying).queue();
                    }

                    @Override
                    public String getHelp(DeckList<String> s, String key) {
                        return "A way to check if Gary is around.  It's nice to check in on your friends.  Maybe call once in a while.";
                    }
                });


        primaryContext
                .add(new String[]{"help"}, new NachoHelpBehavior(primaryContext));
        defaultContext = new GroupBehavior()
                .add(new String[]{"quit", "exit"}, getExitContextBehavior());
    }

    @NotNull
    private Behavior getExitContextBehavior() {
        return new Behavior() {
            @Override
            public void run(DeckList<String> message, MessageChannel channel) {
                currentListenerContext = ListenerContext.DEFAULT;
            }
        };
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        Context.setCaller(event.getAuthor());
        MessageChannel channel = event.getChannel();

        List<Message.Attachment> attachments = event.getMessage().getAttachments();
        for (Message.Attachment attachment : attachments) {
            if ("xml".equals(attachment.getFileExtension())) {
                MessageChannel finalChannel = channel;
                Thread async = new Thread(() -> {
                    try {
                        File file = attachment.downloadToFile().get();

                        String varSet = new PCGVarLoader(file).add();
                        finalChannel.sendMessage("added character sheet to " + varSet).queue();
                        file.delete();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                });
                async.start();
            } else if ("csv".equals(attachment.getFileExtension())) {
                if (attachment.getFileName().startsWith("table_")) {
                    MessageChannel finalChannel = channel;
                    Thread async = new Thread(() -> {
                        try {
                            File file = attachment.downloadToFile().get();

                            String tableName = new TableLoader(file).add();
                            finalChannel.sendMessage("created Table" + tableName).queue();
                            file.delete();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    });
                    async.start();
                }
            }
        }


        try {
            final String key = event.getMessage().getContentRaw().toLowerCase().trim();

            DeckList<String> message = new DeckList<>(Arrays.asList(key.split(" ")));

            if (event.getMessage().isMentioned(event.getJDA().getSelfUser(), Message.MentionType.USER)) {
                channel = event.getAuthor().openPrivateChannel().complete();

                if (message.canDraw()) {
                    message.draw();
                }
            }

            if (currentListenerContext == ListenerContext.DEFAULT) {
                primaryContext.run(message, channel);
            }
            defaultContext.run(message, channel);
        } catch (Exception e) {
            channel.sendMessage(">>>>>>>>>>>>>>ERROR\n" + e.getMessage()).queue();
            throw e;
        }
    }


    private GroupBehavior getRebellionBehavior() {
        Behavior updateRebellion = new Behavior() {
            @Override
            public void run(DeckList<String> message, MessageChannel channel) {
                Rebellion.writeOutRebellionToFile(currentRebellion);
            }
        };
        RebellionBehaviors rebellionBehaviors = new RebellionBehaviors(currentRebellion, updateRebellion);


        return new GroupBehavior()
                .setDefault(new Behavior() {
                    @Override
                    public void run(DeckList<String> message, MessageChannel channel) {
                        if (!message.canDraw()) {
                            channel.sendMessage(currentRebellion.getSheet()).queue();
                        }
                    }

                    @Override
                    public List<String> getFormattedHelp(DeckList<String> s, String key) {
                        return List.of(NachoHelpBehavior.formatHelp(key, "prints the rebellion sheet"));
                    }
                })
                .add(rebellionBehaviors.getSupportersBehavior(), "supporters")
                //.add(rebellionBehaviors.getAddSupportersBehavior(), "supporters add")
                //.add(rebellionBehaviors.getSubSupportersBehavior(), "supporters sub", "supporters subtract")
                .add(rebellionBehaviors.getTreasuryBehavior(), "treasury")
                //.add(rebellionBehaviors.getAddTreasuryBehavior(), "treasury add")
                //.add(rebellionBehaviors.getSubTreasuryBehavior(), "treasury sub", "treasury subtract")
                .add(rebellionBehaviors.getAddPopulationBehavior(), "population add", "pop add")
                .add(rebellionBehaviors.getSubPopulationBehavior(), "population sub", "population subtract", "pop sub", "pop subtract")
                .add(rebellionBehaviors.getAddNotorietyBehavior(), "notoriety add")
                .add(rebellionBehaviors.getSubNotorietyBehavior(), "notoriety subtract", "notoriety sub")
                .add(rebellionBehaviors.getAddMembersBehavior(), "members add")
                .add(rebellionBehaviors.getSubMembersBehavior(), "members sub", "members subtract")
                .add(rebellionBehaviors.getSetMaxRankBehavior(), "max rank set", "max level set")
                .add(rebellionBehaviors.getSetSkillFocusBehavior(), "focus")
                .add(rebellionBehaviors.getSetDemagogueBehavior(), "demagogue")
                .add(rebellionBehaviors.getSetPartisanBehavior(), "partisan")
                .add(rebellionBehaviors.getSetRecruiterBehavior(), "recruiter")
                .add(rebellionBehaviors.getSetSentinelBehavior(), "sentinel")
                .add(rebellionBehaviors.getSetSpymasterBehavior(), "spymaster")
                .add(rebellionBehaviors.getSetStrategistBehavior(), "strategist")
                .add(rebellionBehaviors.getRollLoyaltyBehavior(), "roll loyalty", "loyalty")
                .add(rebellionBehaviors.getRollSecrecyBehavior(), "roll secrecy", "secrecy")
                .add(rebellionBehaviors.getRollSecurityBehavior(), "roll security", "security")
                .add(RebellionEvent.getEventBehavior(currentRebellion), "event");
        //.add(RebellionEvent.getEventDoBehavior(currentRebellion), "event do", "do event");
    }


    @NotNull
    private Behavior getRollBehavior() {
        return new Behavior() {
            @Override
            public void run(DeckList<String> message, MessageChannel channel) {
                List<DieResult> dieResults = rollDiceGroups(message);
                List<String> messages = Lists.newArrayList();
                for (DieResult dieResult : dieResults) {
                    if (dieResult.getMessage() != null) {
                        messages.add(dieResult.getMessage());
                    } else {
                        messages.add(Context.getCaller().getAsMention() + " rolls " + dieResult.getSum());
                        messages.add(dieResult.getSteps());
                        CheckDC.attemptCheck(dieResult.getSum(), channel);
                    }
                }
                ChannelHelper.sendLongMessage(" ", String.join("\n", messages), channel);
            }

            @Override
            public String getHelp(DeckList<String> s, String key) {
                return "Rolls whatever is provided after it.  use the format [XdY + Z] where X is the number of dice to be rolled, Y is the number of sides, and Z is a flat bonus.  Separate all terms with a + or -.  Variables from the var command will be resolved if possible. Separate rolls can be separated with and.  die eqs precided by an ' will be printed back without resolving.";
            }
        };
    }
}
