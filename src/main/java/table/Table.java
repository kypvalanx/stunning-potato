package table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Table {
    private int range;

    public static Builder builder(String filename){
        if(!filename.startsWith("table_")){
            throw new IllegalStateException("tables can only be created by csv files starting with 'table_'");
        }
        return new Builder(filename.substring(6, filename.length() - 4));
    }

    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Map<Integer, String> getTable() {
        return table;
    }

    public void setTable(Map<Integer, String> table) {
        this.table = table;
        range = table.keySet().stream().mapToInt(i -> i).max().orElseThrow();
    }

    private Map<Integer, String> table;

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


    public static class Builder{

        private final String filename;
        private Map<Integer, String> tableRows = new HashMap<>();

        public Builder(String filename) {
            this.filename = filename;
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
            Table table = new Table();
            table.setFilename(filename);
            table.setTable(tableRows);
            return table;
        }
    }
}
