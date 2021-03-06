package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;


public class commandServers implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        MessageChannel channel = event.getChannel();
        Message message = event.getMessage();
        PrivateChannel prich = author.openPrivateChannel().complete();
        channel.sendTyping().queue();
        message.delete().queue();

        String out = "\n This bot is running on the following servers: \n";

        for (Guild g : event.getJDA().getGuilds()){
            out += g.getName() + " (" + g.getId() + ")  \n";
        }
        embedSender.sendEmbed(out, channel, Color.CYAN);
        embedSender.sendPermanentEmbed(out, prich, Color.CYAN);
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("servers", event);

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "Shows the bot's curent servers";
    }

    @Override
    public String usage() {
        return "::servers";
    }

    @Override
    public CommandCategory category() {
        return CommandCategory.BOTINFO;
    }

    @Override
    public int permissionlevel() {
        return 3;
    }
}
