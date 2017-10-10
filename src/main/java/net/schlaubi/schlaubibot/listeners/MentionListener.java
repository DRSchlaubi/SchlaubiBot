package net.schlaubi.schlaubibot.listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.schlaubi.schlaubibot.util.MySQL;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;
import java.util.Arrays;

public class MentionListener extends ListenerAdapter {
    private final String[] ROLES = {"307084559303049216","307084714890625024"};


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        if (message.getMentionedUsers().size() > 0) {
            String[] args = event.getMessage().getContent().split(" ");
            if (args[1].equals("stop"))
                return;
            if (message.getMentionedUsers().get(0).equals(event.getGuild().getSelfMember().getUser())) {
                MessageChannel channel = event.getChannel();
                channel.sendTyping().queue();
                embedSender.sendPermanentEmbed("**Hey I am the Schlaubibot!!** \n:exclamation: **My Prefix** `" + MySQL.getValue(event.getGuild(), "prefix") + "` \n:question: **Help** `" + MySQL.getValue(event.getGuild(), "prefix") + "help`", channel, Color.cyan);
            }
        }
    }
    private void checkMention(MessageReceivedEvent event){
        Message message = event.getMessage();
        if(message.getMentionedUsers().size() > 0){
            if (message.getMentionedUsers().get(0).equals(event.getGuild().getSelfMember().getUser())){
                for(Role r : event.getMember().getRoles()){
                    if(Arrays.stream(ROLES).parallel().anyMatch(r.getId()::contains)){
                        String[] args = event.getMessage().getContent().split(" ");
                        if(args[1].equals("stop")){
                            event.getMessage().delete().queue();
                            event.getChannel().sendMessage(new EmbedBuilder().setDescription("BY BYE :wave: :C").build()).queue();
                            System.exit(0);
                        }
                    }
                }
            }
        }
    }
}
