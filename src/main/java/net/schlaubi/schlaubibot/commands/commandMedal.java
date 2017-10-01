package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.commandLogger;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class commandMedal implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        final Message message = event.getMessage();
        channel.sendTyping().queue();

        if(!(args.length < 2)){
            message.delete().queue();
            String name = "";
            for (int i = 1; i < args.length; i++) {
                name += " " + args[i];
            }
            try {
                InputStream image = new URL("http://www.getamedal.com/generate.php?top1=Congratulations&top2=" + args[0].replace("@", "") + "&top3=" + name.replace(" ", "%20") + "&top4=&sp=").openStream();
                channel.sendFile(image, "medal.png", null ).queue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            message.delete().queue();
            EmbedBuilder embed = new EmbedBuilder().setDescription("Usage: `" + STATIC.prefix + "medal <name> <line>`").setColor(Color.red);
            Message mymsg = channel.sendMessage(embed.build()).complete();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    mymsg.delete().queue();
                    message.delete().queue();
                }
            }, 5000);
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("medal", event);

    }

    @Override
    public String help() {
        return null;
    }
}
