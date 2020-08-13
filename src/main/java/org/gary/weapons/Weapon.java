package org.gary.weapons;

import org.gary.behavior.Behavior;
import org.gary.behavior.ChannelHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.gary.behavior.DeckList;
import java.util.List;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.gary.weapons.modifiers.WeaponModifiers;

public interface Weapon {
	boolean isMasterWork();

	int getEnchantmentLevel();

	String getName();

	String getCategory();

	String getProficiency();

	List<String> getWeaponGroups();

	@JsonIgnore
	default Behavior getWeaponStatBlockBehavior(){
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				String statBlock =
						getName() + "\n" +
								"Cost: " + getCost() + " gp Weight: " + getWeight() + " lbs\n" +
								"Damage: " + getDamage() + " Critical: " + getCritical() + " Range: " + getRange() + " Type: " + getType() + " Special: " + getSpecial() + "\n" +
								"Category: " + getCategory() + " Proficiency: " + getProficiency() + "\n" +
								"Weapon Groups: " + String.join(", ", getWeaponGroups()) + "\nDescription:\n"
								+ getDescription();

				ChannelHelper.sendLongMessage(" ", statBlock, channel);
			}
		};
	}

	String getDescription();

	String getSpecial();

	String getType();

	String getRange();

	CriticalRange getCritical();

	String getDamage();

	int getWeight();

	@JsonIgnore
	default int getCost() {
		return getBaseCost() + (isMasterWork()? 300: 0)+ WeaponModifiers.getEnchantmentCost(getEnchantmentLevel());
	}

	int getBaseCost();

	String getUrl();
}
