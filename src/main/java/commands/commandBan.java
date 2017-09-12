package commands;

import core.permissionHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import util.STATIC;
import util.embedSender;

public class commandBan implements Command {
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
        Member member = guild.getMember(author);
        GuildController guildcon = new GuildController(event.getGuild());
        Member selfmember = guild.getSelfMember();
        
        channel.sendTyping().queue();
        message.delete().queue();

        if(permissionHandler.check(event)){

            embedSender.sendEmbed("Sorry, " + author.getAsMention() + " but you don't have the permission to perform that command!", channel, Color.red);
            return;
        }
            if(args.length >0){
                List<User> mentioned = message.getMentionedUsers();
                for (User users : mentioned){
                    Member members = guild.getMember(users);
                    if(selfmember.canInteract(members)){
                        guildcon.ban(members, 7).queue();
                        embedSender.sendEmbed(":white_check_mark: Succesfully banned " + users.getAsMention(), channel, Color.green);

                    } else {
                        embedSender.sendEmbed(":warning: Can't ban " + users.getAsMention() + " he's an admin!", channel, Color.red);
                    }
                }
                
            } else {
                embedSender.sendEmbed("Usage `" + STATIC.prefix + "ban <@User>`", channel, Color.red);
            }
            
        }


    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        System.out.println("[INFO] Command '" + STATIC.prefix + "ban' was executed by " + event.getAuthor().getName());

    }

    @Override
    public String help() {
        return null;
    }
}
