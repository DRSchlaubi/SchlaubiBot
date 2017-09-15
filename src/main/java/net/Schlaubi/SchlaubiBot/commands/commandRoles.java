package net.Schlaubi.SchlaubiBot.commands;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.Schlaubi.SchlaubiBot.util.STATIC;
import net.Schlaubi.SchlaubiBot.util.embedSender;

import java.awt.*;
import java.util.List;

public class commandRoles implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        PrivateChannel privch = author.openPrivateChannel().complete();
        channel.sendTyping().queue();
        message.delete().queue();
        Guild guild = event.getGuild();

        List<Role> roles = guild.getRoles();

        embedSender.sendEmbed("__**Current Server Roles: **__\n" + roles.toString(), channel, Color.CYAN);
        embedSender.sendPermanentEmbed("__**Current Server Roles: **__\n" + roles.toString(), privch, Color.CYAN);




    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        System.out.println("[INFO] Command '" + STATIC.prefix + "roles' was executed by" + event.getAuthor().getName());

    }

    @Override
    public String help() {
        return null;
    }
}