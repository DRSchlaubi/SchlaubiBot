package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

public class commandPing implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {

        return false;

    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        JDA jda = event.getJDA();
        final Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        message.delete().queue();


        embedSender.sendEmbed("My Ping: `" + jda.getPing() + "`", channel, Color.CYAN);
        }



    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("ping", event);

    }

    @Override
    public String help() {
        return null;
    }
}
