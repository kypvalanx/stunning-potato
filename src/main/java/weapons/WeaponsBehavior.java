package weapons;

import behavior.Behavior;
import behavior.BehaviorHelper;
import behavior.ChannelHelper;
import behavior.GroupBehavior;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.google.common.collect.Lists;
import core.DeckList;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.SetValuedMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import weapons.modifiers.WeaponModifier;
import weapons.modifiers.WeaponModifiers;

public class WeaponsBehavior extends Behavior {
	private final GroupBehavior
			groupBehavior = new GroupBehavior();
	private final Map<String, Weapon> weapons = new HashMap<>();
	private final SetValuedMap<String, String> searchIndex = MultiMapUtils.newSetValuedHashMap();
	private final SetValuedMap<String, String> groups = MultiMapUtils.newSetValuedHashMap();
	private final SetValuedMap<String, String> category = MultiMapUtils.newSetValuedHashMap();
	private final SetValuedMap<String, String> proficiency = MultiMapUtils.newSetValuedHashMap();
	private final WeaponModifiers weaponModifiers = new WeaponModifiers();

	public WeaponsBehavior() {
		File resultFile = new File("resources/weapons.yaml");

		if(resultFile.canRead()){
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

			TypeFactory typeFactory = mapper.getTypeFactory();

			CollectionType mapType = typeFactory.constructCollectionType(ArrayList.class, WeaponImpl.class);

			try {
				List<WeaponImpl> weapons = mapper.readValue(resultFile, mapType);
				weapons.forEach(this::addWeapon);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
		{
			System.out.println("You have no weapon data loaded, this will take a moment...");
			getWeapons("https://www.aonprd.com/EquipmentWeapons.aspx?Proficiency=Simple");
			getWeapons("https://www.aonprd.com/EquipmentWeapons.aspx?Proficiency=Martial");
			getWeapons("https://www.aonprd.com/EquipmentWeapons.aspx?Proficiency=Exotic");
			getWeapons("https://www.aonprd.com/EquipmentWeapons.aspx?Proficiency=Ammo");
			getWeapons("https://www.aonprd.com/EquipmentWeapons.aspx?Proficiency=Firearm");
			getWeapons("https://www.aonprd.com/EquipmentWeapons.aspx?Proficiency=Mod");
			getWeapons("https://www.aonprd.com/EquipmentWeapons.aspx?Proficiency=Siege");

			ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
			try {
				resultFile.createNewFile();
				mapper.writeValue(resultFile, weapons.values());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		groupBehavior
				.add("list", BehaviorHelper.getAlphabetizedList(searchIndex.values().stream().distinct().collect(Collectors.toList()), "Available Weapons"))
				.add(new String[]{"search", "s"}, getSearchBehavior(searchIndex))
				.add(new String[]{"category", "c"}, getSearchBehavior(category))
				.add(new String[]{"proficiency", "p"}, getSearchBehavior(proficiency))
				.add(new String[]{"group", "g"}, getSearchBehavior(groups))
		.add(new String[]{"mods"},  BehaviorHelper.getAlphabetizedList(weaponModifiers.getModifiers().keySet(), "Weapon Modifications"));


	}

	private void getWeapons(String root) {
		Document doc = null;
		try {
			doc = Jsoup.connect(root).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Elements anchors = doc.body().getElementsByTag("a");


		anchors.stream()
				.filter(element -> element.attr("href").startsWith("EquipmentWeaponsDisplay.aspx?ItemName="))
				.forEach(element -> {
					String descriptionURL = element.attr("href");
					try {
						Document description = Jsoup.connect("https://www.aonprd.com/" + descriptionURL).get();
						Element content = description.body().getElementById("ctl00_MainContent_DataListTypes_ctl00_LabelName");

						Weapon weapon = new WeaponImpl(content, "https://www.aonprd.com/" + descriptionURL);

						addWeapon(weapon);
						//return weapon;
					} catch (IOException e) {
						e.printStackTrace();
					}
					//return null;
				});


	}

	private void addWeapon(Weapon weapon) {
		weapons.put(weapon.getName().toLowerCase(), weapon);
		//groupBehavior.add(weapon.getName().toLowerCase(), weapon.getWeaponStatBlockBehavior());

		for (String subKey : weapon.getName().toLowerCase().split(" ")) {
			searchIndex.put(subKey, weapon.getName());
		}
		if (weapon.getCategory() != null) {
			category.put(weapon.getCategory().toLowerCase(), weapon.getName());
		}
		proficiency.put(weapon.getProficiency().toLowerCase(), weapon.getName());
		for (String group : weapon.getWeaponGroups()) {
			groups.put(group.toLowerCase(), weapon.getName());
		}
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

		List<String> payload = Lists.newArrayList(message.getDeck());

		String combined = String.join(" ", payload);

		Map<String, WeaponModifier> allModifiers = weaponModifiers.getModifiers();

		for(String key : allModifiers.keySet()){
			if(key.contains(" ")){
				String underscored = key.replace(" ", "_");
				combined = combined.replace(key, underscored);
			}
		}

		payload = Lists.newArrayList(combined.split(" "));

		List<WeaponModifier> applicableModifiers = new ArrayList<>();

		List<String> weaponOnly = payload.stream().map(new Function<String, String>() {
			@Override
			public String apply(String s) {
				if (s.contains("_")){
					s = s.replace("_", " ");
				}

				WeaponModifier mod = allModifiers.get(s);

				if(mod == null){
					return s;
				}
				applicableModifiers.add(mod);
				return null;
			}
		}).filter(Objects::nonNull).collect(Collectors.toList());


		Weapon weapon = weapons.get(String.join(" ", weaponOnly).toLowerCase());

		if(weapon != null){
		    for(WeaponModifier mod : applicableModifiers){
		    	weapon = mod.add(weapon);
		    }

			weapon.getWeaponStatBlockBehavior().run(event, message);
			return;
		}
		else{
			WeaponModifier modifier = weaponModifiers.getModifiers().get(String.join(" ", message.getDeck()));

			if(modifier != null) {
				event.getChannel().sendMessage(modifier.getUrl()).queue();
			}
		}

		groupBehavior.run(event, message);
	}

}
