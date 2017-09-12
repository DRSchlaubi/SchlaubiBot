package commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import com.rosaloves.bitlyj.Url;
import static  com.rosaloves.bitlyj.Bitly.*;

import util.SECRETS;
import util.STATIC;
import util.embedSender;

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
        PrivateChannel privch = (PrivateChannel) auhtor.openPrivateChannel().complete();
        channel.sendTyping().queue();
        message.delete().queue();

        if(args.length > 0){
            String query = "";
            for(int i = 0; i < args.length; i++){
                query += " " + args[i];
            }
            String url = "http://lmgtfy.com/?iie=1&q=" + query.replace( " ", "%20");
            Url bitlink = as(STATIC.BITLYUSERNAME, SECRETS.bitlytoken).call(shorten(url));

            embedSender.sendEmbed("Link created pleas send the following link to the person how needs help " + bitlink.getShortUrl(), channel, Color.green);
            embedSender.sendPermanentEmbed("Link created pleas send the following link to the person how needs help " + bitlink.getShortUrl(), privch, Color.green);


        } else {
            embedSender.sendEmbed("Usage: `" + STATIC.prefix + "lmgtfy <querry>`", channel, Color.red);
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        System.out.println("[INFO] Command '" + STATIC.prefix + "lmgtfy' was executed by " + event.getAuthor().getName());


    }

    @Override
    public String help() {
        return null;
    }
}
