package tsp.slimebot.command.minecraft;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;
import tsp.slimebot.SlimeBot;

import javax.security.auth.login.LoginException;

public class WakeupCommand extends MinecraftCommand {

    public WakeupCommand() {
        super("wakeup");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof ConsoleCommandSender)) {
            return false; // Hidden from non-console
        }

        if (SlimeBot.getInstance().getJDA() != null) {
            sender.sendMessage("The bot is already running.");
            return true;
        }

        try {
            SlimeBot.getInstance().startBot();
        } catch (LoginException e) {
            sender.sendMessage("Failed to start bot.");
            e.printStackTrace();
        }
        return true;
    }

}
