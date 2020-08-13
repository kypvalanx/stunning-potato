package org.gary.weapons.modifiers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WeaponModifiers {


	private Pattern pricePattern = Pattern.compile("<b>price</b>\\s+\\+([\\d,]+)\\s+(bonus|gp)");
	private Pattern descriptionPattern = Pattern.compile("DESCRIPTION ([\\s\\w<>/=\":.\\-,#'’();+–—\\[\\]%×]*) CONSTRUCTION REQUIREMENTS");
	private Map<String, WeaponBaseMod> modifiers = new HashMap<>();

	public WeaponModifiers() {
		modifiers.put("+1", getEnchantmentBonus(1));
		modifiers.put("+2", getEnchantmentBonus(2));
		modifiers.put("+3", getEnchantmentBonus(3));
		modifiers.put("+4", getEnchantmentBonus(4));
		modifiers.put("+5", getEnchantmentBonus(5));

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

	public Map<String, WeaponBaseMod> getModifiers() {
		return modifiers;
	}

	private void getEnchantments() {
		File resultFile = new File("resources/generated/weaponEnchantments.yaml");

		if (resultFile.canRead()) {
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

			TypeFactory typeFactory = mapper.getTypeFactory();

			CollectionType mapType = typeFactory.constructCollectionType(ArrayList.class, WeaponMod.class);

			try {
				List<WeaponMod> weaponMods = mapper.readValue(resultFile, mapType);
				weaponMods.forEach(this::addWeaponMod);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("You have no weapon enchantment data loaded, this will take a moment...");

			try {
				Document doc = null;
				doc = Jsoup.connect("https://www.d20pfsrd.com/magic-items/magic-weapons/magic-weapon-special-abilities/").get();
				Element body = doc.body();

				Elements anchors = body.getElementsByTag("a");


				List<WeaponMod> weaponModifiers = anchors.stream()
						.filter(new Predicate<Element>() {
							@Override
							public boolean test(Element element) {
								return element.attr("href").startsWith("https://www.d20pfsrd.com/magic-items/magic-weapons/magic-weapon-special-abilities/")
										&& !element.attr("href").equals("https://www.d20pfsrd.com/magic-items/magic-weapons/magic-weapon-special-abilities/");
							}
						}).map(new Function<Element, WeaponMod>() {
							@Override
							public WeaponMod apply(Element element) {
								try {
									String url = element.attr("href");
									Document sub = Jsoup.connect(url).get();

									Elements articleDiv = sub.getElementsByClass("article-content");

									return getPrice(element.text(), articleDiv, url);

								} catch (IOException e) {
									e.printStackTrace();
								}
								return null;
							}
						}).collect(Collectors.toList());
				weaponModifiers.forEach(this::addWeaponMod);
				ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));

				new File("resources/generated/").mkdir();
				resultFile.createNewFile();
				mapper.writeValue(resultFile, weaponModifiers);

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private void addWeaponMod(WeaponMod price) {
		for (String key : price.getKeys()) {
			modifiers.put(key, price.getWeaponMod());
		}
	}

	private WeaponMod getPrice(String key, Elements articleDiv, String url) {

		String nodes = articleDiv.html();

		String text = articleDiv.text();
		Matcher price = pricePattern.matcher(nodes.toLowerCase());

		Matcher descriptionMatcher = descriptionPattern.matcher(text);
		String description = "";
		if (descriptionMatcher.find()) {
			description = descriptionMatcher.group(1);
		} else {
			System.out.println(nodes);
		}
		price.find();

		//System.out.println(price.group(1) + " " + price.group(2));

		return new WeaponMod(key.toLowerCase(), price.group(1), price.group(2), description, url);
	}

	@NotNull
	private static WeaponBaseMod getMasterworkBonus() {
		return new WeaponBaseMod() {
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
			public String getUrl() {
				return "https://www.d20pfsrd.com/equipment/weapons#TOC-Masterwork-Weapons";
			}
		};
	}

	@NotNull
	private static WeaponBaseMod getEnchantmentBonus(int i) {
		return new WeaponBaseMod() {
			@Override
			public int getEnchantmentLevel() {
				return parent.getEnchantmentLevel() + i;
			}

			@Override
			public String getName() {
				return "+" + i + " " + parent.getName().replace("Masterwork ", "");
			}

			@Override
			public String getUrl() {
				return "https://www.d20pfsrd.com/magic-items/magic-weapons/";
			}

			@Override
			public boolean isMasterWork() {
				return true;
			}
		};
	}

	public static int getEnchantmentCost(int enchantmentLevel) {
		return (int) (Math.pow(enchantmentLevel, 2) * 2000);
	}
}
