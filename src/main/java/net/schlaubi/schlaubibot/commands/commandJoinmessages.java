package net.schlaubi.schlaubibot.commands;

import net.schlaubi.schlaubibot.core.permissionHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;
import java.io.*;

public class commandJoinmessages implements Command{





    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        File file = new File("SERVER_SETTINGS/" + guild.getId() + "/disabledjoinmessages.dat");
        User author = event.getAuthor();
        MessageChannel channel = event.getChannel();
        Message message = event.getMessage();
        channel.sendTyping().queue();
        message.delete().queue();

        if(permissionHandler.check(event)){

            embedSender.sendEmbed("Sorry, " + author.getAsMention() + " but you don't have the permission to perform that command!", channel, Color.red);
            return;

        }
        if(!(file.exists())){

            file.mkdirs();
            embedSender.sendEmbed(":white_check_mark: Succesfully disabled joinmessages for this server!", channel, Color.green);

        } else {
            file.delete();
            embedSender.sendEmbed(":white_check_mark: Succesfully enabled joinmessages for this server!", channel, Color.green);

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
