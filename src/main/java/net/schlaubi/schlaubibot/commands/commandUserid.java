package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.MySQL;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;
import java.util.List;


public class commandUserid implements Command {
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
        Guild guild = event.getGuild();
        String prefix = MySQL.getValue(guild, "prefix");
        channel.sendTyping().queue();
        privch.sendTyping().queue();
        message.delete().queue();

        List<User> mentioned = message.getMentionedUsers();
        if(args.length > 0){
            for (User users : mentioned){
                String userid = users.getId();
                embedSender.sendEmbed("__**Userid of " + users.getAsMention() + ":**__ \n ```" + userid + "```", channel, Color.CYAN);
                embedSender.sendPermanentEmbed("__**Userid of " + users.getAsMention() + ":**__ \n ```" + userid + "```", privch, Color.CYAN);
            }

        } else {
            embedSender.sendEmbed("Usage: `" + prefix + "userid <@User>`", channel, Color.red);
        }


    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("userid", event);

    }

    @Override
    public String help() {
        return null;
    }
}
