package rules;

import behavior.Behavior;
import behavior.GroupBehavior;
import behavior.KeyedBehavior;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class RulesLookupBehavior {
	private String rulesList;

	public Behavior getRulesBehavior() {
		GroupBehavior groupBehavior = new GroupBehavior();
		File file = new File("resources/rules.yaml");
		if(file.isFile()){
			try {
				ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

				TypeFactory typeFactory = mapper.getTypeFactory();

				CollectionType mapType = typeFactory.constructCollectionType(ArrayList.class, KeyedBehavior.class);

				List<KeyedBehavior> behaviors = mapper.readValue(file, mapType);

				groupBehavior.add(behaviors).setDefault(getAlphabatizedList(behaviors, "Available Rules:"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return groupBehavior;
	}

	@NotNull
	private Behavior getAlphabatizedList(List<KeyedBehavior> behaviors, final String listTitle) {
		return (event, message) -> {

			if (rulesList == null) {
				rulesList = behaviors.stream()
						.sorted(Comparator.comparing(o -> o.getKeys()[0]))
						.map(keyedBehavior -> String.join(", ", keyedBehavior.getKeys()))
						.reduce(listTitle, (s, s2) -> s.concat("\n").concat(s2));
			}
			event.getChannel().sendMessage(rulesList).queue();
		};
	}
}
