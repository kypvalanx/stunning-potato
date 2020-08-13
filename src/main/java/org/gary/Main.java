package org.gary;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;
import org.gary.core.CoreListener;

public class Main {
    public static void main(String[] args) throws LoginException {
        Config config = Config.getConfiguration();
        if(config == null){
            return;
        }
        String token = config.getToken();

        JDABuilder builder = new JDABuilder(AccountType.BOT)
                .setToken(token)
                .addEventListeners(new CoreListener());
        builder.build();

    }

}
