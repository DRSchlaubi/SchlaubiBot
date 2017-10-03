package net.schlaubi.schlaubibot.util;


import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class embedSender {
    public static void sendEmbed(String content, MessageChannel channel, Color color){
        EmbedBuilder embed = new EmbedBuilder().setDescription(content).setColor(color).setFooter("(c) 2017 Schlaubi | Schlaubibot", "https://cdn.discordapp.com/avatars/264048760580079616/4306cb8bcf063c3cbfa4998fc40080ec.png");
        Message mymsg = channel.sendMessage(embed.build()).complete();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mymsg.delete().queue();
            }
        }, 5000);
    }
    public static void sendPermanentEmbed(String content, MessageChannel channel, Color color){
        EmbedBuilder embed = new EmbedBuilder().setDescription(content).setColor(color).setFooter("(c) 2017 Schlaubi | Schlaubibot", "https://cdn.discordapp.com/avatars/264048760580079616/4306cb8bcf063c3cbfa4998fc40080ec.png");
        channel.sendMessage(embed.build()).complete();

    }
}
