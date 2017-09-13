package commands;

import core.permissionHandler;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;
import util.embedSender;

import java.awt.*;
import java.util.List;

public class commandMute implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        Guild guild = event.getGuild();
        TextChannel channel = event.getTextChannel();
        Message message = event.getMessage();
        channel.sendTyping().queue();
        message.delete().queue();
        if(permissionHandler.check(event)){

            embedSender.sendEmbed("Sorry, " + author.getAsMention() + " but you don't have the permission to perform that command!", channel, Color.red);
            return;
        }


        if(args.length > 0){
            List<User> mentioned = message.getMentionedUsers();
            for(User user : mentioned) {
                Member member = guild.getMember(user);
                if(channel.getPermissionOverride(member) == null) {
                    channel.createPermissionOverride(member).complete();
                }
                    Member selfmember = guild.getSelfMember();
                    if(selfmember.canInteract(member)) {
                        if (!channel.getPermissionOverride(member).getDenied().contains(Permission.MESSAGE_WRITE)) {
                            channel.getPermissionOverride(member).getManager().deny(Permission.MESSAGE_WRITE).complete();
                            embedSender.sendEmbed(":white_check_mark: Succesfully muted " + user.getAsMention(), channel, Color.green);
                        } else {
                            embedSender.sendEmbed(":warning: This user is already muted use `" + STATIC.prefix + "unmute` to unmute", channel, Color.red);
                        }
                    } else {
                        embedSender.sendEmbed(":warning: Can't mute" + user.getAsMention() + " he's an admin!", channel, Color.red);
                    }

            }

        } else {

                embedSender.sendEmbed("Usage: `" + STATIC.prefix + "mute <@User>` ", channel, Color.red);
            }
        }


    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        System.out.println("[INFO] Command '" + STATIC.prefix + "mute was executed by " + event.getAuthor().getName());

    }

    @Override
    public String help() {
        return null;
    }
}
