package behavior;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChannelHelper {
	public static void sendLongMessage(MessageReceivedEvent event, String s, String steps) {
		String[] tokens = steps.split(s);

		String message = "";
		for(String tok : tokens){
			if(tok.length() + message.length() + s.length() < 2000){
				message = message.concat(" "+tok);
			}else{
				event.getChannel().sendMessage(message).queue();
				message = tok;
			}
		}

		event.getChannel().sendMessage(message).queue();
	}
}
