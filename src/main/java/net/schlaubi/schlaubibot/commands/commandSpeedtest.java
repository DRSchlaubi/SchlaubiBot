package net.schlaubi.schlaubibot.commands;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.core.permissionHandler;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class commandSpeedtest implements Command {
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        User author = event.getAuthor();
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();

        Message mymsg = channel.sendMessage("Starting speedtest").complete();
        SpeedTestSocket download = new SpeedTestSocket();
        SpeedTestSocket upload = new SpeedTestSocket();
        StringBuilder string = new StringBuilder();

        mymsg.editMessage("**Speedtest running ...**").complete();

        download.addSpeedTestListener(new ISpeedTestListener() {
            @Override
            public void onCompletion(SpeedTestReport report) {
                string.append("Downstream: ```" + (Math.round(report.getTransferRateBit().floatValue()) / 1024 / 1024 + " MBit/s```\n"));
                mymsg.editMessage("Testing upstream with a 1MB file").queue();
                upload.startUpload("http://2.testdebit.info/", 1000000);
            }

            @Override
            public void onProgress(float v, SpeedTestReport report) {

            }

            @Override
            public void onError(SpeedTestError error, String s) {

                mymsg.editMessage("Sorry i had an error \n ```" + error.toString() + "```").queue();
            }
        });

        upload.addSpeedTestListener(new ISpeedTestListener() {
            @Override
            public void onCompletion(SpeedTestReport report) {
                string.append("Upstram:  ```" + (Math.round(report.getTransferRateBit().floatValue()) / 1024 / 1024 + " MBit/s```"));
                mymsg.editMessage(new EmbedBuilder().setColor(Color.cyan).setFooter("(c) 2017 Schlaubi | Schlaubibot", "https://cdn.discordapp.com/avatars/264048760580079616/4306cb8bcf063c3cbfa4998fc40080ec.png").setDescription("**Speed test finished** \n\n\n" + string.toString() + "").build()).queue();
            }

            @Override
            public void onProgress(float v, SpeedTestReport report) {

            }

            @Override
            public void onError(SpeedTestError error, String s) {

                mymsg.editMessage("Sorry i had an error \n ```" + error.toString() + "```").queue();
            }
        });

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mymsg.editMessage("Testing downstream with a 1MB file").queue();
                download.startDownload("http://2.testdebit.info/10M.iso");
            }
        }, 800);




    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

        commandLogger.logCommand("speedtest", event);

    }

    @Override
    public String help() {
        return null;
    }

    @Override
    public String description() {
        return "Runs a speedtest on the bot's server";
    }

    @Override
    public String usage() {
        return "::speedtes";
    }

    @Override
    public CommandCategory category() {
        return CommandCategory.BOTINFO;
    }

    @Override
    public int permissionlevel() {
        return 3;
    }
}
