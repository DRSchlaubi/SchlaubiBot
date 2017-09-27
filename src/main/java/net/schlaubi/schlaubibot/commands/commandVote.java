package net.schlaubi.schlaubibot.commands;

import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.schlaubi.schlaubibot.core.permissionHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class commandVote implements Command, Serializable {

    private static TextChannel channel;

    public static HashMap<Guild, Poll> polls = new HashMap<>();

    private static final String[] EMOTI = {":one:", ":two:", ":three:", ":four:", ":five:", ":six:", ":seven:", ":eight:", ":nine:", ":keycap_ten:"};

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

    private EmbedBuilder getParsedPoll(Poll poll, Guild guild){

         StringBuilder ansSTR = new StringBuilder();
         final AtomicInteger count = new AtomicInteger();

         poll.answers.forEach(s ->{
             long votescount = poll.votes.keySet().stream().filter(k -> poll.votes.get(k).equals(count.get() + 1)).count();
             ansSTR.append(EMOTI[count.get()] + "  -  " + s + "  -  Votes: `" + votescount + "` \n");
             count.addAndGet(1);
         });

        return new EmbedBuilder()
                .setAuthor(poll.getCreator(guild).getEffectiveName() + "'s poll", null, poll.getCreator(guild).getUser().getAvatarUrl())
                .setDescription(":pencil:   " + poll.heading + "\n\n" + ansSTR.toString())
                .setFooter("Enter: '" + STATIC.prefix + "vote v <number>' to vote", null)
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

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    pollmessage.editMessage(getParsedPoll(poll, event.getGuild()).build()).queue();
                }
            }, 4000);
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
        privateMessage("You have succesfully voted for option `" + args[1] + "`", new Color(0x3AD70E), event);
        Message pollmsg =  channel.getMessageById(String.valueOf(poll.pollmsg)).complete();
        pollmsg.editMessage(getParsedPoll(poll, event.getGuild()).build()).queue();







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
        System.out.println("[INFO] Command '" + STATIC.prefix + "vote' was executed by " + event.getAuthor().getName());

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
}
