package tsp.slimebot.listener.discord;

import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tsp.slimebot.util.Log;

public class BotShutdownListener extends ListenerAdapter {

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        Log.info("機器人已關閉. 原因: " + (event.getCloseCode() != null ? event.getCloseCode() : "未知"));
    }

}
