package weapons;

import behavior.Behavior;
import com.fasterxml.jackson.annotation.JsonIgnore;
import core.DeckList;
import java.util.List;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Weapon {
	String getName();

	String getCategory();

	String getProficiency();

	List<String> getWeaponGroups();

	@JsonIgnore
	default Behavior getWeaponStatBlockBehavior(){
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				String statBlock =
						getName() + "\n" +
								"Cost: " + getCost() + " gp Weight: " + getWeight() + " lbs\n" +
								"Damage: " + getDamage() + " Critical: " + getCritical() + " Range: " + getRange() + " Type: " + getType() + " Special: " + getSpecial() + "\n" +
								"Category: " + getCategory() + " Proficiency: " + getProficiency() + "\n" +
								"WeaponImpl Groups: " + String.join(", ", getWeaponGroups()) + "\nDescription:\n"
								+ getDescription();

				event.getChannel().sendMessage(statBlock).queue();
			}
		};
	}

	String getDescription();

	String getSpecial();

	String getType();

	String getRange();

	String getCritical();

	String getDamage();

	int getWeight();

	int getCost();
}
