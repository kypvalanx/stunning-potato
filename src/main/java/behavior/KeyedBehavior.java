package behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class KeyedBehavior implements Behavior {
    private List<String> keys;
    private String value;
	private List<String> categories;

	public String[] getKeys(){
        return keys.toArray(new String[0]);
    }


	@Override
	public void run(MessageReceivedEvent event, ArrayList<String> message) {

		event.getChannel().sendMessage(value).queue();
	}

	@Override
	public void getHelp(MessageReceivedEvent event, ArrayList<String> message){

		//NOOP
		//event.getChannel().sendMessage(String.join(", ", keys) + "=>" + value).queue();
	}

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public void setValue(String value) {
        this.value = value;
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		KeyedBehavior that = (KeyedBehavior) o;
		return Objects.equals(keys, that.keys) &&
				Objects.equals(value, that.value) &&
				Objects.equals(categories, that.categories);
	}

	@Override
	public int hashCode() {
		return Objects.hash(keys, value, categories);
	}

	public void addCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<String> getCategories() {
		return categories;
	}
}
