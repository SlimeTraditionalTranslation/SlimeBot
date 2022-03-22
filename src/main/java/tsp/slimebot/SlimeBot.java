package tsp.slimebot;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;
import net.dv8tion.jda.api.JDA;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tsp.slimebot.bot.Bot;
import tsp.slimebot.command.discord.CommandManager;
import tsp.slimebot.command.minecraft.SlimeBotCommand;
import tsp.slimebot.listener.minecraft.ResearchUnlockListener;
import tsp.slimebot.util.BuildProperties;
import tsp.slimebot.util.Log;

public class SlimeBot extends JavaPlugin implements SlimefunAddon {

    private static SlimeBot instance;
    private Bot bot;
    private BuildProperties build;
    private CommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Log.info("Loading SlimeBot - " + getDescription().getVersion());
        update();

        build = new BuildProperties(this);
        commandManager = new CommandManager();
        commandManager.registerDefaults();
        new Metrics(this, 14495);

        this.bot = new Bot(getConfig().getString("bot.token"));
        this.bot.start();
        this.bot.startBot();

        new SlimeBotCommand();

        new ResearchUnlockListener();
        Log.info("Done!");
    }

    @Override
    public void onDisable() {
        this.getBot().stopBot();
    }

    public Bot getBot() {
        return bot;
    }

    public JDA getJDA() {
        return bot.getJda();
    }

    private void update() {
        if (getConfig().getBoolean("auto-update", true)) {
            Log.debug("Checking for updates...");
            try {
                new GitHubBuildsUpdater(this, getFile(), "TheSilentPro/SlimeBot/master").start();
            } catch (IllegalArgumentException ex) {
                Log.warning("Failed to get github build.");
                Log.debug(ex);
            }
        }
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public BuildProperties getBuild() {
        return build;
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
        return "https://github.com/TheSilentPro/SlimeBot/issues";
    }

}
