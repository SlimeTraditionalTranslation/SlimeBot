package tsp.slimebot.command.minecraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import tsp.slimebot.SlimeBot;

public abstract class MinecraftCommand implements CommandExecutor {

    public MinecraftCommand(String name) {
        if (!SlimeBot.getInstance().getConfig().getStringList("disabledCommands").contains(name)) {
            SlimeBot.getInstance().getCommand(name).setExecutor(this);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        onCommand(sender, args);
        return true;
    }

    public void onCommand(CommandSender sender, String[] args) {

    }

}
