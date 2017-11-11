package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import com.rosaloves.bitlyj.Url;
import static  com.rosaloves.bitlyj.Bitly.*;

import net.schlaubi.schlaubibot.util.*;

import java.awt.*;

public class commandlmgtfy implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User auhtor = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        String prefix = MySQL.getValue(guild, "prefix");
        PrivateChannel privch = auhtor.openPrivateChannel().complete();
        channel.sendTyping().queue();
        message.delete().queue();

        if(args.length > 0){
            String query = "";
            for(int i = 0; i < args.length; i++){
                query += " " + args[i];
            }
            String url = "http://lmgtfy.com/?iie=1&q=" + query.replace( " ", "%20");
            Url bitlink = as(STATIC.BITLYUSERNAME, SECRETS.bitlytoken).call(shorten(url));

            embedSender.sendEmbed("Link created please send the following link to the person which needs help " + bitlink.getShortUrl(), channel, Color.green);
            embedSender.sendPermanentEmbed("Link created pleas send the following link to the person which needs help " + bitlink.getShortUrl(), privch, Color.green);


        } else {
            embedSender.sendEmbed("Usage: `" + prefix + "lmgtfy <querry>`", channel, Color.red);
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("lmgtfy", event);

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "Generates an help link for people who don't check how to use google";
    }

    @Override
    public String usage() {
        return "::lmgtfy <query>";
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
