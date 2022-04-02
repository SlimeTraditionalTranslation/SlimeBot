package tsp.slimebot.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.ChatColor;
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
        } catch (LoginException ex) {
            Log.error("機器人登入失敗! 請確認在 config 中的機器人 token 是正確的.");
            Log.error(ex);
            return false;
        }

        Log.debug("機器人在線程運行: " + getName() + " (ID: " + getId() + ")");
        Log.info(ChatColor.GREEN + "機器人正在此帳戶上運行: " + jda.getSelfUser().getAsTag());
        Log.info(ChatColor.GREEN + "邀請連結: " + jda.getInviteUrl(Permission.ADMINISTRATOR, Permission.USE_APPLICATION_COMMANDS));
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
