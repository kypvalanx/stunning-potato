package org.gary.table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Table {
    private int range;
    private String name;
    private Map<Integer, String> table;

    public Table(String name, Map<Integer, String> table) {
        this.name = name;
        this.table = table;
        range = table.keySet().stream().mapToInt(i -> i).max().orElseThrow();
    }

    public static Builder builder(String name){
        return new Builder(name);
    }


    public String getName() {
        return name;
    }


    public String get(int i) {
        return table.get(i);
    }

    @JsonIgnore
    public int getRange() {
        return range;
    }

    public String roll() {
        return table.get(new Random().nextInt(getRange())+1);
    }

    public Map<Integer, String> getRows() {
        return table;
    }


    public static class Builder{

        private final String name;
        private Map<Integer, String> tableRows = new HashMap<>();

        public Builder(String filename) {
            this.name = filename;
        }

        public void addRange(String range, String result) {
            String[] tok = range.split("-");

            if(tok.length == 1){
                int key = Integer.parseInt(tok[0]);
                tableRows.put(key, result);
            } else if(tok.length > 1){
                int a = Integer.parseInt(tok[0]);
                int b = Integer.parseInt(tok[1]);
                int min = Math.min(a,b);
                int max = Math.max(a,b);

                for(int i = min; i <= max; i++){
                    tableRows.put(i, result);
                }
            } else {
                //fuck
            }
        }

        public Table build() {
            return new Table(name, tableRows);
        }
    }
}
