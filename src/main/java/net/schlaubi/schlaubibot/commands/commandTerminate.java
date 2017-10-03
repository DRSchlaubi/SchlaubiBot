package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.core.permissionHandler;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;

public class commandTerminate  implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        message.delete().queue();
        channel.sendTyping().queue();

        if(permissionHandler.isOwner(event)){
            embedSender.sendEmbed("Sorry, " + author.getAsMention() + " but you don't have the permission to perform that command!", channel, Color.red);
            return;
        }

        embedSender.sendEmbed(":stop_button:  Terminating ... Bye", channel, Color.green);
        System.exit(0);

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("terminate", event);

    }

    @Override
    public String help() {
        return null;
    }
}
