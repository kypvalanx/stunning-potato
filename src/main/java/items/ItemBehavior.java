package items;

import behavior.Behavior;
import behavior.BehaviorHelper;
import behavior.GroupBehavior;
import behavior.KeyedBehavior;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import core.DeckList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ItemBehavior extends Behavior{

	private final GroupBehavior groupBehavior;

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

		behaviors.forEach(new Consumer<KeyedBehavior>() {
			@Override
			public void accept(KeyedBehavior keyedBehavior) {
				for (String key : keyedBehavior.getCategories()) {
					categoryMap.put(key, keyedBehavior);
				}
			}
		});

		Behavior categoryBehavior = new Behavior() {
			@Override
			public void run(MessageReceivedEvent event, DeckList<String> message) {
				if (message.isEmpty()) {
					List<String> categoryTitles = Lists.newArrayList(categoryMap.keys().elementSet());
					event.getChannel().sendMessage("Categories:").queue();
					for (int i = 0; i < categoryTitles.size(); i += 100) {
						event.getChannel().sendMessage(String.join("\n", categoryTitles.subList(i, Math.min(i + 100, categoryTitles.size())))).queue();
					}
				} else {
					String key = String.join(" ", message);
					List<KeyedBehavior> items2 = categoryMap.get(key);
					BehaviorHelper.getAlphabetizedList(items2, key).run(event, message);
				}
			}
		};

		this.groupBehavior = new GroupBehavior()
				.add(behaviors)
				.add(BehaviorHelper.getAlphabetizedList(behaviors, "Available Items"), "list")
				.add(new String[]{"categories", "c"}, categoryBehavior);
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
								return element1.tagName().equals("table");
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
	public void run(MessageReceivedEvent event, DeckList<String> message) {
		groupBehavior.run(event, message);
	}
}
