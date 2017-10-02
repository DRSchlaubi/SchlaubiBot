package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

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
        String rolelout = "";
        for(Role role : roles){
            rolelout += role.getName() + "(" + role.getId() + ") \n";
        }

        embedSender.sendEmbed("__**Current Server Roles: **__\n" + rolelout, channel, Color.CYAN);
        embedSender.sendPermanentEmbed("__**Current Server Roles: **__\n" + rolelout, privch, Color.CYAN);




    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("roles", event);

    }

    @Override
    public String help() {
        return null;
    }
}
