package commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;
import util.embedSender;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;


public class commandKYS implements Command {


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        final Message message = event.getMessage();
        channel.sendTyping().queue();
        message.delete().queue();

        if(args.length != 0) {
            String name = "";
            for (int i = +0; i < args.length; i++) {
                name += " " + args[i];
            }

            final Message mymsg = channel.sendMessage("```\n __________\n |         |\n |         0 <-- " + name.replace("@", "") + "\n |        /|\\ \n |        / \\ \n |\n |``` \n Kill your self `" + name.replace("@", "") + "`\n http://ropestore.org/?u=" + name.replace("@", "").replace(" ", "%20")).complete();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    mymsg.delete().queue();
                }
            }, 5000);
        } else {

            embedSender.sendEmbed("Usage: `" + STATIC.prefix + "kys <name>`", channel, Color.red);

        }



    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        System.out.println("[INFO] Command '" + STATIC.prefix + "kys' was executed by " + event.getAuthor().getName());

    }

    @Override
    public String help() {
        return null;
    }



}
