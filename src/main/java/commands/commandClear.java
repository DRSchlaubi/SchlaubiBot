package commands;

import core.permissionHandler;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;


import util.STATIC;
import util.embedSender;

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

        channel.sendTyping().queue();
        message.delete().queue();



        if(permissionHandler.check(event)){

            embedSender.sendEmbed("Sorry, " + author.getAsMention() + " but you don't have the permission to perform that command!", channel, Color.red);

            return;
        }
            if(args.length < 1){

                embedSender.sendEmbed("Usage: `" + STATIC.prefix + "clear <count>`", channel, Color.red);

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
        System.out.println("[INFO] Command '" + STATIC.prefix + " clear' was executed by " + event.getAuthor().getName());
    }

    @Override
    public String help() {
        return null;
    }


}
