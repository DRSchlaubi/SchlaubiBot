package net.schlaubi.schlaubibot.commands;


import net.dv8tion.jda.core.entities.*;
import net.schlaubi.schlaubibot.core.permissionHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;
import java.io.*;
import java.util.Properties;

public class commandJoinmessages implements Command{





    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        File file = new File("SERVER_SETTINGS/" + guild.getId() + "/settings.properties");
        File path = new File("SERVER_SETTINGS/" + guild.getId() + "/");
        User author = event.getAuthor();
        MessageChannel channel = event.getChannel();
        Message message = event.getMessage();
        channel.sendTyping().queue();
        message.delete().queue();

        if(permissionHandler.check(event)){

            embedSender.sendEmbed("Sorry, " + author.getAsMention() + " but you don't have the permission to perform that command!", channel, Color.red);
            return;

        }

        Properties properties = new Properties();
        try {

            if (file.exists()) {
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                properties.load(bis);

            } else {
                if(!path.exists()) {
                    path.mkdirs();
                }
                file.createNewFile();
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        if(properties.getProperty("joinmessages") == null){
            properties.setProperty("joinmessages", "enabled");
            try {
                properties.store(new FileOutputStream(file), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(!properties.getProperty("joinmessages").equals("disabled")){
            properties.setProperty("joinmessages", "disabled");
            embedSender.sendEmbed(":white_check_mark: Succesfully disabled joinmessages!", channel, Color.green);
        } else if(properties.getProperty("joinmessages").equals("disabled")){
            properties.setProperty("joinmessages", "enabled");
            embedSender.sendEmbed(":white_check_mark: Succesfully enabled joinmessages!", channel, Color.green);


        }

        try {
            properties.store(new FileOutputStream(file), null);
        } catch (IOException e) {
            e.printStackTrace();
        }





    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
