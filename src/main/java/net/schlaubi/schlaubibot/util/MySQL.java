package net.schlaubi.schlaubibot.util;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

import java.sql.*;

public class MySQL {

    private static String password = SECRETS.password;
    public static Connection connection;

    public static void connect(){
        if(!isConnected()){
            String host = STATIC.HOST;
            String port = STATIC.PORT;
            String database = STATIC.DATABASE;
            String username = STATIC.USERNAME;
            try{


                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&autoReconnectForPools=true&interactiveClient=true&characterEncoding=UTF-8", username, password);
                System.out.println("[SchlaubiBot] MySQL connected");

            } catch (SQLException e) {System.out.println("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&autoReconnectForPools=true&interactiveClient=true&characterEncoding=UTF-8");
                System.out.println("[SchlaubiBot] MySQL connection failed");
                e.printStackTrace();
            }
        }
    }

    public static void disconnect(){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnected(){
        return (connection != null);
    }

    public static boolean ifGuildExists(Guild guild){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM servers WHERE serverid = ?");
            ps.setString(1, guild.getId());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return  false;
    }

    public static void createServer(Guild guild){
        try{
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `servers`(`joinmessage`, `leavemessage`, `joinmessages`, `ownerid`, `serverid`,`joinmessagechannel`,`logchannel`,`prefix`) VALUES ('Welcome %user% on %guild%', 'Good bye **%user%! We had a nice time with you', '1', ?, ?, ?,'0',?)");
            ps.setString(1, guild.getOwner().getUser().getId());
            ps.setString(2, guild.getId());
            ps.setString(3, guild.getDefaultChannel().getId());
            ps.setString(4, STATIC.prefix);
            ps.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateValue(Guild guild, String type, String value){
        try{
            if(connection.isClosed())
                connect();
            if(!ifGuildExists(guild))
                createServer(guild);
            PreparedStatement ps = connection.prepareStatement("UPDATE servers SET " + type + " = '" + value + "' WHERE serverid = " + guild.getId());
            ps.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static String getValue(Guild guild, String type){
        try{
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM servers WHERE `serverid` = ?");
            ps.setString(1, guild.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                return rs.getString(type);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void createUser(User user, Guild guild){
        try{
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `permissiones`(`discordid`, `serverid`, `permlvl`) VALUES  (?, ?, '0')");
            ps.setString(1, user.getId());
            ps.setString(2, guild.getId());
            ps.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void deleteUser(User user, Guild guild){
        try {
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM `ùsers` WHERE discordid=? AND serverid=?");
            ps.setString(1, user.getId());
            ps.setString(2, guild.getId());
            ps.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void deleteServer(Guild guild){
        try {
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM `ùsers` WHERE serverid=?");
            ps.setString(1, guild.getId());
            ps.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean ifUserExits(User user, Guild guild){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM permissiones WHERE discordid = ? AND serverid = ?");
            ps.setString(1, user.getId());
            ps.setString(2, guild.getId());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return  false;
    }

    public static int getUserPermissionLevel(User user, Guild guild){
        try{
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM permissiones WHERE `discordid` = ? AND serverid = ?");
            ps.setString(1, user.getId());
            ps.setString(2, guild.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                return Integer.parseInt(rs.getString("permlvl"));
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public static void setPermissionLevel(User user,Guild guild, int permlvl){
        try{
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("UPDATE permissiones SET permlvl= " + String.valueOf(permlvl) + " WHERE discordid='" + user.getId() + "' AND serverid='" + guild.getId() + "'");
            ps.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
