package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;
import net.schlaubi.schlaubibot.core.permissionHandler;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;

public class commandRemoverole implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        GuildController gcon = new GuildController(guild);
        channel.sendTyping().queue();
        message.delete().queue();
        if(permissionHandler.check(event)){

            embedSender.sendEmbed("Sorry, " + author.getAsMention() + " but you don't have the permission to perform that command!", channel, Color.red);
            return;
        }
        if(args.length == 2){
            if(message.getMentionedUsers().size() == 0){
                embedSender.sendEmbed("Usage: `" + STATIC.prefix + "addrole <@User> <role>`", channel, Color.red);
                return;
            }

            Member member = guild.getMember(message.getMentionedUsers().get(0));
            try {
                Role role = guild.getRolesByName(args[1], true).get(0);
                if (!member.getRoles().contains(role)) {
                    embedSender.sendEmbed(":warning: This user don't has this role", channel, Color.red);
                    return;
                }
                gcon.removeRolesFromMember(member, role).queue();
                embedSender.sendEmbed(":white_check_mark: Succesfully removed role `" + args[1] + "` from " + member.getAsMention(), channel, Color.green);
            } catch (IndexOutOfBoundsException e){
                embedSender.sendEmbed(":warning: Sorry but this role don't exists", channel, Color.red);
            }
        } else {
            embedSender.sendEmbed("Usage: `" + STATIC.prefix + "removerole <@User> <role>`", channel, Color.red);
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        commandLogger.logCommand("removerole", event);
    }

    @Override
    public String help() {
        return null;
    }
}
