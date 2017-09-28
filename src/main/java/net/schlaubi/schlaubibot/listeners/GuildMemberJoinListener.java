package net.schlaubi.schlaubibot.listeners;


import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.schlaubi.schlaubibot.util.MySQL;

public class GuildMemberJoinListener extends ListenerAdapter {


    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e){
        Guild guild = e.getGuild();
        if(!MySQL.ifGuildExists(guild))
            MySQL.createServer(guild);

        String enabled = MySQL.getValue(guild, "joinmessages");
        String channelid = MySQL.getValue(guild, "joinmessagechannel");
        String joinmessage = MySQL.getValue(guild, "joinmessage").replace("%user%", e.getUser().getAsMention()).replace("%guild%", guild.getName());

        if(!enabled.equals("0")) {
            TextChannel channel = guild.getTextChannelById(channelid);
            channel.sendTyping().queue();
            channel.sendMessage(joinmessage).queue();
        }
    }
}
