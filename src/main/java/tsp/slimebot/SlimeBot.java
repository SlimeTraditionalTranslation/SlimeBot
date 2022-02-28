package tsp.slimebot;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsp.slimebot.listener.DiscordMessageListener;
import tsp.slimebot.util.Metrics;
import tsp.slimebot.util.Settings;

import javax.security.auth.login.LoginException;

public class SlimeBot extends JavaPlugin implements SlimefunAddon {

    private static SlimeBot instance;
    private Config config;
    private Settings settings;
    private JDA jda;

    @Override
    public void onEnable() {
        instance = this;
        config = new Config(this);
        settings = new Settings(this);
        settings.load();
        new Metrics(this, 14495);

        try {
            if (config.getString("bot.token").isEmpty()) {
                Bukkit.getConsoleSender().sendMessage("Please enter your bot token in the config.yml!");
                this.setEnabled(false);
                return;
            }

            jda = JDABuilder.createLight(config.getString("bot.token"))
                    .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                    .addEventListeners(new DiscordMessageListener())
                    .build();

        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        jda.shutdown();
    }

    public JDA getJDA() {
        return jda;
    }

    public Settings getSettings() {
        return settings;
    }

    public Config getCfg() {
        return config;
    }

    public static SlimeBot getInstance() {
        return instance;
    }


    @NotNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nullable
    @Override
    public String getBugTrackerURL() {
        return null;
    }

}
