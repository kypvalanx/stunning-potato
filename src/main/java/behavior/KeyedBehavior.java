package behavior;

import java.util.List;

public class KeyedBehavior {
    private List<String> keys;
    private String value;

    public String[] getKeys(){
        return keys.toArray(new String[0]);
    }

    public Behavior getBehavior(){
        return ((event, message) -> {
            event.getChannel().sendMessage(value).queue();
        });
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
