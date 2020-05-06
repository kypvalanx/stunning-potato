package core;

import net.dv8tion.jda.api.entities.User;

public class Context {
    private static User caller;

    public static User getCaller() {
        return caller;
    }

    public static void setCaller(User caller) {
        Context.caller = caller;
    }
}
