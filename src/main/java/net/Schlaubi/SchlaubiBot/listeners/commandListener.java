package net.Schlaubi.SchlaubiBot.listeners;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.Schlaubi.SchlaubiBot.core.commandHandler;
import net.Schlaubi.SchlaubiBot.util.STATIC;

public class commandListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if (event.getMessage().getContent().startsWith(STATIC.prefix) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfUser().getId()) {
            commandHandler.handleCommand(commandHandler.parser.parse(event.getMessage().getContent(), event));
        }

    }

}