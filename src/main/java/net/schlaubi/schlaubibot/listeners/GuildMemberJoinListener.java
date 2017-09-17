package net.schlaubi.schlaubibot.listeners;


import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.*;
import java.util.Properties;

public class GuildMemberJoinListener extends ListenerAdapter {


    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e){
        Guild guild = e.getGuild();
        File file = new File("SERVER_SETTINGS/" + guild.getId() + "/settings.properties");
        File path = new File("SERVER_SETTINGS/" + guild.getId() + "/");
        if(!path.exists()){
            path.mkdirs();
        }
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        Properties properties = new Properties();
        BufferedInputStream bis = null;

        if(properties.getProperty("joinmessages") == null){
            properties.setProperty("joinmessages", "enabled");
            try {
                properties.store(new FileOutputStream(file), null);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            properties.load(bis);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if(!(properties.getProperty("joinmessages").equals("disabled"))) {
            TextChannel channel = guild.getDefaultChannel();
            channel.sendTyping().queue();
            channel.sendMessage("Welcome " + e.getUser().getAsMention() + ", on **" + guild.getName() + "**").queue();
        }
    }
}
