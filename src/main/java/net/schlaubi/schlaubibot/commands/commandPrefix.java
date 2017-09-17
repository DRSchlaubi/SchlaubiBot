package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.core.permissionHandler;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;
import java.io.*;
import java.util.Properties;

public class commandPrefix implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        File file = new File("SERVER_SETTINGS/" + guild.getId() + "/settings.properties");
        File path = new File("SERVER_SETTINGS/" + guild.getId() + "/");
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

        if(properties.getProperty("prefix") == null){
            properties.setProperty("prefix", "!!");
            try {
                properties.store(new FileOutputStream(file), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(args.length > 0){
            if(!(args[0].length() > 2)){
                properties.setProperty("prefix", args[0]);
                embedSender.sendEmbed(":white_check_mark: Successfully set prefix to `" + args[0] + "` !", channel, Color.green);
                try {
                    properties.store(new FileOutputStream(file), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                embedSender.sendEmbed(":warning: The prefix can not be longer than 2 digits", channel, Color.red);
            }
        } else {
            embedSender.sendEmbed("Usage: `" + STATIC.prefix + "preifx <prefix>`", channel, Color.red);
        }




    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        System.out.println("[INFO] Command '" + STATIC.prefix + "prefix' was executed by" + event.getAuthor().getName());

    }

    @Override
    public String help() {
        return null;
    }
}
