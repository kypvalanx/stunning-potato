package weapons.modifiers;

import java.util.HashMap;
import java.util.Map;

public class EnchantmentBonusModifiers {
	public static Map<String, WeaponModifier> getModifiers() {
		Map<String, WeaponModifier> modifiers = new HashMap<>();

		modifiers.put("+1", new WeaponModifier(){
			@Override
			public int getEnchantmentLevel() {
				return parent.getEnchantmentLevel() + 1;
			}

			@Override
			public String getName() {
				return "+1 " + parent.getName().replace("Masterwork ", "");
			}

			@Override
			public boolean isMasterWork() {
				return true;
			}
		});
		modifiers.put("+2", new WeaponModifier(){
			@Override
			public int getEnchantmentLevel() {
				return parent.getEnchantmentLevel() + 2;
			}
			@Override
			public String getName() {
				return "+2 " + parent.getName().replace("Masterwork ", "");
			}

			@Override
			public boolean isMasterWork() {
				return true;
			}
		});
		modifiers.put("+3", new WeaponModifier(){
			@Override
			public int getEnchantmentLevel() {
				return parent.getEnchantmentLevel() + 3;
			}

			@Override
			public String getName() {
				return "+3 " + parent.getName().replace("Masterwork ", "");
			}
			@Override
			public boolean isMasterWork() {
				return true;
			}
		});
		modifiers.put("+4", new WeaponModifier(){
			@Override
			public int getEnchantmentLevel() {
				return parent.getEnchantmentLevel() + 4;
			}

			@Override
			public String getName() {
				return "+4 " + parent.getName().replace("Masterwork ", "");
			}
			@Override
			public boolean isMasterWork() {
				return true;
			}
		});
		modifiers.put("+5", new WeaponModifier(){
			@Override
			public int getEnchantmentLevel() {
				return parent.getEnchantmentLevel() + 5;
			}

			@Override
			public String getName() {
				return "+5 " + parent.getName().replace("Masterwork ", "");
			}

			@Override
			public boolean isMasterWork() {
				return true;
			}
		});

		WeaponModifier masterwork = new WeaponModifier() {
			@Override
			public boolean isMasterWork() {
				return true;
			}

			@Override
			public String getName() {
				if (parent.isMasterWork()) {
					return parent.getName();
				}
				return "Masterwork " + parent.getName();
			}
		};

		modifiers.put("mwk", masterwork);
		modifiers.put("masterwork", masterwork);

		return modifiers;
	}

	public static int getEnchantmentCost(int enchantmentLevel) {
		return (int)(Math.pow(enchantmentLevel, 2) * 2000);
	}
}
