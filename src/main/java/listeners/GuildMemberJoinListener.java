package listeners;


import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.File;

public class GuildMemberJoinListener extends ListenerAdapter {


    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e){
        Guild guild = e.getGuild();
        File file = new File("SERVER_SETTINGS/" + guild.getId() + "/disabledjoinmessages.dat");
        if(!(file.exists())) {
            TextChannel channel = guild.getDefaultChannel();
            channel.sendTyping().queue();
            channel.sendMessage("Welcome " + e.getUser().getAsMention() + ", on **" + guild.getName() + "**").queue();
        }
    }
}
