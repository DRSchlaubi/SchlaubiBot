package net.schlaubi.schlaubibot.listeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.schlaubi.schlaubibot.core.commandHandler;
import net.schlaubi.schlaubibot.util.MySQL;


public class commandListener extends ListenerAdapter {



    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(!MySQL.ifGuildExists(event.getGuild())){
            MySQL.createServer(event.getGuild());
        }
        String prefix = MySQL.getValue(event.getGuild(), "prefix");
        if (event.getMessage().getContent().startsWith(prefix) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfUser().getId()) {
            commandHandler.handleCommand(commandHandler.parser.parse(event.getMessage().getContent(), event));
        }

    }

}