package net.schlaubi.schlaubibot.commands;

import at.mukprojects.giphy4j.Giphy;
import at.mukprojects.giphy4j.entity.search.SearchFeed;
import at.mukprojects.giphy4j.exception.GiphyException;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.MySQL;
import net.schlaubi.schlaubibot.util.SECRETS;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class commandGif implements Command{
    private String prefix;

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        channel.sendTyping().queue();
        message.delete().queue();
        this.prefix = MySQL.getValue(guild, "prefix");

        Message mymsg = channel.sendMessage(new EmbedBuilder().setDescription("Collecting data ...").setColor(Color.blue).build()).complete();
        Giphy giphy = new Giphy(SECRETS.giphykey);
        String arg = "";
        if(!(args.length >0)){
            embedSender.sendEmbed("Usage: ```" + prefix + "gif <query>```", channel, Color.red);
        }
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

    @Override
    public String description() {
        return "Find your favourite gif";
    }

    @Override
    public String usage() {
        return "::gif <query>";
    }

    @Override
    public CommandCategory category() {
        return CommandCategory.FUN;
    }

    @Override
    public int permissionlevel() {
        return 0;
    }
}
