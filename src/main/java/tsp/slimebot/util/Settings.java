package tsp.slimebot.util;

import tsp.slimebot.SlimeBot;

public class Settings {

    private final SlimeBot plugin;
    private String prefix;

    public Settings(SlimeBot plugin) {
        this.plugin = plugin;
    }

    public void load() {
        this.prefix = plugin.getCfg().getString("bot.prefix");
    }

    public String getPrefix() {
        return prefix;
    }

}
