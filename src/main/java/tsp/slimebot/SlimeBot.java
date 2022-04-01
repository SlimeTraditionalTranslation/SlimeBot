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
        build = new BuildProperties(this);
        Log.info("Loading SlimeBot - " + build.getVersion() + " (Build " + build.getNumber() + ")");
        initMetrics();
        update();

        Log.info("[Build Dependencies]: Spigot: " + build.getSpigot() + " | Slimefun: " + build.getSlimefun());

        commandManager = new CommandManager(true);

        this.bot = new Bot(getConfig().getString("bot.token"));
        this.bot.start();
        if (!this.bot.startBot()) {
            this.setEnabled(false);
            return;
        }

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

    private void update() {
        if (getConfig().getBoolean("auto-update", true) && !build.getAuthor().equalsIgnoreCase("$unknown")) {
            Log.debug("Checking for updates...");
            try {
                new GitHubBuildsUpdater(this, getFile(), build.getAuthor() + "/SlimeBot/master", "Build - ").start();
            } catch (IllegalArgumentException ex) {
                Log.warning("Failed to get github build.");
                Log.debug(ex);
            }
        }
    }

    private void initMetrics() {
        Metrics metrics = new Metrics(this, 14495);
        metrics.addCustomChart(new Metrics.SimplePie("release_version", () -> build.getVersion()));
        metrics.addCustomChart(new Metrics.SimplePie("build_version", () -> String.valueOf(build.getNumber())));
    }

}
