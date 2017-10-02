package net.schlaubi.schlaubibot.listeners;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.schlaubi.schlaubibot.util.MySQL;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;

public class MentionListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        if(message.getMentionedUsers().size() > 0){
            if (message.getMentionedUsers().get(0).equals(event.getGuild().getSelfMember().getUser())){
                MessageChannel channel = event.getChannel();
                channel.sendTyping().queue();
                embedSender.sendPermanentEmbed("**Hey I am the Schlaubibot!!** \n:exclamation: **My Prefix** `" + MySQL.getValue(event.getGuild(), "prefix") + "` \n:question: **Help** `" + MySQL.getValue(event.getGuild(), "prefix") + "help`", channel, Color.cyan);
            }
        }
    }
}
