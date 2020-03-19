package weapons.modifiers;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WeaponMod {
	private String bonus;
	private WeaponPriveType type;
	private String[] keys;
	private String description;
	private String url;
	public WeaponMod() {

	}

	public WeaponMod(String key, String bonus, String type, String description, String url) {
		this.bonus = bonus;
		this.type = WeaponPriveType.valueOf(type.toUpperCase());
		if (key.contains(", ")) {
			String[] tok = key.split(", ");
			this.keys = new String[]{tok[1] + " " + tok[0], key};
		} else {
			this.keys = new String[]{key};
		}

		if(description.contains("points of damage")){
			System.err.println(description);
		}
		this.description = description;
		this.url = url;
	}

	public String getBonus() {
		return bonus;
	}

	public WeaponPriveType getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public String[] getKeys() {
		return keys;
	}

	@JsonIgnore
	public WeaponBaseMod getWeaponMod() {
		if (WeaponPriveType.BONUS.equals(type)) {
			return new WeaponBaseMod() {
				@Override
				public String getName() {
					return keys[0] + " " + parent.getName();
				}

				@Override
				public int getEnchantmentLevel() {
					return parent.getEnchantmentLevel() + Integer.parseInt(bonus);
				}

				@Override
				public String getDescription() {
					return parent.getDescription() + "\n\n" + description;
				}

				@Override
				public String getUrl() {
					return url;
				}
			};
		} else if (WeaponPriveType.GP.equals(type)) {
			return new WeaponBaseMod() {
				@Override
				public String getName() {
					return keys[0] + " " + parent.getName();
				}

				@Override
				public int getBaseCost() {
					return parent.getBaseCost() + Integer.parseInt(bonus);
				}

				@Override
				public String getDescription() {
					return parent.getDescription() + "\n\n" + description;
				}
			};
		}
		return new WeaponBaseMod() {

		};
	}
}
