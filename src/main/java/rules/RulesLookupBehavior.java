package rules;

import behavior.Behavior;
import behavior.BehaviorHelper;
import behavior.GroupBehavior;
import behavior.KeyedBehavior;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import core.DeckList;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RulesLookupBehavior extends Behavior{

	private final GroupBehavior behavior;

	public RulesLookupBehavior(){
		behavior = new GroupBehavior();
		File file = new File("resources/rules.yaml");
		if(file.isFile()){
			try {
				ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

				TypeFactory typeFactory = mapper.getTypeFactory();

				CollectionType mapType = typeFactory.constructCollectionType(ArrayList.class, KeyedBehavior.class);

				List<KeyedBehavior> behaviors = mapper.readValue(file, mapType);

				behavior.add(behaviors).setDefault(BehaviorHelper.getAlphabetizedList(behaviors, "Available Rules:"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run(MessageReceivedEvent event, DeckList<String> message) {
		behavior.run(event,message);
	}
}
