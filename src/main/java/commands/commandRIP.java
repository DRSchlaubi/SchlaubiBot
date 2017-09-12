package commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;
import util.embedSender;

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
        User author = event.getAuthor();
        MessageChannel channel = event.getChannel();
        final Message message = event.getMessage();
        String msg = message.getContent();

        channel.sendTyping().queue();


        String line = "";

        if(!(args.length < 2)) {

            for (int i = 1; i < args.length; i++) {
                line += " " + args[i];
            }

            try {
                message.delete().queue();
                InputStream image = new URL("http://www.tombstonebuilder.com/generate.php?top1=R.I.P.&top2=&top3=" + args[0].replace(" ", "%20") + "&top4=" + line.replace(" ", "%20") + "&sp=").openStream();
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
            embedSender.sendEmbed("Usage: `" + STATIC.prefix + "rip <line1> <line2>`", channel, Color.red);
        }




    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        System.out.println("[INFO] Command '" + STATIC.prefix + "rip' was executed by " + event.getAuthor().getName());
    }

    @Override
    public String help() {
        return null;
    }
}
