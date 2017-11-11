package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;

public class commandBug implements Command {
    private String bug = "";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        JDA jda = event.getJDA();
        User author = event.getAuthor();
        Message message = event.getMessage();
        message.delete().queue();

        User schlaubi = jda.getUserById("264048760580079616");
        PrivateChannel schlaubich = schlaubi.openPrivateChannel().complete();
        
        for(int i = 0; i< args.length; i++){
            bug += args[i] + " ";
        }
        schlaubich.sendMessage("The user" + author.getAsMention() + " has reported ```" + bug + "```").queue();
        embedSender.sendEmbed("Your bug was succesfully submited", event.getTextChannel(), Color.green);
        

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("bug", event);

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public CommandCategory category() {
        return CommandCategory.HIDDEN;
    }

    @Override
    public int permissionlevel() {
        return 0;
    }
}
