package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;

public class commandSearch implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        User author = event.getAuthor();
        Message message = event.getMessage();
        TextChannel channel = event.getTextChannel();
        channel.sendTyping().queue();
        message.delete().queue();
        Guild guild = event.getGuild();

        if(args.length > 0) {

            StringBuilder query = new StringBuilder();
            for(int i = 0; i < args.length; i++){
                query.append(args[i]);
            }

            StringBuilder textchannels = new StringBuilder();
            StringBuilder voicechannels = new StringBuilder();
            StringBuilder members = new StringBuilder();
            StringBuilder roles = new StringBuilder();

            Message mymsg = channel.sendMessage(new EmbedBuilder().setColor(Color.cyan).setDescription("Collecting textchannels ...").build()).complete();

            guild.getTextChannels().forEach(i -> {
                if(i.getName().toLowerCase().contains(query.toString().toLowerCase()))
                    textchannels.append(i.getName() + "(`" + i.getId() + "`)").append("\n");
            });

            mymsg.editMessage(new EmbedBuilder().setColor(Color.cyan).setDescription("Collecting voicechannels ...").build()).queue();


            guild.getVoiceChannels().forEach(i -> {
                if(i.getName().toLowerCase().contains(query.toString().toLowerCase()))
                    voicechannels.append(i.getName() + "(`" + i.getId() + "`)").append("\n");
            });

            mymsg.editMessage(new EmbedBuilder().setColor(Color.cyan).setDescription("Collecting users ...").build()).queue();


            guild.getMembers().forEach(i -> {
                if(i.getUser().getName().toLowerCase().contains(query.toString().toLowerCase()) || i.getEffectiveName().toLowerCase().contains(query.toString().toLowerCase()))
                    members.append(i.getUser().getName() + "(`" + i.getUser().getId() + "`)").append("\n");
            });

            mymsg.editMessage(new EmbedBuilder().setColor(Color.cyan).setDescription("Collecting roles ...").build()).queue();

                        guild.getRoles().forEach(i -> {
                if(i.getName().toLowerCase().contains(query.toString().toLowerCase()))
                    roles.append(i.getName() + "(`" + i.getId() + "`)").append("\n");
            });
            try {
                EmbedBuilder results = new EmbedBuilder()
                        .setColor(Color.green)
                        .addField("**Textchannels**", textchannels.toString(), false)
                        .addField("**Voicechannles**", voicechannels.toString(), false)
                        .addField("**Members**", members.toString(), false)
                        .addField("**Roles**", roles.toString(), false);
                mymsg.editMessage(results.build()).queue();
            } catch (IllegalArgumentException e){
                mymsg.editMessage(new EmbedBuilder().setDescription(":warning: TO MANY RESULT HEEEEEELP!").build()).queue();
            }
        } else {
            embedSender.sendEmbed("Usage: `" + usage(), channel, Color.red);
        }



    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("search", event);

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "Searches for users, channels and roles";
    }

    @Override
    public String usage() {
        return "::search <query>";
    }

    @Override
    public CommandCategory category() {
        return CommandCategory.TOOLS;
    }

    @Override
    public int permissionlevel() {
        return 0;
    }
}
