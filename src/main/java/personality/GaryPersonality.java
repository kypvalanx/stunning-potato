package personality;

import behavior.Behavior;
import core.DeckList;
import static core.DieParser.rollDiceGroups;
import core.DieResult;
import java.io.File;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

public class GaryPersonality {
    private static final File GYGAX_GREETING = new File("resources/images/gygax1.jpg");

    @NotNull
    public static Behavior getGaryGreet() {
        return new Behavior() {
            @Override
            public void run(DeckList<String> message, MessageChannel channel) {

                DieResult dieResult = rollDiceGroups("d20").get(0);
                String saying = dieResult.getSum() > 9 ? "pleased to meet you." : "so terribly, terribly disappointed in you";
                channel.sendFile(GYGAX_GREETING, ".pleased_to_meet_you.jpg").queue();
                channel.sendMessage("I am ... \n @GaryBot rolls " + dieResult.getSum() + "\n" + dieResult.getSteps() + "\n ... " + saying).queue();
            }

            @Override
            public String getHelp(DeckList<String> s, String key) {
                return "A way to check if Gary is around.  It's nice to check in on your friends.  Maybe call once in a while.";
            }
        };
    }
}
