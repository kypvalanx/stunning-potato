package org.gary.behavior;

import net.dv8tion.jda.api.entities.MessageChannel;

public class ChannelHelper {
	public static void sendLongMessage(String delimiter, String steps, MessageChannel channel) {
		if(steps == null || steps.isEmpty()){
			return;
		}
		String[] tokens = steps.split(delimiter);

		String message = "";
		for(String tok : tokens){
			if(tok.length() + message.length() + delimiter.length() < 2000){
				message = message.concat(delimiter+tok);
			}else{
				channel.sendMessage(message).queue();
				message = tok;
			}
		}
        if(message.length() > 0) {
			channel.sendMessage(message).queue();
		}
	}
}
