package discord.bot.commands;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import discord.bot.Main.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static discord.bot.Main.api;

public class TrumpQuoteCommand {
    public void trumpQuoteCommand(){
        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("!trump")) {
                try {
                    URL loginUrl = new URL("https://api.tronalddump.io/random/quote");
                    URLConnection yc = loginUrl.openConnection();
                    yc.setConnectTimeout(10 * 1000);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    yc.getInputStream()));
                    String inputLine = in.readLine();

                    JsonObject array = JsonParser.parseString(inputLine).getAsJsonObject();
                    event.getChannel().sendMessage(array.get("value").getAsString());
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
    }

}
