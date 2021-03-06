package net.schlaubi.schlaubibot.commands;

import net.schlaubi.schlaubibot.core.permissionHandler;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;


import net.schlaubi.schlaubibot.util.MySQL;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

public class commandClear implements Command {

    private int getInt(String string){
        try {
            return Integer.parseInt(string);
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        final Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        String prefix = MySQL.getValue(guild, "prefix");

        channel.sendTyping().queue();
        message.delete().queue();

            if(args.length < 1){

                embedSender.sendEmbed("Usage: `" + prefix + "clear <count>`", channel, Color.red);

            } else {
                int numb = getInt(args[0]) +1;
                if(!(numb <=1 && numb >= 1000)){
                    try {

                        MessageHistory history = new MessageHistory(channel);
                        List<Message> msgs;



                        msgs = history.retrievePast(numb).complete();
                        event.getTextChannel().deleteMessages(msgs).queue();

                        int rawnumb = numb - 1;

                        embedSender.sendEmbed(":white_check_mark: Succesfully deleted `" + rawnumb + "` messages", channel, Color.green);

                    } catch (Exception e){
                       embedSender.sendEmbed(":warning: Sorry," + author.getAsMention() + "I had an error while deleting messages", channel, Color.red);


                    }

                } else {

                    embedSender.sendEmbed(":warning: Sorry," + author.getAsMention() + " please provide an number between 1 and 1000", channel, Color.red);


                }
            }

        }







    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        commandLogger.logCommand("clear", event);
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "Clears messages";
    }

    @Override
    public String usage() {
        return "::clear <count>";
    }

    @Override
    public CommandCategory category() {
        return CommandCategory.MODERATION;
    }

    @Override
    public int permissionlevel() {
        return 1;
    }


}
