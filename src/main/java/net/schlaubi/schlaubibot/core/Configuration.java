package net.schlaubi.schlaubibot.core;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;


 /*
    Thanks to https://github.com/max95812
 */
public class Configuration {


    private static final String DEFAULT_CONFIG_FILE = "config.json";

    /* getting input */
    private static final BufferedReader sys_in;
    static {
        InputStreamReader isr = new InputStreamReader(System.in);
        sys_in = new BufferedReader(isr);
    }

    private final File configFile;
    private static Map<String, Entry> entryMap = new HashMap<>();

    private static boolean FLAG_RECONFIG = false;
    private static boolean FLAG_PROMPT_TOKEN = false;
    private static boolean FLAG_PROMPT_DB_PASS = false;
    private static boolean FLAG_PROMPT_BITLY = false;
    private static boolean FLAG_PROMPT_GIPHY = false;


    public Configuration()  {
        this(DEFAULT_CONFIG_FILE);
    }

    public Configuration(String configFile) {
        this(new File(configFile));
    }

    public Configuration(File file) {
        this.configFile = file;

        initiateFile();


        try {
            commit();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println("Can't store configuration");
            Runtime.getRuntime().exit(2);
        }
        FLAG_RECONFIG = false;

    }

    private void initiateFile() {
        /* getting input */
        if(configFile.exists()) {
            try {
                /* read configFile and get token */
                FileInputStream fis = new FileInputStream(configFile);
                JSONTokener tokener = new JSONTokener(fis);
                JSONObject jsonobj = new JSONObject(tokener);
                jsonobj.names().forEach(o -> {
                    String k = (String) o;
                    Object v = jsonobj.get(k);
                    if(String.class.isInstance(v)) {
                        entryMap.put(k, new Entry(v));
                    }
                });
                /* return token if valid */
            } catch (FileNotFoundException | JSONException ignored) {
                configFile.delete();
                FLAG_RECONFIG = true;
            }
            /* if not returned, delete configFile */
        }
    }

    private static String prompt(String req) {
        String token;

        /* prompt for token */
        System.out.println("Enter you'r "+req+":");

        try {
            /* read and trim line */
            String line = sys_in.readLine();
            token = line.trim();

            return token;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        /* RAGEQUIT */
        System.out.println("Exiting");
        Runtime.getRuntime().exit(1);
        return null;
    }

    private static Entry get(String key) {
        return entryMap.get(key);
    }

    public Object getValue(String key) {
        return entryMap.get(key).value;
    }

    public void set(String k, JSONObject v) {
        entryMap.put(k, new Entry(v));
    }
    public void set(String k, boolean v) {
        entryMap.put(k, new Entry(v));
    }
    public void set(String k, long v) {
        entryMap.put(k, new Entry(v));
    }
    public static void set(String k, String v) {
        entryMap.put(k, new Entry(v));
    }

    public void commit() throws IOException {
        /* write token to file */
        JSONObject jsonobj = new JSONObject();
        entryMap.forEach((s, e) -> jsonobj.put(s, e.value));
        FileWriter fw = new FileWriter(configFile);
        fw.write(jsonobj.toString());
        fw.close();
    }

    private static boolean validate_token(String token) {

        if(token == null) {
            return false;
        }

        JDABuilder builder = new JDABuilder(AccountType.BOT);

        builder.setToken(token);

        try {
            JDA jda = builder.buildBlocking();
            jda.shutdownNow();
            return true;
        } catch (InterruptedException | RateLimitedException | LoginException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public static void check_config() {
        System.out.println("Hey I am the SUPERCOOL Setup of the SchlaubiBot \nThanks to https://github.com/max95812 for helping Schlaubi to create me");
        System.out.println("First of all we start with the Discord Bot token you can create one here https://schlb.pw/2wGGJIX");
        while (true) {
                String token = prompt("token");
                set("token", token);
                System.out.println("Perfect now you must create an MySQL Database and import this .sql file (https://schlb.pw/2wFJxGo) ");
                String db_pass = prompt("database password");
                set("db_pass", db_pass);
                String db_user = prompt("database user");
                set("db_user", db_user);
                String db_name = prompt("database name");
                set("db_name", db_name);
                String db_host = prompt("database host");
                set("db_host", db_host);
                String db_port = prompt("database port");
                set("db_port", db_port);
                System.out.println("YEAHHHH i saved you MySQL information and will test it when this setup is finished");
                String giphykey = prompt("Giphy api key (https://schlb.pw/2z99orY)");
                set("giphykey", giphykey);
                String bitlytoken = prompt("Bitly api key (https://schlb.pw/2z7Cisj)");
                set("bitlytoken", bitlytoken);
                FLAG_RECONFIG = false;



            Entry e;

            e = get("token");
            if(e == null) {
                FLAG_PROMPT_TOKEN = true;
            } else {
            /* if type is wrong, trigger prompt because an empty token string can't be valid */
                String v = (e.type == Entry.TYPE_STRING ? (String) e.value : "");
                FLAG_PROMPT_TOKEN = !validate_token(v);
            }

            e = get("db_pass");
            if(e == null) {
                FLAG_PROMPT_DB_PASS = true;
            }

            boolean ok;
            ok  = !FLAG_PROMPT_TOKEN;
            ok &= !FLAG_PROMPT_DB_PASS;
            if(ok) {
                break;
            }
        }
    }


    private static class Entry {
        static final int TYPE_OBJECT = 0;
        static final int TYPE_BOOL = 1;
        static final int TYPE_LONG = 2;
        static final int TYPE_STRING = 3;

        int type;
        Object value;

        Entry(Object obj) {
            type = TYPE_OBJECT;
            if (String.class.isInstance(obj)) {
                type = TYPE_STRING;
            } else if (Integer.class.isInstance(obj) || Long.class.isInstance(obj)) {
                type = TYPE_LONG;
            } else if (Boolean.class.isInstance(obj)) {
                type = TYPE_BOOL;
            }
            value = obj;
        }
        Entry(boolean bool) {
            type = TYPE_BOOL;
            value = bool;
        }
        Entry(long long_int) {
            type = TYPE_LONG;
            value = long_int;
        }
        Entry(String str) {
            type = TYPE_STRING;
            value = str;
        }
    }
}