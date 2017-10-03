package net.schlaubi.schlaubibot.util;

import net.dv8tion.jda.core.entities.Guild;

import java.sql.*;

public class MySQL {

    private static String password = SECRETS.password;
    private static Connection connection;

    public static void connect(){
        if(!isConnected()){
            try{
                String host = STATIC.HOST;
                String port = STATIC.PORT;
                String database = STATIC.DATABASE;
                String username = STATIC.USERNAME;

                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&autoReconnectForPools=true&interactiveClient=true&characterEncoding=UTF-8", username, password);
                System.out.println("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&autoReconnectForPools=true&interactiveClient=true&characterEncoding=UTF-8");
                System.out.println("[SchlaubiBot] MySQL connected");

            } catch (SQLException e) {
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
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM schlaubibot WHERE serverid = ?");
            ps.setString(1, guild.getId());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  false;
    }

    public static void createServer(Guild guild){
        try{
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `schlaubibot`(`joinmessage`, `leavemessage`, `joinmessages`, `ownerid`, `serverid`,`joinmessagechannel`,`logchannel`,`prefix`) VALUES ('Welcome %user% on %guild%', 'Good bye **%user%! We had a nice time with you', '1', ?, ?, ?,'0',?)");
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
            PreparedStatement ps = connection.prepareStatement("UPDATE schlaubibot SET " + type + " = '" + value + "' WHERE serverid = " + guild.getId());
            ps.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static String getValue(Guild guild, String type){
        try{
            if(connection.isClosed())
                connect();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM schlaubibot WHERE `serverid` = ?");
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
}
