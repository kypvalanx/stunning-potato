package core;

import behavior.Behavior;
import behavior.GroupBehavior;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CheckDC {

	private static Integer dc = null;
	private static String failure = null;

	private CheckDC(){

	}

	public static boolean hasDC() {
		return dc != null;
	}

	public static Integer peek() {
		return dc;
	}

	public static void setDC(int checkDC) {
		dc = checkDC;
	}

	public static void setFailureMessage(String failureMessage ) {
		failure = failureMessage;
	}

	public static int getDC() {
		Integer checkDC = dc;
		dc = null;
		return checkDC;
	}

	public static String getFailureMessage() {
		String failureMessage = failure;
		failure = null;
		return failureMessage;
	}

	public static Behavior getDCBehavior() {
		return new GroupBehavior()
				.setDefault(new Behavior() {
					@Override
					public void run(MessageReceivedEvent event, DeckList<String> message) {
						if (!hasDC()) {
							event.getChannel().sendMessage("No current set DC.").queue();
						} else {
							event.getChannel().sendMessage("The next die roll will be checked against DC " + peek()).queue();
						}
					}

					@Override
					public String getHelp(DeckList<String> s, String key) {
						return "Check to see if any checks are pending.  The next die roll will be checked against it.";
					}
				})
				.add("set", new Behavior() {
					@Override
					public void run(MessageReceivedEvent event, DeckList<String> message) {
						setDC(Integer.parseInt(message.draw()));
						if (message.canDraw()) {
							setFailureMessage(String.join(" ", message.getDeck()));
						}
						event.getChannel().sendMessage("The next die roll will be checked against DC " + peek()).queue();
					}

					@Override
					public String getHelp(DeckList<String> s, String key) {
						return "Sets a check for the next die roll.  the first parameter is the DC, the second is a failure message.";
					}
				});
	}

	public static void attemptCheck(MessageReceivedEvent event, int roll) {
		if (hasDC()) {
			if (roll < getDC()) {
				event.getChannel().sendMessage("Check Failed! " + getFailureMessage()).queue();
			} else {
				event.getChannel().sendMessage("Check Passed!").queue();
			}
		}
	}
}
