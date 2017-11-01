package net.schlaubi.schlaubibot.audioCore;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.schlaubi.schlaubibot.commands.Music;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackManager extends AudioEventAdapter{

    private final AudioPlayer PLAYER;
    private static Queue<AudioInfo> QUEUE;





    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason){
        try {
            Guild g = QUEUE.poll().getAuthor().getGuild();
            if (QUEUE.isEmpty()) {

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        g.getAudioManager().closeAudioConnection();
                    }
                }, 3000);


            } else {
                player.playTrack(QUEUE.element().getTrack());
            }
        } catch (NullPointerException ignored){

        }

    }

    public TrackManager(AudioPlayer player){
        this.PLAYER = player;
        this.QUEUE = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track, Member author){
        AudioInfo info = new AudioInfo(track, author);
        QUEUE.add(info);

        if(PLAYER.getPlayingTrack() == null){
            PLAYER.playTrack(track);
        }

    }


    public Set<AudioInfo> getQueue(){
        return new LinkedHashSet<>(QUEUE);
    }

    public static AudioInfo getInfo(AudioTrack track){
        return QUEUE.stream()
                .filter(info -> info.getTrack().equals(track))
                .findFirst().orElse(null);
    }

    public void purgeQueue(){
        QUEUE.clear();
    }

    public void shuffleQueue(){
        List<AudioInfo> cQueue = new ArrayList<>(getQueue());
        AudioInfo current = cQueue.get(0);
        cQueue.remove(0);
        Collections.shuffle(cQueue);
        cQueue.add(0, current);
        purgeQueue();
        QUEUE.addAll(cQueue);
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track){
        AudioInfo info = QUEUE.element();
        VoiceChannel vChan = info.getAuthor().getVoiceState().getChannel();
        TrackManager manager = Music.PLAYERS.get(info.getAuthor().getGuild()).getValue();

        if(vChan == null) {
            player.stopTrack();
        } else {

            info.getAuthor().getGuild().getAudioManager().openAudioConnection(vChan);

        }


    }






}
