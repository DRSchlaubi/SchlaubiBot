package listeners;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.File;

public class GuildMemberLeaveListener extends ListenerAdapter {


    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent e){
        Guild guild = e.getGuild();
        File file = new File("SERVER_SETTINGS/" + guild.getId() + "/disabledjoinmessages.dat");
        if(!(file.exists())) {
            TextChannel channel = guild.getDefaultChannel();
            channel.sendTyping().queue();
            channel.sendMessage("Good bye **" + e.getUser().getName() + "**! We had a nice time with you!").queue();
        }
    }
}
