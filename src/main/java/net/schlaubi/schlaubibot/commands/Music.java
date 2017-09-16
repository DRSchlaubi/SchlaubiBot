package net.schlaubi.schlaubibot.commands;

import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.schlaubi.schlaubibot.audioCore.*;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.schlaubi.schlaubibot.util.STATIC;
import net.schlaubi.schlaubibot.util.embedSender;

import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;



public class Music implements Command {


    private static final int PLAYLIST_LIMIT = 1000;
    private static Guild guild;
    private static final AudioPlayerManager MANAGER = new DefaultAudioPlayerManager();
    private static final Map<Guild, Map.Entry<AudioPlayer, TrackManager>> PLAYERS = new HashMap<>();



    public Music() {
        AudioSourceManagers.registerRemoteSources(MANAGER);
    }


    private AudioPlayer createPlayer(Guild g) {
        AudioPlayer p = MANAGER.createPlayer();
        TrackManager m = new TrackManager(p);
        p.addListener(m);

        guild.getAudioManager().setSendingHandler(new PlayerSendHandler(p));

        PLAYERS.put(g, new AbstractMap.SimpleEntry<>(p, m));

        return p;
    }


    private boolean hasPlayer(Guild g) {
        return PLAYERS.containsKey(g);
    }


    private AudioPlayer getPlayer(Guild g) {
        if (hasPlayer(g))
            return PLAYERS.get(g).getKey();
        else
            return createPlayer(g);
    }

    private TrackManager getManager(Guild g) {
        return PLAYERS.get(g).getValue();
    }


    private boolean isIdle(Guild g) {
        return !hasPlayer(g) || getPlayer(g).getPlayingTrack() == null;
    }


    private void loadTrack(String identifier, Member author, Message msg) {

        Guild guild = author.getGuild();
        getPlayer(guild);

        MANAGER.setFrameBufferDuration(5000);
        MANAGER.loadItemOrdered(guild, identifier, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                getManager(guild).queue(track, author);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (int i = 0; i < (playlist.getTracks().size() > PLAYLIST_LIMIT ? PLAYLIST_LIMIT : playlist.getTracks().size()); i++) {
                    getManager(guild).queue(playlist.getTracks().get(i), author);
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
            }
        });

    }


    private void skip(Guild g) {
        getPlayer(g).stopTrack();
    }


    private String getTimestamp(long milis) {
        long seconds = milis / 1000;
        long hours = Math.floorDiv(seconds, 3600);
        seconds = seconds - (hours * 3600);
        long mins = Math.floorDiv(seconds, 60);
        seconds = seconds - (mins * 60);
        return (hours == 0 ? "" : hours + ":") + String.format("%02d", mins) + ":" + String.format("%02d", seconds);
    }


    private String buildQueueMessage(AudioInfo info) {
        AudioTrackInfo trackInfo = info.getTrack().getInfo();
        String title = trackInfo.title;
        long length = trackInfo.length;
        return "`[ " + getTimestamp(length) + " ]` " + title + "\n";
    }


    private void sendErrorMsg(MessageReceivedEvent event, String content) {
        event.getTextChannel().sendMessage(
                new EmbedBuilder()
                        .setColor(Color.red)
                        .setDescription(content)
                        .build()
        ).queue();
    }


    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        Message message = event.getMessage();
        MessageChannel channel = event.getChannel();
        channel.sendTyping().queue();
        message.delete().queue();

        if(!event.getMember().getVoiceState().inVoiceChannel()){
            embedSender.sendEmbed(":warning: You must be in a voice channel", channel, Color.red);
            return;
        }

        if(event.getGuild().getSelfMember().getVoiceState().inVoiceChannel() && !event.getMember().getVoiceState().getChannel().equals(event.getGuild().getSelfMember().getVoiceState().getChannel())){
            embedSender.sendEmbed(":warning: You must be in the same channel as the bot", channel, Color.red);
            return;
        }


        guild = event.getGuild();

        if (args.length < 1) {
            sendErrorMsg(event, help());
            return;
        }

        switch (args[0].toLowerCase()) {

            case "play":
            case "p":

                if (args.length < 2) {
                    sendErrorMsg(event, "Please enter a valid source!");
                    return;
                }

                String input = Arrays.stream(args).skip(1).map(s -> " " + s).collect(Collectors.joining()).substring(1);

                if (!(input.startsWith("http://") || input.startsWith("https://"))) {
                    embedSender.sendEmbed(":youtube: **Searching** :mag_right: `" + input + "`", channel, Color.cyan);
                    input = "ytsearch: " + input;
                }

                embedSender.sendEmbed("Searching for " + input + " ...", channel, Color.cyan);
                loadTrack(input, event.getMember(), event.getMessage());
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        int tracks = getManager(guild).getQueue().size();
                        if(tracks == 0)
                            embedSender.sendEmbed(":warning: I can't find a video for you search query SORRY!", channel, Color.red);
                        else
                            embedSender.sendEmbed("**Added** :notes:`" + tracks + "` to the queue!", channel, Color.CYAN);
                    }
                },3000);

                break;


            case "skip":
            case "s":

                if (isIdle(guild)){
                    embedSender.sendEmbed("There is no song running on this guild", channel, Color.red);
                    return;
                }
                for (int i = (args.length > 1 ? Integer.parseInt(args[1]) : 1); i >= 1; i--) {
                    skip(guild);
                }
                if (args.length > 1)
                    embedSender.sendEmbed(":white_check_mark: Succefully skipped `" + args[1] + "` songs", channel, Color.green);
                else
                    embedSender.sendEmbed(":white_check_mark: Succefully skipped current songs", channel, Color.green);



                break;


            case "stop":
            case "st":

                if (isIdle(guild)){
                    embedSender.sendEmbed("There is no song running on this guild", channel, Color.red);
                    return;
                }
                getManager(guild).purgeQueue();
                skip(guild);
                guild.getAudioManager().closeAudioConnection();
                embedSender.sendEmbed(":stop_button:  Successfully stopped queue", channel, Color.green);

                break;


            case "shuffle":
            case "sh":

                if (isIdle(guild)){
                    embedSender.sendEmbed("There is no song running on this guild", channel, Color.red);
                    return;
                }
                getManager(guild).shuffleQueue();
                embedSender.sendEmbed(":twisted_rightwards_arrows: Succesfully shuffeled queue", channel, Color.green);

                break;


            case "now":
            case "info":
            case "nowplaying":
            case "np":

                if (isIdle(guild)){
                    embedSender.sendEmbed("There is no song running on this guild", channel, Color.red);
                    return;
                }

                AudioTrack track = getPlayer(guild).getPlayingTrack();
                AudioTrackInfo info = track.getInfo();

                event.getTextChannel().sendMessage(
                        new EmbedBuilder()
                                .setDescription("**CURRENT TRACK INFO:**")
                                .addField("Title", info.title, false)
                                .addField("Duration", "`[ " + getTimestamp(track.getPosition()) + "/ " + getTimestamp(track.getDuration()) + " ]`", false)
                                .addField("Author", info.author, false)
                                .build()
                ).queue();

                break;



            case "queue":

                if (isIdle(guild)){
                    embedSender.sendEmbed("There is no song running on this guild", channel, Color.red);
                    return;
                }

                try {
                    int sideNumb = args.length > 1 ? Integer.parseInt(args[1]) : 1;

                    List<String> tracks = new ArrayList<>();
                    List<String> trackSublist;

                    getManager(guild).getQueue().forEach(audioInfo -> tracks.add(buildQueueMessage(audioInfo)));

                    if (tracks.size() > 20)
                        trackSublist = tracks.subList((sideNumb - 1) * 20, (sideNumb - 1) * 20 + 20);
                    else
                        trackSublist = tracks;

                    String out = trackSublist.stream().collect(Collectors.joining("\n"));
                    int sideNumbAll = tracks.size() >= 20 ? tracks.size() / 20 : 1;

                    event.getTextChannel().sendMessage(
                            new EmbedBuilder()
                                    .setDescription(
                                            "**CURRENT QUEUE:**\n" +
                                                    "*[" + getManager(guild).getQueue().size() + " Tracks | Side " + sideNumb + " / " + sideNumbAll + "]*" +
                                                    out
                                    )
                                    .build()
                    ).queue();
                } catch (NumberFormatException e){
                    embedSender.sendEmbed(":warning: Please send a valid nubmer", channel, Color.red);
                }


                break;
            case "clear":
                if (isIdle(guild)){
                    embedSender.sendEmbed("There is no song running on this guild", channel, Color.red);
                    return;
                }

                if (isIdle(guild)){
                    embedSender.sendEmbed("There is no song running on this guild", channel, Color.red);
                    return;
                }

                getManager(guild).purgeQueue();
                embedSender.sendEmbed(":white_check_mark: Succesfully clearde queue", channel, Color.green);
                break;
            case "volume":
            case "vol":
                if (isIdle(guild)){
                    embedSender.sendEmbed("There is no song running on this guild", channel, Color.red);
                    return;
                }

                try{
                    int vol = Integer.parseInt(args[1]);
                    if (vol > 0 && vol < 11){
                        getPlayer(guild).setVolume(vol * 10);
                        embedSender.sendEmbed(":white_check_mark: Succesfully set the volume to `" + vol * 10 + "%`", channel, Color.green);
                    } else {
                        embedSender.sendEmbed(":warning: Please provide a number between 1 and 10", channel, Color.red);
                    }
                } catch (NumberFormatException e){
                    embedSender.sendEmbed(":warning: Please provide a valid number", channel, Color.red);
                }
                break;
            case "pause":
                if (isIdle(guild)){
                    embedSender.sendEmbed("There is no song running on this guild", channel, Color.red);
                    return;
                }
                if(getPlayer(guild).isPaused()){
                    embedSender.sendEmbed(":warning: This queue is already paused", channel, Color.red);
                    return;
                }

                getPlayer(guild).setPaused(true);
                embedSender.sendEmbed(":pause_button: Successfully paused queue", channel, Color.green);
                break;
            case "resume":
                if (isIdle(guild)){
                    embedSender.sendEmbed("There is no song running on this guild", channel, Color.red);
                    return;
                }

                if(!getPlayer(guild).isPaused()){
                    embedSender.sendEmbed("The queue is not paused on this Guild", channel, Color.red);
                    return;
                }

                if(getPlayer(guild).isPaused()){
                    getPlayer(guild).setPaused(false);
                    embedSender.sendEmbed(":play_pause: Successfully resumed queue", channel, Color.green);
                }
                break;
            case "disconnect":
            case "quit":
               if (isIdle(guild)){
                   embedSender.sendEmbed("There is no song running on this guild", channel, Color.red);
                   return;
               }
               guild.getAudioManager().closeAudioConnection();
               embedSender.sendEmbed(":white_check_mark: Successfully disconected", channel, Color.green);

        }


    }

    @Override
    public void executed(boolean sucess, MessageReceivedEvent event) {

    }

    @Override
    public String help() {

        return "USAGE: \n" +
                "`  " + STATIC.prefix + "music play <link/keyword)  `  -  Queues a song\n" +
                "`  " + STATIC.prefix + "music stop  `  -  Stops the queue\n" +
                "`  " + STATIC.prefix + "music now  `  -  Shows information about the current song\n" +
                "`  " + STATIC.prefix + "music pause/resume  `  -  Pauses/resumes a song\n" +
                "`  " + STATIC.prefix + "music volume <volume> ` - Changes the volume of the bot" +
                "`  " + STATIC.prefix + "music shuffle` - (Shuffles the queue)" +
                "`  " + STATIC.prefix + "music disconnect` - (Let the Bot disconnect)" +
                "`  " + STATIC.prefix + "music skip [number of songs] - (Skips the specified amount of songs)" +
                "`  " + STATIC.prefix + "music queue - (Shows the queue)"
                ;
    }
}