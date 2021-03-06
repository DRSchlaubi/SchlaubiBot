package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.commandLogger;

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
        PrivateChannel privch = author.openPrivateChannel().complete();
        event.getChannel().sendTyping().queue();
        privch.sendTyping().queue();
        privch.sendMessage("``` SchlaubiBot Version" + STATIC.VERSION + " by Schlaubi``` Github: https://github.com/DRSchlaubi/SchlaubiBot/").queue();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                event.getMessage().delete().queue();
            }
        },2000);


    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("version", event);

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "Returns bot's curent version";
    }

    @Override
    public String usage() {
        return "::version";
    }

    @Override
    public CommandCategory category() {
        return CommandCategory.BOTINFO;
    }

    @Override
    public int permissionlevel() {
        return 0;
    }
}
