package items;

import behavior.Behavior;
import behavior.ChannelHelper;
import behavior.GroupBehavior;
import behavior.KeyedBehavior;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ItemListener extends ListenerAdapter {

	private final GroupBehavior core;
	private final GroupBehavior categories;
	private List<String> rulesList;
	private LinkedListMultimap<String, KeyedBehavior> categoryMap;

	public ItemListener(){
		Behavior items = getSurvivalItemBehavior();
		Behavior categoryBehavior = getCategoryBehavior();

		core =  new GroupBehavior().add(new String[]{"item", "!i"}, items);
		categories = new GroupBehavior().add(new String[]{"categories", "c"}, categoryBehavior);
	}

	private Behavior getCategoryBehavior() {
		return (event, message) -> {
			if(message.isEmpty()){
				List<String> categoryTitles = Lists.newArrayList(categoryMap.keys().elementSet());
				event.getChannel().sendMessage("Categories:").queue();
				for(int i=0; i < categoryTitles.size(); i+=100){
					event.getChannel().sendMessage(String.join("\n", categoryTitles.subList(i, Math.min(i + 100, categoryTitles.size())))).queue();
				}
			}else{
				String key = String.join(" ", message);
				List<KeyedBehavior> items = categoryMap.get(key);
				getAlphabatizedList(items, key).run(event, message);
			}
		};
	}

	@NotNull
	private Behavior getSurvivalItemBehavior() {
		GroupBehavior items = new GroupBehavior();

		try {
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

			 categoryMap = LinkedListMultimap.create(behaviors.size());

			behaviors.forEach(keyedBehavior -> {
				for (String key : keyedBehavior.getCategories()) {
					categoryMap.put(key, keyedBehavior);
				}
			});

			items.add(behaviors);
			items.add(getAlphabatizedList(behaviors, "Available Items"), "list");

		} catch (IOException e) {
			e.printStackTrace();
		} return items;

	}

	private List<KeyedBehavior> getKeyedBehaviors(String root) throws IOException {
		Document doc = Jsoup.connect(root).get();

		String[] tokens = root.split("/");
		String category = cleanCategory(tokens[tokens.length-2]);
		Element body = doc.body();

		Elements anchors = body.getElementsByTag("a");

		return anchors.stream()
				.filter(element -> element.attr("href").startsWith("#TOC"))
				.map(element -> {
					KeyedBehavior keyedBahavior = new KeyedBehavior();
					List<Element> tables = element.parents().stream().filter(element1 -> element1.tagName().equals("table")).collect(Collectors.toList());
					String subCategory;
					if(tables.size() == 1){

						Element table = tables.get(0);
						subCategory = table.getElementsByTag("th").get(0).text();
					}else{
						subCategory = "NO CATEGORY";
					}

					keyedBahavior.addCategories(List.of(cleanCategory(subCategory), category));
					String href = element.attr("href");
					String text = href.substring(5).replace("-", " ").toLowerCase();
					keyedBahavior.setKeys(List.of(text));
					keyedBahavior.setValue(root + href);
					return keyedBahavior;
				}).distinct().collect(Collectors.toList());
	}

	private String cleanCategory(String token) {
		token = token.replace("(", "").replace("-", " ").replace(")", "").replace(".", "").toLowerCase();
		//System.out.println(token);
		return token;
	}


	@Override
	public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
		final String contentRaw = event.getMessage().getContentRaw();
		final String key = contentRaw.toLowerCase().trim();

		ArrayList<String> tokens = new ArrayList<>(Arrays.asList(key.split(" ")));
		core.run(event, tokens);
		categories.run(event, tokens);
	}

	@NotNull
	private Behavior getAlphabatizedList(List<KeyedBehavior> behaviors, final String listTitle) {
		return (event, message) -> {

				String rulesList = behaviors.stream()
						.sorted(Comparator.comparing(o -> o.getKeys()[0]))
						.map(keyedBehavior -> String.join(", ", keyedBehavior.getKeys())).collect(Collectors.joining("\n"));

			event.getChannel().sendMessage(listTitle).queue();

			ChannelHelper.sendLongMessage(event, "\n", rulesList);
		};
	}
}
