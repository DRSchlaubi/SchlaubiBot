package net.schlaubi.schlaubibot.listeners;


import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.schlaubi.schlaubibot.util.MySQL;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GuildLeaveListener extends ListenerAdapter{

    @Override
    public void onGuildLeave(GuildLeaveEvent event) {
        try {
            PreparedStatement ps1 = MySQL.connection.prepareStatement("DELETE FROM `servers` WHERE serverid = ?");
            ps1.setString(1, event.getGuild().getId());
            ps1.execute();

            PreparedStatement ps2 = MySQL.connection.prepareStatement("DELETE FROM `permissiones` WHERE serverid = ?");
            ps2.setString(1, event.getGuild().getId());
            ps2.execute();
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
}
