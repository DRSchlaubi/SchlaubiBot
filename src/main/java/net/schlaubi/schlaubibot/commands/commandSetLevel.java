package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;
import net.schlaubi.schlaubibot.core.permissionHandler;
import net.schlaubi.schlaubibot.util.MySQL;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;

public class commandSetLevel implements Command{
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
        channel.sendTyping().queue();
        message.delete().queue();

        if(args.length >= 2){
            if(message.getMentionedUsers().size() == 0){
                embedSender.sendEmbed(help(), channel, Color.red);
                return;
            }
            String prefix = MySQL.getValue(guild, "prefix");
            User target = message.getMentionedUsers().get(0);
            int permlvl = 0;
            try {
                permlvl = Integer.parseInt(args[0]);
            } catch (NumberFormatException e){
                embedSender.sendEmbed(":warning: Please enter a valid number", channel, Color.red);
                return;
            }

            if(permlvl > MySQL.getUserPermissionLevel(author, guild)){
                embedSender.sendEmbed(":warning: You can't assign a permission level, that is higher than yours", channel, Color.red);
                return;
            }

            if(!permissionHandler.isOwner(target)){
                embedSender.sendEmbed(":warning: You cant change the permissionlevel of a bot owner", channel, Color.red);
                return;
            }

            MySQL.setPermissionLevel(target, guild, permlvl);
            embedSender.sendEmbed(":white_check_mark: Successfully set permissionlevel of " + target.getAsMention() + " to `" + String.valueOf(permlvl) + "`", channel, Color.green);
        } else {
            embedSender.sendEmbed(help(), channel, Color.red);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        commandLogger.logCommand("setlevel", event);
    }

    @Override
    public String help() {
        return "Usage: `::setlevel <level> <@User>`";
    }

    @Override
    public String description() {
        return "Sets user's permission level";
    }

    @Override
    public String usage() {
        return "::setlevel <level> <@User> ";
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
