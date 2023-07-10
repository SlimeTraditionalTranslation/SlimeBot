package tsp.slimebot;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.bakedlibs.dough.updater.GitHubBuildsUpdaterTR;
import net.dv8tion.jda.api.JDA;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mini2Dx.gettext.GetText;
import org.mini2Dx.gettext.PoFile;
import tsp.slimebot.bot.Bot;
import tsp.slimebot.command.discord.CommandManager;
import tsp.slimebot.command.minecraft.SlimeBotCommand;
import tsp.slimebot.listener.minecraft.ResearchUnlockListener;
import tsp.slimebot.util.BuildProperties;
import tsp.slimebot.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

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

        GetText.setLocale(Locale.TRADITIONAL_CHINESE);
        InputStream inputStream = getClass().getResourceAsStream("/translations/zh_tw.po");
        if (inputStream == null) {
            getLogger().severe("錯誤！無法找到翻譯檔案，請回報給翻譯者。");
            getServer().getPluginManager().disablePlugin(this);
            return;
        } else {
            getLogger().info("載入繁體翻譯檔案...");
            try {
                PoFile poFile = new PoFile(Locale.TRADITIONAL_CHINESE, inputStream);
                GetText.add(poFile);
            } catch (ParseCancellationException | IOException e) {
                getLogger().severe("錯誤！讀取翻譯時發生錯誤，請回報給翻譯者：" + e.getMessage());
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        Log.info(GetText.tr("Loading SlimeBot - ") + build.getVersion() + GetText.tr(" (Build {0})", build.getNumber()));
        initMetrics();
        update();

        Log.info(GetText.tr("[Build Dependencies]: Spigot: ") + build.getSpigot() + GetText.tr(" | Slimefun: ") + build.getSlimefun());

        commandManager = new CommandManager(true);

        this.bot = new Bot(getConfig().getString("bot.token"));
        this.bot.start();
        if (!this.bot.startBot()) {
            this.setEnabled(false);
            return;
        }

        new SlimeBotCommand();

        new ResearchUnlockListener();
        Log.info(GetText.tr("Done!"));
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
        return "https://github.com/SlimeTraditionalTranslation/SlimeBot/issues";
    }

    private void update() {
        if (getConfig().getBoolean("auto-update", true) && !build.getAuthor().equalsIgnoreCase("$unknown")) {
            Log.debug("Checking for updates...");
            try {
                new GitHubBuildsUpdaterTR(this, getFile(), build.getAuthor() + "/SlimeBot/master", "Build_STCT - ").start();
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
