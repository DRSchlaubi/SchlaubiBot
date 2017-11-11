package net.schlaubi.schlaubibot.commands;

import net.schlaubi.schlaubibot.core.permissionHandler;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.MySQL;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;
import java.util.List;

public class commandMute implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        Guild guild = event.getGuild();
        String prefix = MySQL.getValue(guild, "prefix");
        TextChannel channel = event.getTextChannel();
        Message message = event.getMessage();
        channel.sendTyping().queue();
        message.delete().queue();

        if(args.length > 0){
            List<User> mentioned = message.getMentionedUsers();
            for(User user : mentioned) {
                Member member = guild.getMember(user);
                if(channel.getPermissionOverride(member) == null) {
                    channel.createPermissionOverride(member).complete();
                }
                    Member selfmember = guild.getSelfMember();
                    if(selfmember.canInteract(member)) {
                        if (!channel.getPermissionOverride(member).getDenied().contains(Permission.MESSAGE_WRITE)) {
                            channel.getPermissionOverride(member).getManager().deny(Permission.MESSAGE_WRITE).complete();
                            embedSender.sendEmbed(":white_check_mark: Succesfully muted " + user.getAsMention(), channel, Color.green);
                        } else {
                            embedSender.sendEmbed(":warning: This user is already muted use `" + prefix + "unmute` to unmute", channel, Color.red);
                        }
                    } else {
                        embedSender.sendEmbed(":warning: Can't mute" + user.getAsMention() + " he's an admin!", channel, Color.red);
                    }

            }

        } else {

                embedSender.sendEmbed("Usage: `" + prefix + "mute <@User>` ", channel, Color.red);
            }
        }


    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("mute", event);

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "Mutes/Unmutes a user";
    }

    @Override
    public String usage() {
        return "::mute <@User>";
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
