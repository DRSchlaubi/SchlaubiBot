package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.MySQL;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class commandRIP implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        String prefix = MySQL.getValue(guild, "prefix");
        final Message message = event.getMessage();

        channel.sendTyping().queue();


        String line = "";

        if(!(args.length < 2)) {

            for (int i = 1; i < args.length; i++) {
                line = args[i] + " ";
            }

            try {
                message.delete().queue();
                InputStream image = new URL("http://www.tombstonebuilder.com/generate.php?top1=R.I.P.&top2=&top3=" + args[0].replace(" ", "%20").replace("@", "") + "&top4=" + line.replace(" ", "%20") + "&sp=").openStream();
                channel.sendFile(image, "rip.png", null).queue();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        message.delete().queue();
                    }
                }, 2000);
            } catch (IOException e) {
                e.printStackTrace();
            }



        } else {
            message.delete().queue();
            embedSender.sendEmbed("Usage: `" + prefix + "rip <line1> <line2>`", channel, Color.red);
        }




    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("rip", event);
    }

    @Override
    public String help() {
        return null;
    }
}
