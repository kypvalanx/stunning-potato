package org.gary.rules;

import org.gary.behavior.Behavior;
import org.gary.behavior.GroupBehavior;
import org.gary.behavior.KeyedBehavior;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.gary.behavior.DeckList;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.MessageChannel;

public class RulesLookupBehavior extends Behavior{

	private final GroupBehavior behavior;

	public RulesLookupBehavior(){
		behavior = new GroupBehavior();
		File file = new File("resources/org.gary.rules.yaml");
		if(file.isFile()){
			try {
				ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

				TypeFactory typeFactory = mapper.getTypeFactory();

				CollectionType mapType = typeFactory.constructCollectionType(ArrayList.class, KeyedBehavior.class);

				List<KeyedBehavior> behaviors = mapper.readValue(file, mapType);

				behavior.add(behaviors).setDefault(Behavior.getAlphabetizedList(behaviors.stream().map(behavior -> String.join(", ",behavior.getKeys())).collect(Collectors.toList()), "Available Rules:", ""));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run(DeckList<String> message, MessageChannel channel) {
		behavior.run(message, channel);
	}

	@Override
	public String getHelp(DeckList<String> s, String key) {
		return "This is a rule lookup system.  Without a parameter this will list available org.gary.rules, add one of those as a payload to get a lookup link.";
	}
}
