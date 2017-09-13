package commands;


import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import util.STATIC;

public class commandKMS implements Command{

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        message.delete().queue();
        channel.sendTyping().queue();

        channel.sendMessage("```\n __________\n |         |\n |         0 <-- " + author.getName() + "\n |        /|\\ \n |        / \\ \n |\n |``` \n Kill yourselve `" + author.getName() + "`\n http://ropestore.org/?u=" + author.getName()).queue();



    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        System.out.println("[INFO] Command '" + STATIC.prefix + "kms' was executed by " +  event.getAuthor().getName());

    }

    @Override
    public String help() {
        return null;
    }
}