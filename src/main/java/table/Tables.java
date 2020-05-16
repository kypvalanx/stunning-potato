package table;

import behavior.Behavior;
import behavior.ChannelHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import core.DeckList;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.entities.MessageChannel;

public class Tables {
    private static File tableFile = new File("resources/generated/tables.yaml");
    private static Map<String, Table> tables;

    public static void add(Table table) {
        init();
        tables.put(table.getFilename(), table);
        updateFile();
    }

    private static void init() {
        if (tables != null) {
            return;
        }
        tables = new HashMap<>();
        if(tableFile.canRead()){
            if(tableFile.length() == 0){
                return;
            }
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            TypeFactory typeFactory = mapper.getTypeFactory();

            CollectionType mapType = typeFactory.constructCollectionType(ArrayList.class, Table.class);
            try {
                List<Table> tableList =  mapper.readValue(tableFile, mapType);
                tableList.forEach(table -> {
                    tables.put(table.getFilename(), table);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Behavior getBehavior() {
        return new Behavior() {
            @Override
            public void run(DeckList<String> message, MessageChannel channel) {
                if(message.canDraw()){
                    rollTable(channel, String.join(" ", message.getDeck()));
                }else{
                    listTables(channel);
                }
            }
        };
    }

    private static void rollTable(MessageChannel channel, String join) {
        init();
        Table selected = tables.get(String.join(" ", join));

        if(selected!= null){
            ChannelHelper.sendLongMessage("\n",selected.roll(), channel);
        }
    }

    private static void listTables(MessageChannel channel) {
        init();
        ChannelHelper.sendLongMessage("\n", String.join("\n", tables.keySet()), channel);
    }

    private static void updateFile() {
        tableFile.delete();
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        try {
            tableFile.createNewFile();


            mapper.writeValue(tableFile, tables.values());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

