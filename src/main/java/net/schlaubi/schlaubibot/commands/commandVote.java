package net.schlaubi.schlaubibot.commands;


import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.schlaubi.schlaubibot.core.permissionHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.commandLogger;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class commandVote implements Command, Serializable {

    private static TextChannel channel;

    public static HashMap<Guild, Poll> polls = new HashMap<>();

    private static final String[] EMOTI = ( "\uD83C\uDF4F \uD83C\uDF4E \uD83C\uDF50 \uD83C\uDF4A \uD83C\uDF4B \uD83C\uDF4C \uD83C\uDF49 \uD83C\uDF47 \uD83C\uDF53 \uD83C\uDF48 \uD83C\uDF52 \uD83C\uDF51 \uD83C\uDF4D \uD83E\uDD5D " +
            "\uD83E\uDD51 \uD83C\uDF45 \uD83C\uDF46 \uD83E\uDD52 \uD83E\uDD55 \uD83C\uDF3D \uD83C\uDF36 \uD83E\uDD54 \uD83C\uDF60 \uD83C\uDF30 \uD83E\uDD5C \uD83C\uDF6F \uD83E\uDD50 \uD83C\uDF5E " +
            "\uD83E\uDD56 \uD83E\uDDC0 \uD83E\uDD5A \uD83C\uDF73 \uD83E\uDD53 \uD83E\uDD5E \uD83C\uDF64 \uD83C\uDF57 \uD83C\uDF56 \uD83C\uDF55 \uD83C\uDF2D \uD83C\uDF54 \uD83C\uDF5F \uD83E\uDD59 " +
            "\uD83C\uDF2E \uD83C\uDF2F \uD83E\uDD57 \uD83E\uDD58 \uD83C\uDF5D \uD83C\uDF5C \uD83C\uDF72 \uD83C\uDF65 \uD83C\uDF63 \uD83C\uDF71 \uD83C\uDF5B \uD83C\uDF5A \uD83C\uDF59 \uD83C\uDF58 " +
            "\uD83C\uDF62 \uD83C\uDF61 \uD83C\uDF67 \uD83C\uDF68 \uD83C\uDF66 \uD83C\uDF70 \uD83C\uDF82 \uD83C\uDF6E \uD83C\uDF6D \uD83C\uDF6C \uD83C\uDF6B \uD83C\uDF7F \uD83C\uDF69 \uD83C\uDF6A \uD83E\uDD5B " +
            "\uD83C\uDF75 \uD83C\uDF76 \uD83C\uDF7A \uD83C\uDF7B \uD83E\uDD42 \uD83C\uDF77 \uD83E\uDD43 \uD83C\uDF78 \uD83C\uDF79 \uD83C\uDF7E \uD83E\uDD44 \uD83C\uDF74 \uD83C\uDF7D").split(" ");

    private List<String> toAddEmojis = new ArrayList<>();

    private class Poll implements Serializable{

        private String creator;
        private String heading;
        private List<String> answers;
        private String pollmsg;
        private HashMap<String, Integer> votes;

        private Poll(Member creator, String heading, List<String> answers, String pollmsg) {
            this.creator = creator.getUser().getId();
            this.heading = heading;
            this.answers = answers;
            this.pollmsg = pollmsg;
            this.votes = new HashMap<>();
        }

        private Member getCreator(Guild guild) {
            return guild.getMember(guild.getJDA().getUserById(creator));
        }


    }


    private static void message(String content, Color color){
        EmbedBuilder eb = new EmbedBuilder().setDescription(content).setColor(color);
        Message mymsg = channel.sendMessage(eb.build()).complete();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mymsg.delete().queue();
            }
        }, 5000);
    }

    private static void privateMessage(String content, Color color, MessageReceivedEvent event){
        User author = event.getAuthor();
        EmbedBuilder eb = new EmbedBuilder().setDescription(content).setColor(color);
        PrivateChannel privch = author.openPrivateChannel().complete();
        privch.sendTyping().queue();
        privch.sendMessage(eb.build()).queue();



    }

    private static EmbedBuilder getParsedPoll(Poll poll, Guild guild){

         StringBuilder ansSTR = new StringBuilder();
         final AtomicInteger count = new AtomicInteger();

         poll.answers.forEach(s ->{
             long votescount = poll.votes.keySet().stream().filter(k -> poll.votes.get(k).equals(count.get() + 1)).count();
             ansSTR.append(EMOTI[count.get()] + " - " + (count.get() + 1) + "  -  " + s + "  -  Votes: `" + votescount + "` \n");
             count.addAndGet(1);
         });

        return new EmbedBuilder()
                .setAuthor(poll.getCreator(guild).getEffectiveName() + "'s poll", null, poll.getCreator(guild).getUser().getAvatarUrl())
                .setDescription(":pencil:   " + poll.heading + "\n\n" + ansSTR.toString())
                .setFooter("Enter: '" + STATIC.prefix + "vote v <number>' or react to vote", null)
                .setColor(Color.CYAN);

    }




    private void createPoll(String[] args, MessageReceivedEvent event){

        Message message = event.getMessage();
        User author = event.getAuthor();
        if(permissionHandler.check(event)){

            if(permissionHandler.check(event)){
                embedSender.sendEmbed("Sorry, " + author.getAsMention() + " but you don't have the permission to perform that command!", channel, Color.red);
                return;
            }
            return;
        }

            String argsSTRG = String.join(" ", new ArrayList<>(Arrays.asList(args).subList(1, args.length)));
            List<String> content = Arrays.asList(argsSTRG.split("\\|"));
            String heading = content.get(0);
            List<String> answers = new ArrayList<>(content.subList(1, content.size()));


            Message pollmessage = channel.sendMessage(new EmbedBuilder().setDescription("Creating poll...").setColor(Color.cyan).build()).complete();
            String pollmsg = pollmessage.getId();

            Poll poll = new Poll(event.getMember(), heading, answers, pollmsg);
            polls.put(event.getGuild(), poll);

            List<String> emotis = new ArrayList<>(Arrays.asList(EMOTI));
            answers.forEach(a -> {
                   toAddEmojis.add(emotis.get(0));
                   emotis.remove(0);


            });
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    pollmessage.editMessage(getParsedPoll(poll, event.getGuild()).build()).queue();
                    toAddEmojis.forEach((String s) -> pollmessage.addReaction(s).queue());
                }
            }, 1500);
    }






    private void votePoll(String[] args, MessageReceivedEvent event){

        if(!polls.containsKey(event.getGuild())){
            message("There is currently no poll running on this guild", Color.red);
            return;
        }

        Poll poll = polls.get(event.getGuild());

        int vote;
        try{
            vote = Integer.parseInt(args[1]);
            if(vote > poll.answers.size()){
                throw new Exception();
            }
        } catch (Exception e){
           message(":warning: You entered an wrong answer!", Color.red);
           return;
        }

        if(poll.votes.containsKey(event.getAuthor().getId())){
            message("Sorry, but you can only vote at once for a poll", Color.red);
            return;
        }

        poll.votes.put(event.getAuthor().getId(), vote);
        polls.replace(event.getGuild(), poll);
        privateMessage("You have successfully voted for option `" + args[1] + "`", new Color(0x3AD70E), event);
        Message pollmsg =  channel.getMessageById(String.valueOf(poll.pollmsg)).complete();
        pollmsg.editMessage(getParsedPoll(poll, event.getGuild()).build()).queue();
    }

    public static void reactVote(String reaction, MessageReactionAddEvent event) throws IOException, ClassNotFoundException {
        if(event.getUser().isBot())
            return;
        if(!polls.containsKey(event.getGuild()))
            return;

        Poll poll = polls.get(event.getGuild());

        if(!poll.pollmsg.equals(event.getMessageId()))
            return;

        if(poll.votes.containsKey(event.getUser().getId())){
            embedSender.sendEmbed("Sorry, but you can only vote at once for a poll",event.getChannel(), Color.red);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    event.getReaction().removeReaction(event.getUser()).queue();
                }
            }, 1000);
            return;
        }



        PrivateChannel privch = event.getUser().openPrivateChannel().complete();

        String emoji = event.getReaction().getEmote().getName();
        switch (emoji) {
            case "\uD83C\uDF4F":
                poll.votes.put(event.getUser().getId(), 1);
                polls.replace(event.getGuild(), poll);
                embedSender.sendPermanentEmbed("You have succesfully voted for option `1`", privch, new Color(0x3AD70E));
                break;
            case "\uD83C\uDF4E":
                poll.votes.put(event.getUser().getId(), 2);
                polls.replace(event.getGuild(), poll);
                embedSender.sendPermanentEmbed("You have succesfully voted for option `2`", privch, new Color(0x3AD70E));
                break;
            case "\uD83C\uDF50":
                if (poll.answers.size() < 3)
                    return;
                poll.votes.put(event.getUser().getId(), 3);
                polls.replace(event.getGuild(), poll);
                embedSender.sendPermanentEmbed("You have succesfully voted for option `3`", privch, new Color(0x3AD70E));
                break;
            case "\uD83C\uDF4A":
                if (poll.answers.size() < 4)
                    return;
                poll.votes.put(event.getUser().getId(), 4);
                polls.replace(event.getGuild(), poll);
                embedSender.sendPermanentEmbed("You have succesfully voted for option `4`", privch, new Color(0x3AD70E));
                break;
            case "\uD83C\uDF4B":
                if (poll.answers.size() < 5)
                    return;
                poll.votes.put(event.getUser().getId(), 5);
                polls.replace(event.getGuild(), poll);
                embedSender.sendPermanentEmbed("You have succesfully voted for option `5`", privch, new Color(0x3AD70E));
                break;
            case "\uD83C\uDF4C":
                if (poll.answers.size() < 6)
                    return;
                poll.votes.put(event.getUser().getId(), 6);
                polls.replace(event.getGuild(), poll);
                embedSender.sendPermanentEmbed("You have succesfully voted for option `6`", privch, new Color(0x3AD70E));
                break;
            case "\uD83C\uDF49":
                if (poll.answers.size() < 7)
                    return;
                poll.votes.put(event.getUser().getId(), 7);
                polls.replace(event.getGuild(), poll);
                embedSender.sendPermanentEmbed("You have succesfully voted for option `7`", privch, new Color(0x3AD70E));
                break;
            case "\uD83C\uDF47":
                if (poll.answers.size() < 8)
                    return;
                poll.votes.put(event.getUser().getId(), 8);
                polls.replace(event.getGuild(), poll);
                embedSender.sendPermanentEmbed("You have succesfully voted for option `8`", privch, new Color(0x3AD70E));
                break;
            case "\uD83C\uDF53":
                if (poll.answers.size() < 9)
                    return;
                poll.votes.put(event.getUser().getId(), 9);
                polls.replace(event.getGuild(), poll);
                embedSender.sendPermanentEmbed("You have succesfully voted for option `9`", privch, new Color(0x3AD70E));
                break;
            case "\uD83C\uDF48":
                if (poll.answers.size() < 10)
                    return;
                poll.votes.put(event.getUser().getId(), 10);
                polls.replace(event.getGuild(), poll);
                embedSender.sendPermanentEmbed("You have succesfully voted for option `10`", privch, new Color(0x3AD70E));

                break;
        }
        Message pollmsg =  event.getChannel().getMessageById(String.valueOf(poll.pollmsg)).complete();
        pollmsg.editMessage(getParsedPoll(poll, event.getGuild()).build()).queue();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                event.getReaction().removeReaction(event.getUser()).queue();
            }
        }, 1000);



    }

    private void voteStats(MessageReceivedEvent event){
        if(!polls.containsKey(event.getGuild())){
            message("There is currently no poll running on this guild", Color.red);
            return;
        }

        channel.sendMessage(getParsedPoll(polls.get(event.getGuild()), event.getGuild()).build()).queue();

    }

    private void closeVote(MessageReceivedEvent event){
        Message message = event.getMessage();
        User author = event.getAuthor();
        if(!polls.containsKey(event.getGuild())){
            message("There is currently no poll running on this guild", Color.red);
            return;
        }

        Poll poll = polls.get(event.getGuild());

        if(permissionHandler.check(event)){

            if(permissionHandler.check(event)){

                embedSender.sendEmbed("Sorry, " + author.getAsMention() + " but you don't have the permission to perform that command!", channel, Color.red);
                return;
            }
            return;
        }

        polls.remove(event.getGuild());
        channel.sendMessage(getParsedPoll(poll, event.getGuild()).build()).queue();
        message(":white_check_mark: Poll was closed by" + event.getAuthor().getAsMention(), new Color(0x3AD70E));
        Message pollmsg = channel.getMessageById(String.valueOf(poll.pollmsg)).complete();
        try {
            pollmsg.delete().queue();
        } catch (ErrorResponseException e){
            //This is an empty Catch Block
        }


    }


    private void savePoll(Guild guild) throws IOException {
        if(!polls.containsKey(guild)){
            return;
        }

        String saveFile = "SERVER_SETTINGS/" + guild.getId() + "/vote.dat";
        Poll poll = polls.get(guild);

        FileOutputStream fos = new FileOutputStream(saveFile);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(poll);
        oos.close();
    }

    private static Poll getPoll(Guild guild) throws IOException, ClassNotFoundException {
        if(polls.containsKey(guild))
            return null;

        String saveFile = "SERVER_SETTINGS/" + guild.getId() + "/vote.dat";
        FileInputStream fis = new FileInputStream(saveFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Poll out = (Poll) ois.readObject();
        ois.close();
        return out;


    }

    public static void loadPolls(JDA jda){
        jda.getGuilds().forEach(g ->{

            File f = new File("SERVER_SETTINGS/" + g.getId() + "/vote.dat");
            if(f.exists())
                try {
                    polls.put(g, getPoll(g));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

        });
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        channel = event.getTextChannel();
        event.getMessage().delete().queue();
        channel.sendTyping().queue();

        if (args.length < 1) {
            message(help(), Color.red);
        } else {
            switch (args[0].toLowerCase()) {
                case "create":
                    createPoll(args, event);
                    break;
                case "v":
                    votePoll(args, event);
                    break;
                case "stats":
                    voteStats(event);
                    break;
                case "close":
                    closeVote(event);
                    break;

            }

            polls.forEach((guild, poll) -> {
                File path = new File("SERVER_SETTINGS/" + guild.getId() + "/");
            if(!path.exists())
                path.mkdirs();
                try {
                    savePoll(guild);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }


    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        commandLogger.logCommand("vote", event);

    }

    @Override
    public String help() {
        return
                "USAGE: \n" +
                        "`  " + STATIC.prefix + "vote create <Title>|<Option1>|<Option2>|...  `  -  create a vote\n" +
                        "`  " + STATIC.prefix + "vote vote <index of Option>  `  -  vote for a possibility\n" +
                        "`  " + STATIC.prefix + "vote stats  `  -  get stats of a current vote\n" +
                        "`  " + STATIC.prefix + "vote close  `  -  close a current vote"
                ;
    }

    @Override
    public String description() {
        return "Vote System";
    }

    @Override
    public String usage() {
        return "::vote <subcommand> <args>";
    }

    @Override
    public CommandCategory category() {
        return CommandCategory.POLLS;
    }

    @Override
    public int permissionlevel() {
        return 0;
    }
}
