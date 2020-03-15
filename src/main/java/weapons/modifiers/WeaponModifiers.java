package weapons.modifiers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WeaponModifiers {


	private Pattern pricePattern = Pattern.compile("<b>price</b>\\s+\\+([\\d,]+)\\s+(bonus|gp)");
	private Pattern descriptionPattern = Pattern.compile("DESCRIPTION ([\\s\\w<>/=\":.\\-,#'’();+–—\\[\\]%×]*) CONSTRUCTION REQUIREMENTS");
	private Map<String, WeaponModifier> modifiers = new HashMap<>();

	public WeaponModifiers(){
		modifiers.put("+1", getEnchantmentBonus( 1));
		modifiers.put("+2", getEnchantmentBonus(2));
		modifiers.put("+3", getEnchantmentBonus( 3));
		modifiers.put("+4", getEnchantmentBonus(4));
		modifiers.put("+5", getEnchantmentBonus( 5));

		modifiers.put("mwk", getMasterworkBonus());
		modifiers.put("masterwork", getMasterworkBonus());

		getEnchantments();
		getMaterials();
		getMiscModifiers();
	}

	private void getMiscModifiers() {

	}

	private void getMaterials() {

	}

	public Map<String, WeaponModifier> getModifiers() {
		return modifiers;
	}

	private void getEnchantments() {
		Document doc = null;
		try {
			doc = Jsoup.connect("https://www.d20pfsrd.com/magic-items/magic-weapons/magic-weapon-special-abilities/").get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Element body = doc.body();

		Elements anchors = body.getElementsByTag("a");

		anchors.stream()
				.filter(new Predicate<Element>() {
					@Override
					public boolean test(Element element) {
						return element.attr("href").startsWith("https://www.d20pfsrd.com/magic-items/magic-weapons/magic-weapon-special-abilities/")
								&& !element.attr("href").equals("https://www.d20pfsrd.com/magic-items/magic-weapons/magic-weapon-special-abilities/");
					}
				}).forEach(new Consumer<Element>() {
			@Override
			public void accept(Element element) {
				try {
					String url = element.attr("href");
					Document sub = Jsoup.connect(url).get();

					Elements articleDiv = sub.getElementsByClass("article-content");

					WeaponPriceMod price = getPrice(element.text(), articleDiv, url);
					for(String key: price.keys()) {
						modifiers.put(key, price.getWeaponMod());
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private WeaponPriceMod getPrice(String key, Elements articleDiv, String url) {

		String nodes = articleDiv.html();

		String text = articleDiv.text();
		Matcher price = pricePattern.matcher(nodes.toLowerCase());

		Matcher descriptionMatcher = descriptionPattern.matcher(text);
		String description = "";
		if(descriptionMatcher.find()){
			description = descriptionMatcher.group(1);
		}else{
			System.out.println(nodes);
		}
		price.find();

			//System.out.println(price.group(1) + " " + price.group(2));

		return new WeaponPriceMod(key.toLowerCase(), price.group(1), price.group(2), description, url);
	}

	@NotNull
	private static WeaponModifier getMasterworkBonus() {
		return new WeaponModifier() {
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

			@Override
			public String getUrl(){
				return "https://www.d20pfsrd.com/equipment/weapons#TOC-Masterwork-Weapons";
			}
		};
	}

	@NotNull
	private static WeaponModifier getEnchantmentBonus(int i) {
		return new WeaponModifier() {
			@Override
			public int getEnchantmentLevel() {
				return parent.getEnchantmentLevel() + i;
			}

			@Override
			public String getName() {
				return "+" + i + " " + parent.getName().replace("Masterwork ", "");
			}

			@Override
			public String getUrl(){
				return "https://www.d20pfsrd.com/magic-items/magic-weapons/";
			}

			@Override
			public boolean isMasterWork() {
				return true;
			}
		};
	}

	public static int getEnchantmentCost(int enchantmentLevel) {
		return (int)(Math.pow(enchantmentLevel, 2) * 2000);
	}
}
