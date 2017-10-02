package net.schlaubi.schlaubibot.commands;

import com.rosaloves.bitlyj.BitlyException;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import com.rosaloves.bitlyj.Url;
import static com.rosaloves.bitlyj.Bitly.*;

import net.schlaubi.schlaubibot.util.*;

import java.awt.*;

public class commandShort implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        Guild guild = event.getGuild();
        String prefix = MySQL.getValue(guild, "prefix");
        channel.sendTyping().queue();
        message.delete().queue();


        if(args.length > 0){
            try {
                PrivateChannel privch = author.openPrivateChannel().complete();
                Url bitlink = as(STATIC.BITLYUSERNAME, SECRETS.bitlytoken).call(shorten(args[0]));
                embedSender.sendEmbed(":white_check_mark: Successfully created shortlink " + bitlink.getShortUrl(), channel, Color.green);
                privch.sendTyping().queue();
                embedSender.sendPermanentEmbed(":white_check_mark: You have successfully shortened the link `" + bitlink.getLongUrl() + "` \n to " + bitlink.getShortUrl(), privch, Color.green);
            } catch (BitlyException e){
                embedSender.sendEmbed(":warning: Please send an valid URL", channel, Color.red);
            }
        } else {
            embedSender.sendEmbed("Usage: `" + prefix + "short <longurl>`", channel, Color.red);
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("short", event);

    }

    

    @Override
    public String help() {
        return null;
    }
}
