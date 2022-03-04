package tsp.slimebot;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.config.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsp.slimebot.command.discord.CommandManager;
import tsp.slimebot.command.minecraft.WakeupCommand;
import tsp.slimebot.listener.discord.BotReadyListener;
import tsp.slimebot.listener.discord.BotShutdownListener;
import tsp.slimebot.listener.discord.DiscordCommandListener;
import tsp.slimebot.listener.minecraft.ResearchUnlockListener;
import tsp.slimebot.util.Metrics;

import javax.security.auth.login.LoginException;

public class SlimeBot extends JavaPlugin implements SlimefunAddon {

    private static SlimeBot instance;
    private Config config;
    private JDA jda;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;
        config = new Config(this);
        commandManager = new CommandManager();
        commandManager.registerDefaults();
        new Metrics(this, 14495);

        try {
            if (config.getString("bot.token").isEmpty()) {
                getLogger().severe(ChatColor.RED + "Please enter your bot token in the config.yml!");
                this.setEnabled(false);
                return;
            }

            startBot();

            if (config.getBoolean("debug")) {
                new WakeupCommand();
            }

            new ResearchUnlockListener();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        stopBot();
    }

    public void stopBot() {
        if (jda != null) {
            jda.shutdown();
        }
    }

    public void startBot() throws LoginException {
        stopBot();

        jda = JDABuilder.createLight(config.getString("bot.token"))
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new BotReadyListener())
                .addEventListeners(new BotShutdownListener())
                .addEventListeners(new DiscordCommandListener())
                .build();

        getLogger().info(ChatColor.GREEN + "Bot is running on account: " + jda.getSelfUser().getAsTag());
        getLogger().info(ChatColor.GREEN + "Invite: " + jda.getInviteUrl(Permission.ADMINISTRATOR, Permission.USE_APPLICATION_COMMANDS));
    }

    public JDA getJDA() {
        return jda;
    }

    public CommandManager getCommandManager() {
        return commandManager;
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
