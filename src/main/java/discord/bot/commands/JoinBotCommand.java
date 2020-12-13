package discord.bot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import discord.bot.LavaplayerAudioSource;
import discord.bot.Main;
import discord.bot.commands.finals.FinalValues;
import discord.bot.commands.finals.GetYoutubeURL;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.Collections;
import java.util.List;


public class JoinBotCommand implements TemplateCommand {
    public static AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    public static AudioPlayer PLAYER = playerManager.createPlayer();
    public static TrackScheduler trackScheduler = new TrackScheduler(PLAYER);
    public static AudioConnection audioConnection;
    public static Server server1;

    @Override
    public void executeCommand(MessageCreateEvent event) {
        Server server = event.getServer().get();
        server1 = server;
        DiscordApi api = Main.api;
        PLAYER.addListener(trackScheduler);
        GetYoutubeURL urlGetter = new GetYoutubeURL();
        if (event.getMessage().getContent().contains(FinalValues.PREFIX + FinalValues.PLAY)) {
            ServerVoiceChannel channel;

            channel = event.getMessage().getAuthor().asUser().get().getConnectedVoiceChannel(server).get();
            //System.out.println(channel.getName());
            playerManager = new DefaultAudioPlayerManager();
            playerManager.registerSourceManager(new YoutubeAudioSourceManager());
            GetYoutubeURL getURL = new GetYoutubeURL();

            AudioSource source = new LavaplayerAudioSource(api, PLAYER);

            if (server.getAudioConnection().isEmpty()) {
                channel.connect().thenAccept(audioConnection -> {
                    audioConnection.setAudioSource(source);
                    JoinBotCommand.audioConnection = audioConnection;
                });
            }

            playerManager.loadItem("ytsearch: " + getURL.getResult(event), new FunctionalResultHandler(null, audioPlaylist -> {
                trackScheduler.queue(audioPlaylist.getTracks().get(0));
                event.getChannel().sendMessage(String.format("Added to queue: **%s**", audioPlaylist.getTracks().get(trackScheduler.queue.size()).getInfo().title));
            }, null, null));


        }


    }

    @Override
    public String getCommandName() {
        return FinalValues.PLAY;
    }

    @Override
    public List<String> getCommandDescription() {
        return Collections.singletonList("Joins the Musicbot.");
    }

}

