package behavior;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ChannelHelper {
	public static void sendLongMessage(MessageReceivedEvent event, String delimiter, String steps) {
		String[] tokens = steps.split(delimiter);

		String message = "";
		for(String tok : tokens){
			if(tok.length() + message.length() + delimiter.length() < 2000){
				message = message.concat(delimiter+tok);
			}else{
				event.getChannel().sendMessage(message).queue();
				message = tok;
			}
		}
        if(message.length() > 0) {
			event.getChannel().sendMessage(message).queue();
		}
	}
}
