package tsp.slimebot.listener.minecraft;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import tsp.slimebot.SlimeBot;

public class MinecraftListener implements Listener {

    public MinecraftListener() {
        Bukkit.getPluginManager().registerEvents(this, SlimeBot.getInstance());
    }

}
