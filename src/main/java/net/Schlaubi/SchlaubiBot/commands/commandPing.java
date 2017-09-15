package net.Schlaubi.SchlaubiBot.commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

import net.Schlaubi.SchlaubiBot.util.STATIC;
import net.Schlaubi.SchlaubiBot.util.embedSender;

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


        embedSender.sendEmbed("Mein Ping: `" + jda.getPing() + "`", channel, Color.CYAN);
        }



    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        System.out.println("[INFO] Command '" + STATIC.prefix + "ping' was executed by " + event.getAuthor().getName());

    }

    @Override
    public String help() {
        return null;
    }
}
