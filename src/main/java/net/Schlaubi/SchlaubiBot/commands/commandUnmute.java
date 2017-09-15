package net.Schlaubi.SchlaubiBot.commands;

import net.Schlaubi.SchlaubiBot.core.permissionHandler;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.Schlaubi.SchlaubiBot.util.STATIC;
import net.Schlaubi.SchlaubiBot.util.embedSender;

import java.awt.*;
import java.util.List;

public class commandUnmute implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        TextChannel channel = event.getTextChannel();
        Guild guild = event.getGuild();
        channel.sendTyping().queue();
        message.delete().queue();

        if(permissionHandler.check(event)){

            embedSender.sendEmbed("Sorry, " + author.getAsMention() + " but you don't have the permission to perform that command!", channel, Color.red);
            return;
        }

        if(args.length > 0){
            List<User> mentioned = message.getMentionedUsers();
            for (User users : mentioned){
                Member member = guild.getMember(users);
                if(channel.getPermissionOverride(member) == null){
                    channel.createPermissionOverride(member).complete();
                }
                if (channel.getPermissionOverride(member).getDenied().contains(Permission.MESSAGE_WRITE)){
                    channel.getPermissionOverride(member).getManager().clear(Permission.MESSAGE_WRITE);
                    channel.getPermissionOverride(member).getManager().grant(Permission.MESSAGE_WRITE).complete();
                    embedSender.sendEmbed(":white_check_mark: Succesfully unmuted " + users.getAsMention(), channel, Color.green);
                } else {
                    embedSender.sendEmbed(":warning: This user is not muted use `mute <@User>` to mute him", channel, Color.red);
                }
            }


        } else {
            embedSender.sendEmbed("Usage: `unmute <@User>`", channel, Color.red);
        }



    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        System.out.println("[INFO] Command '-" + STATIC.prefix + "unmute' was executed by" + event.getAuthor().getName());

    }

    @Override
    public String help() {
        return null;
    }
}
