package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.core.permissionHandler;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;
import java.io.IOException;

public class commandRestart implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        message.delete().queue();
        channel.sendTyping().queue();

            try {
            if(System.getProperty("os.name").toLowerCase().contains("linux"))
                Runtime.getRuntime().exec("sh /root/SchlaubiBot/start.sh");
            else
                Runtime.getRuntime().exec("java -jar SchlaubiBot-1.0-SNAPSHOT.jar");
            } catch (IOException e){
                e.printStackTrace();
            }
        embedSender.sendEmbed(":repeat: Restarting :^) ...", channel, Color.green);
        System.exit(0);
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("restart", event);

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "Restarts the Bot";
    }

    @Override
    public String usage() {
        return "::restart";
    }

    @Override
    public CommandCategory category() {
        return CommandCategory.BOTINFO;
    }

    @Override
    public int permissionlevel() {
        return 3;
    }
}
