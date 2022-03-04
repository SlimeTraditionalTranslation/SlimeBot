package tsp.slimebot.command.minecraft;

import org.bukkit.command.CommandExecutor;
import tsp.slimebot.SlimeBot;

public abstract class MinecraftCommand implements CommandExecutor {

    public MinecraftCommand(String name) {
        SlimeBot.getInstance().getCommand(name).setExecutor(this);
    }

}
