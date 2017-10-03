package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;


public class commandKYS implements Command {


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        final Message message = event.getMessage();
        Guild guild = event.getGuild();
        String prefix = STATIC.prefix;
        channel.sendTyping().queue();
        message.delete().queue();

        if(args.length != 0) {
            String name = "";
            for (int i = +0; i < args.length; i++) {
                name += " " + args[i];
            }

            channel.sendMessage("```\n __________\n |         |\n |         0 <-- " + name.replace("@", "") + "\n |        /|\\ \n |        / \\ \n |\n |``` \n Kill yourselve `" + name.replace("@", "") + "`\n http://ropestore.org/?u=" + name.replace("@", "").replace(" ", "%20")).queue();
        } else {

            embedSender.sendEmbed("Usage: `" + prefix+ "kys <name>`", channel, Color.red);

        }



    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("kys", event);

    }

    @Override
    public String help() {
        return null;
    }



}
