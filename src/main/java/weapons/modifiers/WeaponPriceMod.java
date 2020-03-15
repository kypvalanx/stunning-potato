package weapons.modifiers;

public class WeaponPriceMod {
	private final String bonus;
	private final WeaponPriveType type;
	private final String[] keys;
	private final String description;
	private String url;

	public WeaponPriceMod(String key, String bonus, String type, String description, String url) {
		this.bonus = bonus;
		this.type = WeaponPriveType.valueOf(type.toUpperCase());
		if(key.contains(", ")){
			String[] tok = key.split(", ");
			this.keys = new String[]{tok[1] + " " + tok[0], key};
		}else{
			this.keys = new String[]{key};
		}
		this.description = description;
		this.url = url;
	}

	public String[] keys() {
		return keys;
	}

	public WeaponModifier getWeaponMod() {
		if(WeaponPriveType.BONUS.equals(type)){
			return new WeaponModifier(){
				@Override
				public String getName() {
					return keys[0] + " " + parent.getName();
				}

				@Override
				public int getEnchantmentLevel() {
					return parent.getEnchantmentLevel() + Integer.parseInt(bonus);
				}

				@Override
				public String getDescription(){
					return parent.getDescription() + "\n\n" + description;
				}

				@Override
				public String getUrl(){
					return url;
				}
			};
		}else if(WeaponPriveType.GP.equals(type)){
			return new WeaponModifier(){
				@Override
				public String getName() {
					return keys[0] + " " + parent.getName();
				}

				@Override
				public int getBaseCost() {
					return parent.getBaseCost() + Integer.parseInt(bonus);
				}

				@Override
				public String getDescription(){
					return parent.getDescription() + "\n\n" + description;
				}
			};
		}
		return new WeaponModifier(){

		};
	}
}
