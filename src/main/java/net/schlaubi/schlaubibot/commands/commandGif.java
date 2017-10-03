package net.schlaubi.schlaubibot.commands;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.search.SearchFeed;
import at.mukprojects.giphy4j.exception.GiphyException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.SECRETS;
import net.schlaubi.schlaubibot.util.commandLogger;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class commandGif implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        channel.sendTyping().queue();
        message.delete().queue();

        Message mymsg = channel.sendMessage(new EmbedBuilder().setDescription("Collecting data ...").setColor(Color.blue).build()).complete();
        Giphy giphy = new Giphy(SECRETS.giphykey);
        String arg = "";
        for (int i = 0; i < args.length; i++) {
            arg += args[i] + " ";
        }
        try {
            SearchFeed feed = giphy.search(arg, 1, 0);
            try {
                String gifurl = feed.getDataList().get(0).getImages().getOriginal().getUrl();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mymsg.delete().queue();
                        channel.sendMessage(gifurl).queue();
                    }
                },1000);
            } catch (IndexOutOfBoundsException e){
                mymsg.editMessage(new EmbedBuilder().setDescription("Sorry I can't find your gif .").setColor(Color.red).build()).queue();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        mymsg.delete().queue();
                    }
                }, 5000);
            }

        } catch (GiphyException e) {
            mymsg.editMessage(new EmbedBuilder().setDescription("Sorry I can't find your gif .").setColor(Color.red).build()).queue();
            e.printStackTrace();
        }


    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("gif", event);

    }

    @Override
    public String help() {
        return null;
    }
}
