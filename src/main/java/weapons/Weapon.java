package weapons;


import behavior.Behavior;
import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import core.DeckList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jsoup.nodes.Element;

public class Weapon {
	private static Pattern costPattern = Pattern.compile("<b>Cost</b> (\\d+) gp");
	private static Pattern weightPattern = Pattern.compile("<b>Weight</b> (\\d+) lb");
	private static Pattern damagePattern = Pattern.compile("<b>Damage</b> ([\\w,()\\s]+);");
	private static Pattern criticalPattern = Pattern.compile("<b>Critical</b> ([\\w,()/\\s]+);");
	private static Pattern rangePattern = Pattern.compile("<b>Range</b> (\\d+) ft\\.;");
	private static Pattern typePattern = Pattern.compile("<b>Type</b> ([\\w\\s]+);");
	private static Pattern specialPattern = Pattern.compile("<b>Special</b> ([\\w\\s,]+)");
	private static Pattern categoryPattern = Pattern.compile("<b>Category</b> ([\\w\\s]+);");
	private static Pattern proficiencyPattern = Pattern.compile("<b>Proficiency</b> ([\\w\\s]+)<");
	private static Pattern descriptionPattern = Pattern.compile("<h3 class=\"framing\">Description</h3>([\\w()'’;.,\\s-]+)\\s*");
	private final String title;
	private final int cost;
	private final int weight;
	private final String damage;
	private final String critical;
	private final String range;
	private final String type;
	private final String special;
	private final String category;
	private final String proficiency;
	private final String description;
	private final String url;
	private List<String> weaponGroups;
	//private final int cost;

	public Weapon(Element content, String url) {
		this.title = content.getElementsByClass("title").get(0).text();
		String text = content.html();
		this.url = url;
		cost = getCost(text);
		weight = getWeight(text);
		damage = getDamage(text);
		critical = getCritical(text);
		range = getRange(text);
		type = getType(text);
		special = getSpecial(text);
		category = getCategory(text);
		proficiency = getProficiency(text);
		description = getDescription(text);
		weaponGroups = content.getElementsByTag("a").stream()
				.filter((Predicate<Element>) input -> "FighterWeapons.aspx".equals(input.attr("href")))
				.map(Element::text)
				.collect(Collectors.toList());

	}

	private int getCost(String text) {
		Matcher m = costPattern.matcher(text);

		if(m.find()) {
			return Integer.parseInt(m.group(1));
		}
		return 0;
	}
	private int getWeight(String text) {
		Matcher m = weightPattern.matcher(text);

		if(m.find()) {
			return Integer.parseInt(m.group(1));
		}
		return 0;
	}
	private String getDamage(String text) {
		Matcher m = damagePattern.matcher(text);

		if(m.find()) {
			return m.group(1);
		}
		return null;
	}
	private String getCritical(String text) {
		Matcher m = criticalPattern.matcher(text);

		if(m.find()) {
			return m.group(1);
		}
		return null;
	}
	private String getRange(String text) {
		Matcher m = rangePattern.matcher(text);

		if(m.find()) {
			return m.group(1);
		}
		return null;
	}
	private String getType(String text) {
		Matcher m = typePattern.matcher(text);

		if(m.find()) {
			return m.group(1);
		}
		return null;
	}
	private String getSpecial(String text) {
		Matcher m = specialPattern.matcher(text);

		if(m.find()) {
			return m.group(1);
		}
		return null;
	}
	private String getCategory(String text) {
		Matcher m = categoryPattern.matcher(text);

		if(m.find()) {
			return m.group(1);
		}
		return null;
	}
	private String getProficiency(String text) {
		Matcher m = proficiencyPattern.matcher(text);

		if(m.find()) {
			return m.group(1);
		}
		return null;
	}
	private String getDescription(String text) {
		Matcher m = descriptionPattern.matcher(text);

		if(m.find()) {
			return m.group(1);
		}
		return null;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("title", title)
				.add("cost", cost)
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

	public Behavior getWeaponStatBlockBehavior(){
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				String statBlock =
						title + "\n" +
						"Cost: " + cost + " gp Weight: " + weight + " lbs\n" +
						"Damage: " + damage + " Critical: " + critical + " Range: " + range + " Type: " + type + " Special: " + special + "\n" +
						"Category: " + category + " Proficiency: " + proficiency + "\n" +
						"Weapon Groups: " + String.join(", ", weaponGroups) + "\nDescription:\n"
								+ description;

				event.getChannel().sendMessage(statBlock).queue();
			}
		};
	}

	public String getName() {
		return title;
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
}
