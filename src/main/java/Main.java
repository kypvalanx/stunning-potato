import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import rebellion.RebellionListener;
import rules.RulesListener;

public class Main {
    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken("NjgxODgyMjQ3NTUyNjk2Mzc0.XlVCVA.j4JeFa4Uqu40BBSrhLXHFPf1EVw");
        builder.addEventListeners(new RebellionListener());
        builder.addEventListeners(new RulesListener());
        JDA jda = builder.build();
    }
}
