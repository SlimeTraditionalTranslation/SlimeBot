package tsp.slimebot.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.ChatColor;
import org.mini2Dx.gettext.GetText;
import tsp.slimebot.listener.discord.BotReadyListener;
import tsp.slimebot.listener.discord.BotShutdownListener;
import tsp.slimebot.listener.discord.DiscordCommandListener;
import tsp.slimebot.util.Log;

import javax.security.auth.login.LoginException;
import java.util.concurrent.atomic.AtomicInteger;

public final class Bot extends Thread {

    private static final AtomicInteger ID = new AtomicInteger(1);
    private final String token;
    private JDA jda;

    public Bot(String token) {
        super("SlimeBotThread-" + ID.getAndIncrement());
        this.token = token;
    }

    public boolean startBot() {
        stopBot();

        try {
            jda = JDABuilder.createLight(token)
                    .enableIntents(GatewayIntent.GUILD_MESSAGES)
                    .addEventListeners(new BotReadyListener())
                    .addEventListeners(new BotShutdownListener())
                    .addEventListeners(new DiscordCommandListener())
                    .build();
        } catch (Exception ex) {
            Log.error(GetText.tr("Bot failed to login! Make sure the token is correct inside the config."));
            Log.error(ex);
            return false;
        }


        Log.debug(GetText.tr("Bot is running on thread: ") + getName() + GetText.tr(" (ID: {0})", getId()));
        Log.info(ChatColor.GREEN + GetText.tr("Bot is running on account: ") + jda.getSelfUser().getName());
        Log.info(ChatColor.GREEN + GetText.tr("Invite: ") + jda.getInviteUrl(Permission.ADMINISTRATOR, Permission.USE_APPLICATION_COMMANDS));
        return true;
    }

    public void stopBot() {
        if (jda != null) {
            jda.shutdown();
        }
    }

    public JDA getJda() {
        return jda;
    }

}
