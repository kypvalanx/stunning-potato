package core;

import armor.ArmorBehavior;
import behavior.Behavior;
import behavior.GroupBehavior;
import behavior.NachoHelpBehavior;
import items.ItemBehavior;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import pack.PackBehavior;
import personality.GaryPersonality;
import rebellion.Rebellion;
import rebellion.RebellionBehaviors;
import rebellion.RebellionDelegate;
import rules.RulesLookupBehavior;
import table.Tables;
import weapons.WeaponsBehavior;

public class CoreListener extends ListenerAdapter {
    private final GroupBehavior primaryContext;

    public CoreListener() {

        primaryContext = new GroupBehavior()
                .setDefault(new Behavior() {
                    @Override
                    public void run(DeckList<String> message, MessageChannel channel) {
                        String key = String.join(" ", message.getAll());
                        String value = Variables.get(key);
                        System.err.println("I DON'T THINK THIS SHOULD BE REACHABLE");
                        if (value != null) {
                            primaryContext.run(new DeckList<>(Arrays.asList(value.split(" "))), channel);
                        }
                    }
                })
                .add(new String[]{"rebellion", "!r"}, RebellionBehaviors.getRebellionBehavior())
                .add(new String[]{"roll", "/roll", "!roll"}, DieParser.getRollBehavior())
                .add(new String[]{"var"}, Variables.getVarBehavior())
                .add(new String[]{"pvar"}, Variables.getPVarBehavior())
                .add(new String[]{"dc"}, CheckDC.getDCBehavior())
                .add(new String[]{"rule", "rules"}, new RulesLookupBehavior())
                .add(new String[]{"item", "!i"}, new ItemBehavior())
                .add(new String[]{"weapon", "!w"}, new WeaponsBehavior())
                .add(new String[]{"pack", "!p"}, new PackBehavior())
                .add(new String[]{"armor", "!a"}, new ArmorBehavior())
                .add(new String[]{"table"}, Tables.getBehavior())
                .add(new String[]{"hey gary"}, GaryPersonality.getGaryGreet());

        primaryContext
                .add(new String[]{"help"}, new NachoHelpBehavior(primaryContext));
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        Context.setCaller(event.getAuthor());
        MessageChannel channel = event.getChannel();

        handleAttachments(event, channel);

        try {
            DeckList<String> message = getDeckListMessage(event);

            if (event.getMessage().isMentioned(event.getJDA().getSelfUser(), Message.MentionType.USER)) {
                channel = event.getAuthor().openPrivateChannel().complete();

                if (message.canDraw()) {
                    message.draw();
                }
            }

            primaryContext.run(message, channel);
        } catch (Throwable e) {
            channel.sendMessage(">>>>>>>>>>>>>>ERROR\n" + e.getMessage()).queue();
            throw e;
        }
    }

    @NotNull
    private DeckList<String> getDeckListMessage(@NotNull MessageReceivedEvent event) {
        final String key = event.getMessage().getContentRaw().toLowerCase().trim();

        return new DeckList<>(Arrays.asList(key.split(" ")));
    }

    private void handleAttachments(@NotNull MessageReceivedEvent event, MessageChannel channel) {
        List<Message.Attachment> attachments = event.getMessage().getAttachments();
        for (Message.Attachment attachment : attachments) {
            if ("xml".equals(attachment.getFileExtension())) {
                runAsync(attachment, file -> {
                    String varSet = new PCGVarLoader(file).add();
                    channel.sendMessage("added character sheet to " + varSet).queue();
                });
            } else if ("csv".equals(attachment.getFileExtension())) {
                if (attachment.getFileName().startsWith("table_")) {
                    runAsync(attachment, file -> {
                        String tableName = new TableLoader(file).add();
                        channel.sendMessage("created Table" + tableName).queue();
                    });
                }
            } else if ("yaml".equals(attachment.getFileExtension())){
                if (attachment.getFileName().startsWith("rebellion_")) {
                    runAsync(attachment, file -> {
                        Rebellion rebellion = RebellionDelegate.getRebellionFromFile(file);
                        RebellionDelegate rebellionDelegate = new RebellionDelegate();

                        Rebellion currentRebellion = RebellionDelegate.getCurrentRebellion(channel.getId());

                        rebellion.setId(currentRebellion.getId());

                        rebellionDelegate.storeRebellion(rebellion);

                        channel.sendMessage("updated rebellion in this channel, id: " + currentRebellion.getId()).queue();
                    });
                }
            }
        }
    }

    private void runAsync(Message.Attachment attachment, Consumer<File> func) {
        Thread async = new Thread(() -> {
            try {
                File file = attachment.downloadToFile().get();
                func.accept(file);
                file.delete();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        async.start();
    }

}
