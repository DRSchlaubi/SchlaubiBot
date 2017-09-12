package listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildMemberJoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e){
        Guild guild = e.getGuild();
        TextChannel channel = guild.getDefaultChannel();
        channel.sendTyping().queue();
        channel.sendMessage("Welcome " + e.getUser().getAsMention() + ", on **" + guild.getName() + "**").queue();
    }
}
