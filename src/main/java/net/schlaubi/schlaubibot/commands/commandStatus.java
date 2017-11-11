package net.schlaubi.schlaubibot.commands;

import net.schlaubi.schlaubibot.core.permissionHandler;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;

public class commandStatus implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        MessageChannel channel = event.getChannel();
        Message message = event.getMessage();
        JDA jda = event.getJDA();
        channel.sendTyping().queue();
        message.delete().queue();



        if(args.length > 0){
            if(args[0].equals("reset")){
                jda.getPresence().setGame(Game.of(STATIC.game));
                embedSender.sendEmbed(":repeat: Succesfully restored default status", channel, Color.green);
                return;
            }
                String status = "";
                for (int i = 0; i < args.length; i++) {
                    status += " " + args[i];
                }
                jda.getPresence().setGame(Game.of(status));
                embedSender.sendEmbed(":white_check_mark: Successfully set Status to `" + status + "`", channel, Color.green);

        } else {
            embedSender.sendEmbed("Usage `" + STATIC.prefix + "status <status>`", channel, Color.RED);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("status", event);
}

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "Changes the game of the bot";
    }

    @Override
    public String usage() {
        return "::status <name>";
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
