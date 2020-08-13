package org.gary.rebellion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class RebellionDelegate {
    private static final String DEFAULT_RESOURCE_FILE = "resources/save/org.gary.rebellion.yaml";
    public static final String DATABASE_LOCATION = "jdbc:h2:./resources/db/org.gary.rebellion";


    public RebellionDelegate() {
        try {
            Class.forName("org.h2.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        createTableIfNonExistent();
    }

    @NotNull
    public static Rebellion getRebellionFromFile() {
        File rebellionFile = new File(DEFAULT_RESOURCE_FILE);
        return getRebellionFromFile(rebellionFile);
    }

    @NotNull
    public static Rebellion getRebellionFromFile(File rebellionFile) {
        if (rebellionFile.canRead()) {

            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

            try {
                return mapper.readValue(rebellionFile, Rebellion.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Rebellion();
    }

    public static void writeOutRebellionToFile(Rebellion rebellion) {
        new File("resources/save/").mkdir();
        File rebellionFile = new File(DEFAULT_RESOURCE_FILE);
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        try {
            if (!rebellionFile.createNewFile()) {
                rebellionFile.delete();
                rebellionFile.createNewFile();
            }
            mapper.writeValue(rebellionFile, rebellion);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Rebellion getCurrentRebellion(String channelId) {
        RebellionDelegate delegate = new RebellionDelegate();
        Optional<Integer> mapping = delegate.getMapping(channelId);

        if(mapping.isPresent()){
            Optional<Rebellion> rebellion = delegate.getRebellion(mapping.get());
            if(rebellion.isPresent()){
                return rebellion.get();
            }
        }

        long id = delegate.storeRebellion(new Rebellion());

        delegate.setMapping(channelId,id);

        return getCurrentRebellion(channelId);
    }

    private void createTableIfNonExistent() {
        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * " +
                    "FROM INFORMATION_SCHEMA.TABLES " +
                    "WHERE TABLE_SCHEMA = 'PUBLIC' " +
                    "AND  TABLE_NAME = 'REBELLION'");
            resultSet.last();
            if (resultSet.getRow() == 0) {
                String sql = "CREATE TABLE REBELLION " +
                        "(id INT NOT NULL AUTO_INCREMENT, name VARCHAR(100), supporters INT(255), treasury INT(50), " +
                        "population INT(255), notoriety INT(50), maxLevel INT(50), members INT(50), " +
                        "dangerRating INT(50), focus VARCHAR(15), demagogueLoyaltyBonus INT(50), " +
                        "partisanSecurityBonus INT(50), recruiterLvlBonus INT(50), sentinelLoyaltyBonus INT(50), " +
                        "sentinelSecurityBonus INT(50), sentinelSecrecyBonus INT(50), spymasterSecrecyBonus INT(50), " +
                        "hasStrategist BOOL, PRIMARY KEY (id))";
                statement.execute(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection connection = getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * " +
                    "FROM INFORMATION_SCHEMA.TABLES " +
                    "WHERE TABLE_SCHEMA = 'PUBLIC' " +
                    "AND  TABLE_NAME = 'REBELLION_MAP'");
            resultSet.last();
            if (resultSet.getRow() == 0) {
                statement.execute("CREATE TABLE REBELLION_MAP (key varchar(100) primary key, rebellion_id int)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_LOCATION);
    }

    public Optional<Rebellion> getRebellion(long id) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM REBELLION WHERE id = (?)");

            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            resultSet.last();

            if (resultSet.getRow() != 0) {

                return Optional.of(new Rebellion(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public long storeRebellion(Rebellion rebellion) {


        try (Connection connection = getConnection()) {
        String sql = "INSERT INTO REBELLION(name, supporters, treasury, population, notoriety, maxLevel, members, " +
                "dangerRating, focus, demagogueLoyaltyBonus, partisanSecurityBonus, recruiterLvlBonus, " +
                "sentinelLoyaltyBonus, sentinelSecurityBonus, sentinelSecrecyBonus, spymasterSecrecyBonus, " +
                "hasStrategist) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        if (rebellion.getId() > -1) {
            sql = "UPDATE REBELLION SET name = ?, supporters = ?, treasury = ?, population = ?, notoriety = ?, maxLevel = ?, members = ?, " +
                    "dangerRating = ?, focus = ?, demagogueLoyaltyBonus = ?, partisanSecurityBonus = ?, recruiterLvlBonus = ?, " +
                    "sentinelLoyaltyBonus = ?, sentinelSecurityBonus = ?, sentinelSecrecyBonus = ?, spymasterSecrecyBonus = ?, " +
                    "hasStrategist = ? WHERE id = " + rebellion.getId();
        }
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            rebellion.prepare(statement);
            if (statement.executeUpdate() == 0) {
                throw new SQLException("failed to store org.gary.rebellion");
            } else {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.last();

                if (generatedKeys.getRow() == 0) {
                    throw new SQLException("no generated Key");
                } else {
                    return generatedKeys.getLong(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void deleteRebellion(int id){
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM REBELLION WHERE id = (?)");

            statement.setLong(1, id);

            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Optional<Integer> getMapping(String key){
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT rebellion_id FROM REBELLION_MAP WHERE key = (?)");

            statement.setString(1, key);

            ResultSet resultSet = statement.executeQuery();
            resultSet.last();

            if (resultSet.getRow() != 0) {

                return Optional.of(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void setMapping(String key, long id){
        deleteMapping(key);
        insertMapping(key, id);
    }

    private void insertMapping(String key, long id) {
        try (Connection connection = getConnection()) {
            String sql = "INSERT INTO REBELLION_MAP(key, rebellion_id) VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, key);
            statement.setLong(2, id);
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteMapping(String key) {
        try (Connection connection = getConnection()) {
            String sql = "DELETE FROM REBELLION_MAP WHERE key = (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, key);
            statement.execute();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Map<String, Long> getMappings() {
        Map<String, Long> mappings = new HashMap<>();
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM REBELLION_MAP");

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                mappings.put(resultSet.getString(1), resultSet.getLong(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mappings;
    }
}
