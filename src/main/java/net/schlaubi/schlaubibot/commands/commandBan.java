package net.schlaubi.schlaubibot.commands;

import net.schlaubi.schlaubibot.core.permissionHandler;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

import java.awt.*;
import java.util.List;

import net.schlaubi.schlaubibot.util.MySQL;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

public class commandBan implements Command {

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
        String prefix = MySQL.getValue(guild, "prefix");
        GuildController guildcon = new GuildController(event.getGuild());
        Member selfmember = guild.getSelfMember();

        channel.sendTyping().queue();
        message.delete().queue();
        if (args.length > 0) {
            List<User> mentioned = message.getMentionedUsers();
            for (User users : mentioned) {
                Member members = guild.getMember(users);
                if (selfmember.canInteract(members)) {
                    guildcon.ban(members, 7).queue();
                    embedSender.sendEmbed(":white_check_mark: Succesfully banned " + users.getAsMention(), channel, Color.green);

                } else {
                    embedSender.sendEmbed(":warning: Can't ban " + users.getAsMention() + " he's an admin!", channel, Color.red);
                }
            }

        } else {
            embedSender.sendEmbed("Usage `" + prefix + "ban <@User>`", channel, Color.red);
        }

    }




    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("ban", event);

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "Bans a user";
    }
    @Override
    public String usage() {
        return "::ban <@User>";
    }

    @Override
    public CommandCategory category() {
        return CommandCategory.MODERATION;
    }

    @Override
    public int permissionlevel() {
        return 2;
    }
}
