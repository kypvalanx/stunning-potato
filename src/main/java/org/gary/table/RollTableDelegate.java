package org.gary.table;

import org.gary.behavior.Behavior;
import org.gary.behavior.ChannelHelper;
import org.gary.behavior.DeckList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.dv8tion.jda.api.entities.MessageChannel;

public class RollTableDelegate {
    public static final String DATABASE_LOCATION = "jdbc:h2:./resources/db/roll_tables";

    public RollTableDelegate(){
        try {
            Class.forName("org.h2.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        createTableIfNonExistent();
    }

    private void createTableIfNonExistent() {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * " +
                    "FROM INFORMATION_SCHEMA.TABLES " +
                    "WHERE TABLE_SCHEMA = 'PUBLIC' " +
                    "AND  TABLE_NAME = 'ROLL_TABLES'");
            resultSet.last();
            if (resultSet.getRow() == 0) {
                statement.execute("CREATE TABLE ROLL_TABLES (name varchar(100), key varchar(50), payload varchar, PRIMARY KEY (name))");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_LOCATION);
    }

    public Optional<Table> getRollTable(String name){
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM ROLL_TABLES WHERE name = (?)");

            statement.setString(1, name);

            ResultSet resultSet = statement.executeQuery();

            Table.Builder builder = Table.builder(name);

            while(resultSet.next()){
                builder.addRange(resultSet.getString(2), resultSet.getString(3));
            }

            return Optional.of(builder.build());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void addRollTable(Table table){
        String name = table.getName();
        Map<Integer, String> rows = table.getRows();

        try(Connection connection = getConnection()){
            String insertSQL = "INSERT INTO ROLL_TABLES(name, key, payload) VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(insertSQL);
            statement.setString(1, name);
            for(Map.Entry<Integer, String> entry : rows.entrySet()) {
                statement.setString(2, entry.getKey()+"");
                statement.setString(3, entry.getValue());

                statement.execute();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteRollTable(String name){
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM ROLL_TABLES WHERE name = (?)");

            statement.setString(1, name);

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
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
        Optional<Table> rollTable = new RollTableDelegate().getRollTable(String.join(" ", join));

        rollTable.ifPresent(table -> ChannelHelper.sendLongMessage("\n", table.roll(), channel));
    }

    private static void listTables(MessageChannel channel) {
        ChannelHelper.sendLongMessage("\n", String.join("\n", new RollTableDelegate().getRollTables()), channel);
    }

    private Set<String> getRollTables() {
        Set<String> rollTables = new HashSet<>();
        try (Connection connection = getConnection()) {
        PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT name FROM ROLL_TABLES");

        ResultSet resultSet = statement.executeQuery();


        while(resultSet.next()){
            rollTables.add(resultSet.getString(1));
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

        return rollTables;
    }

}

