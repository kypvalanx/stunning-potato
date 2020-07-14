package rebellion;

import behavior.Behavior;
import behavior.NachoHelpBehavior;
import core.CheckDC;
import core.DeckList;
import core.DieParser;
import core.DieResult;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

public class RebellionBehaviors {
	private final Behavior followUpBehavior;
	private Rebellion currentRebellion;

	public RebellionBehaviors(Rebellion currentRebellion, Behavior followUpBehavior) {
		this.currentRebellion = currentRebellion;
		this.followUpBehavior = followUpBehavior;
	}

	public void setCurrentRebellion(Rebellion currentRebellion) {
		this.currentRebellion = currentRebellion;
	}



	public Behavior getSupportersBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				if(message.canDraw()){

					currentRebellion.addSupporters(DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());
				}

				channel.sendMessage("Current Supporters " + currentRebellion.getSupporters()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"reports the current supporters or adds the payload"));
			}
		};
	}

	@NotNull
	public Behavior getSubSupportersBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {

				currentRebellion.addSupporters(-DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Supporters " + currentRebellion.getSupporters()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"subtracts from supporters, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getAddSupportersBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {

				currentRebellion.addSupporters(DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Supporters " + currentRebellion.getSupporters()).queue();
				followUpBehavior.run(message, channel);
			}
			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"adds to supporters, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getSetSupportersBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {

				currentRebellion.setSupporters(DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Supporters " + currentRebellion.getSupporters()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"sets supporters, accepts a die roll equation"));
			}
		};
	}


	@NotNull
	public Behavior getSubMembersBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {

				currentRebellion.addMembers(-DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"subtracts members, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getAddMembersBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {

				currentRebellion.addMembers(DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Members " + currentRebellion.getNotoriety()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"adds members, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getSubNotorietyBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {

				currentRebellion.addNotoriety(-DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"subtracts Notoriety, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getAddNotorietyBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {

				currentRebellion.addNotoriety(DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Notoriety " + currentRebellion.getNotoriety()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"adds members, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getSubPopulationBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {

				currentRebellion.addPopulation(-DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"subtracts population, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getAddPopulationBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {

				currentRebellion.addPopulation(DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Population " + currentRebellion.getPopulation()).queue();
				followUpBehavior.run( message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"adds population, accepts a die roll equation"));
			}
		};
	}

	public Behavior getTreasuryBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				if(message.canDraw()){

					currentRebellion.addTreasury(DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());
				}

				channel.sendMessage("Treasury " + currentRebellion.getTreasury()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"reports the treasury or adds a supplied payload"));
			}
		};
	}

	@NotNull
	public Behavior getSubTreasuryBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {

				currentRebellion.addTreasury(-DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"subtracts from treasury, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getAddTreasuryBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {

				currentRebellion.addTreasury(DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());

				channel.sendMessage("Current Treasury " + currentRebellion.getTreasury()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"adds to treasury, accepts a die roll equation"));
			}
		};
	}

	@NotNull
	public Behavior getSetStrategistBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				currentRebellion.setStrategist(message.getDeck());
				channel.sendMessage("Strategist Available: " + currentRebellion.isHasStrategist()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"adds a strategist, or removes the strategist if nothing is provided."));
			}
		};
	}

	@NotNull
	public Behavior getSetSpymasterBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				currentRebellion.setSpyMaster(message.getDeck());
				channel.sendMessage("Spymaster Dex/Int Set: " + currentRebellion.getSpymasterSecrecyBonus()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the Spymaster's Dex/Int stat, accepts the bonus you want active, both appropriate bonuses, or a 6 bonus array"));
			}
		};
	}

	@NotNull
	public Behavior getSetSentinelBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				currentRebellion.setSentinal(message.getDeck());
				channel.sendMessage("Sentinel Con/Cha, Str/Wis, Dex/Int Set: " + currentRebellion.getSentinelLoyaltyBonus() + ", " + currentRebellion.getSentinelSecurityBonus() + ", " + currentRebellion.getSentinelSecrecyBonus()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the Sentinel stats. Accepts three bonuses [Con/Cha, Str/Wis, Dex/Int], or a 6 bonus array"));
			}
		};
	}

	@NotNull
	public Behavior getSetRecruiterBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				currentRebellion.setRecruiter(Integer.parseInt(message.getDeck().get(0)));
				channel.sendMessage("Recruiter Level Set: " + currentRebellion.getRecruiterLvlBonus()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the Recruiters Character Level. Accepts a single number"));
			}
		};
	}

	@NotNull
	public Behavior getSetPartisanBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				currentRebellion.setPartisan(message.getDeck());
				channel.sendMessage("Partisan Str/Wis Set: " + currentRebellion.getPartisanSecurityBonus()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the Partisan's Str/Wis stat, accepts the bonus you want active, both appropriate bonuses, or a 6 bonus array"));
			}
		};
	}

	@NotNull
	public Behavior getSetDemagogueBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				currentRebellion.setDemagogue(message.getDeck());
				channel.sendMessage("Demagogue Con/Cha Set: " + currentRebellion.getDemagogueLoyaltyBonus()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the Demagogue's Con/Cha stat, accepts the bonus you want active, both appropriate bonuses, or a 6 bonus array"));
			}
		};
	}

	@NotNull
	public Behavior getSetSkillFocusBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				Focus focus = Focus.valueOf(String.join(" ", message.getDeck()).toUpperCase());
				currentRebellion.setFocus(focus);
				channel.sendMessage("Focus Set: " + currentRebellion.getFocus()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the rebellion Skill Focus: none, loyalty, secrecy, security."));
			}
		};
	}

	@NotNull
	public Behavior getSetMaxRankBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {

				currentRebellion.setMaxRank(DieParser.rollDiceGroups(String.join(" ", message.getDeck())).get(0).getSum());
				channel.sendMessage("Max Level Set: " + currentRebellion.getRebellionMaxLevel()).queue();
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Sets the maximum rank the rebellion can reach"));
			}
		};
	}

	public Behavior getRollLoyaltyBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				rollCheck(message, currentRebellion.getLoyaltyBonus(), channel);
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Rolls using your rebellion's loyalty bonus."));
			}
		};
	}

	public Behavior getRollSecrecyBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				rollCheck(message, currentRebellion.getSecrecyBonus(), channel);
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Rolls using your rebellion's secrecy bonus."));
			}
		};
	}

	public Behavior getRollSecurityBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				rollCheck(message, currentRebellion.getSecurityBonus(), channel);
				followUpBehavior.run(message, channel);
			}

			@Override
			public List<String> getFormattedHelp(DeckList<String> s, String key) {
				return List.of(NachoHelpBehavior.formatHelp(key,"Rolls using your rebellion's security bonus."));
			}
		};
	}




	private void rollCheck(DeckList<String> message, int bonus, MessageChannel channel) {
		DieResult dieResult;
		if (!message.canDraw()) {

			dieResult = DieParser.rollDiceGroups("1d20+" + bonus).get(0);
		} else {

			dieResult = DieParser.rollDiceGroups(String.join(" ", message.getDeck()) + "+" + bonus).get(0);
		}
		channel.sendMessage(" " + dieResult.getSum()).queue();
		channel.sendMessage(" " + dieResult.getSteps()).queue();
		CheckDC.attemptCheck(dieResult.getSum(), channel);
	}
}
