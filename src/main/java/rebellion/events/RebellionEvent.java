package rebellion.events;

import behavior.Behavior;
import core.DeckList;
import core.DieParser;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;
import rebellion.Rebellion;
import rebellion.RebellionDelegate;

public interface RebellionEvent {
	RebellionEvent WEEK_OF_SECRECY = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Week of Secrecy: The rebellion is poised to have\n" +
					"an excellent week, due to internal squabbling among\n" +
					"Lord-Mayor Thrune’s minions and the church. All\n" +
					"Organization checks made for the next week gain a +6\n" +
					"bonus, and double the amount of supporters gained.";
		}
	};
	RebellionEvent SUCCESSFUL_PROTEST = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Successful Protest: Your supporters have successfully\n" +
					"protested against House Thrune or the church of\n" +
					"Asmodeus. Increase your total supporters by 2d6. Select\n" +
					"one of the following settlement modifiers—Corruption,\n" +
					"Crime, Economy, Law, Lore, or Society. For the next week,\n" +
					"this modifier is increased by 4.";
		}
	};
	RebellionEvent DIMINISHED_PERIL = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Diminished Peril: The streets of Kintargo are\n" +
					"unusually safe. For the following week (including the\n" +
					"next week’s events, if any), decrease Kintargo’s danger\n" +
					"rating by 10.";
		}
	};
	RebellionEvent DONATION = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Donation: One or several of your wealthier supporters\n" +
					"have donated food, gold, and supplies to the Silver\n" +
					"Ravens. Roll a Loyalty check. The Rebellion treasury\n" +
					"gains gold equal to the result of this check × 20.";
		}
	};
	RebellionEvent INCREASED_SUPPORT = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Increased Support: An unexpected number of new\n" +
					"supporters join the cause. Increase the rebellion’s\n" +
					"supporters by 2d6.";
		}
	};
	RebellionEvent MARKETPLACE_BOOM = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Marketplace Boom: A new minor, new intermediate,\n" +
					"and new major magic item become available for sale in\n" +
					"Kintargo’s markets. The GM randomly determines what\n" +
					"items are for sale and where they can be purchased.";
		}
	};
	RebellionEvent ALL_IS_CALM = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "All Is Calm: No event occurs this week or next week;\n" +
					"gain a +1 bonus on all Security checks made during the\n" +
					"next week. If you roll All Is Calm as a second event, it does\n" +
					"not cancel the effects of the first event but still prevents\n" +
					"an event from happening next week. An eventless week\n" +
					"caused by this event does not raise the chance of an event\n" +
					"occurring in the week after.";
		}
	};
	RebellionEvent ROLL_TWICE = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Roll Twice: Roll two times. Both events occur this\n" +
					"phase, in the order they are rolled. Multiple rolls of Roll\n" +
					"Twice stack.";
		}
	};
	RebellionEvent SNITCH = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Snitch: One of your supporters is a snitch who has been\n" +
					"leaking rumors and information about the Silver Ravens\n" +
					"to House Thrune and the church of Asmodeus. Attempt\n" +
					"a DC 15 Loyalty check. If you are successful, reduce your\n" +
					"supporters by 1, but you need fear no further repercussions\n" +
					"as your other loyal supporters have handled the situation.\n" +
					"If you fail this Loyalty check, reduce your supporters by 1\n" +
					"and your Notoriety score increases by 1d6.";
		}
	};
	RebellionEvent RIVALRY = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Rivalry*: Choose two random teams; those teams\n" +
					"have developed an unhealthy but temporary rivalry.\n" +
					"During the next Activity phase, you can’t use either of\n" +
					"these teams to take actions. If you roll Rivalry twice in\n" +
					"one Event phase, it becomes persistent. \n" +
					"Mitigation: If a\n" +
					"Silver Ravens officer makes a DC 20 Bluff, Diplomacy, or\n" +
					"Intimidate check to deal with the rivalry, it ends.";
		}
	};
	RebellionEvent DANGEROUS_TIMES = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Dangerous Times*: The streets of Kintargo have\n" +
					"grown particularly dangerous. For the following week\n" +
					"(including the next week’s events, if any), increase\n" +
					"Kintargo’s danger rating by 10. If you roll Dangerous\n" +
					"Times twice in one Event phase, it becomes persistent.\n" +
					"Mitigation: If a rebellion officer succeeds at a DC 20\n" +
					"Intimidate check to spread word about the rebellion’s\n" +
					"strength, the danger rating increase is halved.";
		}
	};
	RebellionEvent MISSING_IN_ACTION = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Missing in Action: Randomly determine one of your\n" +
					"teams that was committed to an action this week. That\n" +
					"team accomplished its goal, but soon thereafter went\n" +
					"missing. The missing team still counts against the\n" +
					"maximum number of teams the rebellion can have.\n" +
					"During the next Upkeep phase, attempt a DC 15 Security\n" +
					"check. If you’re successful, the team is rescued or returns\n" +
					"to base, but must wait until the next week to be used for\n" +
					"rebellion actions. If you fail, the team remains missing\n" +
					"for another week. If you roll a natural 1 on this Security\n" +
					"check, the team is lost. If no teams were committed to\n" +
					"an action during the week, treat this event as a result of\n" +
					"Dangerous Times.";
		}
	};
	RebellionEvent CACHE_DISCOVERED = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Cache Discovered: Randomly determine one of your\n" +
					"current caches hidden in Kintargo. That cache has been\n" +
					"discovered, and its contents are lost. If you have no \n" +
					"caches hidden at this time, Asmodean inquisitors instead\n" +
					"capture some of your supporters; reduce your supporters\n" +
					"and Kintargo’s population by 1d6.";
		}
	};
	RebellionEvent INCREASED_PATROLS = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Increased Patrols*: Dottari patrols on the streets of\n" +
					"Kintargo are increased this week, resulting in heightened\n" +
					"security. For the next week (including the next week’s\n" +
					"events, if any), all Secrecy checks take a –4 penalty. If\n" +
					"you roll Increased Patrols twice in one Event phase, it\n" +
					"becomes persistent. \n" +
					"Mitigation: If a Silver Ravens officer\n" +
					"makes a successful DC 20 Survival check to study the\n" +
					"patrol routes and interpret the patterns, this penalty is\n" +
					"reduced to –2.";
		}
	};
	RebellionEvent LOW_MORALE = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Low Morale*: Things in Kintargo have begun to\n" +
					"overwhelm the rebels with a sense of hopelessness and\n" +
					"despair. For the next week (including the next week’s\n" +
					"events, if any), all Loyalty checks take a –4 penalty. If you\n" +
					"roll Low Morale twice in one Event phase, it becomes\n" +
					"persistent. \n" +
					"Mitigation: If a Silver Ravens officer makes a\n" +
					"successful DC 20 Perform check to entertain the rebels\n" +
					"and raise spirits, this penalty is reduced to –2. ";
		}
	};
	RebellionEvent SICKNESS = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Sickness*: Contagion has spread through the ranks,\n" +
					"leaving your rebels listless and sickly. For the next week\n" +
					"(including the next week’s events, if any), all Security\n" +
					"checks take a –4 penalty. If you roll Sickness twice in one\n" +
					"Event phase, it becomes persistent. \n" +
					"Mitigation: If a Silver\n" +
					"Ravens officer makes a successful DC 20 Heal check to\n" +
					"treat the sickness, this penalty is reduced to –2.";
		}
	};
	RebellionEvent DISABLED_TEAM = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Disabled Team: Randomly determine one of your\n" +
					"teams that was committed to an action this week. That\n" +
					"team accomplished its goal, but took some damage\n" +
					"during the mission and became disabled. The team\n" +
					"cannot be used for a rebellion action during the next\n" +
					"week. If no teams were committed to an action during\n" +
					"the week, treat this event as a result of Dangerous Times.\n" +
					"Mitigation: If you spend gold equal to your current\n" +
					"minimum treasury value, you can restore the disabled\n" +
					"team to full health.";
		}
	};
	RebellionEvent DISSENSION_IN_THE_RANKS = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Dissension in the Ranks*: Personality conflicts among\n" +
					"the Silver Ravens have compromised cooperation. For\n" +
					"the next week (including the next week’s events, if any),\n" +
					"all Organization checks take a –4 penalty. If you roll\n" +
					"Dissension in the Ranks twice in one Event phase, it becomes persistent. \n" +
					"Mitigation: If a Silver Ravens officer \n" +
					"makes a successful DC 20 Diplomacy check to soothe\n" +
					"over the dissension, this penalty is reduced to –2.";
		}
	};
	RebellionEvent INVASION = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Invasion: A dangerous creature has invaded! The GM\n" +
					"rolls or selects a wandering monster from the encounter\n" +
					"tables available to her in the adventures, and you must\n" +
					"step in to fight this intruder. The location in which the\n" +
					"invasion occurs is selected by the GM. If you choose not\n" +
					"to deal with the invader in person, the Silver Ravens\n" +
					"themselves handle the situation, but in doing so, 1d4\n" +
					"randomly determined teams are lost and 1d4 randomly\n" +
					"determined teams are disabled. In addition, the party’s\n" +
					"failure to handle the invader causes the rebellion to gain\n" +
					"a persistent Low Morale event.";
		}
	};
	RebellionEvent FAILED_PROTEST = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Failed Protest: Your supporters have failed in a protest\n" +
					"against House Thrune or the church of Asmodeus. Reduce\n" +
					"your total supporters (and Kintargo’s population) by 2d6.\n" +
					"Mitigation: A successful DC 25 Security check negates\n" +
					"this reduction. Regardless of that outcome, randomly\n" +
					"determine one of Kintargo’s settlement modifiers—\n" +
					"Corruption, Crime, Economy, Law, Lore, or Society. For\n" +
					"the next week, this modifier is decreased by 4.";
		}
	};
	RebellionEvent ALLY_IN_PERIL = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Ally in Peril: One of your allies, randomly determined,\n" +
					"is put in peril. Attempt a Security check (DC = 20 – the\n" +
					"ally’s level; minimum DC 10). If the check is a success,\n" +
					"the ally is merely missing for a week. During the next\n" +
					"Upkeep phase, attempt a new Security check against the\n" +
					"same DC. If that check is successful, the ally returns\n" +
					"with a harrowing story or the like of how he or she had\n" +
					"to flee or lie low for a time, but if that check is a failure,\n" +
					"the ally has been captured. If you fail the initial check,\n" +
					"the ally is also captured. The GM decides who captured\n" +
					"the ally and where he or she is being held prisoner, but\n" +
					"in most cases, the ally can be rescued by a successful\n" +
					"Rescue Character action.";
		}
	};
	RebellionEvent DISASTROUS_MISSION = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Disastrous Mission: Randomly determine one of your\n" +
					"teams that was committed to an action this week. That\n" +
					"team accomplished its goal, but took significant damage\n" +
					"in the process. Attempt a DC 20 Security check. If you\n" +
					"are successful, the team becomes disabled. If you fail this\n" +
					"check, the team is destroyed and must be replaced. In any\n" +
					"event, increase your Notoriety score by 1d6. If no teams\n" +
					"were committed to an action during the week, treat this\n" +
					"event as a result of Dangerous Times.";
		}
	};
	RebellionEvent TRAITOR = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Traitor: One of the Silver Ravens is revealed to be a\n" +
					"traitor! Randomly determine one team from all of your\n" +
					"teams; that team is the one that housed the traitor. That\n" +
					"team becomes disabled after the traitor is uncovered. If\n" +
					"you make a successful DC 20 Loyalty check, the traitor\n" +
					"has been discovered before he or she could significantly\n" +
					"damage the Silver Ravens and you can either attempt\n" +
					"to redeem, execute, exile, or imprison the traitor. If you\n" +
					"fail this Loyalty check (or if you do not take any of the\n" +
					"previously four mentioned responses), the traitor escapes\n" +
					"and the Silver Ravens’ Notoriety score increases by 2d6.\n" +
					"If you wish to attempt to redeem a captured traitor, you\n" +
					"must first imprison him or her, then take a Special Action\n" +
					"during the next Activity phase, during which the Silver\n" +
					"Ravens must make a successful DC 20 Loyalty check. If\n" +
					"you succeed, the traitor changes allegiance, your disabled\n" +
					"team is no longer disabled, and you need not fear an\n" +
					"increase in your Notoriety score in the future from this\n" +
					"particular one-time traitor. Each time you redeem a traitor\n" +
					"in this way, you automatically gain 1d6 supporters at the\n" +
					"start of the next Upkeep phase. If you capture and execute\n" +
					"the traitor, you prevent any increase in your Notoriety\n" +
					"score but damage the Silver Ravens’ morale, causing the\n" +
					"rebels to suffer from a persistent Low Morale unless you\n" +
					"make a successful DC 20 Loyalty check. If you capture and\n" +
					"wish to exile the traitor, you must make a successful DC\n" +
					"25 Security check to convince the traitor to never return\n" +
					"to Kintargo. Failure results in an increase of 2d6 to your\n" +
					"Notoriety score as the traitor sneaks back into the city to\n" +
					"report to Barzillai Thrune. If you capture and imprison\n" +
					"the traitor, you must make a successful DC 20 Secrecy\n" +
					"check during every Upkeep phase until you choose to\n" +
					"execute, exile, or successfully redeem the traitor. If you fail\n" +
					"this Secrecy check, the traitor escapes and the rebellion’s\n" +
					"Notoriety score increases by 2d6. If the Silver Ravens do\n" +
					"not currently have any teams, treat this event as no event.";
		}
	};
	RebellionEvent DIABOLIC_INFILTRATION = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Diabolic Infiltration: One of the Silver Ravens is in\n" +
					"fact a magically disguised devil, or has been possessed\n" +
					"by a diabolic spirit. Roll 1d6; if you roll a 6 (and each\n" +
					"time thereafter you roll a 6), roll another 1d6 and add\n" +
					"those results together. The final result tells you the\n" +
					"number of weeks that the infiltration has persisted\n" +
					"unnoticed until this event reveals the truth; the\n" +
					"infiltration cannot last more weeks than the Silver\n" +
					"Ravens have been active. Increase you Notoriety score\n" +
					"by 1d6 for each week that the infiltration was active; a\n" +
					"successful DC 15 Loyalty check halves this Notoriety gain. \n" +
					"Mitigation: If an officer makes a successful DC 20\n" +
					"Sense Motive check, the infiltration is noticed early.\n" +
					"Halve all results for determining the number of weeks\n" +
					"the infiltration has been afflicting the rebellion.";
		}
	};
	RebellionEvent INQUISITION = new RebellionEvent() {
		@Override
		public String doEvent() {
			return "";
		}

		@Override
		public String getDescription() {
			return "Inquisition*: Thrune and the church have grown tired\n" +
					"of the rebellion, and for the following week, the rebellion\n" +
					"loses twice as many supporters as indicated whenever\n" +
					"supporters are lost. In addition, the bonuses and\n" +
					"penalties applied to Kintargo’s modifiers by the city’s\n" +
					"martial law disadvantage are doubled for the week. If\n" +
					"you roll Inquisition twice in one Event phase, it becomes\n" +
					"persistent. \n" +
					"Mitigation: By taking the Lie Low action, the\n" +
					"rebellion can end a persistent Inquisition by making a\n" +
					"successful DC 20 Secrecy check.";
		}
	};

	static RebellionEvent getEvent(int eventNumber) {

		if (eventNumber <= 4) {
			return WEEK_OF_SECRECY;
		} else if (eventNumber <= 8) {
			return SUCCESSFUL_PROTEST;
		} else if (eventNumber <= 14) {
			return DIMINISHED_PERIL;
		} else if (eventNumber <= 20) {
			return DONATION;
		} else if (eventNumber <= 28) {
			return INCREASED_SUPPORT;
		} else if (eventNumber <= 38) {
			return MARKETPLACE_BOOM;
		} else if (eventNumber <= 48) {
			return ALL_IS_CALM;
		} else if (eventNumber <= 51) {
			return ROLL_TWICE;
		} else if (eventNumber <= 59) {
			return SNITCH;
		} else if (eventNumber <= 63) {
			return RIVALRY;
		} else if (eventNumber <= 67) {
			return DANGEROUS_TIMES;
		} else if (eventNumber <= 71) {
			return MISSING_IN_ACTION;
		} else if (eventNumber <= 75) {
			return CACHE_DISCOVERED;
		} else if (eventNumber <= 79) {
			return INCREASED_PATROLS;
		} else if (eventNumber <= 83) {
			return LOW_MORALE;
		} else if (eventNumber <= 87) {
			return SICKNESS;
		} else if (eventNumber <= 91) {
			return DISABLED_TEAM;
		} else if (eventNumber <= 95) {
			return DISSENSION_IN_THE_RANKS;
		} else if (eventNumber <= 99) {
			return INVASION;
		} else if (eventNumber <= 103) {
			return FAILED_PROTEST;
		} else if (eventNumber <= 107) {
			return ALLY_IN_PERIL;
		} else if (eventNumber <= 111) {
			return DISASTROUS_MISSION;
		} else if (eventNumber <= 115) {
			return TRAITOR;
		} else if (eventNumber <= 119) {
			return DIABOLIC_INFILTRATION;
		} else {
			return INQUISITION;
		}
	}

	String doEvent();

	String getDescription();

	@NotNull
	public static Behavior getEventDoBehavior(Rebellion rebellion) {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				int eventNumber = getEventNumber(message, rebellion);
				RebellionEvent rebellionEvent = RebellionEvent.getEvent(eventNumber);
				channel.sendMessage("{" + eventNumber + "}").queue();
				channel.sendMessage(rebellionEvent.getDescription()).queue();
				String text = rebellionEvent.doEvent();
				if (!text.isBlank()) {
					channel.sendMessage(text).queue();
				}
			}

			@Override
			public String getHelp(DeckList<String> s, String key) {
				return "rolls for an event then performs the event TBD";
			}
		};
	}

	@NotNull
	static Behavior getEventBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				int eventNumber = getEventNumber(message, RebellionDelegate.getCurrentRebellion(channel.getId()));
				RebellionEvent rebellionEvent = RebellionEvent.getEvent(eventNumber);
				channel.sendMessage("{" + eventNumber + "}").queue();
				channel.sendMessage(rebellionEvent.getDescription()).queue();
			}

			@Override
			public String getHelp(DeckList<String> s, String key) {
				return "rolls for an event on the event table.";
			}
		};
	}

	static int getEventNumber(DeckList<String> message, Rebellion rebellion) {
		if (!message.canDraw()) {

			return DieParser.rollDiceGroups("1d100" + "+" + rebellion.getDangerRating()).get(0).getSum();
		} else {
			return Integer.parseInt(String.join(" ", message.getDeck()));
		}
	}
}
