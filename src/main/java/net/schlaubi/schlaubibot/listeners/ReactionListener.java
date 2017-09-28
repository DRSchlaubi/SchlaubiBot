package net.schlaubi.schlaubibot.listeners;

import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.schlaubi.schlaubibot.commands.commandVote;

import java.io.IOException;

public class ReactionListener extends ListenerAdapter{

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {

        try {
            commandVote.reactVote(event.getReaction().getEmote().getName().replace(":", ""),event);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
    }
}
