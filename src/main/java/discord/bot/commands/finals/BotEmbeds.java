package discord.bot.commands.finals;

import com.merakianalytics.orianna.Orianna;
import com.merakianalytics.orianna.types.common.Region;
import com.merakianalytics.orianna.types.core.staticdata.ProfileIcon;
import com.merakianalytics.orianna.types.core.summoner.Summoner;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord.bot.ApiKey;
import discord.bot.commands.JoinBotCommand;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class BotEmbeds {
    public static EmbedBuilder createMusicEmbed(AudioTrack track) {
        return new EmbedBuilder()
                .setTitle("Added to queue")
                .setDescription(track.getInfo().title)
                .setThumbnail("https://img.youtube.com/vi/" + track.getInfo().identifier + "/0.jpg")
                .addField("URL", track.getInfo().uri);
    }

    public static EmbedBuilder createTFTEmbed(String summonerName, String tier, String rank, String leaguePoints, String wins, Float wr, String losses) {
        Orianna.setRiotAPIKey(ApiKey.riotApiKey);
        Orianna.setDefaultRegion(Region.EUROPE_NORTH_EAST);
        Summoner summoner = Orianna.summonerNamed(summonerName).get();
        ProfileIcon profileIcon = summoner.getProfileIcon();

        return new EmbedBuilder()
                .setTitle("TFT Profile of: " + summonerName)
                .addField("Tier", tier + " " + rank + " - " + leaguePoints + "LP")
                .addField("Losses", losses)
                .addField("Wins", wins)
                .addField("Winrate", wr.toString() + "%")
                .setThumbnail(profileIcon.getImage().getURL());
    }

    public static EmbedBuilder musicQueueEmbed(BlockingQueue<AudioTrack> queue) {

        EmbedBuilder embedBuilder = new EmbedBuilder();

        AtomicInteger i = new AtomicInteger();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format(("**Now playing: %s** \n"), JoinBotCommand.PLAYER.getPlayingTrack().getInfo().title));
        JoinBotCommand.trackScheduler.queue.forEach(elements -> {
            i.getAndIncrement();
            stringBuilder.append(String.format("**%s.** %s \n", i.toString(), elements.getInfo().title));
        });

        return embedBuilder.setTitle("Current music queue: ")
                .setDescription(stringBuilder.toString());

    }

    public static EmbedBuilder musicDisconnectEmbed() {
        return new EmbedBuilder().setTitle("Disconnected because I had no music and was in channel for too long.");
    }

    public static EmbedBuilder tempEmbed(String channelName) {
        return new EmbedBuilder().setTitle("Created temporary channel")
                .setDescription("With the name: " + channelName);
    }


}
