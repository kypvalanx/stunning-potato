package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import table.Table;
import table.Table.Builder;
import table.Tables;

public class TableLoader {
    private Table table;

    public TableLoader(File file) {
        if (!file.getName().startsWith("table_")) {
            return;
        }

        try {
            Builder builder = Table.builder(file.getName());
            BufferedReader reader = new BufferedReader(new FileReader(file));

            reader.lines().forEach(line -> {
                String[] toks = line.split(",");
                String range = toks[0].trim();
                String result = toks[1].trim();
                builder.addRange(range, result);
            });
            table = builder.build();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public String add() {
        if (table == null) {
            return "";
        }

        Tables.add(table);
        return "";
    }


}