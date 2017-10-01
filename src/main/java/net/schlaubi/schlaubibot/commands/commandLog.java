package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.core.permissionHandler;
import net.schlaubi.schlaubibot.util.MySQL;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;

public class commandLog implements Command{
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
        String logchannelid = MySQL.getValue(guild, "logchannel");
        channel.sendTyping().queue();
        message.delete().queue();

        if(permissionHandler.check(event)){
            embedSender.sendEmbed("Sorry, " + author.getAsMention() + " but you don't have the permission to perform that command!", channel, Color.red);
            return;
        }

        if(args.length < 0){
            embedSender.sendEmbed(help(), channel, Color.red);
            return;
        }

        switch (args[0]){
            case "toggle":
                if(!MySQL.getValue(guild, "logchannel").equals("0")){
                    MySQL.updateValue(guild, "logchannel", guild.getDefaultChannel().getId());
                    embedSender.sendEmbed(":white_check_mark: Successfully enabled command logger", channel, Color.green);
                } else {
                    MySQL.updateValue(guild, "logchannel", "0");
                    embedSender.sendEmbed(":white_check_mark: Successfully disabled command logger", channel, Color.green);

                }
                break;
            case "channel":
                if(!(message.getMentionedChannels().size() > 0)){
                    embedSender.sendEmbed("Usage : `" + STATIC.prefix + "joinmessages channel <#Channel>`", channel, Color.red);
                    return;
                }
                MySQL.updateValue(guild, "logchannel", message.getMentionedChannels().get(0).getId());
                embedSender.sendEmbed(":white_check_mark: Succesfully set logchannel to " + message.getMentionedChannels().get(0).getAsMention(), channel, Color.green);

        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        commandLogger.logCommand("log", event);
    }

    @Override
    public String help() {
        return "USAGE: \n" +
                "`  " + STATIC.prefix + "log  toggle`  -  Toggles command logger\n" +
                "`  " + STATIC.prefix + "joinmessages channel  <#Channel>`  -  Sets log channel\n"
                ;
    }
}
