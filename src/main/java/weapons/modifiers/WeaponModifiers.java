package weapons.modifiers;

import java.util.HashMap;
import java.util.Map;

public class WeaponModifiers {
	private static Map<String, WeaponModifier> modifiers = new HashMap<>();

	static {
		modifiers.putAll(EnchantmentBonusModifiers.getModifiers());
	}

	public static Map<String,WeaponModifier> get() {
		return modifiers;
	}
}
