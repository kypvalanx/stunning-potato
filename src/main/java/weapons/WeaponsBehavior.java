package weapons;

import behavior.Behavior;
import behavior.BehaviorHelper;
import behavior.ChannelHelper;
import behavior.GroupBehavior;
import core.DeckList;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.SetValuedMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WeaponsBehavior extends Behavior {
	private final GroupBehavior
			groupBehavior = new GroupBehavior();
	private final SetValuedMap<String, String> searchIndex = MultiMapUtils.newSetValuedHashMap();
	private final SetValuedMap<String, String> groups = MultiMapUtils.newSetValuedHashMap();
	private final SetValuedMap<String, String> category = MultiMapUtils.newSetValuedHashMap();
	private final SetValuedMap<String, String> proficiency = MultiMapUtils.newSetValuedHashMap();

	public WeaponsBehavior() {
		getWeapons("https://www.aonprd.com/EquipmentWeapons.aspx?Proficiency=Simple");
		getWeapons("https://www.aonprd.com/EquipmentWeapons.aspx?Proficiency=Martial");
		getWeapons("https://www.aonprd.com/EquipmentWeapons.aspx?Proficiency=Exotic");
		getWeapons("https://www.aonprd.com/EquipmentWeapons.aspx?Proficiency=Ammo");
		getWeapons("https://www.aonprd.com/EquipmentWeapons.aspx?Proficiency=Firearm");
		getWeapons("https://www.aonprd.com/EquipmentWeapons.aspx?Proficiency=Mod");
		getWeapons("https://www.aonprd.com/EquipmentWeapons.aspx?Proficiency=Siege");

		groupBehavior
				.add("list", BehaviorHelper.getAlphabetizedList(searchIndex.values().stream().distinct().collect(Collectors.toList()), "Available Weapons"))
				.add(new String[]{"search", "s"}, getSearchBehavior(searchIndex))
				.add(new String[]{"category", "c"}, getSearchBehavior(category))
				.add(new String[]{"proficiency", "p"}, getSearchBehavior(proficiency))
				.add(new String[]{"group", "g"}, getSearchBehavior(groups));


	}

	private void getWeapons(String root) {
		Document doc = null;
		try {
			doc = Jsoup.connect(root).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Element body = doc.body();

		Elements anchors = body.getElementsByTag("a");


		anchors.stream()
				.filter(element -> element.attr("href").startsWith("EquipmentWeaponsDisplay.aspx?ItemName="))
				.forEach(element -> {
					String descriptionURL = element.attr("href");
					try {
						Document description = Jsoup.connect("https://www.aonprd.com/" + descriptionURL).get();
						Element content = description.body().getElementById("ctl00_MainContent_DataListTypes_ctl00_LabelName");

						Weapon weapon = new Weapon(content, "https://www.aonprd.com/" + descriptionURL);

						groupBehavior.add(weapon.getName(), weapon.getWeaponStatBlockBehavior());

						for (String subKey : weapon.getName().split(" ")) {
							searchIndex.put(subKey.toLowerCase(), weapon.getName());
						}
						if (weapon.getCategory() != null) {
							category.put(weapon.getCategory().toLowerCase(), weapon.getName());
						}
						proficiency.put(weapon.getProficiency().toLowerCase(), weapon.getName());
						for (String group : weapon.getWeaponGroups()) {
							groups.put(group.toLowerCase(), weapon.getName());
						}
						//return weapon;
					} catch (IOException e) {
						e.printStackTrace();
					}
					//return null;
				});
	}

	private Behavior getSearchBehavior(final SetValuedMap<String, String> searchIndex) {
		return new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				if (message.canDraw()) {
					String searchTerm = String.join(" ", message.getDeck());

					String results = searchIndex.entries().stream()
							.filter(entry -> entry.getKey().contains(searchTerm))
							.map(Map.Entry::getValue).distinct().sorted().collect(Collectors.joining("\n"));
					ChannelHelper.sendLongMessage(event, "\n", results);
				} else {

					ChannelHelper.sendLongMessage(event, "\n", String.join("\n", searchIndex.keySet()));
				}
			}
		};
	}

	@Override
	public void run(MessageReceivedEvent event, DeckList<String> message) {
		groupBehavior.run(event, message);
	}
}