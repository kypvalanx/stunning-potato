package org.gary.items;

import org.gary.behavior.Behavior;
import org.gary.behavior.ChannelHelper;
import org.gary.behavior.GroupBehavior;
import org.gary.behavior.KeyedBehavior;
import org.gary.behavior.NachoHelpBehavior;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import org.gary.behavior.DeckList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.SetValuedMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ItemBehavior extends Behavior{

	private final GroupBehavior groupBehavior;

	private final SetValuedMap<String, String> searchIndex = MultiMapUtils.newSetValuedHashMap();

	public ItemBehavior(){

		List<KeyedBehavior> behaviors = new ArrayList<>(getKeyedBehaviors("https://www.d20pfsrd.com/equipment/goods-and-services/hunting-camping-survival-gear/"));
		behaviors.addAll(getKeyedBehaviors("https://www.d20pfsrd.com/equipment/goods-and-services/herbs-oils-other-substances/"));
		behaviors.addAll(getKeyedBehaviors("https://www.d20pfsrd.com/equipment/goods-and-services/animals-animal-gear"));
		behaviors.addAll(getKeyedBehaviors("https://www.d20pfsrd.com/equipment/goods-and-services/books-paper-writing-supplies"));
		behaviors.addAll(getKeyedBehaviors("https://www.d20pfsrd.com/equipment/goods-and-services/containers-bags-boxes-more"));
		behaviors.addAll(getKeyedBehaviors("https://www.d20pfsrd.com/equipment/goods-and-services/furniture-trade-goods-vehicles"));
		behaviors.addAll(getKeyedBehaviors("https://www.d20pfsrd.com/equipment/goods-and-services/hirelings-servants-services"));
		behaviors.addAll(getKeyedBehaviors("https://www.d20pfsrd.com/equipment/goods-and-services/tools-kits"));
		behaviors.addAll(getKeyedBehaviors("https://www.d20pfsrd.com/equipment/goods-and-services/toys-games-puzzles"));
		behaviors.addAll(getKeyedBehaviors("https://www.d20pfsrd.com/equipment/goods-and-services/technological-gear"));

		LinkedListMultimap<String, KeyedBehavior> categoryMap = LinkedListMultimap.create(behaviors.size());

		behaviors.forEach(keyedBehavior -> {
			for (String key : keyedBehavior.getCategories()) {
				categoryMap.put(key, keyedBehavior);
			}
		});

		behaviors.forEach(keyedBehavior -> {
			for(String key : keyedBehavior.getKeys()){
				for(String subKey: key.split(" ")){
					searchIndex.put(subKey, key);
				}
			}
		});

		Behavior categoryBehavior = new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				if (!message.canDraw()) {
					List<String> categoryTitles = Lists.newArrayList(categoryMap.keys().elementSet());
					channel.sendMessage("Categories:").queue();
					for (int i = 0; i < categoryTitles.size(); i += 100) {
						channel.sendMessage(String.join("\n", categoryTitles.subList(i, Math.min(i + 100, categoryTitles.size())))).queue();
					}
				} else {
					String key = String.join(" ", message.getDeck());
					List<KeyedBehavior> items2 = categoryMap.get(key);
					Behavior.getAlphabetizedList(items2.stream().map(behavior -> String.join(", ",behavior.getKeys())).collect(Collectors.toList()), key, "").run(message, channel);
				}
			}

			@Override
			public String getHelp(DeckList<String> s, String key) {
				return "Lists and looks up org.gary.items by category.";
			}
		};

		this.groupBehavior = new GroupBehavior()
				.add("list" , Behavior.getAlphabetizedList(behaviors.stream().map(behavior -> String.join(", ",behavior.getKeys())).collect(Collectors.toList()), "Available Items", "Lists available org.gary.items."))
				.add(new String[]{"categories", "c"}, categoryBehavior)
				.add("search", getSearchBehavior())
				.add(behaviors);
	}

	private Behavior getSearchBehavior() {
		return new Behavior() {
			@Override
			public void run(DeckList<String> message, MessageChannel channel) {
				if(message.canDraw()){
					String searchTerm = String.join(" ", message.getDeck());

					String results = searchIndex.entries().stream()
							.filter(entry -> entry.getKey().contains(searchTerm))
							.map(Map.Entry::getValue).distinct().sorted().collect(Collectors.joining("\n"));

					ChannelHelper.sendLongMessage("\n",results, channel);
				}
			}

			@Override
			public String getHelp(DeckList<String> s, String key) {
				return "Search for org.gary.items that contain your search term.";
			}
		};
	}

	private static List<KeyedBehavior> getKeyedBehaviors(String root) {
		Document doc = null;
		try {
			doc = Jsoup.connect(root).get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] tokens = root.split("/");
		String category = cleanCategory(tokens[tokens.length - 2]);
		Element body = doc.body();

		Elements anchors = body.getElementsByTag("a");

		return anchors.stream()
				.filter(new Predicate<Element>() {
					@Override
					public boolean test(Element element) {
						return element.attr("href").startsWith("#TOC");
					}
				})
				.map(new Function<Element, KeyedBehavior>() {
					@Override
					public KeyedBehavior apply(Element element) {
						List<Element> tables = element.parents().stream().filter(new Predicate<Element>() {
							@Override
							public boolean test(Element element1) {
								return element1.tagName().equals("org/gary/table");
							}
						}).collect(Collectors.toList());
						Set<String> categories = Set.of(category);
						if (tables.size() == 1) {

							Element table = tables.get(0);
							String subCategory = table.getElementsByTag("th").get(0).text();
							categories = Set.of(cleanCategory(subCategory), category);
						}
						String href = element.attr("href");
						String text = href.substring(5).replace("-", " ").toLowerCase();
						return new KeyedBehavior(Set.of(text), root + href, categories);
					}
				}).distinct().collect(Collectors.toList());
	}

	private static String cleanCategory(String token) {
		token = token.replace("(", "").replace("-", " ").replace(")", "").replace(".", "").replace(",", "").replace("/", " ").toLowerCase();
		return token;
	}


	@Override
	public void run(DeckList<String> message, MessageChannel channel) {
		groupBehavior.run(message, channel);
	}

	@Override
	public List<String> getFormattedHelp(DeckList<String> s, String key) {
		ArrayList<String> strings = Lists.newArrayList(groupBehavior.getFormattedHelp(s, key));
		strings.add(NachoHelpBehavior.formatHelp(key, "A lookup for org.gary.items"));
		return strings;
	}
}
