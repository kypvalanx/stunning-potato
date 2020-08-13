package org.gary.table;

import java.io.File;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TableTest {

    @Test
    public void invalidFileName(){
        File file = mock(File.class);
        when(file.getName()).thenReturn("bad_darthjarjar.csv");

        assertThatThrownBy(() -> {Table.builder(file.getName(), file.getName().substring(6, file.getName().length() - 4));}).isInstanceOf(IllegalStateException.class).hasMessage("tables can only be created by csv files starting with 'table_'");
    }

    @Test
    public void oneRangeRow(){
        File file = mock(File.class);
        when(file.getName()).thenReturn("table_darthjarjar.csv");

        Table.Builder builder = Table.builder(file.getName(), file.getName().substring(6, file.getName().length() - 4));
        builder.addRange("1", "this is a thing");

        Table table = builder.build();

        String s = table.get(1);

        assertThat(s).isEqualTo("this is a thing");
        assertThat(table.getRange()).isEqualTo(1);
    }

    @Test
    public void multiRangeRow(){
        File file = mock(File.class);
        when(file.getName()).thenReturn("table_darthjarjar.csv");

        Table.Builder builder = Table.builder(file.getName(), file.getName().substring(6, file.getName().length() - 4));
        builder.addRange("1-3", "this is a thing");

        Table table = builder.build();

        String s = table.get(1);

        assertThat(s).isEqualTo("this is a thing");
        assertThat(table.getRange()).isEqualTo(3);
    }

    @Test
    public void tableName(){
        File file = mock(File.class);
        when(file.getName()).thenReturn("table_darthjarjar.csv");

        Table.Builder builder = Table.builder(file.getName(), file.getName().substring(6, file.getName().length() - 4));

        Table table = builder.build();

        String s = table.getName();

        assertThat(s).isEqualTo("darthjarjar");
    }
}
