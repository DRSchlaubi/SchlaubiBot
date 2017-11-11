package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.commandLogger;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class commandUserInfo implements Command {
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
        channel.sendTyping().queue();
        message.delete().queue();


            Member member;
            if(args.length > 0){
                member = guild.getMember(message.getMentionedUsers().get(0));
            } else {
                member = guild.getMember(author);
            }
            String NAME = member.getEffectiveName();
            String TAG = member.getUser().getName() + "#" + member.getUser().getDiscriminator();
            String ID = member.getUser().getId();
            String STATUS = member.getOnlineStatus().getKey();
            String ROLES = "";
            String GAME;
            String AVATAR = member.getUser().getAvatarUrl();
            String GUILDDATE = member.getJoinDate().format(DateTimeFormatter.RFC_1123_DATE_TIME);
            String JOINDATE = member.getUser().getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME);

            try {
                 GAME = member.getGame().getName();
            } catch (Exception e){
                GAME = "-/-";
            }
            if(AVATAR == null){
                AVATAR = "No avatar";
            }

            for(Role role : member.getRoles()){
                ROLES += role.getName() + ", ";
            }
            if(ROLES.length() > 0){
                ROLES = ROLES.substring(0, ROLES.length()-2);
            } else {
                ROLES = "NO ROLES";
            }

            EmbedBuilder embed = new EmbedBuilder()
                    .setColor(Color.cyan)
                    .addField("Name/Nick", NAME, false)
                    .addField("User tag", TAG, false)
                    .addField("User id", ID , false)
                    .addField("Current status", STATUS, false)
                    .addField("Current game", GAME, false)
                    .addField("Guild joined", GUILDDATE, false)
                    .addField("Roles", ROLES, false)
                    .addField("Dicord joined", JOINDATE, false)
                    .addField("Avatar url", AVATAR, false);
            if(AVATAR != "No avatar"){
                embed.setThumbnail(AVATAR);
            }
            channel.sendMessage(embed.build()).queue();






    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("userinfo", event);

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "Returns some information about a user";
    }

    @Override
    public String usage() {
        return "::userinfo <@User>";
    }

    @Override
    public CommandCategory category() {
        return CommandCategory.TOOLS;
    }

    @Override
    public int permissionlevel() {
        return 0;
    }
}
