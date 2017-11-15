package net.schlaubi.schlaubibot.commands;

import net.schlaubi.schlaubibot.core.permissionHandler;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

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


        if(args.length > 0){
            List<User> mentioned = message.getMentionedUsers();
            for (User users : mentioned){
                Member member = guild.getMember(users);
                if(channel.getPermissionOverride(member) == null){
                    channel.createPermissionOverride(member).complete();
                }
                if (channel.getPermissionOverride(member).getDenied().contains(Permission.MESSAGE_WRITE)){
                    guild.getTextChannels().forEach(c -> {
                        if(c.getPermissionOverride(member) == null){
                            c.createPermissionOverride(member).complete();
                        }
                        c.getPermissionOverride(member).getManager().clear(Permission.MESSAGE_WRITE);
                        c.getPermissionOverride(member).getManager().grant(Permission.MESSAGE_WRITE).complete();
                    });
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

        commandLogger.logCommand("unmute", event);

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "Unmutes a player";
    }

    @Override
    public String usage() {
        return "::unmute <@User>";
    }

    @Override
    public CommandCategory category() {
        return CommandCategory.MODERATION;
    }

    @Override
    public int permissionlevel() {
        return 1;
    }
}
