package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;


import java.awt.*;
import java.util.List;

import net.schlaubi.schlaubibot.core.permissionHandler;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

public class commandKick implements Command {

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        final Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        String prefix = STATIC.prefix;
        GuildController guildcon = new GuildController(event.getGuild());

        channel.sendTyping().queue();
        message.delete().queue();


        if(permissionHandler.check(event)){
            if(permissionHandler.check(event)){

                embedSender.sendEmbed("Sorry, " + author.getAsMention() + " but you don't have the permission to perform that command!", channel, Color.red);
                return;
            }
            return;
        }
            if(args.length >0){
                List<User> mentioned = message.getMentionedUsers();
                for(User users : mentioned){
                    Member members = guild.getMember(users);
                    Member selfmember = guild.getSelfMember();
                    if(selfmember.canInteract(members)){
                        guildcon.kick(members).reason(" ").queue();
                        embedSender.sendEmbed(":white_check_mark: Succesfully kicked " + members.getAsMention(), channel, Color.green);
                    } else {
                       embedSender.sendEmbed(":warning: Can't kick " + users.getAsMention() + " he's an admin!", channel, Color.red);
                    }
                }
            } else {
                embedSender.sendEmbed("Usage: `kick <@User>`", channel, Color.red);
            }
        }





    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("kick", event);

    }

    @Override
    public String help() {
        return null;
    }
}
