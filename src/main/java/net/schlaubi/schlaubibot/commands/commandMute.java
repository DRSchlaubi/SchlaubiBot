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

        if(message.getMentionedUsers().size() > 0){
            Member member = guild.getMember(message.getMentionedUsers().get(0));
            if(channel.getPermissionOverride(member) == null){
                channel.createPermissionOverride(member).complete();
            }

            if(!guild.getSelfMember().canInteract(member)){
                embedSender.sendEmbed(":warning: You can't mute " + member.getAsMention() + "he's an admin", channel, Color.red);
                return;
            }

            if(!channel.getPermissionOverride(member).getDenied().contains(Permission.MESSAGE_WRITE)){
                channel.getPermissionOverride(member).getManager().deny(Permission.MESSAGE_WRITE).queue();
                embedSender.sendEmbed(":white_check_mark:  Successfully muted " + member.getAsMention(), channel, Color.green);
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
