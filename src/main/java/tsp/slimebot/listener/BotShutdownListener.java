package tsp.slimebot.listener;

import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tsp.slimebot.SlimeBot;

public class BotShutdownListener extends ListenerAdapter {

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        SlimeBot.getInstance().getLogger().info("The bot has shutdown. Reason: " + (event.getCloseCode() != null ? event.getCloseCode() : "Unknown"));
    }

}
