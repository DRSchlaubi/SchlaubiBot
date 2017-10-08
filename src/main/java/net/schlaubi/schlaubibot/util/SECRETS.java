package net.schlaubi.schlaubibot.util;

import net.schlaubi.schlaubibot.core.Configuration;

public class SECRETS {

    public static final Configuration SECRET = new Configuration("secrets.json");
    public static String token = SECRET.getValue("token").toString();
    public static String password = SECRET.getValue("db_pass").toString();
    public static String bitlytoken = SECRET.getValue("bitlytoken").toString();
    public static String giphykey = SECRET.getValue("giphykey").toString();
}
