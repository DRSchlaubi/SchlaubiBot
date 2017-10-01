package net.schlaubi.schlaubibot.util;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class commandLogger {


    public static void logCommand(String command, MessageReceivedEvent event){
        Guild guild = event.getGuild();
        String logchannel = MySQL.getValue(guild, "logchannel");

        if(logchannel.equals("0"))
            return;

        TextChannel channel = guild.getTextChannelById(logchannel);
        channel.sendTyping().queue();
        embedSender.sendPermanentEmbed("Command `" + STATIC.prefix +  command + "` was executed by **" + event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator() + "**", channel, Color.cyan);

    }
}
