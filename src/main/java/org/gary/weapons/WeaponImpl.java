package org.gary.weapons;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.jsoup.nodes.Element;

public class WeaponImpl implements Weapon{
	private static Pattern costPattern = Pattern.compile("<b>Cost</b> (\\d+) gp");
	private static Pattern weightPattern = Pattern.compile("<b>Weight</b> (\\d+) lb");
	private static Pattern damagePattern = Pattern.compile("<b>Damage</b> ([\\w,()\\s/]+);");
	private static Pattern criticalPattern = Pattern.compile("<b>Critical</b> ([\\w,()/\\s-—\"]+);");
	private static Pattern rangePattern = Pattern.compile("<b>Range</b> (\\d+) ft\\.;");
	private static Pattern typePattern = Pattern.compile("<b>Type</b> ([\\w\\s]+);");
	private static Pattern specialPattern = Pattern.compile("<b>Special</b> ([\\w\\s,]+)");
	private static Pattern categoryPattern = Pattern.compile("<b>Category</b> ([\\w\\s]+);");
	private static Pattern proficiencyPattern = Pattern.compile("<b>Proficiency</b> ([\\w\\s]+)<");
	private static Pattern descriptionPattern = Pattern.compile("<h3 class=\"framing\">Description</h3>(.*)");


	private String name;
	private int baseCost;
	private int weight;
	private String damage;
	private CriticalRange critical;
	private String range;
	private String type;
	private String special;
	private String category;
	private String proficiency;
	private String description;
	private String url;
	private List<String> weaponGroups;
	//private final int baseCost;

	public WeaponImpl(){

	}

	public WeaponImpl(Element content, String url) {
		this.name = content.getElementsByClass("title").get(0).text();
		String text = content.html();
		this.url = url;
		baseCost = generateCost(text);
		weight = generateWeight(text);
		damage = generateDamage(text);
		critical = new CriticalRange(generateCritical(text));
		if(critical.toString() == null){
			System.err.println(text);
		}
		range = generateRange(text);
		type = generateType(text);
		special = generateSpecial(text);
		category = generateCategory(text);
		proficiency = generateProficiency(text);
		description = generateDescription(text);
		weaponGroups = content.getElementsByTag("a").stream()
				.filter((Predicate<Element>) input -> "FighterWeapons.aspx".equals(input.attr("href")))
				.map(Element::text)
				.collect(Collectors.toList());

	}

	private int generateCost(String text) {
		Matcher m = costPattern.matcher(text);

		if(m.find()) {
			return Integer.parseInt(m.group(1));
		}
		return 0;
	}
	private int generateWeight(String text) {
		Matcher m = weightPattern.matcher(text);

		if(m.find()) {
			return Integer.parseInt(m.group(1));
		}
		return 0;
	}
	private String generateDamage(String text) {
		Matcher m = damagePattern.matcher(text);

		if(m.find()) {
			return m.group(1);
		}
		return null;
	}
	private String generateCritical(String text) {
		Matcher m = criticalPattern.matcher(text);

		if(m.find()) {
			return m.group(1);
		}
		return null;
	}
	private String generateRange(String text) {
		Matcher m = rangePattern.matcher(text);

		if(m.find()) {
			return m.group(1);
		}
		return null;
	}
	private String generateType(String text) {
		Matcher m = typePattern.matcher(text);

		if(m.find()) {
			return m.group(1);
		}
		return null;
	}
	private String generateSpecial(String text) {
		Matcher m = specialPattern.matcher(text);

		if(m.find()) {
			return m.group(1);
		}
		return null;
	}
	private String generateCategory(String text) {
		Matcher m = categoryPattern.matcher(text);

		if(m.find()) {
			return m.group(1);
		}
		return null;
	}
	private String generateProficiency(String text) {
		Matcher m = proficiencyPattern.matcher(text);

		if(m.find()) {
			return m.group(1);
		}
		return null;
	}
	private String generateDescription(String text) {
		Matcher m = descriptionPattern.matcher(text);

		if(m.find()) {
			String group = m.group(1);
			if(group.length() > 2000){
				return url;
			}
			return group;
		}
		return null;
	}

	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("name", name)
				.add("baseCost", baseCost)
				.add("weight", weight)
				.add("damage", damage)
				.add("critical", critical)
				.add("range", range)
				.add("type", type)
				.add("special", special)
				.add("category", category)
				.add("proficiency", proficiency)
				.add("description", description)
				.add("url", url)
				.toString();
	}


	@JsonIgnore
	public boolean isMasterWork() {
		return false;
	}

	@JsonIgnore
	public int getEnchantmentLevel() {
		return 0;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public String getProficiency() {
		return proficiency;
	}

	public List<String> getWeaponGroups() {
		return weaponGroups;
	}

	public String getDescription() {
		return description;
	}

	public String getSpecial() {
		return special;
	}

	public String getType() {
		return type;
	}

	public String getRange() {
		return range;
	}

	public CriticalRange getCritical() {
		return critical;
	}

	public String getDamage() {
		return damage;
	}

	public int getWeight() {
		return weight;
	}



	public int getBaseCost() {
		return baseCost;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBaseCost(int baseCost) {
		this.baseCost = baseCost;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void setDamage(String damage) {
		this.damage = damage;
	}

	public void setCritical(CriticalRange critical) {
		this.critical = critical;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setProficiency(String proficiency) {
		this.proficiency = proficiency;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setWeaponGroups(List<String> weaponGroups) {
		this.weaponGroups = weaponGroups;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WeaponImpl weapon = (WeaponImpl) o;
		return baseCost == weapon.baseCost &&
				weight == weapon.weight &&
				Objects.equals(name, weapon.name) &&
				Objects.equals(damage, weapon.damage) &&
				Objects.equals(critical, weapon.critical) &&
				Objects.equals(range, weapon.range) &&
				Objects.equals(type, weapon.type) &&
				Objects.equals(special, weapon.special) &&
				Objects.equals(category, weapon.category) &&
				Objects.equals(proficiency, weapon.proficiency) &&
				Objects.equals(description, weapon.description) &&
				Objects.equals(url, weapon.url) &&
				Objects.equals(weaponGroups, weapon.weaponGroups);
	}

	public int hashCode() {
		return Objects.hash(name, baseCost, weight, damage, critical, range, type, special, category, proficiency, description, url, weaponGroups);
	}
}
