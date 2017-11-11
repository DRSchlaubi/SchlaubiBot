package net.schlaubi.schlaubibot.listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.schlaubi.schlaubibot.util.MySQL;

public class GuildMemberLeaveListener extends ListenerAdapter {


    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent e){
        Guild guild = e.getGuild();
        if(!MySQL.ifGuildExists(guild))
            MySQL.createServer(guild);

        String enabled = MySQL.getValue(guild, "joinmessages");
        String channelid = MySQL.getValue(guild, "joinmessagechannel");
        String leavemessage = MySQL.getValue(guild, "leavemessage").replace("%user%", e.getUser().getName());

        if(!enabled.equals("0")) {
            TextChannel channel = guild.getTextChannelById(channelid);
            try {
                channel.sendTyping().queue();
                channel.sendMessage(leavemessage).queue();
            } catch (InsufficientPermissionException ex){
                System.out.println("Could not send message to user" + ex.getCause());
            }
        }
    }
}
