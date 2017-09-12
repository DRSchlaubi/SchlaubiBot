package commands;

import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;

import java.util.Timer;
import java.util.TimerTask;

public class commandVersion implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, final MessageReceivedEvent event) {
        User author = event.getAuthor();
        PrivateChannel privch = (PrivateChannel)author.openPrivateChannel().complete();
        event.getChannel().sendTyping().queue();
        privch.sendTyping().queue();
        privch.sendMessage("``` Schlaubibot Version" + STATIC.VERSION + " by Schlaubi```").queue();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                event.getMessage().delete().queue();
            }
        },2000);


    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        System.out.println("[INFO] Command '" + STATIC.prefix + "version' was executed by " + event.getAuthor().getName());

    }

    @Override
    public String help() {
        return null;
    }
}
