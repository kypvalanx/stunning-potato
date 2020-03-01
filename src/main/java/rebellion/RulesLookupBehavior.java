package rebellion;

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
import java.util.Arrays;
import java.util.List;

public class RulesLookupBehavior {
	public Behavior getRulesBehavior() {
		GroupBehavior groupBehavior = new GroupBehavior();
		File file = new File("resources/rules.yaml");
		if(file.isFile()){
			try {
				ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

				TypeFactory typeFactory = mapper.getTypeFactory();

				CollectionType mapType = typeFactory.constructCollectionType(ArrayList.class, KeyedBehavior.class);

				List<KeyedBehavior> behaviors = mapper.readValue(file, mapType);


				groupBehavior.add(behaviors).setDefault((event, message) -> {
					String rules= "";
					for(KeyedBehavior keyedBehavior: behaviors){
						rules = rules.concat(Arrays.toString(keyedBehavior.getKeys()) + "\n");
					}
					event.getChannel().sendMessage("Available Rules:\n" + rules).queue();
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return groupBehavior;
	}
}
