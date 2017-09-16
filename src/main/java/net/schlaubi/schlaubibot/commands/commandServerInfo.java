package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.STATIC;

import java.awt.*;

public class commandServerInfo implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        message.delete().queue();
        channel.sendTyping().queue();

        String afktimeout = guild.getAfkTimeout().toString();
        Integer afk = Integer.parseInt(afktimeout.replace("SECONDS_", ""))/60;

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle(":desktop: Serverinformation for " + guild.getName())
                .setThumbnail(guild.getIconUrl())
                .addField("ID:", guild.getId(), false)
                .addField("Guildname", guild.getName(), false)
                .addField("Server region", guild.getRegion().toString(), false)
                .addField("Members", String.valueOf(guild.getMembers().size()), false)
                .addField("Textchannels", String.valueOf(guild.getTextChannels().size()), false)
                .addField("Voicechannels", String.valueOf(guild.getVoiceChannels().size()), false)
                .addField("Roles", String.valueOf(guild.getRoles().size()), false)
                .addField("AFK timeout", "AFK timeout after " + afk + "minutes", false)
                .addField("Owner",  guild.getOwner().getUser().getName() + "#" + guild.getOwner().getUser().getDiscriminator(), false)
                .addField("Icon URL", guild.getIconUrl(), false)
                .setColor(Color.cyan)
                .setFooter("SchlaubiBot made by Schlaubi", null);
        channel.sendMessage(embed.build()).queue();




    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        System.out.println("[INFO] Command '-" + STATIC.prefix +"serverinfo' was executed by " + event.getAuthor().getName());

    }

    @Override
    public String help() {
        return null;
    }
}
