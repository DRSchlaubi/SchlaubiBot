package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;
import net.schlaubi.schlaubibot.core.permissionHandler;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;

public class commandRemoverole implements Command {
    private String query = "";

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
        String prefix = STATIC.prefix;
        GuildController gcon = new GuildController(guild);
        channel.sendTyping().queue();
        message.delete().queue();

        if(args.length >= 2){
            if(message.getMentionedUsers().size() == 0){
                embedSender.sendEmbed("Usage: `" + prefix + "addrole <@User> <role>`", channel, Color.red);
                return;
            }

            Member member = guild.getMember(message.getMentionedUsers().get(0));
            try {
                StringBuilder query = new StringBuilder();
                for(int i = 1; i < args.length; i++){
                    query.append(args[i]).append(" ");
                }
                String search = query.toString();
                Role role = guild.getRolesByName(search.replaceFirst(" ", ""), true).get(0);
                if (!member.getRoles().contains(role)) {
                    embedSender.sendEmbed(":warning: This user don't has this role", channel, Color.red);
                    return;
                }
                gcon.removeRolesFromMember(member, role).queue();
                embedSender.sendEmbed(":white_check_mark: Succesfully removed role `" + search.replaceFirst(" ", "") + "` from " + member.getAsMention(), channel, Color.green);
            } catch (IndexOutOfBoundsException e){
                embedSender.sendEmbed(":warning: Sorry but this role don't exists", channel, Color.red);
            }
        } else {
            embedSender.sendEmbed("Usage: `" + prefix + "removerole <@User> <role>`", channel, Color.red);
        }

    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        commandLogger.logCommand("removerole", event);
    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "Removes a role from a member";
    }

    @Override
    public String usage() {
        return "::removerole <@User> <role>";
    }

    @Override
    public CommandCategory category() {
        return CommandCategory.TOOLS;
    }

    @Override
    public int permissionlevel() {
        return 2;
    }
}
