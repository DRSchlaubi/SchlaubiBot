package util;


import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class embedSender {
    public static void sendEmbed(String content, MessageChannel channel, Color color){
        EmbedBuilder embed = new EmbedBuilder().setDescription(content).setColor(color);
        Message mymsg = channel.sendMessage(embed.build()).complete();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mymsg.delete().queue();
            }
        }, 5000);
    }
    public static void sendPermanentEmbed(String content, MessageChannel channel, Color color){
        EmbedBuilder embed = new EmbedBuilder().setDescription(content).setColor(color);
        channel.sendMessage(embed.build()).complete();

    }
}
