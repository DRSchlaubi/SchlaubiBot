package listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildMemberLeaveListener extends ListenerAdapter {

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent e){
        Guild guild = e.getGuild();
        TextChannel channel = guild.getDefaultChannel();
        channel.sendTyping().queue();
        channel.sendMessage("God bye **" + e.getUser().getName() + "**! We had a nice time with you!").queue();
    }
}
