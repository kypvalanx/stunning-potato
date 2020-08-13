package org.gary.weapons.modifiers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import org.gary.weapons.CriticalRange;
import org.gary.weapons.Weapon;

public class WeaponBaseMod implements Weapon {

	protected Weapon parent;

	public Weapon add(Weapon parent) {
		this.parent = parent;
		return this;
	}

	@Override
	@JsonIgnore
	public boolean isMasterWork() {
		return false;
	}

	@Override
	@JsonIgnore
	public int getEnchantmentLevel() {
		return parent.getEnchantmentLevel();
	}

	@Override
	@JsonIgnore
	public String getName() {
		return parent.getName();
	}

	@Override
	@JsonIgnore
	public String getCategory() {
		return parent.getCategory();
	}

	@Override
	@JsonIgnore
	public String getProficiency() {
		return parent.getProficiency();
	}

	@Override
	@JsonIgnore
	public List<String> getWeaponGroups() {
		return parent.getWeaponGroups();
	}

	@Override
	@JsonIgnore
	public String getDescription() {
		return parent.getDescription();
	}

	@Override
	@JsonIgnore
	public String getSpecial() {
		return parent.getSpecial();
	}

	@Override
	@JsonIgnore
	public String getType() {
		return parent.getType();
	}

	@Override
	@JsonIgnore
	public String getRange() {
		return parent.getRange();
	}

	@Override
	@JsonIgnore
	public CriticalRange getCritical() {
		return parent.getCritical();
	}

	@Override
	@JsonIgnore
	public String getDamage() {
		return parent.getDamage();
	}

	@Override
	@JsonIgnore
	public int getWeight() {
		return parent.getWeight();
	}

	@Override
	@JsonIgnore
	public int getBaseCost() {
		return parent.getBaseCost();
	}

	@Override
	@JsonIgnore
	public String getUrl() {
		return null;
	}
}
