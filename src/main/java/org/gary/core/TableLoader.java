package org.gary.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.gary.table.Table;
import org.gary.table.Table.Builder;
import org.gary.table.RollTableDelegate;

public class TableLoader {
    private Table table;

    public TableLoader(File file) {
        if (!file.getName().startsWith("table_")) {
            return;
        }

        try {
            Builder builder = Table.builder(file.getName().substring(6, file.getName().length() - 4));
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

        new RollTableDelegate().addRollTable(table);
        return "";
    }


}