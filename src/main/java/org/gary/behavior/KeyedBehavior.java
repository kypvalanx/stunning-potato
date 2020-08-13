package org.gary.behavior;

import com.google.common.base.MoreObjects;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import net.dv8tion.jda.api.entities.MessageChannel;

public class KeyedBehavior extends Behavior {
    private Set<String> keys;
    private String value;
	private Set<String> categories;

	public KeyedBehavior(){

	}

	public KeyedBehavior(Set<String> keys, String value, Set<String> categories) {
		this.keys = keys;
		this.value = value;
		this.categories = categories;
	}

	public String[] getKeys(){
        return keys.toArray(new String[0]);
    }


	@Override
	public void run(DeckList<String> message, MessageChannel channel) {

		channel.sendMessage(value).queue();
	}

	@Override
	public List<String> getFormattedHelp(DeckList<String> s, String key) {
		return List.of();
	}

	public void setKeys(Set<String> keys) {
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

	public void addCategories(Set<String> categories) {
		this.categories = categories;
	}

	public Set<String> getCategories() {
		return categories;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("getKeys", keys)
				.add("value", value)
				.add("categories", categories)
				.toString();
	}

	@Override
	public Behavior merge(Behavior that) {
		if(that == null){
			return this;
		}else if(that instanceof KeyedBehavior) {
			this.keys = new HashSet<>(this.keys);
			keys.addAll(((KeyedBehavior) that).keys);
			this.categories = new HashSet<>(this.categories);
			categories.addAll(((KeyedBehavior) that).categories);

			this.value += "\nor " + ((KeyedBehavior) that).value;
		} else if(that instanceof GroupBehavior){
			return that.merge(this);
		}
		return this;
	}
}
