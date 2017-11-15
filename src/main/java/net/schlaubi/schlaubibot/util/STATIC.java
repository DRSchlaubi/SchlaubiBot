package net.schlaubi.schlaubibot.util;


public class STATIC {

    public static final String game = "SchlaubiBot v" + STATIC.VERSION + " | " + STATIC.prefix + "help | Made by Schlaubi";
    public static final String prefix = "::";
    public static final String VERSION = "1.9.5";
    public static final String[] PERMS = {"Administrator", "Admin", "Friend", "Moderator", "Supporter", "Bot Admin", "Bot Owner"};
    public static final String[] OWNERS = {"264048760580079616", "240797338430341120", "240797338430341120", "257187269943754752"};
    public static final String BITLYUSERNAME = SECRETS.SECRET.getValue("bitlyuser").toString();
    public static String input;
    public static final String HOST = SECRETS.SECRET.getValue("db_host").toString();
    public static final String PORT = SECRETS.SECRET.getValue("db_port").toString();
    public static final String DATABASE = SECRETS.SECRET.getValue("db_name").toString();
    public static final String USERNAME = SECRETS.SECRET.getValue("db_user").toString();




}
