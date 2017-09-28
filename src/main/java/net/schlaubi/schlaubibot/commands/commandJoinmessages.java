package net.schlaubi.schlaubibot.commands;


import net.dv8tion.jda.core.entities.*;
import net.schlaubi.schlaubibot.core.permissionHandler;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.MySQL;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;

public class commandJoinmessages implements Command{


    private String joinmessage;
    private String leavemessage;

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        User author = event.getAuthor();
        MessageChannel channel = event.getChannel();
        Message message = event.getMessage();
        channel.sendTyping().queue();
        message.delete().queue();

        if(!(args.length > 0)){
            embedSender.sendEmbed(help(), channel, Color.red);
            return;
        }

        if(permissionHandler.check(event)){

            embedSender.sendEmbed("Sorry, " + author.getAsMention() + " but you don't have the permission to perform that command!", channel, Color.red);
            return;

        }

        if(!MySQL.ifGuildExists(guild))
            MySQL.createServer(guild);

       switch (args[0]){
           case "toggle":

               String enabled = MySQL.getValue(guild, "joinmessages");

                if(enabled.equals("1")){
                   MySQL.updateValue(guild, "joinmessages", "0");
                   embedSender.sendEmbed(":white_check_mark: Succesfully disabled joinmessages!", channel, Color.green);
               } else if(enabled.equals("0")){
                   MySQL.updateValue(guild, "joinmessages", "1");
                   embedSender.sendEmbed(":white_check_mark: Succesfully enabled joinmessages!", channel, Color.green);
               }
               break;

           case "channel":
               if(!(message.getMentionedChannels().size() > 0)){
                   embedSender.sendEmbed("Usage : `" + STATIC.prefix + "joinmessages channel <#Channel>`", channel, Color.red);
                   return;
               }

               String channelid = message.getMentionedChannels().get(0).getId();
               MySQL.updateValue(guild,"joinmessagechannel", channelid);
               embedSender.sendEmbed(":white_check_mark: Succesfully set joinmessagechannel to " + message.getMentionedChannels().get(0).getAsMention(), channel, Color.green);
                break;

           case "join":
               if(!(args.length > 0)){
                   embedSender.sendEmbed("Usage : `" + STATIC.prefix + "joinmessages join <Hello %user% on %guild%>`", channel, Color.red);
                   return;
               }

               for(int i = 1; i < args.length; i++){
                   joinmessage += " " + args[i];
               }
               MySQL.updateValue(guild, "joinmessage", joinmessage.replaceFirst("null ", ""));
               embedSender.sendEmbed(":white_check_mark:  Successfully set joinmessage to `" + joinmessage.replaceFirst("null ", "") + "`!", channel, Color.green);
               break;
           case "leave":
               if(!(args.length > 0)){
                   embedSender.sendEmbed("Usage : `" + STATIC.prefix + "joinmessages leave <Bye %user%>`", channel, Color.red);
                   return;
               }

               for(int i = 1; i < args.length; i++){
                   leavemessage += " " + args[i];
               }
               MySQL.updateValue(guild, "leavemessage", leavemessage.replaceFirst("null ", ""));
               embedSender.sendEmbed(":white_check_mark:  Successfully set leavemessage to `" + leavemessage.replaceFirst("null ", "") + "`!", channel, Color.green);

       }







    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {

        return "USAGE: \n" +
                "`  " + STATIC.prefix + "joinmessages toggle`  -  Toggles joinmesasges\n" +
                "`  " + STATIC.prefix + "joinmessages join  <Hello %user% on %guild%>`  -  Sets join message\n" +
                "`  " + STATIC.prefix + "joinmessages leave `<Godbye %user%> -  Sets leave message\n" +
                "`  " + STATIC.prefix + "joinmessages channel <#Channel>` - Sets message channel"
                ;
    }
}
