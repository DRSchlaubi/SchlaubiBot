package net.schlaubi.schlaubibot.listeners;

import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.schlaubi.schlaubibot.util.MySQL;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;

public class GuildJoinListener extends ListenerAdapter{

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        if(!MySQL.ifGuildExists(event.getGuild()))
            MySQL.createServer(event.getGuild());
        MessageChannel channel = event.getGuild().getDefaultChannel();
        channel.sendTyping().queue();
        embedSender.sendPermanentEmbed("**Hey I am the Schlaubibot!! Thank's for adding me!!** \n:exclamation: **My Prefix** `" + MySQL.getValue(event.getGuild(), "prefix") + "` \n:question: **Help** `" + MySQL.getValue(event.getGuild(), "prefix") + "help`", channel, Color.cyan);

    }
}
