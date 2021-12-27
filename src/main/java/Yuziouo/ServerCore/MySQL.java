package Yuziouo.ServerCore;

import Yuziouo.ServerCore.AbilitySystem.Ability;
import Yuziouo.ServerCore.BodyStr.Strength;
import Yuziouo.ServerCore.GradeSystem.Grade;

import java.sql.*;
import java.util.UUID;

public class MySQL {
    private Connection connection;
    String host = ServerCore.getPlugin().getConfig().getString("host"), password = ServerCore.getPlugin().getConfig().getString("password"), database = ServerCore.getPlugin().getConfig().getString("database"), user = ServerCore.getPlugin().getConfig().getString("user");
    int port = ServerCore.getPlugin().getConfig().getInt("port");

    public boolean isConnection() {
        return (connection != null);
    }

    public void connect() throws ClassNotFoundException, SQLException {
        if (!isConnection()) {
            Class.forName("com.mysql.jdbc.Driver");
            if (password.equals("null")) password = "";
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&amp;characterEncoding=UTF-8", user, password);
        }
    }

    public void disconnect() {
        if (isConnection()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public void createTable() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS gradet (id integer AUTO_INCREMENT PRIMARY KEY, uuid text, grade integer, exp integer)");
            statement.execute("CREATE TABLE IF NOT EXISTS abilityt (id integer AUTO_INCREMENT PRIMARY KEY, uuid text, point integer, str integer, def integer, high integer, speed integer, mine integer, health integer)");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createAccount(UUID uuid) {
        try {
            ResultSet result = connection.createStatement().executeQuery("SELECT id FROM gradet WHERE uuid = '" + uuid + "'");
            if (!result.next()) {
                PreparedStatement stm = connection.prepareStatement("INSERT INTO gradet(uuid,grade,exp) VALUES (?,?,?)");
                stm.setString(1, uuid.toString());
                stm.setInt(2, 0);
                stm.setInt(3, 0);
                stm.executeUpdate();
                stm = connection.prepareStatement("INSERT INTO abilityt(uuid,point,str,def,high,speed,mine,health) VALUES (?,?,?,?,?,?,?,?)");
                stm.setString(1, uuid.toString());
                for (int i = 2; i < 9; i++)
                    stm.setInt(i, 0);
                stm.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadPlayer(UUID uuid, Grade grade, Ability ability, Strength strength) {
        ServerCore.getPlugin().getGradeHashMap().put(uuid.toString(), grade);
        ServerCore.getPlugin().getAbilityHashMap().put(uuid.toString(), ability);
        ServerCore.getPlugin().getStrengthHashMap().put(uuid.toString(),strength);
        try {
            ResultSet result = connection.createStatement().executeQuery("SELECT grade,exp FROM gradet WHERE uuid = '" + uuid.toString() + "'");
            if (result.next()) {
                grade.setGrade(result.getInt("grade"));
                grade.setExp(result.getInt("exp"));
            }
            result = connection.createStatement().executeQuery("SELECT point,str,def,high,speed,mine,health FROM abilityt WHERE uuid = '" + uuid.toString() + "'");
            if (result.next()) {
                ability.setStr(result.getInt("str"));
                ability.setDef(result.getInt("def"));
                ability.setHealth(result.getInt("health"));
                ability.setHigh(result.getInt("high"));
                ability.setMine(result.getInt("mine"));
                ability.setSpeed(result.getInt("speed"));
                ability.setPoint(result.getInt("point"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlayer(UUID uuid, Grade grade, Ability ability) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE gradet SET grade = ?, exp = ? WHERE uuid = ?");
            preparedStatement.setInt(1, grade.getGrade());
            preparedStatement.setInt(2, grade.getExp());
            preparedStatement.setString(3, uuid.toString());
            preparedStatement.executeUpdate();
            preparedStatement = connection.prepareStatement("UPDATE abilityt SET point = ?, str = ? ,def = ?, high = ?, speed = ?, mine = ?, health = ? WHERE uuid = ?");
            preparedStatement.setInt(1, ability.getPoint());
            preparedStatement.setInt(2, ability.getStr());
            preparedStatement.setInt(3, ability.getDef());
            preparedStatement.setInt(4, ability.getHigh());
            preparedStatement.setInt(5, ability.getSpeed());
            preparedStatement.setInt(6, ability.getMine());
            preparedStatement.setInt(7, ability.getHealth());
            preparedStatement.setString(8, uuid.toString());
            preparedStatement.executeUpdate();
            ServerCore.getPlugin().getAbilityHashMap().remove(uuid.toString());
            ServerCore.getPlugin().getGradeHashMap().remove(uuid.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
