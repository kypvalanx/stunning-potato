package weapons.modifiers;

import java.util.List;
import weapons.CriticalRange;
import weapons.Weapon;

public class WeaponModifier implements Weapon {

	protected Weapon parent;

	public Weapon add(Weapon parent) {
		this.parent = parent;
		return this;
	}

	@Override
	public boolean isMasterWork() {
		return false;
	}

	@Override
	public int getEnchantmentLevel() {
		return parent.getEnchantmentLevel();
	}

	@Override
	public String getName() {
		return parent.getName();
	}

	@Override
	public String getCategory() {
		return parent.getCategory();
	}

	@Override
	public String getProficiency() {
		return parent.getProficiency();
	}

	@Override
	public List<String> getWeaponGroups() {
		return parent.getWeaponGroups();
	}

	@Override
	public String getDescription() {
		return parent.getDescription();
	}

	@Override
	public String getSpecial() {
		return parent.getSpecial();
	}

	@Override
	public String getType() {
		return parent.getType();
	}

	@Override
	public String getRange() {
		return parent.getRange();
	}

	@Override
	public CriticalRange getCritical() {
		return parent.getCritical();
	}

	@Override
	public String getDamage() {
		return parent.getDamage();
	}

	@Override
	public int getWeight() {
		return parent.getWeight();
	}

	@Override
	public int getBaseCost() {
		return parent.getBaseCost();
	}
}
