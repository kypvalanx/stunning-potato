import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import core.CoreListener;
import java.io.File;
import java.io.IOException;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDABuilder;

public class Main {
    public static void main(String[] args) throws LoginException {
            File configFile = new File("resources/config/token.yaml");

            if(configFile.canRead()){
                ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

                try {
                    String token = mapper.readValue(configFile, Config.class).getToken();

                    JDABuilder builder = new JDABuilder(AccountType.BOT)
                            .setToken(token)
                            .addEventListeners(new CoreListener());
                    builder.build();

                } catch (IOException e) {
                    e.printStackTrace();
                    throw new IllegalStateException("the existing config file is malformed");
                }
            } else
            {
                System.out.println("you don't have a config file i'll add one for you to populate");

                ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
                try {
                    new File("resources/config/").mkdir();
                    configFile.createNewFile();
                    mapper.writeValue(configFile, new Config());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }
}
